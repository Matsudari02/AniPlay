package com.example.aniplay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela para anotações e metas de animes do perfil (usada para demonstrar CRUD completo no Room).
 */
@Entity(tableName = "profile_notes")
data class ProfileNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val category: String = "Geral",
    val createdAt: Long = System.currentTimeMillis(),
)
