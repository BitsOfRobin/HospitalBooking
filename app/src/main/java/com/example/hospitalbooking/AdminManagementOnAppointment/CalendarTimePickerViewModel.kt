package com.example.hospitalbooking.AdminManagementOnAppointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.google.firebase.firestore.FirebaseFirestore

class CalendarTimePickerViewModel: ViewModel() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore

    private val _feedbackReview = MutableLiveData<List<feedbackReview>>()
    val feedbackReview: LiveData<List<feedbackReview>>
        get() = _feedbackReview

    init {
        readUser()
    }

    private fun readUser() {
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
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