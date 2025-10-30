package com.example.post4_364

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_wni")
data class Wni(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val nik: String,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: String,
    val rw: String,
    val jenisKelamin: String,
    val statusPernikahan: String
)
