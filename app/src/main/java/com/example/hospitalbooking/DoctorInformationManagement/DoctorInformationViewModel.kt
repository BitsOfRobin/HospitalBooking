package com.example.hospitalbooking.DoctorInformationManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.KotlinClass.ModalFormMain

class DoctorInformationViewModel(private var str: String):ViewModel() {

      val _validString=MutableLiveData<Boolean>()
    val validString: LiveData<Boolean>
        get() = _validString

    val _string=MutableLiveData<String>()
    val string: LiveData<String>
        get() = _string



        init {

            _string.value=str
            getString()
        }




    fun getString(){



        _validString.value=isLetters(str)

    }


    fun validString(errStr:String){


        _validString.value=isLetters(errStr)


    }



     fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }





}