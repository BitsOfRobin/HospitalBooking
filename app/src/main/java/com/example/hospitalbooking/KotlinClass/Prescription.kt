package com.example.hospitalbooking.KotlinClass

class Prescription {


    var user: String? =null
    var doc: String? =null
    var payStatus: String? =null
    var appointment: String? =null
    var medicine: String? =null
    var ratingStar:Float? =0F


    constructor()

    constructor(user:String,doc:String,medicine:String,ratingStar: Float,payStatus:String,appointment:String){
        this.user=user
        this.doc=doc
        this.medicine=medicine
        this.appointment=appointment
        this.payStatus=payStatus





    }

    fun setRatingStar(ratingStar: Float) {
        this.ratingStar = ratingStar
    }


}