package com.example.hospitalbooking.DoctorInformationManagement

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalbooking.KotlinClass.MyCache
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



        val docName = findViewById<TextView>(R.id.dotName)
        val docJob = findViewById<TextView>(R.id.dtPro)
        val docLocation = findViewById<TextView>(R.id.dtHos)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)


        val userGoogle = Firebase.auth.currentUser
        var dtname = ""
        var hospital = ""
        var doctorSpecialist = ""

        userGoogle.let {
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
            }

        }
        val cache = MyCache()
        val bitmap = cache.retrieveBitmapFromCache(dtname)
        val imgDoct = findViewById<ImageView>(R.id.ImgMed)
        imgDoct.setImageBitmap(bitmap)

        val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
        documentRef.whereEqualTo("name", dtname)
            .get().addOnSuccessListener {
                for(document in it){
                    doctorSpecialist = document.get("pro").toString()
                    hospital = document.get("hospital").toString()
                }
                docName.text = dtname
                docJob.text = doctorSpecialist
                docLocation.text = hospital
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }


        summarizeReportViewModel = ViewModelProvider(this).get(SummarizeReportViewModel::class.java)
        summarizeReportViewModel.calculateAverageRating(dtname)

        summarizeReportViewModel.averageRating.observe(this, Observer { averageRating ->
            // Update the UI with the average rating
            ratingBar.rating = averageRating.toFloat()
        })
//        var rating = 0.0
//        summarizeReportViewModel.updateAppointmentRating(dtname, rating)
//        ratingBar.setOnRatingBarChangeListener { _, rate, _ ->
//            ratingBar.rating = rate
//            Toast.makeText(this, "Rate $rate", Toast.LENGTH_SHORT).show()
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}