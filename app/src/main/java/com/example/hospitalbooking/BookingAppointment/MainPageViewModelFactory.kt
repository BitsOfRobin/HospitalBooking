package com.example.hospitalbooking.BookingAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalbooking.KotlinClass.ModalFormMain

class MainPageViewModelFactory(private val arrayListModalFormMain:ArrayList<ModalFormMain>):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            return MainPageViewModel(arrayListModalFormMain) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}