package com.example.hospitalbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Matcher
import java.util.regex.Pattern

class UserRegister : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        registerUser()
    }



    private fun registerUser() {

        val id = findViewById<EditText>(R.id.id)
        val pass = findViewById<EditText>(R.id.pass)
        val age = findViewById<EditText>(R.id.age)


        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
//            medicine.filters = arrayOf<InputFilter>(ValidateFilter())
//            disease.filters = arrayOf<InputFilter>(ValidateFilter())
//        dosage.filters = arrayOf<InputFilter>(ValidateFilter())
            val truth:Boolean = isNumber(age.text.toString())
            val truthId:Boolean = isLetters(id.text.toString())
            val truthPass:Boolean = isLetters(pass.text.toString())
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




            if (truth && truthId && truthPass) {


                val userMedicine = hashMapOf(
                    "userId" to id.text.toString(),
                    "userPass" to pass.text.toString(),
                    "userAge" to age.text.toString()


                )
//        val  doc =doctor?.uid

                mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
                mFirebaseDatabaseInstance?.collection("user")?.document("userInfo")
                    ?.set(userMedicine)?.addOnSuccessListener {


                        Toast.makeText(this, "Successfully added user  ", Toast.LENGTH_SHORT).show()

                    }
                    ?.addOnFailureListener {

                        Toast.makeText(this, "Failed to add user ", Toast.LENGTH_SHORT).show()
                    }


            } else {

                Toast.makeText(this, "The inputs for user id and password contains non alphabet", Toast.LENGTH_SHORT).show()

            }



//            val intent= Intent(this,MedicineViewCustomer::class.java)
////            intent.putExtra("DoctorName", tempListViewClickedValue)
//            btnSubmit.context.startActivity(intent)

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




}