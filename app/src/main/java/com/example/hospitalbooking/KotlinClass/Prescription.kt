package com.example.hospitalbooking.KotlinClass

class Prescription {


    var user: String? =null
    var doc: String? =null
    var medicine: String? =null
    var ratingStar:Float? =0F

    constructor(user:String,doc:String,medicine:String,ratingStar: Float){
        this.user=user
        this.doc=doc
        this.medicine=medicine




    }

    fun setRatingStar(ratingStar: Float) {
        this.ratingStar = ratingStar
    }


}