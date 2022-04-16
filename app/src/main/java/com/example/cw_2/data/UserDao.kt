package com.example.cw_2.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * from User")
    suspend fun getAll(): List<User>
}