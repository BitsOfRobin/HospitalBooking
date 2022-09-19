package com.example.hospitalbooking

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class FilterAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    private var arraylistDocName = ArrayList<String>()
    private var arraylistDocSearch = ArrayList<String>()
    private var docToSearch: String = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_appointment)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("This New Page")
        val txt = findViewById<TextView>(R.id.txtDoc)
//        txt.text = "None"

        filter(txt)
        filDoc(txt)
        refresh(txt)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun filter(txt: TextView)
    {

//        val docView = findViewById<ListView>(R.id.listFil)
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val spinDocName = findViewById<Spinner>(R.id.spinnerDocName)
        val arraylist = ArrayList<String>()

        val arraylistUser = ArrayList<String>()


        var user = " "
        var doc = " "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
            } else {

                user = " NOne"
            }

        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.whereEqualTo("user",user)?.get()?.addOnSuccessListener {


//            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                arraylistPro.add(document.id.toString())
//                arraylist.add(document.data.toString())

//                var user=document.get("user").toString()


//                    val photoUrl = user.photoUrl
//
//                    // Check if user's email is verified
//                    val emailVerified = user.isEmailVerified
//
//                    // The user's ID, unique to the Firebase project. Do NOT use this value to
//                    // authenticate with your backend server, if you have one. Use
//                    // FirebaseUser.getToken() instead.
//                    val uid = user.uid

                var docName = document.get("docName").toString()
                if (!arraylistDocName.contains(docName.toString())) {
                    arraylistDocName.add(docName)

                }


                doc = document.get("doctorAppoint").toString()
                arraylistUser.add(user)
                if (user == null) {
//                    arraylist.add("No records found")

                } else {
                    arraylist.add("User: $user\n Appointed Doctor:$docName\n Appointment Detail: $doc\n")
                }


//                Toast.makeText(
//                    this,
//                    "Enter the firebase${arraylistDocName.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Toast.makeText(
//                    this,
//                    "Enter the firebase id ${document.id.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
            }


            val arrDocName = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arraylistDocName)
            spinDocName.adapter = arrDocName

            Toast.makeText(
                this,
                "Found search doc for spinner =${arraylistDocName.toString()} ",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

//        var docToSearch:String="Tan Ah kau"
//            var docToSearch = ArrayList<String>()
        private fun filDoc(txt: TextView)
        {
//            arraylistDocSearch.clear()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val spinDocName = findViewById<Spinner>(R.id.spinnerDocName)
        val arraylist = ArrayList<String>()
//        var arraylistDocSearch = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
//        val arraylistDocName= ArrayList<String>()

        var user=" "
        var doc=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
            } else {

                user = " NOne"
            }

        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        spinDocName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                docToSearch = arraylistDocName[p2].toString()
                docRef?.whereEqualTo("docName",docToSearch)
                    ?.get()
                    ?.addOnSuccessListener {
                        arraylistDocSearch.clear()
                        for (document in it) {
                            var docName = document.get("docName").toString()
//                            if (docName != null) {
//                                arraylistDocName.add(docName)
//
//                            }


                            doc = document.get("doctorAppoint").toString()
                            var userFound= document.get("user").toString()
                            arraylistUser.add(user)
                            if (userFound != user) {
//                                arraylistDocSearch.add("No records found")

                            } else {

                                arraylistDocSearch.add("User: $user\n Appointed Doctor:$docName\n Appointment Detail: $doc\n\n\n\n")
                            }


//
//                            Toast.makeText(
//                                this,
//                                "Found=${arraylistDocSearch.toString()} ",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                    ?.addOnFailureListener {

                    }
//                        Toast.makeText(
//                            this,
//                            "\"Error getting documents: \" ",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                Toast.makeText(
//                    this,
//                    "Found search=${arraylist.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()


//
//                val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistDocSearch)
//                docView.adapter = arr



            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                txt.text="NONE"
            }


        }
//        spinDocName.onItemSelectedListener=this
        Toast.makeText(
            this,
            "Found search doc=${arraylistDocSearch.toString()} ",
            Toast.LENGTH_SHORT
        ).show()



        Toast.makeText(
            this,
            "Found search=$docToSearch ",
            Toast.LENGTH_SHORT
        ).show()

//
//        val arr=ArrayAdapter(this, android.R.layout.simple_list_item_1,arraylistDocSearch)
//        docView.adapter=arr
            txt.text=" "
            txt.text=arraylistDocSearch.toString()


      }

//    private fun readDoc(txt: TextView)
//    {
//
//
//        txt.text = "None"
//        Toast.makeText(
//            this,
//            "Found search doc=${arraylistDocSearch.toString()} ",
//            Toast.LENGTH_SHORT
//        ).show()
//
//        txt.text=arraylistDocSearch.toString()
//
//
//    }

    private fun refresh(txt: TextView)
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {
//            txt.setText(null)
//            arraylistDocSearch.clear()

            filDoc(txt)
//           readDoc(txt)
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }



//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }


}