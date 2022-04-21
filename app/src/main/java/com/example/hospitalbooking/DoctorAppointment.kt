package com.example.hospitalbooking



import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.example.hospitalsmartt.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DoctorAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userNum:Int=0
    private var docDetail:String?=null
    private var arrayDel = ArrayList<String>()
    //    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointment)



//        writeUser()
        readUser()
        deleteUser()
        refresh()



    }



    private fun writeUser(){

        val tempHolder = intent.getStringExtra("DoctorName")
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


        val user= hashMapOf(
            "doctorAppoint" to tempHolder,
            "user" to "user$userNum"



        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("user")?.document( "user"+userNum.toString())?.set(user)?.addOnSuccessListener {


            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add user",Toast.LENGTH_SHORT).show()
            }






//        userNum+=1









    }


    private fun readUser() {
        val doctor = FirebaseAuth.getInstance().currentUser
        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName= ArrayList<String>()
        var user=" "
        var doc=" "
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listAppoint)

        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.get()?.addOnSuccessListener {



            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())
//                arraylist.add(document.data.toString())

//                var user=document.get("user").toString()


                val userGoogle = Firebase.auth.currentUser
                userGoogle.let {
                    // Name, email address, and profile photo Url
//                    val name = user.displayName
                    if (userGoogle != null) {
                        user = userGoogle.email.toString()
                    }

                    else{

                        user=" NOne"
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
                var docName=document.get("docName").toString()
                if(docName!=null)
                {
                    arraylistDocName.add(docName)

                }



                 doc=document.get("doctorAppoint").toString()
                arraylistUser.add(user)
                if(user ==null){
                    arraylist.add("No records found")

                }
                else
                {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")
                    arrayDel.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                }


//                Toast.makeText(
//                    this,
//                    "Enter the firebase${document.data.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Toast.makeText(
//                    this,
//                    "Enter the firebase id ${document.id.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

//            val spinDocName=findViewById<Spinner>(R.id.spinnerDoc)
//            val arrDocName=ArrayAdapter(this, android.R.layout.simple_list_item_1,arraylistDocName )
//            spinDocName.adapter=arrDocName
//            spinDocName.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    arraylist.add("User: $user\n Appointed Doctor:${arraylistDocName.elementAt(p2)}\n Appointment Detail: $doc\n")
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//
//
//            }

            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
            docView.adapter = arr



//            docView.setOnItemClickListener { adapterView, view, i, l ->
//                val intent= Intent(this,userLogin::class.java)
////                intent.putExtra("DoctorName", tempListViewClickedValue)
//                startActivity(intent)
//
//
//            }




//                val tempListViewClickedValue = arraylist[i].toString()

//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()





        }

//        docView.setOnItemClickListener { adapterView, view, i, l ->
//
//
//////           mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")
////            val updates = hashMapOf<String,FieldValue>(
////            "user" to FieldValue.delete(),
////            "doctorAppoint" to FieldValue.delete()
////            )
////
////            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.update(updates as Map<String, Any>)?.addOnCompleteListener {
////
////
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
////
////
////            }
//
////            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.delete()?.addOnSuccessListener {
////
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
////
////            }
//            var user=arraylistUser[i]
//
//            val docRef = mFirebaseDatabaseInstance!!.collection("$user").document("user$userNum")
//
//// Remove the 'capital' field from the document
//            val updates = hashMapOf<String, Any>(
//                "user" to FieldValue.delete(),
//                "doctorAppoint" to FieldValue.delete()
//            )
//
//            docRef.update(updates).addOnCompleteListener {
//
//                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
//
//
//
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//
//            }


        val btn=findViewById<Button>(R.id.btnNext)

        btn.setOnClickListener() {
//            val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
            val intent= Intent(this,FilterAppointment::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
            btn.context.startActivity(intent)
//            Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()



        }










    }

    private fun deleteUser(){

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docView=findViewById<ListView>(R.id.listAppoint)
        val userGoogle = Firebase.auth.currentUser
        var user=" "
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.email.toString()
            } else {

                user = " NOne"
            }
        }
        docView.setOnItemClickListener { adapterView, view, i, l ->


////           mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")
//            val updates = hashMapOf<String,FieldValue>(
//            "user" to FieldValue.delete(),
//            "doctorAppoint" to FieldValue.delete()
//            )
//
//            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.update(updates as Map<String, Any>)?.addOnCompleteListener {
//
//
//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//
//            }

//            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.delete()?.addOnSuccessListener {
//
//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
            Toast.makeText(this, "Success ${arrayDel.elementAt(i)}delete the user ", Toast.LENGTH_SHORT).show()
            val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDel.elementAt(i)}")

// Remove the 'capital' field from the document
            val updates = hashMapOf<String, Any>(
                "user" to FieldValue.delete(),
                "doctorAppoint" to FieldValue.delete(),
                "docName" to FieldValue.delete()
            )

            docRef.update(updates).addOnCompleteListener {

                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()

            }

            docRef.collection("userAppointment").document("${arrayDel.elementAt(i)}")
                .delete()
                .addOnSuccessListener {  Toast.makeText( this,"${arrayDel.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
                .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


        }


    }


    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

            readUser()
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }







}




