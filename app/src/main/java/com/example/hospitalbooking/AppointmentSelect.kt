package com.example.hospitalbooking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.hospitalbooking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class AppointmentSelect : AppCompatActivity(){
    private var arraylistTime = ArrayList<String>()
    private var docName=" "
    var nameNtime:String=" "
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_select)

        appointSelect()
    }

    private fun readUser():String{
        val arraylist = ArrayList<String>()
        var userId=" "
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


        }

        return userId
    }

    private fun appointSelect() {
        val tempHolder = intent.getStringExtra("DoctorName")
        val docSpin=findViewById<Spinner>(R.id.spin)
        docName=tempHolder.toString()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

//        val arraylistTime2= ArrayList<String>()
        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.whereEqualTo("name",tempHolder)?.get()?.addOnSuccessListener {


            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                var time = document.get("Time") as com.google.firebase.Timestamp
                val date = time.toDate()
                arraylistTime.add(date.toString())
                var time2 = document.get("Time2") as com.google.firebase.Timestamp
                val date2 = time2.toDate()

                arraylistTime.add(date2.toString())




            }


        }
        arraylistTime= arrayListOf(arraylistTime.toString())
        val arr = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arraylistTime)
        val btn=findViewById<Button>(R.id.btn)
        docSpin.adapter = arr
        val txt=findViewById<TextView>(R.id.txtV)
        var checkBtn:Boolean=true
//            val str=docSpin.selectedItem.toString()
//            val str=arraylistTime[i].toString()
//        val str=docSpin.selectedIte()
//        btn.isClickable=false
        docSpin.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                txt.textSize=20f
                txt.text = "Selected: "+arraylistTime[p2].toString()
                nameNtime = tempHolder +"\n" +arraylistTime[p2].toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()

                btn.isClickable=true
                checkBtn=true


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

                checkBtn=false
                btn.isClickable=false

            }


        }
//
        if(checkBtn)
        {

            btn.setOnClickListener() {
                val intent = Intent(this, DoctorAppointment::class.java)
//                intent.putExtra("DoctorName", nameNtime)
                startActivity(intent)
                Toast.makeText(
                    this,
                    "navigate to appointment ${nameNtime.toString()} ",
                    Toast.LENGTH_SHORT
                ).show()
                writeUser()
            }

        }

        else
        {

            Toast.makeText(
                this,
                "You do not choose an appointment time ",
                Toast.LENGTH_SHORT
            ).show()


        }



    }

    private fun writeUser(){

//        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
        Toast.makeText(this,"Enter getData",Toast.LENGTH_SHORT).show()
        val arraylist=ArrayList<String>()
        val arraylistPro=ArrayList<String>()
        //        val docView=findViewById<RecyclerView>(R.id.Rview)
//        val docView=findViewById<ListView>(R.id.Rview)
        //        val txt=findViewById<TextView>(R.id.txtV)
        //        val name=findViewById<TextView>(R.id.txtName)
        //        val pro=findViewById<TextView>(R.id.txtPro)

        var loginUser=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.email.toString()
            }

            else{

                loginUser=" NOne"
            }
//                    val photoUrl = user.photoUrl
//
//                    // Check if user's email is verified
//                    val emailVerified = user.isEmailVerified
//
//                    // The user's ID, unique to the Firebase project. Do NOT use this value to
//                    // authenticate with your backend server, if you have one. Use
//                    // FirebaseUser.getToken() instead.
//                    val uid = user.uid
        }
//        val loginUser=readUser()
        val user= hashMapOf(
            "doctorAppoint" to nameNtime,
            "user" to loginUser,
            "docName" to docName



        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("userAppointment")?.document( "$user")?.set(user)?.addOnSuccessListener {


            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add user",Toast.LENGTH_SHORT).show()
            }






//        userNum+=1









    }






}

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val txt=findViewById<TextView>(R.id.textView)
//            txt.text=arraylistTime[p2].toString()
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        val txt=findViewById<TextView>(R.id.textView)
//        txt.text="Please select 1 item from spinner"
//    }

