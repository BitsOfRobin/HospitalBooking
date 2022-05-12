package com.example.hospitalbooking

import android.graphics.Bitmap

class ModalFormMain {

    var name:String?=null

    var image:Bitmap?=null
    var docName:String?=null
    var time:String?=null
    constructor(name:String,image:Bitmap,docName:String,time:String){
        this.name=name

        this.image=image

        this.docName=docName
        this.time=time




    }
}