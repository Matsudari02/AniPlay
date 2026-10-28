package com.example.aniplay.data.repository

import com.example.aniplay.data.local.ProfileNoteEntity
import com.example.aniplay.data.local.UserProfileDao
import com.example.aniplay.data.local.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório responsável por gerenciar as informações do perfil do usuário e suas anotações no Room.
 */
@Singleton
class ProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
) {

    /**
     * Observa o perfil do usuário em tempo real. Se não existir no banco, retorna o padrão.
     */
    fun observeProfile(): Flow<UserProfileEntity> {
        return userProfileDao.observeProfile().map { profile ->
            profile ?: UserProfileEntity()
        }
    }

    /**
     * Atualiza ou cria o perfil no Room.
     */
    suspend fun saveProfile(profile: UserProfileEntity) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    /**
     * Observa todas as anotações do usuário no Room.
     */
    fun observeNotes(): Flow<List<ProfileNoteEntity>> {
        return userProfileDao.observeNotes()
    }

    /**
     * Adiciona uma nova anotação/meta de anime no Room (CREATE).
     */
    suspend fun addNote(title: String, content: String, category: String) {
        val note = ProfileNoteEntity(
            title = title,
            content = content,
            category = category,
        )
        userProfileDao.insertNote(note)
    }

    /**
     * Atualiza uma anotação existente no Room (UPDATE).
     */
    suspend fun updateNote(note: ProfileNoteEntity) {
        userProfileDao.updateNote(note)
    }

    /**
     * Deleta uma anotação do Room (DELETE).
     */
    suspend fun deleteNote(note: ProfileNoteEntity) {
        userProfileDao.deleteNote(note)
    }

    /**
     * Reseta as informações de perfil e deleta todas as anotações.
     */
    suspend fun resetProfileData() {
        userProfileDao.deleteProfile()
        userProfileDao.deleteAllNotes()
        // Garante que recria com valores padrão
        userProfileDao.insertOrUpdateProfile(UserProfileEntity())
    }
}
