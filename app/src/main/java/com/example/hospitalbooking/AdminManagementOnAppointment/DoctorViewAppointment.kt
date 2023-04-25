package com.example.hospitalbooking.AdminManagementOnAppointment

import com.example.hospitalbooking.KotlinClass.MyCache
import android.annotation.SuppressLint
import android.app.backup.BackupManager.dataChanged
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hospitalbooking.Adapter.ListCustomAdapterForPrescription
import com.example.hospitalbooking.BookingAppointment.MainPageViewModel
import com.example.hospitalbooking.BookingAppointment.MainPageViewModelFactory
import com.example.hospitalbooking.KotlinClass.AppointmentDetail
import com.example.hospitalbooking.PrescriptionControl.PatientPrescription
import com.example.hospitalbooking.KotlinClass.Prescription
import com.example.hospitalbooking.R
import com.example.hospitalbooking.databinding.ActivityDoctorViewAppointmentBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorViewAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var fragmentInput:String?=null
    private var tempList=ArrayList<Prescription>()
    val arraylist = ArrayList<Prescription>()
    val arraySearchUser = ArrayList<Prescription>()
    val arraylistPro = ArrayList<String>()
    val arraylistUser = ArrayList<String>()
    var arraylistDocName = ArrayList<String>()
    var appointment = ArrayList<String>()
    val arrayForSearch=ArrayList<String>()
    val arraylistAppointment = ArrayList<AppointmentDetail>()
//    private lateinit var binding:ActivityDoctorViewAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding= ActivityDoctorViewAppointmentBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_doctor_view_appointment)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Appointment Checking")
        readUserRecord()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun readUserRecord() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
//        val arraylist = ArrayList<Prescription>()
////        val arraySearchUser = ArrayList<Prescription>()
//        val arraylistPro = ArrayList<String>()
//        val arraylistUser = ArrayList<String>()
//        var arraylistDocName = ArrayList<String>()
//        var appointment = ArrayList<String>()
//        val arrayForSearch=ArrayList<String>()
//        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var docApp = " "
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
            } else {
//
                user = " NOne"
            }

        }
        val docRef =
            mFirebaseDatabaseInstance?.collection("userAppointment")?.whereEqualTo("docName","Dr $user")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }
            val arrayVisitStatus=ArrayList<String>()

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                val docName = document.get("docName").toString()
                if (docName != "null") {
                    arraylistDocName.add(docName)

                }



                docApp = document.get("doctorAppoint").toString()
               val visitStatus = document.get("visitStatus").toString()
                arrayVisitStatus.add(visitStatus)
                user=document.get("user").toString()
                if(docApp!="null") {
                    appointment.add(docApp)

                }

                if(user!="null"){

                    arraylistUser.add(user)
                }

//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {
                if(docName.contains("Dr")&&docApp!="null"&&user!="null")
                {
//                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")
                    var doctime=docApp
                    val index=docApp.indexOf(",")
                    doctime=doctime.substring(0,index)+"\n"+doctime.substring(index,doctime.length)
                    arraylist.add(Prescription(user,docName,doctime,0F,visitStatus,""))

                    arrayForSearch.add("{docName=$docName, doctorAppoint=$docApp, user=$user}")
                    arraylistAppointment.add(AppointmentDetail(user, docName, docApp,""))

                }


//                }


            }


            val arr = ListCustomAdapterForPrescription(this,arraylist)
//            fragmentSearchPatient(arr)
            docView.adapter = arr


            val searchView=findViewById<SearchView>(R.id.searchDoc)
            searchView.queryHint="search User"

//            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocomplete_text_view)
//
//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistUser)
//            autoCompleteTextView.setAdapter(adapter)
//            autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
//                val selectedItem = parent.getItemAtPosition(position) as String
//                searchView.setQuery(selectedItem, true)
//            }


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {

                    tempList.clear()
                    if (p0 != null) {
                        if(p0.isNotEmpty()) {
                            for(i in arraylistUser.indices)
                            {
                                if(arraylistUser[i].contains(p0,true))
                                {
                                    tempList.add(Prescription(arraylistUser[i],arraylistDocName[i],appointment[i],
                                        0F,arrayVisitStatus[i],""))

                                }

                            }


                                dataChanged()



                        }


                        else
                        {
                            docView.adapter = arr

                        }
                    }

//                    if (p0 != null) {
//                        showSuggestion(p0,adapter)
//                    }
                    return false
                }



            })



           docView.setOnItemClickListener { adapterView, view, i, l ->

//                val intent= Intent(this, PatientPrescription::class.java)

                var doctorAppointment=" "
                var docName=""
                var userName=""
                var appointment=""
                if(tempList.isNotEmpty()){
//                    for(k in arrayForSearch.indices){
//
//                        if(arrayForSearch[k].contains(tempList.get(i).user.toString())
//                            &&arrayForSearch[k].contains(tempList.get(i).doc.toString())){

//                            doctorAppointment=arrayForSearch[k].toString()
                            docName=tempList.get(i).doc.toString()
                            userName=tempList.get(i).user.toString()
                            appointment=tempList.get(i).medicine.toString()

//                        }

                    if(currentDateTime(appointment)){
                        clickToCertify(docName,userName,appointment)
                    }

                    else{

                        Toast.makeText(this,"Sorry Doctor this appointment does not reach the time",Toast.LENGTH_LONG).show()
                    }


                    }




                else{
//
                    docName=arraylistAppointment.get(i).docName
                    userName=arraylistAppointment.get(i).userName
                    appointment=arraylistAppointment.get(i).AppointmentDetail


                    if(currentDateTime(appointment)){
                        clickToCertify(docName,userName,appointment)
                    }

                    else{

                        Toast.makeText(this,"Sorry Doctor this appointment does not reach the time",Toast.LENGTH_LONG).show()
                    }

                }






            }

        }






    }

    private fun currentDateTime(dateString:String):Boolean{



        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
        val date = dateFormat.parse(dateString)

        val now=Calendar.getInstance().time

        return now.after(date)

    }






    private fun clickToCertify(docName:String,userName:String,appointment:String){





            val builder = AlertDialog.Builder(this)
            builder.setTitle("Certify the present of patient")
            builder.setMessage("Are you sure to finalize the appointment?")


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT
                ).show()

                updateVisitStatus(docName,userName,appointment)
//                Toast.makeText(this,"The patient is presented",Toast.LENGTH_LONG).show()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()

                Toast.makeText(this,"The status is not updated",Toast.LENGTH_LONG).show()

            }




            builder.show()



    }


    private fun updateVisitStatus(docName:String,userName:String,appointment:String){
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val userAppointmentID ="{docName=$docName, doctorAppoint=$appointment, user=$userName}"



        val updateVisit = hashMapOf(

            "visitStatus" to "Visited"


        )

        mFirebaseDatabaseInstance!!.collection("userAppointment")
            .document(userAppointmentID).update(updateVisit as Map<String, Any>).addOnSuccessListener {

                Toast.makeText(this,"Visit Status is updated Successfully",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Visit Status is Failed to Update",Toast.LENGTH_LONG).show()
            }

    }



    private fun showSuggestion(query: String, adapter: ArrayAdapter<String>){


        val suggestions = arraylistUser.filter { it.contains(query) }
        adapter.clear()
        adapter.addAll(suggestions)
        adapter.notifyDataSetChanged()



    }



    private  fun dataChanged()
    {

    val docView = findViewById<ListView>(R.id.listDocAppoint)
    val arr = ListCustomAdapterForPrescription(this,tempList)

    docView.adapter = arr
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
            var viewHolder: ViewHolder
            if(p1==null)
            {
                var layout= LayoutInflater.from(context)
                view=layout.inflate(R.layout.list_view_prescription,p2,false)
                viewHolder= ViewHolder(view)
                view.tag=viewHolder
            }

            else{
                view=p1
                viewHolder=view.tag as ViewHolder
            }

            var prescription=getItem(p0) as Prescription
            var userName=prescription.user.toString()
            if(userName!="null"){

                viewHolder.txtName.text=prescription.user.toString()
            }

//            viewHolder.txtMedi.text=prescription.medicine.toString()




            var dateInString = prescription.medicine.toString()

            if(dateInString!="null"&&dateInString.isNotEmpty()&&dateInString.isNotBlank()){
                dateInString = dateInString.replace(" ", "-")

                if (dateInString[0].toString()!="n"&&dateInString[0].toString().toInt() < 10) {
                    dateInString = "0$dateInString"

                }
                val calendarDate = Calendar.getInstance().time


//        val utc = TimeZone.getTimeZone("UTC")
//        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
//        sourceFormat.timeZone =utc

//
//        val convertedDate = sourceFormat.parse(dateInString)
//        destFormat.format(convertedDate)


                val formatter = SimpleDateFormat("dd-MMM-yyyy")
                val date = formatter.parse(dateInString)
                if (calendarDate.after(date) ) {

                    viewHolder.txtMedi.text=prescription.medicine.toString()
                    viewHolder.txtMedi.setTextColor(Color.parseColor("#282BDC"))


                }
                else{
                    viewHolder.txtMedi.text=prescription.medicine.toString()
                    viewHolder.txtMedi.setTextColor(Color.parseColor("#FFE91E63"))

                }
            }


//
//            if (dateInString[0].toString()!="n"&&dateInString[0].toString().toInt() < 10) {
//                dateInString = "0$dateInString"
//
//            }
//            val calendarDate = Calendar.getInstance().time
//
//
////        val utc = TimeZone.getTimeZone("UTC")
////        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
////        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
////        sourceFormat.timeZone =utc
//
////
////        val convertedDate = sourceFormat.parse(dateInString)
////        destFormat.format(convertedDate)
//
//
//            val formatter = SimpleDateFormat("dd-MMM-yyyy")
//            val date = formatter.parse(dateInString)
//            if (calendarDate.after(date) ) {
//
//                viewHolder.txtMedi.text=prescription.medicine.toString()
//                viewHolder.txtMedi.setTextColor(Color.parseColor("#282BDC"))
//
//
//            }
//            else{
//                viewHolder.txtMedi.text=prescription.medicine.toString()
//                viewHolder.txtMedi.setTextColor(Color.parseColor("#FFE91E63"))
//
//            }





            var CheckName=prescription.doc.toString()
            var docName=pres[p0].doc.toString()
            if (CheckName != "null") {
                if(CheckName.length>10) {
                    var index=CheckName.indexOf(" ",5,true)
                    CheckName=CheckName.substring(0,index)+"\n"+CheckName.substring(index,CheckName.length)
                    viewHolder.txtDoc.text =CheckName

                }

                else{
                    viewHolder.txtDoc.text =CheckName

                }
            }

            val cache= MyCache()
//        Glide.with(context)
//            .load(cache.retrieveBitmapFromCache("a"))
//            .into(viewHolder.ivImage2)
//        val cache=com.example.hospitalbooking.KotlinClass.MyCache()
//        cache.saveBitmapToCahche(firebaseUser.toString(),bitmap)
//        cache.retrieveBitmapFromCache(firebaseUser.toString())
//        Picasso.get().load(image).into(viewHolder.ivImage);
            val bit: Bitmap? =cache.retrieveBitmapFromCache(docName)
            Glide.with(context)
                .load(bit)
                .into(viewHolder.ivImage2)

            viewHolder.txtVisit.text=prescription.visitStatus.toString()

            return view as View

        }







        private  class ViewHolder(row: View?){
            lateinit var txtName: TextView
            lateinit var txtDoc: TextView
            lateinit var txtVisit: TextView
            lateinit var txtMedi: TextView
        lateinit var ivImage2:ImageView

            init {
                this.txtName=row?.findViewById(R.id.txtUser) as TextView
                this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
                this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
                this.txtVisit= row.findViewById(R.id.txtVisitStatus) as TextView
//            this.ivImage=row?.findViewById(R.id.imgAppoint) as ImageView
                this.ivImage2= row.findViewById(R.id.imageView3) as ImageView


            }

        }



    }


    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser




    }


//    fun captureInput(p0:String){
//
//        fragmentInput?.replace("",p0,true)
//
//
//    }
//
//
//    private fun fragmentSearchPatient(arr: ListCustomAdapterForPrescription) {
//
//
//        val docView = findViewById<ListView>(R.id.listDocAppoint)
//
//        var tempList = ArrayList<Prescription>()
//        val searchView = findViewById<SearchView>(R.id.searchDoc)
////        searchView.queryHint = "search User"
//
//
//        tempList.clear()
//        if (fragmentInput != null) {
//            if (fragmentInput!!.isNotEmpty()) {
//                for (i in arraylistUser.indices) {
//                    if (arraylistUser[i].contains(fragmentInput!!, true)) {
//                        tempList.add(
//                            Prescription(
//                                arraylistUser[i],
//                                arraylistDocName[i],
//                                appointment[i]
//                            )
//                        )
//
//                    }
//
//                }
//
//
//                dataChanged(tempList)
//
//
//            } else {
//                docView.adapter = arr
//
//            }
//        }
//
//
//
//
//
//    }

}