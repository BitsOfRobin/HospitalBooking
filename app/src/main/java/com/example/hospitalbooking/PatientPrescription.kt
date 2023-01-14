package com.example.hospitalbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
        var equal = 0
        equal = userName?.indexOf("r=")!!
        val name = userName.substring(equal + 2, userName.length - 1)
        val userGoogle = Firebase.auth.currentUser
        val nurseName= userGoogle?.displayName
        val arr=retrieveRegMedicine(name)
        val arrNurse=retrieveRegMedicine(nurseName)
        val spin = findViewById<Spinner>(R.id.spinnerMedi)
        val spinNurse = findViewById<Spinner>(R.id.spinnerMediNurse)
        arr.add(0,"Choose patient Medicine")
        setSpinner(arr,spin)
        arrNurse.add(0,"Choose Nurse Medicine")
        setSpinner(arrNurse,spinNurse)
        Toast.makeText(
            this,
            "$name ",
            Toast.LENGTH_SHORT
        ).show()
//        val med1=findViewById<EditText>(R.id.med1)
        val med2 = findViewById<EditText>(R.id.med2)
        val dos1 = findViewById<EditText>(R.id.dos1)
        val dos2 = findViewById<EditText>(R.id.dos2)
        val submit = findViewById<Button>(R.id.submitBtn)

        val medi1 = findViewById<EditText>(R.id.medi1)
        var dosText1 = 0
        var dosText2 = 0
        var truth: Boolean = true
        var truth2: Boolean = true

        var medText = medi1.text.toString().replace(" ", "")
        var medText2 = med2.text.toString().replace(" ", "")


        medi1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isLetters(p0.toString()) && p0.toString().isEmpty() || p0.toString()
                        .contains("Choose", true) || p0.toString().isBlank()
                ) {
                    medi1.error = "Empty inputs are detected and accept no space "

                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!isLetters(p0.toString()) && p0.toString().isEmpty() || p0.toString()
                        .contains("Choose", true) || p0.toString().isBlank()
                ) {
                    medi1.error = "Empty inputs are detected and accept no space "

                } else {
                    medText = p0.toString()
                    medi1.error = "Completed"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }

        )



        med2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (isLetters(p0.toString()) && p0.toString().isEmpty() || p0.toString()
                        .isBlank()
                ) {
                    med2.error = "Empty inputs are detected and accept no space "

                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (isLetters(p0.toString()) && p0.toString().isEmpty() || p0.toString()
                        .isBlank()
                ) {
                    med2.error = "Empty inputs are detected and accept no space "

                } else {
                    medText2 = p0.toString()
                    med2.error = "Completed"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })




        if (dos1.text.toString().isBlank() || dos1.text.toString().isEmpty()) {

            dos1.error = "No Input"
        }
            dos1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dos1.error = "Accept Only Integer"
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dos1.error = "NON integer inputs for dosage"



                    try {
                        dosText1 = dos1.text.toString().toInt()
                        truth = true
                        dos1.error = "Completed"

                    } catch (NumberFormatException: IllegalArgumentException) {
                        truth = false
                        dos1.error = "NON integer inputs for dosage"


                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }


            })




        if (dos2.text.toString().isBlank() || dos2.text.toString().isEmpty()) {

            dos2.error = "No Input"
        }


            dos2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dos2.error = "Accept Only Integer"

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dos2.error = "NON integer inputs for dosage"

                    if (p0.toString().isBlank() || p0.toString().isEmpty()) {

                        dos2.error = "No Input"
                    } else {
                        try {

                            dosText2 = dos2.text.toString().toInt()
                            truth2 = true
                            dos2.error = "Completed"

                        } catch (NumberFormatException: IllegalArgumentException) {
                            dos2.error = "NON integer inputs for dosage"

                            truth2 = false
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })


        submit.setOnClickListener {


//            if (truth&&isLetters(medText)&&medText.isNotEmpty()&&!medText.contains("Choose patient",true) || truth2&&isLetters(medText2)&&medText2.isNotEmpty()) {


            mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

            val medicine = hashMapOf(
                "medicine1" to medText,
                "medicine2" to medText2,
                "dosage1" to dosText1,
                "dosage2" to dosText2


            )


//        val  doc =doctor?.uid

//
            if ( (medText.isNotBlank()&&medText.isNotEmpty() &&  medText2.isNotBlank()&&medText2.isNotEmpty()) &&
                dosText1!=0  ) {
                mFirebaseDatabaseInstance?.collection("userAppointment")
                    ?.document("$userName")?.update(
                        medicine as Map<String, Any>
                    )?.addOnSuccessListener {


                        Toast.makeText(this, "Successfully added medicine ", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, PrescriptionDisplay::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                        startActivity(intent)

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
            } else {

                Toast.makeText(
                    this,
                    "Empty inputs are detected for dosage",
                    Toast.LENGTH_SHORT
                ).show()

            }


//        }
//        }







        }
    }


    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z0-9 ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }

     private fun retrieveRegMedicine(userName: String?): ArrayList<String> {
         var medi = ""
         val spin = findViewById<Spinner>(R.id.spinnerMedi)
         val arrMedi = ArrayList<String>()

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


         return arrMedi


     }

    private fun setSpinner(arrMedi:ArrayList<String>,spin:Spinner) {
//        val arrMedi = ArrayList<String>()
//        arrMedi.add(0,"Choose patient medicine")
        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arrMedi)


        spin.adapter = arr

        val med1=findViewById<EditText>(R.id.medi1)
        val med2=findViewById<EditText>(R.id.med2)
        val med3=findViewById<TextView>(R.id.textView7)
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                if(med1.text.isEmpty()&&p2>0){

                    med1.setText(arrMedi[p2])
                }
                else if(med2.text.isEmpty()&&p2>0)
                {
                    med2.setText(arrMedi[p2])

                }


                else if(med1.text.isNotEmpty() && med2.text.isNotEmpty()&&p2>0 ){

                    med1.setText("")
                    med2.setText("")
//                    med1.setText(arrMedi[p2])
                }




            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }



    }


//    private fun setSpinnerNurse(arrMedi:ArrayList<String>) {
//        arrMedi.add(0,"Choose medicine found by Nurse")
////        val arrMedi = ArrayList<String>()
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arrMedi)
//        val spin = findViewById<Spinner>(R.id.spinnerMediNurse)
//
//        spin.adapter = arr
//
//        val med1=findViewById<EditText>(R.id.medi1)
//        val med2=findViewById<EditText>(R.id.med2)
//        val med3=findViewById<TextView>(R.id.textView7)
//        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//
//                if(med1.text.isEmpty()&&p2>0){
//
//                    med1.setText(arrMedi[p2])
//                }
//                else if(med2.text.isEmpty()&&p2>0)
//                {
//                    med2.setText(arrMedi[p2])
//
//                }
//
//
//                else if(med1.text.isNotEmpty() && med2.text.isNotEmpty() &&p2>0){
//
//                    med1.setText(arrMedi[p2])
//                }
//
//
//
//
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//
//        }
//
//
//
//    }

}