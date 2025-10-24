package com.example.cryptoassistant.api.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptoassistant.api.data.AssetsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetsDao {

    @Query("SELECT * FROM assets")
    fun getAllCharacters(): Flow<List<AssetsEntity>>

    // Добавляем метод для прямого получения списка
    @Query("SELECT * FROM assets LIMIT :limit")
    suspend fun getCharacters(limit: Int): List<AssetsEntity>


    @Query("SELECT * FROM assets WHERE idCrypto = :characterId")
    suspend fun getCharacterById(characterId: Long): AssetsEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCharacters(characters: List<AssetsEntity>)

    @Query("DELETE FROM assets")
    suspend fun deleteAllCharacters()

    @Query("SELECT COUNT(*) FROM assets")
    suspend fun getCount(): Int
}