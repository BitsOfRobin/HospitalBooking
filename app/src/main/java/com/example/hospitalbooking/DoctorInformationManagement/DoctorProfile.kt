package com.example.hospitalbooking.DoctorInformationManagement

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hospitalbooking.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class DoctorProfile : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore ? = null
    private val arrayListHos=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Retrieve value from Firebase
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        // Navigation Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Profile")

        // Declare variable
        var hospital = ""
        var hospitalTxt = ""
        var doctorName = ""
        var doctorSpecialist = ""

        val docName = findViewById<TextView>(R.id.doc_Name)
        val docJobText = findViewById<EditText>(R.id.doc_Pro)
        val autoCompleteHospital= findViewById<AutoCompleteTextView>(R.id.autoCurrentHospital)
        val docLocationText = findViewById<EditText>(R.id.doc_Hosp)
        val firebaseImg = findViewById<ImageView>(R.id.ImgMed)

        // Authorized current user and pass value to the variable
        val userGoogle = Firebase.auth.currentUser
        var dtname=""
        userGoogle.let {
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
            }

        }

        // Button variable
        val editProfile = findViewById<Button>(R.id.editBtn)
        val updateProfile = findViewById<Button>(R.id.updateBtn)
        val selectImage = findViewById<Button>(R.id.picsBtn)
//        val cancelEdit = findViewById<Button>(R.id.cancelBtn)

        // Retrieve image from Firebase Storage
        val storageReference= FirebaseStorage.getInstance().getReference("Img/$dtname.jpg")
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            // Successfully downloaded data to bytes
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            firebaseImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }

        // Retrieve User ID and pass to variable display the value
        val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
        documentRef.whereEqualTo("name", dtname)
            .get().addOnSuccessListener {
                for(document in it){
                    hospital = document.get("hospital").toString()
                    doctorName = document.get("name").toString()
                    doctorSpecialist = document.get("pro").toString()
                }
                docName.text = dtname
                docJobText.setText(doctorSpecialist)
                docLocationText.setText(hospital)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }

        getDoctorHos()

        // Edit Button Function
        editProfile.setOnClickListener {
//            val intent = Intent(this, EditDoctorProfile::class.java)
//            startActivity(intent)
            // Make text fields editable
            docJobText.isEnabled = true
            autoCompleteHospital.isEnabled = true
            docLocationText.isEnabled = true
            firebaseImg.isEnabled = true

            // Hide the "Edit" button and show the "Update" and "Cancel" buttons
            editProfile.visibility = View.GONE
            updateProfile.visibility = View.VISIBLE
            selectImage.visibility = View.VISIBLE
//            cancelButton.visibility = View.VISIBLE
            autoCompleteHospital.visibility = View.VISIBLE
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListHos)
        autoCompleteHospital.setAdapter(adapter)
        autoCompleteHospital.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            hospital = parent.getItemAtPosition(position) as String

            docLocationText.setText(hospital)
        }

        updateProfile.setOnClickListener {
            // Save changes to Firebase


            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            // Make text fields read-only
            docJobText.isEnabled = false
            autoCompleteHospital.isEnabled = false
            docLocationText.isEnabled = false
            firebaseImg.isEnabled = false

            // Hide the "Update" and "Cancel" buttons and show the "Edit" button
            editProfile.visibility = View.VISIBLE
            updateProfile.visibility = View.GONE
            selectImage.visibility = View.GONE
//            cancelButton.visibility = View.VISIBLE
            autoCompleteHospital.visibility = View.GONE
        }
    }
    private fun getDoctorHos(){
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.get()?.addOnSuccessListener {
            arrayListHos.clear()
            for(document in it){
                val hospital = document.get("hospital").toString()
                arrayListHos.add(hospital)
            }
        }
            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}