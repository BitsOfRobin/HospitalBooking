package com.example.hospitalbooking.BookingAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalbooking.KotlinClass.ModalFormMain

    class MainPageViewModelFactory(private val p0:ArrayList<String>, private val i:Int):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            return MainPageViewModel(p0,i) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}