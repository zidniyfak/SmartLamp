package com.example.smart_lamp

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smart_lamp.databinding.ActivityTimerBinding
import java.util.Calendar

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.fabAddTimer.setOnClickListener{
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        binding.fabAddTimer.setOnClickListener{
            val hours = Calendar.HOUR
            val minutes = Calendar.MINUTE
            val timePicker = TimePickerDialog(this, { _, hour, minute ->
                val format = String.format("%02d:%02d", hour, minute)
                binding.tvTimer.text = format}, hours, minutes, true)
            timePicker.show()
        }
    }


}