package com.example.hospitalbooking.KotlinClass

class Prescription {


    var user: String? =null
    var doc: String? =null
    var visitStatus: String? =null
    var appointment: String? =null
    var medicine: String? =null
    var ratingStar:Float? =0F


    constructor()

    constructor(user:String,doc:String,medicine:String,ratingStar: Float,visitStatus:String,appointment:String){
        this.user=user
        this.doc=doc
        this.medicine=medicine
        this.appointment=appointment
        this.visitStatus=visitStatus





    }

    fun setRatingStar(ratingStar: Float) {
        this.ratingStar = ratingStar
    }


}