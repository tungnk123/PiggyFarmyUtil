package com.example.piggyfarmyutil

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piggyfarmyutil.ThongTinAdapter
import com.example.piggyfarmyutil.ThongTinViewModel

class MainActivity : AppCompatActivity() {

    private val NUMBER_OF_PIGS: Int = 10

    private lateinit var thongTinViewModel: ThongTinViewModel
    private lateinit var thongTinAdapter: ThongTinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNhap: Button = findViewById(R.id.btn_nhap)
        val spHeotitle: Spinner = findViewById(R.id.sp_heoTitle)
        val edtTrongLuongHeo: EditText = findViewById(R.id.edt_trongLuongHeo)
        val rcvThongTinHeo: RecyclerView = findViewById(R.id.rcv_thongTinHeo)

        // Initialize ViewModel and Adapter
        thongTinViewModel = ViewModelProvider(this).get(ThongTinViewModel::class.java)
        thongTinAdapter = ThongTinAdapter(this, emptyList())
        rcvThongTinHeo.adapter = thongTinAdapter
        rcvThongTinHeo.layoutManager = LinearLayoutManager(this)

        // Populate the Spinner with initial data
        val myNewImmutableList = mutableListOf("Chọn cá thể heo")
        for (i in 1..NUMBER_OF_PIGS) {
            myNewImmutableList.add("Cá thể heo số $i")
        }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            myNewImmutableList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spHeotitle.adapter = adapter
        }

        // Button click listener
        btnNhap.setOnClickListener {
            val selectedItem = spHeotitle.selectedItem as String

            val trongLuong = edtTrongLuongHeo.text.toString().toIntOrNull()
            Toast.makeText(this, trongLuong.toString(), Toast.LENGTH_LONG).show()
            if (trongLuong != null) {
                thongTinViewModel.addItemToThongTin(Pair(selectedItem, trongLuong))
            } else {
                // Handle invalid input, show an error message, etc.
            }
        }
        thongTinViewModel.listThongTin.observe(this) { newList ->
            thongTinAdapter.updateData(newList)
        }
    }
}
