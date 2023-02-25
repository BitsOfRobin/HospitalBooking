package com.example.hospitalbooking.MedicineOCR

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MedicineRecord : AppCompatActivity() {
    private lateinit var toggle:ActionBarDrawerToggle
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
  
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_record)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Patient Medicine Record")
        retrieveMedi()
        refresh()
        showNavBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun retrieveMedi()
    {
        var loginUser=" "
        val userGoogle = Firebase.auth.currentUser
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.displayName.toString()
                naviImg(userGoogle!!.photoUrl,loginUser)
            }

            else{

                loginUser=" NOne"
            }
//
        }

        val arraylistMedi= ArrayList<String>()

//        var medicine=" "
        var mediText=" "
        Toast.makeText(this,loginUser,Toast.LENGTH_SHORT).show()
        val docRef = mFirebaseDatabaseInstance?.collection("medicine")
        docRef?.whereEqualTo("user",loginUser)?.get()?.addOnSuccessListener {


            for(document in it) {
                val medicine=document.get("userMedicine").toString()
                arraylistMedi.add(medicine)

            }
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()
            val arr= ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistMedi)
            val medilist=findViewById<ListView>(R.id.listMedi)
            medilist.adapter=arr

        }

            ?.addOnFailureListener{

                Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show()
            }
        Toast.makeText(this,"$arraylistMedi",Toast.LENGTH_SHORT).show()


//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)


    }




    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z0-9]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

            retrieveMedi()
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }



    private fun showNavBar(){


        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view=findViewById<NavigationView>(R.id.nav_view)
        toggle= ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_BookAppoint -> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }




                R.id.nav_Pres -> {
                    val intent = Intent(this, PrescriptionDisplay::class.java)
                    startActivity(intent)

                }
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
                R.id.nav_viewAppoint -> {
                    val intent = Intent(this, DoctorAppointment::class.java)
                    startActivity(intent)

                }
                R.id.nav_medicineRecord -> {
                    val  intent = Intent(this, MedicineRecord::class.java)
                    startActivity(intent)

                }
                R.id.nav_OCR -> {
                    val intent = Intent(this, UserMedicine::class.java)
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


    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser




    }

}