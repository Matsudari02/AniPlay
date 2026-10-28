package com.example.aniplay.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO responsável pelas operações de CRUD no perfil do usuário e anotações personalizadas.
 */
@Dao
interface UserProfileDao {

    // --- READ (Perfil) ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): UserProfileEntity?

    // --- CREATE / UPDATE (Perfil) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // --- DELETE (Perfil) ---
    @Query("DELETE FROM user_profile WHERE id = 1")
    suspend fun deleteProfile()

    // --- READ (Anotações/Metas) ---
    @Query("SELECT * FROM profile_notes ORDER BY createdAt DESC")
    fun observeNotes(): Flow<List<ProfileNoteEntity>>

    // --- CREATE (Anotações) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ProfileNoteEntity): Long

    // --- UPDATE (Anotações) ---
    @Update
    suspend fun updateNote(note: ProfileNoteEntity)

    // --- DELETE (Anotações) ---
    @Delete
    suspend fun deleteNote(note: ProfileNoteEntity)

    @Query("DELETE FROM profile_notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("DELETE FROM profile_notes")
    suspend fun deleteAllNotes()
}
