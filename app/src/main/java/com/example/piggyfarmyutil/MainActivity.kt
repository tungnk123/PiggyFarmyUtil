package com.example.piggyfarmyutil

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.m
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private var NUMBER_OF_PIGS: Int = 10
    val myNewImmutableList : MutableList<String> = mutableListOf("Chọn cá thể heo")

    private lateinit var thongTinViewModel: ThongTinViewModel
    private lateinit var thongTinAdapter: ThongTinAdapter

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNhap: Button = findViewById(R.id.btn_nhap)
        val spHeotitle: Spinner = findViewById(R.id.sp_heoTitle)
        val edtTrongLuongHeo: EditText = findViewById(R.id.edt_trongLuongHeo)
        val rcvThongTinHeo: RecyclerView = findViewById(R.id.rcv_thongTinHeo)

        val btnXong: Button = findViewById(R.id.btn_xong)
        val btnHuy: Button = findViewById(R.id.btn_huy)


        GetSoHeoFromFireStore()


        // Initialize ViewModel and Adapter
        thongTinViewModel = ViewModelProvider(this).get(ThongTinViewModel::class.java)
        thongTinAdapter = ThongTinAdapter(this, emptyList())
        rcvThongTinHeo.adapter = thongTinAdapter
        rcvThongTinHeo.layoutManager = LinearLayoutManager(this)


        // Populate the Spinner with initial data


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
            if (trongLuong != null) {
                thongTinViewModel.addItemToThongTin(Pair(selectedItem, trongLuong))
            } else {
                // Handle invalid input, show an error message, etc.
            }
            val curIndex = myNewImmutableList.indexOf(selectedItem)
            val nextIndex = (curIndex + 1) % myNewImmutableList.size
            spHeotitle.setSelection(nextIndex)
            updateThongKe()
        }
        thongTinViewModel.listThongTin.observe(this) { newList ->
            thongTinAdapter.updateData(newList)

        }



        btnXong.setOnClickListener {
            SendDataToFireStore()
        }
    }

    fun GetSoHeoFromFireStore() {
        db.collection("number")
            .document("xuatheo")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val soHeo = documentSnapshot.getLong("SoHeo")?.toInt() ?:10
                    NUMBER_OF_PIGS = soHeo
                    for (i in 1..NUMBER_OF_PIGS) {
                        myNewImmutableList.add("Cá thể heo số $i")
                    }

                } else {
                    Toast.makeText(this, "Not exist", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }

    }

    fun SendDataToFireStore() {

        val map = thongTinViewModel.uniqueItemsMap
        map.put("Size", NUMBER_OF_PIGS)
        map.put("TimeStamp", System.currentTimeMillis().toInt())
        db.collection("data")
            .add(thongTinViewModel.uniqueItemsMap)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Save data successfully!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun updateThongKe() {
        var tongTrongLuong: Int = 0
        var trongLuongTrungBinh : Float = 0.0f
        var countHeo0Toi50 : Int = 0
        var countHeo50Toi70 : Int = 0
        var countHeo70Toi90 : Int = 0
        var countHeo90Toi110 : Int = 0
        var countHeoTren110 : Int = 0

        for ((key, value) in thongTinViewModel.uniqueItemsMap) {
            tongTrongLuong += value
            if (value in 0..49) {
                countHeo0Toi50++
            }
            else if (value in 50..70) {
                countHeo50Toi70++
            }
            else if (value in 70..90) {
                countHeo70Toi90++
            }
            else if (value in 90..110) {
                countHeo90Toi110++
            }
            else if (value > 110){
                countHeoTren110++
            }

        }
        trongLuongTrungBinh = tongTrongLuong.toFloat() / thongTinViewModel.uniqueItemsMap.size


        val tvTongTrongLuong: TextView = findViewById(R.id.tv_thongKe_tongTrongLuongValue)
        val tvTrongLuongTrungBinh: TextView = findViewById(R.id.tv_thongKe_trongLuongTrungBinhValue)
        val tvSoHeo0Toi50: TextView = findViewById(R.id.tv_thongKe_soHeo0Toi50Value)
        val tvSoHeo50Toi70: TextView = findViewById(R.id.tv_thongKe_soHeo50Toi70Value)
        val tvSoHeo70Toi90: TextView = findViewById(R.id.tv_thongKe_soHeo70Toi90Value)
        val tvSoHeo90Toi110: TextView = findViewById(R.id.tv_thongKe_soHeo90Toi110Value)
        val tvSoHeoTren110: TextView = findViewById(R.id.tv_thongKe_soHeoTren110Value)

        tvTongTrongLuong.text = tongTrongLuong.toString()
        tvTrongLuongTrungBinh.text = trongLuongTrungBinh.toString()
        tvSoHeo0Toi50.text = countHeo0Toi50.toString()
        tvSoHeo50Toi70.text = countHeo50Toi70.toString()
        tvSoHeo70Toi90.text = countHeo70Toi90.toString()
        tvSoHeo90Toi110.text = countHeo90Toi110.toString()
        tvSoHeoTren110.text = countHeoTren110.toString()
    }
}
