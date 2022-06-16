package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class DoctorViewAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view_appointment)
        readUserRecord()
    }

    private fun readUserRecord() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<Prescription>()
        val arraySearchUser = ArrayList<Prescription>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName = ArrayList<String>()
        val arrayForSearch=ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var doc = " "
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
//        val userGoogle = Firebase.auth.currentUser
//        userGoogle.let {
//            // Name, email address, and profile photo Url
////                    val name = user.displayName
//            if (userGoogle != null) {
//                user = userGoogle.displayName.toString()
//            } else {
////
//                user = " NOne"
//            }
//
//        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName != null) {
                    arraylistDocName.add(docName)

                }



                doc = document.get("doctorAppoint").toString()
                user=document.get("user").toString()


                arraylistUser.add(user)
//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {
                if(docName.contains("Dr"))
                {
//                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")
                    var doctime=doc
                    val index=doc.indexOf(",")
                    doctime=doctime.substring(0,index)+"\n"+doctime.substring(index,doctime.length)
                    arraylist.add(Prescription(user,docName,doctime))

                    arrayForSearch.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                    arraylistAppointment.add(AppointmentDetail(user, docName, doc))

                }


//                }


            }


            var arr = ListCustomAdapterForPrescription(this,arraylist)

            docView.adapter = arr

//            var count=0
//            val searchView=findViewById<SearchView>(R.id.searchDoc)
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String): Boolean {
//
//                    for(i in arraylist.indices)
//                    {
//
//                        if(arraylist.get(i).user.toString().contains(query))
//                        {
//                            arraySearchUser.add(Prescription(arraylist.get(i).user.toString(),
//                                arraylist.get(i).doc.toString(),arraylist.get(i).medicine.toString()))
//                            count++
//                        }
//
//                    }
//
//
//
//
//
//                    return false
//                }
//
//                override fun onQueryTextChange(p0: String): Boolean {
//                    for(i in arraylist.indices)
//                    {
//
//                        if(arraylist.get(i).user.toString().contains(p0))
//                        {
//                            arraySearchUser.add(Prescription(arraylist.get(i).user.toString(),
//                                arraylist.get(i).doc.toString(),arraylist.get(i).medicine.toString()))
//                            count++
//                        }
//
//                    }
//
//                    return false
//                }
//
//            })
//
//            if(count>0)
//            {
//                val arl = ListCustomAdapterForPrescription(this,arraySearchUser)
//
//                docView.adapter = arl
//
//            }
//
//            else{
//
//                arr = ListCustomAdapterForPrescription(this,arraylist)
//
//                docView.adapter = arr
//            }


            docView.setOnItemClickListener { adapterView, view, i, l ->

                val intent= Intent(this,PatientPrescription::class.java)
                intent.putExtra("userName", arrayForSearch[i])
                startActivity(intent)



            }

        }


    }





    class ListCustomAdapterForPrescription(var context: DoctorViewAppointment, private var pres:ArrayList<Prescription>):
        BaseAdapter() {
        override fun getCount(): Int {
            return pres.count()
        }

        override fun getItem(p0: Int): Any {
            return pres.get(p0)
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var view: View
            var viewHolder:ViewHolder
            if(p1==null)
            {
                var layout= LayoutInflater.from(context)
                view=layout.inflate(R.layout.list_view_prescription,p2,false)
                viewHolder=ViewHolder(view)
                view.tag=viewHolder
            }

            else{
                view=p1
                viewHolder=view.tag as ViewHolder
            }

            var prescription=getItem(p0) as Prescription
            viewHolder.txtName.text=prescription.user.toString()
            viewHolder.txtDoc.text=prescription.doc.toString()
            viewHolder.txtMedi.text=prescription.medicine.toString()


            return view as View

        }

        private  class ViewHolder(row: View?){
            lateinit var txtName: TextView
            lateinit var txtDoc: TextView
            lateinit var txtMedi: TextView
//        lateinit var ivImage:ImageView

            init {
                this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
                this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
                this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
//            this.ivImage=row?.findViewById(R.id.imgAppoint) as ImageView

            }

        }



    }



}