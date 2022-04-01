package com.example.koronaapp.network

import com.example.koronaapp.Model.HospitalInfoResponse
import androidx.room.*

@Dao
interface RoomDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hospitalInfoResponse : HospitalInfoResponse)

    @Update
    suspend fun update(hospitalInfoResponse : HospitalInfoResponse)

    @Delete
    suspend fun delete(hospitalInfoResponse : HospitalInfoResponse)

    @Query("SELECT * FROM HospitalInfoResponse")
    suspend fun getAll() : List<HospitalInfoResponse>

    @Query("DELETE FROM HospitalInfoResponse ")
    suspend fun deleteAll()
}