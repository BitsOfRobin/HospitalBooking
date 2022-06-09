package com.example.hospitalbooking

import android.graphics.Bitmap

class ModalFormMain {

    var pro:String?=null

    var image:Bitmap?=null
    var docName:String?=null
    var time:String?=null
    constructor(pro:String,image:Bitmap,docName:String,time:String){
        this.pro=pro

        this.image=image

        this.docName=docName
        this.time=time




    }
}