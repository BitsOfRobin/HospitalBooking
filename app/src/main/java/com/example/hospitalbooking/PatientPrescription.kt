package com.example.hospitalbooking

import android.content.Intent
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Patient Prescription")
        setPrescription()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setPrescription() {

        val userName = intent.getStringExtra("userName")

        val med1=findViewById<EditText>(R.id.med1)
        val med2=findViewById<EditText>(R.id.med2)
        val dos1=findViewById<EditText>(R.id.dos1)
        val dos2=findViewById<EditText>(R.id.dos2)
        val submit=findViewById<Button>(R.id.submitBtn)


        var dosText1=0
        var dosText2=0

        submit.setOnClickListener {
            val medText=med1.text.toString()
            val medText2=med2.text.toString()
            try {
                dosText1 = dos1.text.toString().toInt()
                dosText2 = dos2.text.toString().toInt()
            } catch (NumberFormatException: IllegalArgumentException) {

                Toast.makeText(
                    this,
                    "NON integer inputs for dosage",
                    Toast.LENGTH_SHORT
                ).show()

            }







            if (isLetters(medText)&&medText!=" "&&medText!="" || isLetters(medText2)&&medText2!=" "&&medText2!="" ) {

                    mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

                    val medicine = hashMapOf(
                        "medicine1" to medText,
                        "medicine2" to medText2,
                        "dosage1" to dosText1,
                        "dosage2" to dosText2


                    )


//        val  doc =doctor?.uid

//
                    if (userName != null) {
                        mFirebaseDatabaseInstance?.collection("userAppointment")
                            ?.document("$userName")?.update(
                            medicine as Map<String, Any>
                        )?.addOnSuccessListener {


                            Toast.makeText(this, "Successfully added medicine ", Toast.LENGTH_SHORT)
                                .show()


//                                val start=userName.indexOf("user=")
//                               val end=userName.indexOf("}")
//                               var name=" "
//                                name=userName.substring(start,end)
//                               name=userName.replace("user=","")
//                                val intent= Intent(this,PrescriptionDisplay::class.java)
//                                intent.putExtra("userName", name)
//                                startActivity(intent)

                        }
                            ?.addOnFailureListener {

                                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }


                } else {

                    Toast.makeText(
                        this,
                        "Empty inputs are detected and accept no space",
                        Toast.LENGTH_SHORT
                    ).show()

                }



        }
    }


    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }


}