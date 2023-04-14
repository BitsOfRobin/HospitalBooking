package com.example.hospitalbooking.BookingAppointment

import android.annotation.SuppressLint
import android.app.backup.BackupManager.dataChanged
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.hospitalbooking.AdminManagementOnAppointment.CalendarTimePicker
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.UserLogin
import com.example.hospitalbooking.KotlinClass.ModalFormMain
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import kotlin.collections.ArrayList




class MainPageViewModel(private val p0:ArrayList<String>, private  val num:Int):ViewModel() {


    val _modalList= MutableLiveData<List<ModalFormMain>>()
    val modalListLive: LiveData<List<ModalFormMain>>
        get() = _modalList
    val _modalListSearch= MutableLiveData<List<ModalFormMain>>()
    val modalListLiveSearch: LiveData<List<ModalFormMain>>
        get() = _modalListSearch
    val _arrName= MutableLiveData<List<String>>()
    val arrName: LiveData<List<String>>
        get() = _arrName
    val _searchQueryLiveData = MutableLiveData<String>()
    val searchQueryLiveData :LiveData<String>
            get()=_searchQueryLiveData

    var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    var docDetail: String? = null
    val modalListSearch = ArrayList<ModalFormMain>()

    //    private lateinit var binding: ActivityMainBinding
    var modalList = ArrayList<ModalFormMain>()
    var arraylistName = ArrayList<String>()
    val arraylistData = ArrayList<String>()
    val arraylistTime = ArrayList<String>()
    val arraylistTime2 = ArrayList<String>()
    val arraylistPro = ArrayList<String>()
     val arraylistHospital = ArrayList<String>()
    init {

        getDataDoc()
        retrieveCache()


//        if(p0.isNotEmpty()){
//            dataChanged(p0,"")
//
//        }







        Log.i("ScoreViewModel", "Final score is $modalList")

    }

    fun getSearchQuery(str:String){

       _searchQueryLiveData.value=str

    }


    fun searchDoctor(p0:String){

        val temp = ArrayList<String>()
        val tempHos = ArrayList<String>()
        val tempName = ArrayList<String>()
        temp.clear()
        tempHos.clear()
        tempName.clear()


        for (i in arraylistPro.indices) {
            if (arraylistPro[i].contains(p0, true)) {
                temp.add(arraylistName[i])

            }

            else if (arraylistHospital[i].contains(p0, true)) {

                tempHos.add(arraylistName[i])

            }

            else if (arraylistName[i].contains(p0, true)) {

                tempName.add(arraylistName[i])

            }

        }


//        for (k in arraylistHospital.indices) {
//            if (arraylistHospital[k].contains(p0, true)) {
//
//                tempHos.add(arraylistName[k])
//
//            }
//        }
//        for (l in arraylistName.indices) {
//            if (arraylistName[l].contains(p0, true)) {
//
//                tempName.add(arraylistName[l])
//
//            }
//        }

        if (temp.isNotEmpty()) {

                dataChanged(temp,"")
//                            paramForSearching()
        }
        else if(tempHos.isNotEmpty()) {

            dataChanged(tempHos,"")
//                            paramForSearching()


        }
        else if(tempName.isNotEmpty()){


            dataChanged(tempName,"")
//                            paramForSearching()
        }

//                    callForSearching(tempHos,searchQuery,docView)

    }



     fun getDataDoc() {


        val doctor = FirebaseAuth.getInstance().currentUser

        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()


        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.get()?.addOnSuccessListener {




//                }
            arraylistData.clear()
            arraylistName.clear()
            arraylistTime.clear()
            arraylistTime2.clear()
            arraylistPro.clear()
            modalList.clear()
            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                val time = document.get("Time").toString()
                val time2 = document.get("Time2").toString()
//                var time2 = document.get("Time2") as com.google.firebase.Timestamp
//                val date2 = time2.toDate()
                var date = time
//                arraylistTime.add(date.toString())
                val name = document.get("name").toString()
                val pro = document.get("pro").toString()

                val hospital = document.get("hospital").toString()


                arraylistHospital.add(hospital)
//                Toast.makeText(this,"hos$arraylistHospital",Toast.LENGTH_LONG).show()
//                Toast.makeText(this,"hosp$hospital",Toast.LENGTH_LONG).show()
                if (name.contains("Dr")) {

                    arraylistName.add(name)
//                if(hospital.isNotEmpty()&&hospital.isNotBlank()){
//
//
//                }

                    arraylistPro.add(pro)
                    if (date.contains("0")) {
                        val index = date.indexOf(",")
                        date =
                            date.substring(0, index) + "\n" + date.substring(index, date.length - 2)

                        arraylistTime.add(date)

                    } else {

                        arraylistTime.add(" ")
                    }
                    if (time2.contains("0")) {
                        val index = time2.indexOf(",")
                        var date2 = time2.substring(0, index) + "\n" + time2.substring(
                            index,
                            time2.length - 2
                        )

                        arraylistTime2.add(date2)

                    } else {

                        arraylistTime2.add(" ")
                    }

                    arraylistData.add("Name: $name \nProfessional:\n $pro \nAvailable Date:\n$date \n")
                }


//                Toast.makeText(this, "Enter the first read ${arraylistTime.toString()} ", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Enter the firebase id ${document.id.toString()} ", Toast.LENGTH_SHORT).show()
            }

            _arrName.value=arraylistName
//                val doc= listOf(arraylist)
            var imageArr = ArrayList<Bitmap>()
//            var images= intArrayOf()
//            var dt=1
            var arrBitMap = ArrayList<Bitmap>()
            val file = File.createTempFile("img", "jpg")






//
            val cache= MyCache()
            var check=0

            for(i in arraylistName.indices){

                if(cache.retrieveBitmapFromCache(arraylistName[i])==null){


                    check++



                }

            }




            if(check>0){


                getImg()
                retrieveCache()
//                    refreshMain()
//                    retrieveCache()



            }

            else{

                retrieveCache()
            }

            if(arraylistName.size>modalList.size){


                retrieveCache()

            }






        }



            ?.addOnFailureListener {
//                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }




//        var user = " "
//        var userEmail = " "
//        val userGoogle = Firebase.auth.currentUser
//        userGoogle.let {
//            // Name, email address, and profile photo Url
////                    val name = user.displayName
//            if (userGoogle != null) {
//                user = userGoogle.displayName.toString()
//                userEmail = userGoogle.email.toString()
//                naviImg(userGoogle!!.photoUrl,user)
//            } else {
//
//                val intent = Intent(this, UserLogin::class.java)
////            intent.putExtra("DoctorName", tempListViewClickedValue)
//                startActivity(intent)
//            }








//        if (userEmail.contains("@student.tar")) {
//            var tempListViewClickedValue=""
//            var dtname=getGoogleName()
//
//
//
//            docView.setOnItemClickListener { adapterView, view, i, l ->
//                tempListViewClickedValue = modalList.get(i).docName.toString()
////                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
//
//
//
//
//
//                if(tempListViewClickedValue=="Dr $dtname"){
//
////                    val intent = Intent(this, CalendarTimePicker::class.java)
////                    intent.putExtra("DoctorName", tempListViewClickedValue)
////                    startActivity(intent)
//                }
//
//                else{
//
//                    Toast.makeText(this,"this is not your profile",Toast.LENGTH_LONG).show()
//
//                }
//
////                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()
//
//
//            }
//




//            deleteDoc(dtname)
//
//        } else {
//
//            docView.setOnItemClickListener { adapterView, view, i, l ->
//
//                var name = " "
////                val time = modalList.get(i).time.toString()
////            val time = arraylistTime[i].toString()
//
//
//                name = if(modalListSearch.isNotEmpty()){
//
//                    modalListSearch.get(i).docName.toString()
//
//                } else{
//
//
//                    modalList.get(i).docName.toString()
//                }
//
//







////                    writeUser(time,name,user)
////                    val intent = Intent(this, AppointmentSelect::class.java)
//                val intent = Intent(this, CalendarTimePicker::class.java)
//                intent.putExtra("DoctorName", name)
//                startActivity(intent)
//
//
//            }
//
//
//        }






    }


    fun getImg() {
//        arraylistName.ensureCapacity(arraylistData.size)
        val arrBitMap = ArrayList<Bitmap>()
//        count=0
//        val docView = findViewById<GridView>(R.id.gridView)
        var detect = 0
        val extractName = ArrayList<String>()
        val cache= MyCache()
//        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        var times=0

//        count = arraylistName.size
//             Toast.makeText(this, "${arraylistName}",Toast.LENGTH_SHORT).show()
        for (i in arraylistName.indices) {

            val fireb = Firebase.storage.reference.child("Img/${arraylistName.get(i)}.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
            val localfile = File.createTempFile("tempImage", "jpg")




            fireb.getFile(localfile).addOnCompleteListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)





                cache.saveBitmapToCahche(arraylistName[i],bitmap)
                arrBitMap.add(bitmap)






                times++


            }.addOnFailureListener {



//                Toast.makeText(this, "${arraylistName }failed to retrieve iamge$extractName", Toast.LENGTH_SHORT).show()
            }

//                dt++
        }







//        }

//        val customAdapter = CustomAdapter(modalList, this)
//
//
//
////        docView.adapter = customAdapter
//
//        searchDoc(customAdapter)
//
//        swipe.isRefreshing=false
//        if(call!=0){

    }

    fun retrieveCache(){


        val cache= MyCache()
        for (i in arraylistName.indices) {
            val bitmap: Bitmap? =cache.retrieveBitmapFromCache(arraylistName[i])
            var time = arraylistTime[i] + "\n\n" + arraylistTime2[i]

            bitmap?.let {
                ModalFormMain(
                    arraylistPro[i],
                    it,
                    arraylistName[i],
                    time,
                    arraylistHospital[i]
                )
            }?.let {
                modalList.add(
                    it
                )
            }

//            Toast.makeText(this,"b$bitmap", Toast.LENGTH_SHORT ).show()

        }

        _modalList.value=modalList



        sortByPro()







    }





    fun retrieveImg(extractName: ArrayList<String>) {
        val arrBitMap = ArrayList<Bitmap>()
//        count=0

        var detect = 0

        if (modalList.size > arraylistData.size) {



        } else {
//            var i=0

//             Toast.makeText(this, "${arraylistName}",Toast.LENGTH_SHORT).show()
            for (i in arraylistName.indices) {
                if (!extractName.contains(arraylistName[i])) {  ///TRY THIS OUT
                    val fireb = Firebase.storage.reference.child("Img/${arraylistName.get(i)}.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
                    val localfile = File.createTempFile("tempImage", "jpg")
                    var bitmap: Bitmap
                    fireb.getFile(localfile).addOnSuccessListener {
//                 bitmap=BitmapFactory.decodeFile(file.absolutePath)
//                imageArr.add(bitmap)
                        bitmap = BitmapFactory.decodeFile(localfile.absolutePath)




//                            {
                        val time = arraylistTime[i] + "\n" + arraylistTime2[i]
                        if (modalList.isEmpty()) {
                            modalList.add(
                                ModalFormMain(
                                    arraylistPro[i],
                                    bitmap,
                                    arraylistName[i],
                                    time,
                                    arraylistHospital[i]
                                )
                            )

                        } else {

                            if (!extractName.contains(arraylistName[i])||extractName.isEmpty()) {
                                modalList.add(
                                    ModalFormMain(
                                        arraylistPro[i],
                                        bitmap,
                                        arraylistName[i],
                                        time,
                                        arraylistHospital[i]
                                    )
                                )

                            }


                        }


//                            }

//                        }

//                    }


//                    Toast.makeText(this,"name=$modalList",Toast.LENGTH_SHORT).show()

//                    count=0
                        arrBitMap.add(bitmap)
//                    modalList.add(docModal(arraylist[i],))
                        val cache= MyCache()
                        cache.saveBitmapToCahche(arraylistName[i],bitmap)
//                    Toast.makeText(this,"success to retrieve iamge",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        if(modalList.size!=arraylistName.size){

                            getImg()
                        }

//                    refresh()
//                    ++call

                    }

//                dt++
                }
            }
        }




    }


     fun setDoctor() {
//        Toast.makeText(this,"enter set Doctor ", Toast.LENGTH_SHORT).show()
        val docName = "Mr Liew"


//        val doctor=FirebaseAuth.getInstance()
        val doctor = hashMapOf(
            "name" to docName


        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("doctor")?.document("docDetail")?.set(doctor)
            ?.addOnSuccessListener {


//                Toast.makeText(this, "Successfully added ", Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

//                Toast.makeText(this, "Failed to add ", Toast.LENGTH_SHORT).show()
            }


    }


    fun writeUser(appointTime: String, docName: String, loginUser: String) {

//        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val arraylist = java.util.ArrayList<String>()
        val arraylistPro = java.util.ArrayList<String>()

        val user = hashMapOf(
            "doctorAppoint" to appointTime,
            "user" to loginUser,
            "docName" to docName


        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("userAppointment")?.document("$user")?.set(user)
            ?.addOnSuccessListener {


//            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

//                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }


//        userNum+=1


    }

     fun deletionDoctor(dtname: String,i:Int) {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()








         if(i>-1) {



                    val deleteDoc = modalList.get(i).docName

                    if (deleteDoc =="Dr $dtname") {
                        val docRef = mFirebaseDatabaseInstance!!.collection("doctor")
                            .document("${modalList.get(i).docName}")

                        docRef
                            .delete()
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener {

                            }


                        val fireb =
                            Firebase.storage.reference.child("Img/${modalList.get(i).docName}.jpg")
                        fireb.delete().addOnSuccessListener {

                        }.addOnFailureListener {

                        }

                        modalList.removeAt(i)

                        _modalList.value=modalList
//                                val arr = CustomAdapter(modalList, this)
//                                arr.notifyDataSetChanged()
//                                docView.adapter = arr

                    }


                }

     }














//    fun searchDoc() {
//
//
//
//
////        searchView()
//
//        val temp = ArrayList<String>()
//        val tempHos = ArrayList<String>()
//        val tempName = ArrayList<String>()
//
//
////        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocomplete_text_view)
////
////        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistPro)
////        autoCompleteTextView.setAdapter(adapter)
////        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
////                        val selectedItem = parent.getItemAtPosition(position) as String
////
////
////
////            //            if(autoCompleteTextView.text.isEmpty() || autoCompleteTextView.text.isBlank()){
////            //
////            //
////            //                searchView.setQuery(null,true)
////            //            }
////            //            else{
////
////                        searchView.setQuery(selectedItem, true)
////            //            }
////
////
////
////
////
////        }
//
//
//
//
//
//
//
//
//
//
//
//                temp.clear()
//                tempHos.clear()
//                tempName.clear()
//                var searchQuery=""
////                val first= p0?.let { p0.toString().indexOf(it[0]) }
//                if (p0 != null) {
//                    for (i in arraylistPro.indices) {
//                        if (arraylistPro[i].contains(p0, true)) {
//                            temp.add(arraylistName[i])
//                            searchQuery.replace("",p0)
//
//                        }
//
//                    }
//
//
//                    for (i in arraylistHospital.indices) {
//                        if (arraylistHospital[i].contains(p0, true)) {
//
//                            tempHos.add(arraylistName[i])
//                            searchQuery.replace("", p0)
//                        }
//                    }
//                    for (i in arraylistName.indices) {
//                        if (arraylistName[i].contains(p0, true)) {
//
//                            tempName.add(arraylistName[i])
//                            searchQuery.replace("", p0)
//                        }
//                    }
//
//                    if (temp.isNotEmpty()) {
//                        callForSearching(temp,searchQuery)
//                    }
//                    else if(tempHos.isNotEmpty()) {
//
//
//
//                        callForSearching(tempHos,searchQuery)
//
//                    }
//                    else if(tempName.isNotEmpty()){
//
//                        callForSearching(tempName,searchQuery)
//
//                    }
//
////                    callForSearching(tempHos,searchQuery,docView)
//
//
//
//                } else {
////                    showMsg(docView)
//
//
//                }
//
//
//
//
//
//
//            }






     fun callForSearching(temp:ArrayList<String>,searchQuery:String){


        if (temp.isNotEmpty()) {
            dataChanged(temp, searchQuery)
        }
    }









     fun dataChanged(tempName: ArrayList<String>, searchQuery: String) {


        modalListSearch.clear()
        var j = 0
        val size=tempName.size
        for (i in arraylistName.indices) {
            if (arraylistName[i] == tempName[j]) {

                val cache= MyCache()
                val bitmap: Bitmap? =cache.retrieveBitmapFromCache(tempName[j])
                val time = arraylistTime[i] + "\n" + arraylistTime2[i]
                bitmap?.let {
                    ModalFormMain(
                        arraylistPro[i],
                        it,
                        arraylistName[i],
                        time,
                        arraylistHospital[i]
                    )
                }?.let {
                    modalListSearch.add(
                        it
                    )
                }




//                if (j < tempName.size - 1) {
//
//
//                }


//                setColorText(i)

            }
            else{
                if(j < size-1){
                    j++
                }

            }





//                dt++
        }

         _modalListSearch.value=modalListSearch



    }






  fun sortByPro(){


        if(modalList.isNotEmpty()) {
            Collections.sort(modalList, object :


                java.util.Comparator<ModalFormMain> {
                override fun compare(p0: ModalFormMain?, p1: ModalFormMain?): Int {
                    var num = 0

                    if (p0 != null) {
                        if (p1 != null) {
                            num = p0.pro.toString() compareTo (p1.pro.toString())
                        }
                    }

                    return num
                }


            })

        }

      _modalList.value=modalList

    }



    fun sortByName(){

        Collections.sort(modalList, object :


            java.util.Comparator<ModalFormMain> {
            override fun compare(p0: ModalFormMain?, p1: ModalFormMain?): Int {
                var num = 0

                if (p0 != null) {
                    if (p1 != null) {
                        num = p0.docName.toString() compareTo (p1.docName.toString())
                    }
                }

                return num
            }


        })

        _modalList.value=modalList



    }


    fun getGoogleName(): String {



        var dtname=""
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                dtname = userGoogle.displayName.toString()

            }
        }


        return dtname

    }













}