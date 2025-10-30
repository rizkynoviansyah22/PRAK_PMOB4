package com.example.post4_364


import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseWni
    private lateinit var wniDao: WniDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseWni.getDatabase(this)
        wniDao = db.wniDao()

        val etNama = findViewById<EditText>(R.id.etNama)
        val etNIK = findViewById<EditText>(R.id.etNIK)
        val etKabupaten = findViewById<EditText>(R.id.etKabupaten)
        val etKecamatan = findViewById<EditText>(R.id.etKecamatan)
        val etDesa = findViewById<EditText>(R.id.etDesa)
        val etRT = findViewById<EditText>(R.id.etRT)
        val etRW = findViewById<EditText>(R.id.etRW)
        val rgJenisKelamin = findViewById<RadioGroup>(R.id.rgJenisKelamin)
        val spStatus = findViewById<Spinner>(R.id.spStatus)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val tvData = findViewById<TextView>(R.id.tvData)

        // Isi spinner
        val statusList = listOf("Belum Menikah", "Menikah", "Cerai")
        spStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusList)

        // Tampilkan data awal
        tampilkanData(tvData)

        // Tombol SIMPAN
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val nik = etNIK.text.toString()
            val kab = etKabupaten.text.toString()
            val kec = etKecamatan.text.toString()
            val desa = etDesa.text.toString()
            val rt = etRT.text.toString()
            val rw = etRW.text.toString()
            val jenisKelaminId = rgJenisKelamin.checkedRadioButtonId
            val jenisKelamin = if (jenisKelaminId != -1)
                findViewById<RadioButton>(jenisKelaminId).text.toString() else ""

            val status = spStatus.selectedItem.toString()

            if (nama.isEmpty() || nik.isEmpty() || kab.isEmpty() || kec.isEmpty() || desa.isEmpty() ||
                rt.isEmpty() || rw.isEmpty() || jenisKelamin.isEmpty()
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Peringatan")
                    .setMessage("Semua data harus diisi!")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val wni = Wni(
                    nama = nama,
                    nik = nik,
                    kabupaten = kab,
                    kecamatan = kec,
                    desa = desa,
                    rt = rt,
                    rw = rw,
                    jenisKelamin = jenisKelamin,
                    statusPernikahan = status
                )

                lifecycleScope.launch {
                    wniDao.insert(wni)
                    tampilkanData(tvData)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Data disimpan", Toast.LENGTH_SHORT).show()
                    }
                    resetForm(etNama, etNIK, etKabupaten, etKecamatan, etDesa, etRT, etRW, rgJenisKelamin, spStatus)
                }
            }
        }

        // Tombol RESET: hapus semua data dari database + reset form
        btnReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus semua data yang tersimpan?")
                .setPositiveButton("Ya") { _, _ ->
                    lifecycleScope.launch {
                        wniDao.deleteAll()
                        tampilkanData(tvData)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Semua data telah dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                    resetForm(etNama, etNIK, etKabupaten, etKecamatan, etDesa, etRT, etRW, rgJenisKelamin, spStatus)
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun resetForm(
        etNama: EditText, etNIK: EditText, etKabupaten: EditText, etKecamatan: EditText,
        etDesa: EditText, etRT: EditText, etRW: EditText,
        rg: RadioGroup, sp: Spinner
    ) {
        etNama.text.clear()
        etNIK.text.clear()
        etKabupaten.text.clear()
        etKecamatan.text.clear()
        etDesa.text.clear()
        etRT.text.clear()
        etRW.text.clear()
        rg.clearCheck()
        sp.setSelection(0)
    }

    private fun tampilkanData(tv: TextView) {
        lifecycleScope.launch {
            val list = wniDao.getAll()
            val builder = StringBuilder()

            if (list.isEmpty()) {
                builder.append("Belum ada data warga yang tersimpan.")
            } else {
                builder.append("📋 Daftar Warga Negara Indonesia:\n\n")
                list.forEachIndexed { index, wni ->
                    builder.append("${index + 1}. Nama: ${wni.nama}\n")
                    builder.append("   NIK: ${wni.nik}\n")
                    builder.append("   Jenis Kelamin: ${wni.jenisKelamin}\n")
                    builder.append("   Status: ${wni.statusPernikahan}\n")
                    builder.append("   Alamat:\n")
                    builder.append("      RT ${wni.rt}/RW ${wni.rw}\n")
                    builder.append("      ${wni.desa}, ${wni.kecamatan}, ${wni.kabupaten}\n")
                    builder.append("────────────────────────────\n\n")
                }
            }

            runOnUiThread {
                tv.apply {
                    text = builder.toString()
                    setPadding(16, 16, 16, 16)
                    textSize = 15f
                    setLineSpacing(4f, 1f)
                }
            }
        }
    }
}
