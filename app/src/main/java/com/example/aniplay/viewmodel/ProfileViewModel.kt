package com.example.aniplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplay.data.local.ProfileNoteEntity
import com.example.aniplay.data.local.UserProfileEntity
import com.example.aniplay.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfileEntity = UserProfileEntity(),
    val notes: List<ProfileNoteEntity> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val isEditingProfile: Boolean = false,
    val isNoteDialogOpen: Boolean = false,
    val selectedNoteForEdit: ProfileNoteEntity? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileAndNotes()
    }

    private fun loadProfileAndNotes() {
        viewModelScope.launch {
            combine(
                profileRepository.observeProfile(),
                profileRepository.observeNotes(),
            ) { profile, notes ->
                profile to notes
            }.collect { (profile, notes) ->
                _uiState.update { state ->
                    state.copy(
                        profile = profile,
                        notes = notes,
                        isLoading = false,
                    )
                }
            }
        }
    }

    // --- OPERAÇÕES CRUD DO PERFIL (Room) ---

    fun updateProfile(
        name: String,
        email: String,
        bio: String,
        favoriteGenre: String,
    ) {
        viewModelScope.launch {
            val current = _uiState.value.profile
            val updated = current.copy(
                name = name.ifBlank { current.name },
                email = email.ifBlank { current.email },
                bio = bio.ifBlank { current.bio },
                favoriteGenre = favoriteGenre.ifBlank { current.favoriteGenre },
                updatedAt = System.currentTimeMillis(),
            )
            profileRepository.saveProfile(updated)
            _uiState.update {
                it.copy(
                    isEditingProfile = false,
                    userMessage = "Perfil atualizado com sucesso!",
                )
            }
        }
    }

    fun toggleVipStatus() {
        viewModelScope.launch {
            val current = _uiState.value.profile
            val updated = current.copy(
                isVip = !current.isVip,
                updatedAt = System.currentTimeMillis(),
            )
            profileRepository.saveProfile(updated)
            val msg = if (updated.isVip) "Plano VIP ativado!" else "Plano alterado para Gratuito."
            _uiState.update { it.copy(userMessage = msg) }
        }
    }

    fun toggleNotifications() {
        viewModelScope.launch {
            val current = _uiState.value.profile
            val updated = current.copy(
                notificationsEnabled = !current.notificationsEnabled,
                updatedAt = System.currentTimeMillis(),
            )
            profileRepository.saveProfile(updated)
            val msg = if (updated.notificationsEnabled) "Notificações ativadas." else "Notificações desativadas."
            _uiState.update { it.copy(userMessage = msg) }
        }
    }

    // --- OPERAÇÕES CRUD DAS ANOTAÇÕES/METAS (Room) ---

    fun saveNote(
        title: String,
        content: String,
        category: String,
    ) {
        if (title.isBlank() || content.isBlank()) return

        viewModelScope.launch {
            val editingNote = _uiState.value.selectedNoteForEdit
            if (editingNote != null) {
                // UPDATE
                val updatedNote = editingNote.copy(
                    title = title,
                    content = content,
                    category = category,
                )
                profileRepository.updateNote(updatedNote)
                _uiState.update {
                    it.copy(
                        isNoteDialogOpen = false,
                        selectedNoteForEdit = null,
                        userMessage = "Anotação atualizada!",
                    )
                }
            } else {
                // CREATE
                profileRepository.addNote(
                    title = title,
                    content = content,
                    category = category,
                )
                _uiState.update {
                    it.copy(
                        isNoteDialogOpen = false,
                        userMessage = "Anotação adicionada!",
                    )
                }
            }
        }
    }

    fun deleteNote(note: ProfileNoteEntity) {
        viewModelScope.launch {
            profileRepository.deleteNote(note)
            _uiState.update { it.copy(userMessage = "Anotação excluída.") }
        }
    }

    fun resetProfileData() {
        viewModelScope.launch {
            profileRepository.resetProfileData()
            _uiState.update {
                it.copy(
                    isEditingProfile = false,
                    isNoteDialogOpen = false,
                    selectedNoteForEdit = null,
                    userMessage = "Dados do perfil redefinidos para o padrão.",
                )
            }
        }
    }

    // --- CONTROLE DE DIÁLOGOS DA UI ---

    fun openEditProfileDialog() {
        _uiState.update { it.copy(isEditingProfile = true) }
    }

    fun closeEditProfileDialog() {
        _uiState.update { it.copy(isEditingProfile = false) }
    }

    fun openNoteDialog(note: ProfileNoteEntity? = null) {
        _uiState.update {
            it.copy(
                isNoteDialogOpen = true,
                selectedNoteForEdit = note,
            )
        }
    }

    fun closeNoteDialog() {
        _uiState.update {
            it.copy(
                isNoteDialogOpen = false,
                selectedNoteForEdit = null,
            )
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
