package com.example.hospitalbooking

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MedicineRecord : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_record)

        retrieveMedi()
        refresh()
    }



    private fun retrieveMedi()
    {
        var loginUser=" "
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
        val docRef = mFirebaseDatabaseInstance?.collection("medicine")
        var medicine=" "
        var mediText=" "
        docRef?.whereEqualTo("user",loginUser)?.get()?.addOnSuccessListener {


            for (document in it) {


                medicine = document.get("medicine").toString()
    //                mediText=medicine.replace(" ","")
    //                if(isLetters(mediText))
    //                {

                arraylistMedi.add("$medicine\n$loginUser")

    //                }


            }

            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener{

                Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
            }


        Toast.makeText(this,"$arraylistMedi",Toast.LENGTH_SHORT).show()


//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)

        val arr= ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistMedi)
        val medilist=findViewById<ListView>(R.id.listMedi)
        medilist.adapter=arr

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




}