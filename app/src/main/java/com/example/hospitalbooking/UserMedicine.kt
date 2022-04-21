package com.example.hospitalbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

class UserMedicine : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userId=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)
        readUser()
        Toast.makeText(this, "retrieve=$userId", Toast.LENGTH_SHORT).show()
        readMedicine()
    }



    private fun readMedicine() {

        val medicine = findViewById<EditText>(R.id.id)
        val disease = findViewById<EditText>(R.id.pass)
        val dosage = findViewById<EditText>(R.id.age)


        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
//            medicine.filters = arrayOf<InputFilter>(ValidateFilter())
//            disease.filters = arrayOf<InputFilter>(ValidateFilter())
//        dosage.filters = arrayOf<InputFilter>(ValidateFilter())
            val truth:Boolean = isNumber(dosage.text.toString())
            val truthMed:Boolean = isLetters(medicine.text.toString())
            val truthDis:Boolean = isLetters(disease.text.toString())
            if (truth) {

                Toast.makeText(
                    this,
                    "It is digit} ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(
                    this,
                    "It is not digit} ",
                    Toast.LENGTH_SHORT
                ).show()

            }




            if (truth && truthMed && truthDis) {


                val userMedicine = hashMapOf(
                    "userMedicine" to medicine.text.toString(),
                    "userDisease" to disease.text.toString(),
                    "userDosage" to dosage.text.toString(),
                    "userId" to userId


                )
//        val  doc =doctor?.uid

                mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
                mFirebaseDatabaseInstance?.collection("userMedicine")?.document("medicine")
                    ?.set(userMedicine)?.addOnSuccessListener {


                        Toast.makeText(this, "Successfully added user medicine ", Toast.LENGTH_SHORT).show()

                    }
                    ?.addOnFailureListener {

                        Toast.makeText(this, "Failed to add user medicine", Toast.LENGTH_SHORT).show()
                    }


            } else {

                Toast.makeText(this, "The inputs for Disease and medicine contains non alphabet", Toast.LENGTH_SHORT).show()

            }



            val intent= Intent(this,MedicineViewCustomer::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
            btnSubmit.context.startActivity(intent)

        }
    }


    inner class ValidateFilter : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            var string:String =" "
            for (i in start until end) {
//                val string = editText.text.toString().trim()

                val checkMe = source[i].toString()
                val pattern: Pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789]*")
                val matcher: Matcher = pattern.matcher(checkMe)
                val valid: Boolean = matcher.matches()
                string = if (!valid) {

                    " "
                } else{

                    checkMe
                }
            }
            return string
        }





    }


    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z]*$".toRegex())
    }




    private fun isNumber(s: String?): Boolean {
        return !s.isNullOrEmpty() && s.matches(Regex("\\d+"))
    }



    private fun readUser(){
        val arraylist = ArrayList<String>()
        Toast.makeText(this, "enter to retrieve", Toast.LENGTH_SHORT).show()
        val docRef = mFirebaseDatabaseInstance?.collection("ValidUser")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

            //                }

            for (document in it) {


                userId = document.get("userId").toString()

                if (userId == null) {
                    arraylist.add("No records found")

                }

            }

            Toast.makeText(this, "success to retrieve", Toast.LENGTH_SHORT).show()

        }?.addOnFailureListener {


            Toast.makeText(this, "failed to retrieve", Toast.LENGTH_SHORT).show()


        }


    }









}