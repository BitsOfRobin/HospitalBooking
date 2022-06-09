package com.example.hospitalbooking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class DoctorViewAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view_appointment)
        readUserRecord()
    }

    private fun readUserRecord() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName = ArrayList<String>()
        val arrayForSearch=ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var doc = " "
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
//        val userGoogle = Firebase.auth.currentUser
//        userGoogle.let {
//            // Name, email address, and profile photo Url
////                    val name = user.displayName
//            if (userGoogle != null) {
//                user = userGoogle.displayName.toString()
//            } else {
////
//                user = " NOne"
//            }
//
//        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName != null) {
                    arraylistDocName.add(docName)

                }



                doc = document.get("doctorAppoint").toString()
                user=document.get("user").toString()


                arraylistUser.add(user)
//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {
                if(docName.contains("Dr"))
                {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")
                    arrayForSearch.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                    arraylistAppointment.add(AppointmentDetail(user, docName, doc))

                }


//                }


            }


            val arr = ListCustomAdapterForPrescription(this,arraylist)




            docView.adapter = arr


            docView.setOnItemClickListener { adapterView, view, i, l ->

                val intent= Intent(this,PatientPrescription::class.java)
                intent.putExtra("userName", arrayForSearch[i])
                startActivity(intent)



            }

        }


    }










}