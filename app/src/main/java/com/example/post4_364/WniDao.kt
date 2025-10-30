package com.example.post4_364

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WniDao {

    @Insert
    suspend fun insert(wni: Wni)

    // ✅ Perbaikan: ubah nama fungsi agar sesuai pemanggilan di MainActivity
    @Query("SELECT * FROM tbl_wni")
    suspend fun getAll(): List<Wni>

    @Query("DELETE FROM tbl_wni")
    suspend fun deleteAll()
}
