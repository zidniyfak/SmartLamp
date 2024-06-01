package com.example.smart_lamp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_lamp.adapter.LampAdapter
import com.example.smart_lamp.databinding.ActivityLampControllerBinding
import com.example.smart_lamp.model.LampResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LampControllerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLampControllerBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var dialog: Dialog
    private var lampList: ArrayList<LampResponse> = arrayListOf()
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLampControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("Lamp")
        dialog = Dialog(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        getLampData()
    }

    private fun setupUI() {
        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.rvLampList.layoutManager = LinearLayoutManager(this)
        binding.rvLampList.setHasFixedSize(true)

        binding.fabAddLamp.setOnClickListener {
            showAddLampDialog()
        }
    }

    private fun showAddLampDialog() {
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

    private fun getLampData() {
        binding.rvLampList.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lampList.clear()
                if (snapshot.exists()) {
                    for (lampSnap in snapshot.children) {
                        val lampData = lampSnap.getValue(LampResponse::class.java)
                        lampData?.let { lampList.add(it) }
                    }
                    setupAdapter()
                    binding.rvLampList.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleFirebaseError(error)
            }
        })
    }

    private fun setupAdapter() {
        val mAdapter = LampAdapter(lampList, object : LampAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("TAG", "onItemClick: $position")
                showEditLampDialog(position)
            }

            override fun onItemSwitch(position: Int, isChecked: Boolean) {
                updateLampStatus(position, isChecked)
            }
        })
        binding.rvLampList.adapter = mAdapter
    }

    private fun updateLampStatus(position: Int, isChecked: Boolean) {
        val lamp = lampList[position]
        val firebaseStatus = if (isChecked) 1 else 0

        if (lamp.localStatus != firebaseStatus) {
            lamp.localStatus = firebaseStatus
            val imageView = binding.rvLampList.findViewHolderForAdapterPosition(position)
                ?.itemView?.findViewById<ImageView>(R.id.iv_lamp)
            imageView?.setImageResource(if (isChecked) R.drawable.ic_bulb_on else R.drawable.ic_bulb_off)

            Log.d(
                "LampControllerActivity",
                "Lamp: ${lamp.lampName}, Firebase Status: $firebaseStatus"
            )
            updateLampStatusInFirebase(lamp.lampId, firebaseStatus)
        }
    }

    private fun updateLampStatusInFirebase(lampId: String?, status: Int) {
        if (isUpdating) return

        isUpdating = true
        val lampRef = firebaseRef.child(lampId.toString())
        lampRef.child("firebaseStatus").setValue(status).addOnSuccessListener {
            val updatedLamp = lampList.find { it.lampId == lampId }
            updatedLamp?.let {
                val index = lampList.indexOf(it)
                if (index != -1) {
                    lampList[index] = it.copyWithStatus(status, status)
                    binding.rvLampList.adapter?.notifyItemChanged(index)
                    Toast.makeText(this, "Lamp status updated successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            isUpdating = false
        }.addOnFailureListener { error ->
            Log.e("LampControllerActivity", "Failed to update lamp status: ${error.message}")
            Toast.makeText(this, "Failed to update lamp status", Toast.LENGTH_SHORT).show()
            isUpdating = false
        }
    }

    private fun showEditLampDialog(position: Int) {
        val lamp = lampList[position]
        dialog.setContentView(R.layout.dialog_edit_lamp)
        dialog.setCancelable(false)

        val etLamp = dialog.findViewById<EditText>(R.id.et_lamp_name)
        etLamp.setText(lamp.lampName)

        dialog.findViewById<Button>(R.id.btn_save_edit).setOnClickListener {
            val newLampName = etLamp.text.toString().trim()
            if (newLampName.isNotEmpty()) {
                updateLampNameInFirebase(lamp.lampId, newLampName)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Lamp name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.findViewById<Button>(R.id.btn_cancel_edit).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateLampNameInFirebase(lampId: String?, newLampName: String) {
        val lampRef = firebaseRef.child(lampId.toString())
        lampRef.child("lampName").setValue(newLampName).addOnSuccessListener {
            Toast.makeText(this, "Lamp name updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update lamp name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLampData() {
        val etLamp = dialog.findViewById<EditText>(R.id.et_lamp_name)
        val lampName = etLamp.text.toString()

        if (lampName.isEmpty()) {
            etLamp.error = "Lamp name cannot be empty"
            return
        }

        val lampId = firebaseRef.push().key!!
        val lamp = LampResponse(lampId, lampName, "#000000", 0, 0, 0, 0, 0)

        if (lampList.none { it.localStatus == 1 }) {
            firebaseRef.child(lampId).setValue(lamp).addOnCompleteListener {
                Toast.makeText(this, "Lamp added successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Failed to add lamp: ${err.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                this,
                "Please turn off all lamps before adding a new one",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleFirebaseError(error: DatabaseError) {
        Log.e("LampControllerActivity", "Failed to read lamp data: ${error.message}")
        Toast.makeText(this, "Failed to read lamp data", Toast.LENGTH_SHORT).show()
    }
}
