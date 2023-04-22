package com.example.hospitalbooking.DoctorInformationManagement

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hospitalbooking.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class DoctorSummarizeReport : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore ?= null
    private lateinit var  summarizeReportViewModel: SummarizeReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_summarize_report)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Summarize Report")
        var doctorSpecialist = ""

        val docJob = findViewById<TextView>(R.id.dtPro)
        val docName = findViewById<TextView>(R.id.dotName)
        val firebaseImg = findViewById<ImageView>(R.id.ImgMed)

        val userGoogle = Firebase.auth.currentUser
        var dtname=""


        userGoogle.let {
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
            }

        }

        val storageReference= FirebaseStorage.getInstance().getReference("Img/$dtname.jpg")
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            // Successfully downloaded data to bytes
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            firebaseImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }

        val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
        documentRef.whereEqualTo("name", dtname)
            .get().addOnSuccessListener {
                for(document in it){
                    doctorSpecialist = document.get("pro").toString()
                }
                docJob.text = doctorSpecialist
                docName.text = dtname
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}