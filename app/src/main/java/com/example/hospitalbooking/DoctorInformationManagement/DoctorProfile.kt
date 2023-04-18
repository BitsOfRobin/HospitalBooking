package com.example.hospitalbooking.DoctorInformationManagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hospitalbooking.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DoctorProfile : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        var hospital = ""
        var doctorName = ""
        var doctorSpecialist = ""

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val userGoogle = Firebase.auth.currentUser
        var dtname=""
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
//                name.text=dtname
            }

        }
        val firebaseImg= findViewById<ImageView>(R.id.ImgMed)
        firebaseImg.setImageURI(null)

        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
            docRef?.whereEqualTo("name", dtname)
                ?.get()?.addOnSuccessListener {
                    for(document in it){
                        hospital = document.get("hospital").toString()
                        doctorName = document.get("name").toString()
                        doctorSpecialist = document.get("pro").toString()

                    }
                    val docName = findViewById<TextView>(R.id.doc_Name)
                    val docLocation = findViewById<TextView>(R.id.doc_Hosp)
                    val docJob = findViewById<TextView>(R.id.doc_Pro)
                    docName.text = dtname
                    docLocation.text = hospital
                    docJob.text = doctorSpecialist
                }
            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }



    }
}