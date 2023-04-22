package com.example.hospitalbooking.DoctorInformationManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class SummarizeReportViewModel: ViewModel() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore

    val _feedbackRateStar = MutableLiveData<String>()
    val feedbackRateStar: LiveData<String>
        get() = _feedbackRateStar

    val _feedbackAnsOne = MutableLiveData<String>()
    val feedbackAnsOne: LiveData<String>
        get() = _feedbackAnsOne

    val _feedbackAnsTwo = MutableLiveData<String>()
    val feedbackAnsTwo: LiveData<String>
        get() = _feedbackAnsTwo

    val _feedbackComment = MutableLiveData<String>()
    val feedbackComment: LiveData<String>
        get() = _feedbackComment




}