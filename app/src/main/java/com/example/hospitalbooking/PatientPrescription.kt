package com.example.hospitalbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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
        var equal=0
        equal= userName?.indexOf("r=")!!
        val name=  userName.substring(equal+2,userName.length-1)

        retrieveRegMedicine(name)
        Toast.makeText(
            this,
            "$name ",
            Toast.LENGTH_SHORT
        ).show()
        val med1=findViewById<EditText>(R.id.med1)
        val med2=findViewById<EditText>(R.id.med2)
        val dos1=findViewById<EditText>(R.id.dos1)
        val dos2=findViewById<EditText>(R.id.dos2)
        val submit=findViewById<Button>(R.id.submitBtn)


        var dosText1=0
        var dosText2=0
        var truth:Boolean=true
        var truth2:Boolean=true
        submit.setOnClickListener {
            val medText=med1.text.toString()
            val medText2=med2.text.toString()
            try {
                dosText1 = dos1.text.toString().toInt()
               truth=true
            } catch (NumberFormatException: IllegalArgumentException) {
                truth=false
                Toast.makeText(
                    this,
                    "NON integer inputs for dosage",
                    Toast.LENGTH_SHORT
                ).show()

            }

            try {

                dosText2 = dos2.text.toString().toInt()
                truth2=true
            } catch (NumberFormatException: IllegalArgumentException) {
                truth2=false
                Toast.makeText(
                    this,
                    "NON integer inputs for dosage",
                    Toast.LENGTH_SHORT
                ).show()

            }





            if (truth&&isLetters(medText)&&medText.isNotEmpty()&&!medText.contains("Choose",true) || truth2&&isLetters(medText2)&&medText2.isNotEmpty()) {

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
        return string.matches("^[a-zA-Z0123456789 ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }

     private fun retrieveRegMedicine(userName: String?) {
         var medi = ""
         val spin = findViewById<Spinner>(R.id.spinnerMedi)
         val arrMedi = ArrayList<String>()
         arrMedi.add(0,"Choose patient medicine")
         mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
         mFirebaseDatabaseInstance?.collection("medicine")
             ?.whereEqualTo("user", "$userName")?.get()?.addOnSuccessListener {
                 for (document in it) {
                     medi = document.get("userMedicine").toString()
                     arrMedi.add(medi)


                 }
                 Toast.makeText(
                     this,
                     "$userName Success $arrMedi",
                     Toast.LENGTH_SHORT
                 ).show()
             }?.addOnFailureListener {

                 Toast.makeText(
                     this,
                     "Failed to retrieve User Recognise Medicine",
                     Toast.LENGTH_SHORT
                 ).show()
             }
         val arr = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arrMedi)
         spin.adapter = arr

         val med1=findViewById<EditText>(R.id.med1)
         val med2=findViewById<EditText>(R.id.med2)
         val med3=findViewById<TextView>(R.id.textView7)
         spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                 if(med1.text.isEmpty()){

                     med1.setText(arrMedi[p2])
                 }
                 else if(med2.text.isEmpty())
                 {
                     med2.setText(arrMedi[p2])

                 }


                 else if(med1.text.isNotEmpty() && med2.text.isNotEmpty() ){

                     med1.setText(arrMedi[p2])
                 }




             }

             override fun onNothingSelected(p0: AdapterView<*>?) {
                 TODO("Not yet implemented")
             }


         }
     }

}