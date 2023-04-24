package com.example.hospitalbooking.DoctorInformationManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.google.firebase.firestore.FirebaseFirestore

class SummarizeReportViewModel: ViewModel() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore

    private val _averageRating = MutableLiveData<Double>()
    val averageRating: LiveData<Double>
        get() = _averageRating

    private val _feedbackAnsOne = MutableLiveData<List<Int>>()
    val feedbackAnsOne: LiveData<List<Int>>
        get() = _feedbackAnsOne

    private val _feedbackAnsTwo = MutableLiveData<List<Int>>()
    val feedbackAnsTwo: LiveData<List<Int>>
        get() = _feedbackAnsTwo

    private val _feedbackReview = MutableLiveData<List<feedbackReview>>()
    val feedbackReview: LiveData<List<feedbackReview>>
        get() = _feedbackReview

    init {
        readUser()
    }

    private fun readUser() {
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

                val averageRating = if (ratingCount > 0)
                    totalRating / ratingCount
                else
                    0.0

                // Pass the docName, averageRating, and ratingCount to updateAppointmentRating function in summarizeReportViewModel
                updateAppointmentRating(docName, averageRating, ratingCount)
                _averageRating.value = averageRating
            }
    }

    private fun updateAppointmentRating(docName: String, rating: Double, ratingCount: Int) {
        val documentRef = mFirebaseDatabaseInstance.collection("doctor")
        documentRef.whereEqualTo("name", docName)
            .get().addOnSuccessListener {
                val appointmentDoc = it.documents[0]

                // Update the appointment with the new rating and number of ratings
                appointmentDoc.reference.update(
                    mapOf(
                        "rateFrequency" to rating,
                        "numRatings" to ratingCount
                    )
                )
            }
    }

    fun questionOneResult(docName: String) {
        val comment = "commented"
        val documentRef = mFirebaseDatabaseInstance.collection("userAppointment")
        documentRef.whereEqualTo("docName", docName)
            .whereEqualTo("commentStatus", comment)
            .get().addOnSuccessListener {
                val data = mutableListOf(0, 0, 0, 0)

                for (document in it) {
                    val ansOne = document.getString("radioAns1")
                    val ansOneOption = arrayOf("Excellent", "Good", "Fair", "Poor")

                    if (ansOne != null && ansOne in ansOneOption) {
                        when (ansOne) {
                            "Excellent" -> data[0]++
                            "Good"  -> data[1]++
                            "Fair"  -> data[2]++
                            "Poor"  -> data[3]++
                        }
                    } else {
                        Log.w(this.toString(), "Incorrect database collection")
                    }
                }
                _feedbackAnsOne.value = data
            }
    }
    fun questionTwoResult(docName: String) {
        val comment = "commented"
        val documentRef = mFirebaseDatabaseInstance.collection("userAppointment")
        documentRef.whereEqualTo("docName", docName)
            .whereEqualTo("commentStatus", comment)
            .get().addOnSuccessListener {
                val data = mutableListOf(0, 0, 0, 0)

                for (document in it) {
                    val ansTwo = document.getString("radioAns2")
                    val ansTwoOption = arrayOf("The doctor communicated clearly and effectively", "The doctor was mostly clear in their communication", "The doctor's communication was somewhat unclear", "The doctor's communication was difficult to understand")

                    if (ansTwo != null && ansTwo in ansTwoOption) {
                        when (ansTwo) {
                            "The doctor communicated clearly and effectively" -> data[0]++
                            "The doctor was mostly clear in their communication"  -> data[1]++
                            "The doctor's communication was somewhat unclear"  -> data[2]++
                            "The doctor's communication was difficult to understand"  -> data[3]++
                        }
                    } else {
                        Log.w(this.toString(), "Incorrect database collection")
                    }
                }
                _feedbackAnsTwo.value = data
            }
    }

    fun feedbackReview(docName: String) {
        val comment = "commented"
        val feedbackList = arrayListOf<feedbackReview>()
        val documentRef = mFirebaseDatabaseInstance.collection("userAppointment")
        documentRef.whereEqualTo("docName", docName)
            .whereEqualTo("commentStatus", comment)
            .get().addOnSuccessListener {

                for (document in it) {
                    val feedbackUser = document.getString("user")
                    val feedbackRateStar = document.getDouble("rateStar")
                    val feedbackComment = document.getString("comment")
                    if (feedbackUser != null && feedbackRateStar != null && feedbackComment != null){
                        feedbackList.add(feedbackReview(feedbackUser, feedbackRateStar, feedbackComment))
                    }
                    else{
                        Log.w(this.toString(), "Incorrect database collection")
                    }
                }
                _feedbackReview.value = feedbackList
            }
    }
}