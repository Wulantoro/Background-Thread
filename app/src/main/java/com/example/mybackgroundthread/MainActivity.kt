package com.example.mybackgroundthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        val executor = Executors.newSingleThreadExecutor() //newSingleThreadExecutor yang artinya hanya satu thread yang Anda buat
        val handler = Handler(Looper.getMainLooper()) //getMainLooper karena Anda ingin proses yang di dalam Handler dijalankan di main/ui thread.

        btnStart.setOnClickListener{

            //memnggunakan coroutine
            //launch karena kita akan memulai background process tanpa nilai kembalian alias fire and forget
            //Dispatchers.Default dipilih karena kita akan melakukan proses biasa di Background Thread
            // yang tidak memerlukan proses read-write
            lifecycleScope.launch(Dispatchers.Default) {
                //simulate process in background thread
                for (i in 0..10) {
                    delay(500)
                    val percentage = i * 10

                    //withContext(Dispatchers.Main) karena kita perlu pindah ke Main Thread untuk update UI berupa TextView,
                    withContext(Dispatchers.Main) {
                        //update ui in main thread
                        if (percentage == 100) {
                            tvStatus.setText(R.string.task_completed)
                        } else {
                            tvStatus.text = String.format(getString(R.string.compressing), percentage)
                        }
                    }
                }
            }

            //menggunakan eksekutor apk berjalan lancar
//            executor.execute {
//                try {
//                    // //simulate process in background thread
//                    for (i in 0..10) {
//                        Thread.sleep(500)
//                        val percentage = i * 10

            //Handler.post untuk berpindah antar thread tersebut.
//                        handler.post{
//                            //update ui in main thread
//                            if (percentage == 100) {
//                                tvStatus.setText(R.string.task_completed)
//                            } else {
//                                tvStatus.text = String.format(getString(R.string.compressing), percentage)
//                            }
//                        }
//                    }
//                } catch (e : InterruptedException) {
//                    e.printStackTrace()
//                }
//            }


            //apk delay 5 detik saat di scroll
//            try {
//                //simulate process compressing
//                for (i in 0..10) {
//                    Thread.sleep(500)
//                    val percentage = i * 10
//                    if (percentage == 100) {
//                        tvStatus.setText(R.string.task_completed)
//                    } else {
//                        tvStatus.text = String.format(getString(R.string.compressing), percentage)
//                    }
//                }
//            } catch (e : InterruptedException) {
//                e.printStackTrace()
//            }
        }
    }
}