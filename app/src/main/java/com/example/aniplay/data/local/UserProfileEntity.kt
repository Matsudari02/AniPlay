package com.example.aniplay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela do perfil do usuário no banco de dados local (Room).
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Otaku AniPlay",
    val email: String = "otaku@aniplay.com",
    val bio: String = "Apaixonado por animes de ação e aventura!",
    val favoriteGenre: String = "Ação",
    val isVip: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
)
