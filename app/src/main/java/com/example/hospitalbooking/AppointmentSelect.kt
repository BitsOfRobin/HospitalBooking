package com.example.hospitalbooking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AppointmentSelect : AppCompatActivity(){
//    private var arraylistTime = ArrayList<String>()
    private var docName=" "
    var nameNtime:String=" "
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_select)

        validAppoint()
//        appointSelect()
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

    private fun validAppoint()
    {


        var user=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                appointSelect()
            } else {

                val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            }

        }
    }


    private fun appointSelect() {
        var arraylistTime = ArrayList<String>()

        val tempHolder = intent.getStringExtra("DoctorName")
        val docSpin=findViewById<Spinner>(R.id.spinDocAppoint)
        docName=tempHolder.toString()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

//        val arraylistTime2= ArrayList<String>()
        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        val calendarDate= Calendar.getInstance().time
        docRef?.whereEqualTo("name",tempHolder)?.get()?.addOnSuccessListener {


            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                var time = document.get("Time")
                var time2 = document.get("Time2")


//                if(calendarDate.before(date))
//                {
                arraylistTime.add(time.toString())
                arraylistTime.add(time2.toString())

////                }
//                var time2 = document.get("Time2") as com.google.firebase.Timestamp
//                val date2 = time2.toDate()
//
////                if(calendarDate.before(date2))
////                {
//                arraylistTime.add(date2.toString())

//                }





            }


        }



        var appointedTime=" "

        arraylistTime= arrayListOf(arraylistTime.toString())
        Toast.makeText(this,"arraTime=$arraylistTime",Toast.LENGTH_SHORT).show()
//        for(i in arraylistTime.indices)
//        {
//            if(arraylistTime[i].isEmpty())
//            {
//                arraylistTime.remove(arraylistTime[i])
//            }
//
//        }
        Toast.makeText(this,"arraTime2=$arraylistTime",Toast.LENGTH_SHORT).show()
        val arr = ArrayAdapter(this, android.R.layout.simple_gallery_item, arraylistTime)
        docSpin.adapter = arr
        val btn=findViewById<Button>(R.id.btn)

        val txt=findViewById<TextView>(R.id.txtV)
        var checkBtn:Boolean=true
//            val str=docSpin.selectedItem.toString()
//            val str=arraylistTime[i].toString()
//        val str=docSpin.selectedIte()
//        btn.isClickable=false

        var deleteTime=0
        docSpin.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(arraylistTime[p2]!=null)
                {
                    txt.textSize=20f
                    txt.text = "Selected: "+arraylistTime[p2].toString()
                    appointedTime=arraylistTime[p2].toString()

//                    nameNtime = tempHolder +"\n" +arraylistTime[p2].toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
                    deleteTime=p2
                    btn.isEnabled=true

                    checkBtn=true

                }





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
                writeUser(appointedTime)
                val intent = Intent(this, DoctorAppointment::class.java)
                arraylistTime.remove(arraylistTime[deleteTime])
//                intent.putExtra("DoctorName", nameNtime)
                startActivity(intent)
//                Toast.makeText(
//                    this,
//                    "navigate to appointment ${nameNtime.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
//                writeUser(appointedTime)
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

    private fun writeUser(appointTime:String){

//        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

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
                loginUser = userGoogle.displayName.toString()
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
            "doctorAppoint" to appointTime,
            "user" to loginUser,
            "docName" to docName



        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("userAppointment")?.document( "$user")?.set(user)?.addOnSuccessListener {


//            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

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

