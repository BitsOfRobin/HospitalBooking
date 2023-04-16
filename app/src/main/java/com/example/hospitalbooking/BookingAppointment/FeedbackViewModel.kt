package com.example.hospitalbooking.BookingAppointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedbackViewModel:ViewModel() {
    val _ansOneRadioButton = MutableLiveData<String>()
    val ansOneRadioButton : LiveData<String>
        get() = _ansOneRadioButton

    val _ansTwoRadioButton = MutableLiveData<String>()
    val ansTwoRadioButton : LiveData<String>
        get() = _ansTwoRadioButton

    val _ratingValue = MutableLiveData<Float>()
    val ratingValue : LiveData<Float>
        get() = _ratingValue

    fun radioButtonQuesOneValue(value: String) {
        ansOneRadioButton
    }

    fun radioButtonQuesTwoValue(value: String) {
        ansTwoRadioButton
    }

    fun onRatingBarChanged(value: Float) {
        ratingValue
    }
}