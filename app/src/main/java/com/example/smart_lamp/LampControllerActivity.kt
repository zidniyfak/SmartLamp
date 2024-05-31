package com.example.smart_lamp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_lamp.adapter.LampAdapter
import com.example.smart_lamp.databinding.ActivityLampControllerBinding

class LampControllerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLampControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLampControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val dataset = arrayOf("Lamp 1", "Lamp 2", "Lamp 3", "Lamp 4", "Lamp 5", "Lamp 6")
//        val lampAdapter = LampAdapter(dataset)
//
//        val recyclerView: RecyclerView = findViewById(R.id.rv_lamp_list)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = lampAdapter

        binding.back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.lampSwitch1.addOnStatusChangedListener { isChecked ->
            // Lakukan sesuatu ketika status berubah
            val imageResource = if (isChecked) {
                R.drawable.ic_bulb_on // Gambar untuk status tercentang
            } else {
                R.drawable.ic_bulb_off // Gambar untuk status tidak tercentang
            }
            binding.ivLamp1.setImageResource(imageResource)
        }

        binding.lampSwitch2.addOnStatusChangedListener { isChecked ->
            // Lakukan sesuatu ketika status berubah
            val imageResource = if (isChecked) {
                R.drawable.ic_bulb_on // Gambar untuk status tercentang
            } else {
                R.drawable.ic_bulb_off // Gambar untuk status tidak tercentang
            }
            binding.ivLamp2.setImageResource(imageResource)
        }

        binding.lampSwitch3.addOnStatusChangedListener { isChecked ->
            // Lakukan sesuatu ketika status berubah
            val imageResource = if (isChecked) {
                R.drawable.ic_bulb_on // Gambar untuk status tercentang
            } else {
                R.drawable.ic_bulb_off // Gambar untuk status tidak tercentang
            }
            binding.ivLamp3.setImageResource(imageResource)
        }



        binding.fabAddLamp.setOnClickListener{
            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_add_lamp)
            dialog.setCancelable(false)

            dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener{
                dialog.dismiss()
            }
            dialog.show()


        }

    }
}