package com.example.hospitalbooking.AdminManagementOnAppointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.google.firebase.firestore.FirebaseFirestore

class CalendarTimePickerViewModel: ViewModel() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore

    private val _averageRating = MutableLiveData<Double>()
    val averageRating: LiveData<Double>
        get() = _averageRating

    private val _feedbackReview = MutableLiveData<List<feedbackReview>>()
    val feedbackReview: LiveData<List<feedbackReview>>
        get() = _feedbackReview

    private val _numberFeedback = MutableLiveData<Int>()
    val numberFeedback: LiveData<Int>
        get() = _numberFeedback

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

                _averageRating.value = averageRating
                _numberFeedback.value = ratingCount
            }
    }

    fun feedbackReview(docName: String) {
        val comment = "commented"
        var numRate = 0
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
                        numRate++
                    }
                    else{
                        Log.w(this.toString(), "Incorrect database collection")
                    }
                }
                _feedbackReview.value = feedbackList
                _numberFeedback.value = numRate
            }
    }


}