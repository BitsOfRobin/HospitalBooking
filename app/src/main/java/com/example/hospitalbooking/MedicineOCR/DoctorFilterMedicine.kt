package com.example.hospitalbooking.MedicineOCR

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.hospitalbooking.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class DoctorFilterMedicine : AppCompatActivity() {


    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_filter_medicine)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Filter Medicine")
        retrieveMedi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    @SuppressLint("SuspiciousIndentation")
    private fun retrieveMedi()
    {
        var loginUser=" "
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.displayName.toString()

            }

            else{

                loginUser=" NOne"
            }
//
        }

        val arraylistMedi= ArrayList<String>()


        val btn=findViewById<Button>(R.id.btnRet)
        var detect=0

        btn.setOnClickListener {
            arraylistMedi.clear()
            val  userMedicine=findViewById<EditText>(R.id.medicineText)
          var medicine=userMedicine.text.toString()
          var dbMedicine=" "
          var dbMedicine2=" "
              var user=" "

                if(isLetters(medicine)&&medicine!=""&&medicine!=" ") {

                Toast.makeText(this, medicine, Toast.LENGTH_SHORT).show()
                val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
                docRef?.get()?.addOnSuccessListener {


                    for (document in it) {
                        dbMedicine = document.get("medicine1").toString()
                        dbMedicine2 = document.get("medicine2").toString()
                        user = document.get("user").toString()

                        if (dbMedicine.contains(medicine,true)||dbMedicine2.contains(medicine,true)) {
                            arraylistMedi.add("user is $user, medicine 1 is $dbMedicine, medicine 2 is $dbMedicine2")
                            detect++
                        }




                    }

                    if(detect==0)
                    {
                        arraylistMedi.add("No records found")


                    }
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "med=$arraylistMedi", Toast.LENGTH_SHORT).show()
                    val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistMedi)
//            val medilist=findViewById<TextView>(R.id.medDisplay)
//            medilist.text=arraylistMedi.toString()
                    val filterMedi = findViewById<ListView>(R.id.filterMediList)
                    filterMedi.adapter = arr
                }

                    ?.addOnFailureListener {

                        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                    }
                Toast.makeText(this, "$arraylistMedi", Toast.LENGTH_SHORT).show()


//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)

            }

            else
            {


                Toast.makeText(this,"Medicine inputs contains NON alpahbet and digit",Toast.LENGTH_SHORT).show()
            }
      }
    }




    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z0-9 ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }









}