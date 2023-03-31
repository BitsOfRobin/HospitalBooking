package com.example.hospitalbooking.BookingAppointment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.KotlinClass.ModalFormMain

class MainPageViewModel(val arrayListModalFormMain:ArrayList<ModalFormMain>):ViewModel() {


    val _modalList= MutableLiveData<ArrayList<ModalFormMain>>()
    val modalList: LiveData<ArrayList<ModalFormMain>>
        get() = _modalList


    init {
        _modalList.value=arrayListModalFormMain




        Log.i("ScoreViewModel", "Final score is $arrayListModalFormMain")

    }










}