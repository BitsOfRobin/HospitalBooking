package com.example.hospitalbooking.DoctorInformationManagement

import android.provider.SyncStateContract.Helpers.update
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class SummarizeReportViewModel: ViewModel() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore

    private val _averageRating  = MutableLiveData<Double>()
    val averageRating: LiveData<Double>
        get() = _averageRating

//    private val _feedbackAnsOne = MutableLiveData<String>()
//    val feedbackAnsOne: LiveData<String>
//        get() = _feedbackAnsOne
//
//    private val _feedbackAnsTwo = MutableLiveData<String>()
//    val feedbackAnsTwo: LiveData<String>
//        get() = _feedbackAnsTwo
//
//    private val _feedbackComment = MutableLiveData<String>()
//    val feedbackComment: LiveData<String>
//        get() = _feedbackComment

    init {
        readUser()
    }

    fun readUser(){
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
    }
    fun calculateAverageRating(docName: String) {
        val comment = "commented"
        val documentRef = mFirebaseDatabaseInstance.collection("userAppointment")
        documentRef.whereEqualTo("docName", docName)
            .whereEqualTo("commentStatus", comment)
            .get().addOnSuccessListener {
                var totalRating = 0.0
                var ratingCount = 0

                for (document in it) {
                    val rating = document.getDouble("rateStar")
                    if (rating != null) {
                        totalRating += rating
                        ratingCount++
                    }
                }

                var averageRating = if (ratingCount > 0)
                    totalRating / ratingCount
                else
                    0.0

                // Pass the docName, averageRating, and ratingCount to updateAppointmentRating function in summarizeReportViewModel
                updateAppointmentRating(docName, averageRating, ratingCount)
                _averageRating.value = averageRating
            }
    }

    fun updateAppointmentRating(docName: String, rating: Double, ratingCount: Int) {
        val documentRef = mFirebaseDatabaseInstance.collection("doctor")
        documentRef.whereEqualTo("name", docName)
            .get().addOnSuccessListener {
                val appointmentDoc = it.documents[0]
                val currentRating = rating
                val numRatings = ratingCount

                // Update the appointment with the new rating and number of ratings
                appointmentDoc.reference.update(
                    mapOf(
                        "rateFrequency" to currentRating,
                        "numRatings" to numRatings
                    )
                )
            }
    }
}