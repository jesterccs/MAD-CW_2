package com.example.cw_2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * from User")
    suspend fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg user: User)

    @Query("DELETE FROM User")
    suspend fun deleteAll()

    @Query("SELECT title FROM User WHERE actors LIKE '%' || :value || '%'" )
    suspend fun search(value: String) : List<String>

    @Query("SELECT actors From User")
    suspend fun selectActors() : List<String>
}