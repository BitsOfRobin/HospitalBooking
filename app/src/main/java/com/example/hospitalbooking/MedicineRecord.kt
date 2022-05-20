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
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
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




}