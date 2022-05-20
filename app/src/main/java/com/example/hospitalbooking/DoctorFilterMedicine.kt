package com.example.hospitalbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DoctorFilterMedicine : AppCompatActivity() {


    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_filter_medicine)

        retrieveMedi()
    }




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

            if(isLetters(medicine)) {

                Toast.makeText(this, medicine, Toast.LENGTH_SHORT).show()
                val docRef = mFirebaseDatabaseInstance?.collection("medicine")
                docRef?.get()?.addOnSuccessListener {


                    for (document in it) {
                        dbMedicine = document.get("userMedicine").toString()

                        if (dbMedicine.contains(medicine)) {
                            arraylistMedi.add(dbMedicine)
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