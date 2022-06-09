package com.example.hospitalbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class PatientPrescription : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_prescription)
        setPrescription()
    }

    private fun setPrescription() {

        val userName = intent.getStringExtra("userName")

        val med1=findViewById<EditText>(R.id.med1)
        val med2=findViewById<EditText>(R.id.med2)
        val dos1=findViewById<EditText>(R.id.dos1)
        val dos2=findViewById<EditText>(R.id.dos2)
        val submit=findViewById<Button>(R.id.submitBtn)

        var medText=med1.text.toString()
        var medText2=med2.text.toString()
        var dosText1=dos1.text.toString()
        var dosText2=dos2.text.toString()

        submit.setOnClickListener {


       if(isLetters(medText) || isLetters(medText2))
        {

            mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

                val medicine= hashMapOf(
                    "medicine1" to medText,
                    "medicine2" to medText2,
                    "dosage1"  to dosText1,
                    "dosage2"  to dosText2


                )


//        val  doc =doctor?.uid

//
            if (userName != null) {
                mFirebaseDatabaseInstance?.collection("userAppointment")?.document("$userName")?.update(
                    medicine as Map<String, Any>
                )?.addOnSuccessListener {


                    Toast.makeText(this,"Successfully added medicine ",Toast.LENGTH_SHORT).show()

                }
                    ?.addOnFailureListener {

                        Toast.makeText(this,"Failed to add user", Toast.LENGTH_SHORT).show()
                    }
            }








        }

            else
            {

                Toast.makeText(this,"Empty inputs are detected and accept no space", Toast.LENGTH_SHORT).show()

            }




        }
    }


    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }


}