package com.example.smart_lamp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smart_lamp.databinding.ActivityLampControllerBinding
import com.example.smart_lamp.model.LampResponse
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LampControllerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLampControllerBinding

    private lateinit var firebaseRef: DatabaseReference

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLampControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("Lamp")

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

        binding.back.setOnClickListener {
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
            val imageResource = if (isChecked) {
                R.drawable.ic_bulb_on
            } else {
                R.drawable.ic_bulb_off
            }
            binding.ivLamp3.setImageResource(imageResource)
        }

        binding.fabAddLamp.setOnClickListener {
            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_add_lamp)
            dialog.setCancelable(false)

            dialog.findViewById<Button>(R.id.btn_save_add).setOnClickListener {
                saveLampData()
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.btn_cancel_add).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()


        }

    }

    private fun saveLampData() {
        val etLamp = dialog.findViewById<EditText>(R.id.et_lamp_name)

        // Default value
        val red = 0
        val green = 0
        val blue = 0
        val hexCode = "#000000"
        val status = 0

        val lampName = etLamp.text.toString()

        if (lampName.isEmpty()) {
            etLamp.error = "Lamp name cannot be empty"
        }

        val lampId = firebaseRef.push().key!!

        val lamp = LampResponse(lampId, lampName, hexCode, red, green, blue, status)

        firebaseRef.child(lampId).setValue(lamp).addOnCompleteListener {
            Toast.makeText(this, "Lamp added successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Failed to add lamp : ${err.message}", Toast.LENGTH_SHORT).show()
        }


    }
}