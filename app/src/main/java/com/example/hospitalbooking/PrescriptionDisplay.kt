package com.example.hospitalbooking

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PrescriptionDisplay : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var arraylistDocName = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_display)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Prescription")
        showPrescription()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun showPrescription() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylistPres = ArrayList<Prescription>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()

        val arrayForSearch = ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var userG = " "
        var medicine1 = " "
        var medicine2 = " "
        var dos1=" "
        var dos2=" "

//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.presListCheck)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                userG = userGoogle.displayName.toString()


            }

            else {
//
                userG = " NOne"
            }

        }

//        var userName = intent.getStringExtra("userName").toString()
//
//        Toast.makeText(this, userName,Toast.LENGTH_SHORT).show()
//        if(userName.contains("     ")){
//
//            user=userG
//        }
//
//        else{
//
//            user=userName
//        }
        user=userG
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.whereEqualTo("user",user)?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName !=" "&& docName!="") {
                    arraylistDocName.add(docName)

                }



                medicine1 = document.get("medicine1").toString()
                medicine2 = document.get("medicine2").toString()

                dos1=document.get("dosage1").toString()
                dos2=document.get("dosage2").toString()

                user = document.get("user").toString()





                arraylistUser.add(user)
//                dos1.toColorInt()
//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {

                if (docName.contains("Dr")) {
//                    arraylistPres.add("User:$user\nAppointed Doctor:$docName\n Medicine Detail:$medicine1 dosage=$dos1 ,$medicine2 dosage=$dos2\n\n")
                    val medi= "$medicine1\n$dos1 mg \n\n$medicine2\n$dos2 mg\n\n"
                        arraylistPres.add(Prescription(user,docName,medi,0F))


                }


//                }


            }


            val arr = ListCustomAdapterForPrescription(this,arraylistPres)




            docView.adapter = arr

//            var rating=0.0
            docView.setOnItemClickListener { adapterView, view, i, l ->





                val intent= Intent(this,PopUpWindow::class.java)
                intent.putExtra("docName", arraylistDocName[i])
//                intent.putExtra("rating",rating)

//                val extras = Bundle()
//                extras.putDouble("rating",rating)

                startActivity(intent)

            }



        }
    }
//    private fun setColorText(str: String): SpannableString {
//
//        val docPro=findViewById<TextView>(R.id.docPro)
//
//
//        val yellow = ForegroundColorSpan(Color.YELLOW)
//        val spannableString = SpannableString(str)
//
//
//
//        spannableString.setSpan(yellow,
//            0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////        val num=arraylistPro[position].length
//
//        Toast.makeText(this,"$spannableString",Toast.LENGTH_SHORT).show()
//
//        return spannableString
////        docPro.text = spannableString.toString()
//
//    }



//
//    private fun getRate( i:Int){
//
//        val rating=findViewById<RatingBar>(R.id.ratingBarInput)
//
//
//        val docList = findViewById<ListView>(R.id.presListCheck)
//        var rate:Float= 0F
//
//
//
//
//            rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
//
//                ratingBar.rating=fl
//                rate=fl
//
//
//            }
//
//
//
//        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//
//
//
//
//
//
//
//            val ratingStar= hashMapOf(
//                "ratingStart" to rate,
//
//
//
//
//            )
////        val  doc =doctor?.uid
//
////
//            mFirebaseDatabaseInstance?.collection("doctor")?.document(arraylistDocName[i])?.set(ratingStar)
//                ?.addOnSuccessListener {
//
//
//            Toast.makeText(this,"Successfully rate  doctor ",Toast.LENGTH_SHORT).show()
//
//                }
//                ?.addOnFailureListener {
//
//                    Toast.makeText(this, "Failed to rate doctor", Toast.LENGTH_SHORT).show()
//                }
//
//
////        userNum+=1
//
//
//
//
//
//    }




}