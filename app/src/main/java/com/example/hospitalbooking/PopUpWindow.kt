package com.example.hospitalbooking

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class PopUpWindow : AppCompatActivity() {
    private lateinit var toggle:ActionBarDrawerToggle
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
        showNavBar()
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



    private fun showNavBar(){


        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view=findViewById<NavigationView>(R.id.nav_view)
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_BookAppoint-> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }




                R.id.nav_Pres-> {
                    val intent = Intent(this, PrescriptionDisplay::class.java)
                    startActivity(intent)

                }
                R.id.nav_home-> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_profile-> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
                R.id.nav_viewAppoint-> {
                    val intent = Intent(this,DoctorAppointment::class.java)
                    startActivity(intent)

                }
                R.id.nav_medicineRecord-> {
                    val  intent = Intent(this,MedicineRecord::class.java)
                    startActivity(intent)

                }
                R.id.nav_OCR-> {
                    val intent = Intent(this,UserMedicine::class.java)
                    startActivity(intent)
                }





            }


            true

        }





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true


        }


        return super.onOptionsItemSelected(item)
    }





}