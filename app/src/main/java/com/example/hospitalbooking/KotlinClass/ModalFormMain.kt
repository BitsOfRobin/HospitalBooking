package com.example.hospitalbooking.KotlinClass

import android.graphics.Bitmap

class ModalFormMain {

    var pro:String?=null

    var image:Bitmap?=null
    var docName:String?=null
    var time:String?=null
    var hospital:String?=null
    constructor(pro:String,image:Bitmap,docName:String,time:String,hospital:String){
        this.pro=pro

        this.image=image

        this.docName=docName
        this.time=time
        this.hospital=hospital






    }


    @JvmName("getPro1")
    private fun getPro(): String? {
        this.pro=pro
        return pro

    }




}