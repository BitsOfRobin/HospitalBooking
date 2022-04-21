package com.example.hospitalbooking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MedicineViewCustomer : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userNum:Int=0
    private var docDetail:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_view_customer)
        readMedi()
        deleteMedi()
        refresh()
    }

    private fun readMedi() {
        val doctor = FirebaseAuth.getInstance().currentUser
        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val arraylist = ArrayList<String>()
        val arraylistDos = ArrayList<String>()
        val arraylistMedi = ArrayList<String>()
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listMedi)

        val docRef = mFirebaseDatabaseInstance?.collection("userMedicine")
        docRef?.get()?.addOnSuccessListener {



            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                arraylistPro.add(document.id.toString())
//                arraylist.add(document.data.toString())

                var medi=document.get("userMedicine").toString()
                var dos=document.get("userDosage").toString()
                var dis=document.get("userDisease").toString()
                var user=document.get("userId").toString()
//                arraylistUser.add(user)
                if(medi ==null){
                    arraylist.add("No records found")

                }
                else
                {
                    arraylist.add("Medicine: $medi\nDosage: $dos\nDisease:$dis\n User Id:$user")
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


//        val btn=findViewById<Button>(R.id.btnNext)
//
//        btn.setOnClickListener() {
////            val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
//            val intent= Intent(this,UserMedicine::class.java)
////            intent.putExtra("DoctorName", tempListViewClickedValue)
//            btn.context.startActivity(intent)
////            Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()
//
//
//
//        }










    }

    private fun deleteMedi(){

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docView=findViewById<ListView>(R.id.listMedi)

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

            val docRef = mFirebaseDatabaseInstance!!.collection("userMedicine").document("medicine")

// Remove the 'capital' field from the document
            val updates = hashMapOf<String, Any>(
                "userMedicine" to FieldValue.delete(),
                "userDosage" to FieldValue.delete(),
                "userDisease" to FieldValue.delete(),
            )

            docRef.update(updates).addOnCompleteListener {

                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()

            }



//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


        }


    }


    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

            readMedi()
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }


}