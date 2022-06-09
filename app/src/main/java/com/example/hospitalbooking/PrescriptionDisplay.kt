package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class PrescriptionDisplay : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_display)
        showPrescription()
    }

    private fun showPrescription() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName = ArrayList<String>()
        val arrayForSearch = ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var medicine1 = " "
        var medicine2 = " "
        var dos1=" "
        var dos2=" "

//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.presList)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()

            }

            else {
//
                user = " NOne"
            }

        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.whereEqualTo("user",user)?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName != null) {
                    arraylistDocName.add(docName)

                }



                medicine1 = document.get("medicine1").toString()
                medicine2 = document.get("medicine2").toString()

                dos1=document.get("dosage1").toString()
                dos2=document.get("dosage2").toString()

                user = document.get("user").toString()





                arraylistUser.add(user)
//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {
                if (docName.contains("Dr")) {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\n Medicine Detail:$medicine1 dosage=$dos1 ,$medicine2 dosage=$dos2\n\n")


                }


//                }


            }


            val arr = ListCustomAdapterForPrescription(this,arraylist)




            docView.adapter = arr

        }
    }




}