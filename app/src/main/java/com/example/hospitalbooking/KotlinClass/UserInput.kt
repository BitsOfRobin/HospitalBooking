package com.example.hospitalbooking.KotlinClass

class UserInput {

    var answer1: String? = null
    var answer2: String? = null
    var commentStatus: String? = null
    var rating: Float? = null

    constructor()

    constructor(answer1:String, answer2:String, rating:Float,commentStatus:String){
        this.answer1 = answer1
        this.answer2 = answer2
        this.rating = rating
        this.commentStatus=commentStatus
    }
}




