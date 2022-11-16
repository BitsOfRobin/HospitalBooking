package com.example.hospitalbooking

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PopUpWindow : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var rateFrequency=1.0
    private var rateStar = 0.0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_window)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Rating Doctor")

        val btnOk=findViewById<Button>(R.id.popup_window_button)
        btnOk.setOnClickListener{


            getRate()
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getRate(){
        val docName = intent.getStringExtra("docName")
        val rating=findViewById<RatingBar>(R.id.ratingBarInput)

        val extras = intent.extras
        rateStar = extras!!.getDouble("rating")

        val docList = findViewById<ListView>(R.id.presListCheck)
        var rate:Float= 0F



//        rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
//
//            ratingBar.rating=fl
//            rate= ratingBar.numStars.toFloat()
//
//
//        }
        rate=   rating.rating
        Toast.makeText(this,"$rate",Toast.LENGTH_SHORT).show()
        val calRating=calStar(docName.toString(),rate,docName.toString())
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()







        val ratingStar= hashMapOf(
            "ratingStar" to rate.toInt(),
            "name" to docName




            )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("doctor")?.document(docName.toString())?.update("rating",calRating)
            ?.addOnSuccessListener {


                Toast.makeText(this,"Successfully rate  doctor ", Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

                Toast.makeText(this, "Failed to rate doctor", Toast.LENGTH_SHORT).show()
            }


//        userNum+=1






    }


    private fun calStar(tempHolder: String, rate: Float,docName:String): Double {


        mFirebaseDatabaseInstance?.collection("doctor")?.whereEqualTo("name", tempHolder)?.get()
            ?.addOnSuccessListener {


                for (document in it) {

                    rateStar = document.get("rating") as Double
                    rateFrequency = document.get("rateFrequency") as Double



                }


            } ?.addOnFailureListener {

                Toast.makeText(this, "Failed to retrieve rate frequency", Toast.LENGTH_SHORT).show()
            }
        rateFrequency++


        mFirebaseDatabaseInstance?.collection("doctor")?.document(docName)?.update("rateFrequency",rateFrequency)
            ?.addOnSuccessListener {


                Toast.makeText(this,"Successfully update rate frequency ", Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

                Toast.makeText(this, "Failed to update rate frequency", Toast.LENGTH_SHORT).show()
            }

//        1*a+2*b+3*c+4*d+5*e/(5*#R)

        val res=rateStar + rate
        Toast.makeText(this,"rat$rateStar",Toast.LENGTH_SHORT).show()
        return rate.toDouble()
    }








}