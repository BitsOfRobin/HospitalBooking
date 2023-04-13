package com.example.hospitalbooking.DoctorInformationManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalbooking.BookingAppointment.MainPageViewModel

class DoctorInformationViewModelFactory(private val str:String):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorInformationViewModel::class.java)) {
            return DoctorInformationViewModel(str) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}