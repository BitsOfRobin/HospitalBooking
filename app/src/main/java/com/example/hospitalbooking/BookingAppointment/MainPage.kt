package com.example.hospitalbooking.BookingAppointment



import com.example.hospitalbooking.KotlinClass.MyCache
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide.with
import com.example.hospitalbooking.*
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.UserLogin
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.AdminManagementOnAppointment.CalendarTimePicker
import com.example.hospitalbooking.KotlinClass.ModalFormMain
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.bottomnavigation.BottomNavigationView
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


class MainPage : AppCompatActivity() {
    private lateinit var toggle:ActionBarDrawerToggle
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    private var docDetail: String? = null
     private val modalListSearch = ArrayList<ModalFormMain>()
    private var fragmentInput:String?=null
    var str=SpannableString("s")
    //    private lateinit var binding: ActivityMainBinding
    private var modalList = ArrayList<ModalFormMain>()
    var images = intArrayOf(R.drawable.dt1, R.drawable.dt2, R.drawable.dt3)
    private var arraylistName = ArrayList<String>()
    private val arraylistData = ArrayList<String>()
    private val arraylistTime = ArrayList<String>()
    private val arraylistTime2 = ArrayList<String>()
    private val arraylistPro = ArrayList<String>()
    private val arraylistHospital = ArrayList<String>()
    private val arraylistEmpty = ArrayList<String>()
    private lateinit var mainPageViewModel: MainPageViewModel
    //    private val docView: GridView =findViewById<GridView>(R.id.gridView)
    private var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_page)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Appointment")

        val docView = findViewById<GridView>(R.id.gridView)
        arraylistEmpty.add("")


        val drawer = findViewById<BottomNavigationView>(R.id.naviBtm)
        drawer.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home -> mainAc()


            }

            true

            Toast.makeText(this, "Enter the layout ", Toast.LENGTH_SHORT).show()


        }




        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        setDoctor()






        var user = " "
        var userEmail = " "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                userEmail = userGoogle.email.toString()
                naviImg(userGoogle.photoUrl,user)
            } else {

                val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            }

        }





        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
                            .get(MainPageViewModel::class.java)
       mainPageViewModel.getDataDoc()
//        var getCache=mainPageViewModel.retrieveCache()
//        displayAdapter()
        mainPageViewModel.retrieveCache()
        displayAdapter()



        val cache= MyCache()
        var check=0


//
//        mainPageViewModel.arrName.observe(this, androidx.lifecycle.Observer {
//
//
//            for(i in it.indices){
//
//                if(cache.retrieveBitmapFromCache(it[i])==null){
//
//
//                    check++
//
//                }
//            }
//
//
//
//
//
//
//
//        })
//
//
//        if(check>0){
//
//
//            mainPageViewModel.getImg()
//            mainPageViewModel.retrieveCache()
//            displayAdapter()
//            refreshMain()
//
////                    refreshMain()
////                    retrieveCache()
//
//
//
//        }
//
//        else{
////            refreshMain()
//          mainPageViewModel.retrieveCache()
//            displayAdapter()
//
//        }







//        displayDocData()
        callViewModel(docView,userEmail)
        sorting()
        doctorSearching()
//        searchDoctor()















//        getDataDoc()
        showNavBar()
//        getDataDoc()
//        getDataDoc()

        refresh()
//        refreshMain()
//        getDataDoc()



    }

//    private fun displayDocData(){
//
//        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory("",-1))
//            .get(MainPageViewModel::class.java)
//        mainPageViewModel.getDataDoc()
//
//
//        mainPageViewModel.arrName.observe(this, androidx.lifecycle.Observer {
//
//
//
//            mainPageViewModel.retrieveCache()
//            displayAdapter()
//
//
////            displayAdapter()
//        })
////        var getCache=mainPageViewModel.retrieveCache()
////        displayAdapter()
//
//
//
//    }

    private fun displayAdapter(){
        val docView: GridView = findViewById<GridView>(R.id.gridView)
        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
            .get(MainPageViewModel::class.java)
//            val arrayList=ArrayList<ModalFormMain>()\

//        val adapter = CustomAdapter(
//            (mainPageViewModel.modalListLive.value as ArrayList<ModalFormMain>?
//                ?:emptyList()) as ArrayList<ModalFormMain>,this,)
//       docView.adapter = adapter

        mainPageViewModel.modalListLive.observe(this, androidx.lifecycle.Observer {




            val customAdapter= CustomAdapter(it as ArrayList<ModalFormMain>, this)
            customAdapter.notifyDataSetChanged()
            docView.adapter=customAdapter
            customAdapter.notifyDataSetChanged()

        })




    }


    private fun getAdapter(){

        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
            .get(MainPageViewModel::class.java)

//        mainPageViewModel.retrieveCache()
        val adapter=CustomAdapter(mainPageViewModel.modalList,this)
        val gridView=findViewById<GridView>(R.id.gridView)
        adapter.notifyDataSetChanged()
        gridView.adapter=adapter


    }



    private fun sorting(){

        val btnSort=findViewById<ToggleButton>(R.id.sortByName)

        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
            .get(MainPageViewModel::class.java)

        btnSort.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (mainPageViewModel.modalList.isNotEmpty()) {


                if (isChecked) {
                    mainPageViewModel.sortByName()
                } else {

                   mainPageViewModel.sortByPro()

                }



            }
        }


        displayAdapter()
        doctorSearching()

    }



    private fun doctorDel(dtname: String) {





            mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
            val docView = findViewById<GridView>(R.id.gridView)




            docView.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->


                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Delete Doctor Alert")
                    builder.setMessage("Are you sure to delete Doctor?")


                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            applicationContext,
                            android.R.string.yes, Toast.LENGTH_SHORT
                        ).show()


                        mainPageViewModel=ViewModelProvider(this, MainPageViewModelFactory(arraylistEmpty,i))
                            .get(MainPageViewModel::class.java)

                        mainPageViewModel.deletionDoctor(dtname,i)
                        Toast.makeText(this, "Dr $dtname ,$i",Toast.LENGTH_LONG).show()
                        val bo="Dr $dtname"==mainPageViewModel.modalList.get(i).docName
//                        Toast.makeText(this, "doctor deletion is a success",Toast.LENGTH_LONG).show()

//                        val docRef = mFirebaseDatabaseInstance!!.collection("doctor")
//                            .document("${modalList.get(i).docName}")
//
//                        val deleteDoc=modalList.get(i).docName
                     displayAdapter()


                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            applicationContext,
                            android.R.string.no, Toast.LENGTH_SHORT
                        ).show()
                    }




                    builder.show()
                    true
                }




        }


    private fun callViewModel(docView: GridView, userEmail: String) {



        if (userEmail.contains("@student.tar")) {
            var tempListViewClickedValue=""
            val dtname=getGoogleName()



            docView.setOnItemClickListener { adapterView, view, i, l ->
//                tempListViewClickedValue = modalList.get(i).docName.toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()





//                if(tempListViewClickedValue!="Dr $dtname"){
//                    Toast.makeText(this,"this is not your profile",Toast.LENGTH_LONG).show()
////                    val intent = Intent(this, CalendarTimePicker::class.java)
////                    intent.putExtra("DoctorName", tempListViewClickedValue)
////                    startActivity(intent)
//                }



//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()


            }



            doctorDel(dtname)
            displayAdapter()



        } else {

            docView.setOnItemClickListener { adapterView, view, i, l ->

                var name = ""
                var docPro = ""
//                val time = modalList.get(i).time.toString()
//            val time = arraylistTime[i].toString()


                mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
                    .get(MainPageViewModel::class.java)
               if( mainPageViewModel.modalListSearch.isNotEmpty()){

                    name=mainPageViewModel.modalListSearch.get(i).docName.toString()
                   docPro=mainPageViewModel.modalListSearch.get(i).pro.toString()

                } else{


                    name=mainPageViewModel.modalList.get(i).docName.toString()
                   docPro=mainPageViewModel.modalList.get(i).pro.toString()
                }









//                    writeUser(time,name,user)
//                    val intent = Intent(this, AppointmentSelect::class.java)
                val intent = Intent(this, CalendarTimePicker::class.java)
                intent.putExtra("DoctorName", name)
                intent.putExtra("DoctorPro", docPro)
                startActivity(intent)


            }


        }

    }


    private fun paramForSearching(){
        val docView: GridView = findViewById<GridView>(R.id.gridView)

//        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
//            .get(MainPageViewModel::class.java)
//        val arrayList=ArrayList<ModalFormMain>()
        mainPageViewModel.modalListLiveSearch.observe(this, androidx.lifecycle.Observer {


            val customAdapter= CustomAdapter(it as ArrayList<ModalFormMain>, this)
//            showMsg(it.get(0).toString())
            customAdapter.notifyDataSetChanged()
            docView.adapter=customAdapter
            customAdapter.notifyDataSetChanged()

        })

    }


//    private fun arrForSearching(arr:ArrayList<String>){
//        val docView: GridView = findViewById<GridView>(R.id.gridView)
//
//        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arr,-1))
//            .get(MainPageViewModel::class.java)
//
//        mainPageViewModel.dataChanged(arr,"")
//
//        mainPageViewModel.modalListLiveSearch.observe(this, androidx.lifecycle.Observer {
//
//
//            val customAdapter= CustomAdapter(it as ArrayList<ModalFormMain>, this)
//            customAdapter.notifyDataSetChanged()
//            docView.adapter=customAdapter
//            customAdapter.notifyDataSetChanged()
//
//        })

//        val arrayList=ArrayList<ModalFormMain>()

//        mainPageViewModel.dataChanged(arr,"")

//        mainPageViewModel.modalListLiveSearch.observe(this, androidx.lifecycle.Observer {
//
//
//            val customAdapter= CustomAdapter(it as ArrayList<ModalFormMain>, this)
//            customAdapter.notifyDataSetChanged()
//            docView.adapter=customAdapter
//            customAdapter.notifyDataSetChanged()
//
//        })

//    }





    private fun doctorSearching(){



            val docView: GridView = findViewById<GridView>(R.id.gridView)



//            docView.adapter = customAdapter

//            paramForSearching()
//        searchView()

            val temp = ArrayList<String>()
            val tempHos = ArrayList<String>()
            val tempName = ArrayList<String>()
            val searchView = findViewById<SearchView>(R.id.searchDoc)
            searchView.queryHint = " Professional/Hospital/Doc Name"

            mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
                .get(MainPageViewModel::class.java)
//
//            mainPageViewModel.searchQueryLiveData.observe(this, androidx.lifecycle.Observer {
//
//                searchView.setQuery(it,false)
//
//
//            })

//        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocomplete_text_view)
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistPro)
//        autoCompleteTextView.setAdapter(adapter)
//        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
//                        val selectedItem = parent.getItemAtPosition(position) as String
//
//
//
//            //            if(autoCompleteTextView.text.isEmpty() || autoCompleteTextView.text.isBlank()){
//            //
//            //
//            //                searchView.setQuery(null,true)
//            //            }
//            //            else{
//
//                        searchView.setQuery(selectedItem, true)
//            //            }
//
//
//
//
//
//        }











            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    temp.clear()
                    tempHos.clear()
                    tempName.clear()
                    var searchQuery=""
//                val first= p0?.let { p0.toString().indexOf(it[0]) }
                    if (p0 != null) {


                        searchQuery.replace("",p0,true)
                        mainPageViewModel.getSearchQuery(p0)
//                        showMsg(p0)

                        mainPageViewModel.searchDoctor(p0)
                        paramForSearching()
//                        for (i in arraylistPro.indices) {
//                            if (arraylistPro[i].contains(p0, true)) {
//                                temp.add(arraylistName[i])
//                                searchQuery.replace("",p0)
//                                mainPageViewModel.searchQueryLiveData.value=searchQuery
//
//                            }
//
//                        }
//
//
//                        for (i in arraylistHospital.indices) {
//                            if (arraylistHospital[i].contains(p0, true)) {
//
//                                tempHos.add(arraylistName[i])
//                                searchQuery.replace("", p0)
//                                mainPageViewModel.searchQueryLiveData.value=searchQuery
//                            }
//                        }
//                        for (i in arraylistName.indices) {
//                            if (arraylistName[i].contains(p0, true)) {
//
//                                tempName.add(arraylistName[i])
//                                searchQuery.replace("", p0)
//                                mainPageViewModel.searchQueryLiveData.value=searchQuery
//                            }
//                        }
//
//                        if (temp.isNotEmpty()) {
//
//                           arrForSearching(temp)
////                            paramForSearching()
//                        }
//                        else if(tempHos.isNotEmpty()) {
//
//                            arrForSearching(tempHos)
////                            paramForSearching()
//
//
//                        }
//                        else if(tempName.isNotEmpty()){
//
//
//                            arrForSearching(tempName)
////                            paramForSearching()
//                        }

//                    callForSearching(tempHos,searchQuery,docView)



                    } else {
//                    showMsg(docView)




                        getAdapter()
                        paramForSearching()
                        displayAdapter()
//                        paramForSearching()

                    }




                    return false

                }

                override fun onQueryTextChange(p0: String?): Boolean {

                    temp.clear()

                    var searchQuery=""
                    if (p0 != null) {
                        searchQuery.replace("",p0,true)
                        mainPageViewModel.getSearchQuery(p0)
//                        showMsg(p0)

                        mainPageViewModel.searchDoctor(p0)
                        paramForSearching()
                    } else {
                        getAdapter()
                        paramForSearching()
//                        paramForSearching()
                        displayAdapter()
                    }
//                if (p0 != null) {
//                    showSuggestion(p0,adapter)
//                }






                    return true
                }


            })


        }


//    private fun searchDoctor(){
//
//        val temp = ArrayList<String>()
//        val tempHos = ArrayList<String>()
//        val tempName = ArrayList<String>()
//        temp.clear()
//        tempHos.clear()
//        tempName.clear()
//        mainPageViewModel=ViewModelProvider(this,MainPageViewModelFactory(arraylistEmpty,-1))
//            .get(MainPageViewModel::class.java)
//            mainPageViewModel.searchQueryLiveData.observe(this, androidx.lifecycle.Observer {p0->
//
//
//
//            if (p0!= null) {
//                for (i in arraylistPro.indices) {
//                    if (arraylistPro[i].contains(p0, true)) {
//                        temp.add(arraylistName[i])
//
//                    }
//
//                }
//
//
//                for (i in arraylistHospital.indices) {
//                    if (arraylistHospital[i].contains(p0, true)) {
//
//                        tempHos.add(arraylistName[i])
//
//                    }
//                }
//                for (i in arraylistName.indices) {
//                    if (arraylistName[i].contains(p0, true)) {
//
//                        tempName.add(arraylistName[i])
//
//                    }
//                }
//
//                if (temp.isNotEmpty()) {
//
//                    dataChanged(temp,"")
////                            paramForSearching()
//                }
//                else if(tempHos.isNotEmpty()) {
//
//                    dataChanged(tempHos,"")
////                            paramForSearching()
//
//
//                }
//                else if(tempName.isNotEmpty()){
//
//
//                    dataChanged(tempName,"")
////                            paramForSearching()
//                }
//
////                    callForSearching(tempHos,searchQuery,docView)
//
//
//
//            }
//
//
//
//
//            })
//
//
//
//    }








    private fun refreshMain(){

        val refresh = Intent(this, MainPage::class.java)
        startActivity(refresh) //Start the same Activity

        finish()
    }

    private fun mainAc() {

        val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
        startActivity(intent)
        Toast.makeText(this, "Enter  ", Toast.LENGTH_SHORT).show()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getDataDoc() {

        val docView = findViewById<GridView>(R.id.gridView)
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
                    refreshMain()

                    retrieveCache()

                }






        }



            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }




        var user = " "
        var userEmail = " "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                userEmail = userGoogle.email.toString()
                naviImg(userGoogle!!.photoUrl,user)
            } else {

                val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            }

        }






        if (userEmail.contains("@student.tar")) {
            var tempListViewClickedValue=""
            var dtname=getGoogleName()



            docView.setOnItemClickListener { adapterView, view, i, l ->
                tempListViewClickedValue = modalList.get(i).docName.toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()





                if(tempListViewClickedValue=="Dr $dtname"){

//                    val intent = Intent(this, CalendarTimePicker::class.java)
//                    intent.putExtra("DoctorName", tempListViewClickedValue)
//                    startActivity(intent)
                }

                else{

                    Toast.makeText(this,"this is not your profile",Toast.LENGTH_LONG).show()

                }

//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()


            }





            deleteDoc(dtname)

        } else {

            docView.setOnItemClickListener { adapterView, view, i, l ->

                var name = " "
//                val time = modalList.get(i).time.toString()
//            val time = arraylistTime[i].toString()


                name = if(modalListSearch.isNotEmpty()){

                    modalListSearch.get(i).docName.toString()

                } else{


                    modalList.get(i).docName.toString()
                }









//                    writeUser(time,name,user)
//                    val intent = Intent(this, AppointmentSelect::class.java)
                val intent = Intent(this, CalendarTimePicker::class.java)
                intent.putExtra("DoctorName", name)
                startActivity(intent)


            }


        }






    }


     private fun getImg() {
//        arraylistName.ensureCapacity(arraylistData.size)
        val arrBitMap = ArrayList<Bitmap>()
//        count=0
        val docView = findViewById<GridView>(R.id.gridView)
        var detect = 0
        val extractName = ArrayList<String>()
         val cache= MyCache()
         val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
         var times=0

            count = arraylistName.size
//             Toast.makeText(this, "${arraylistName}",Toast.LENGTH_SHORT).show()
            for (i in arraylistName.indices) {

                val fireb = Firebase.storage.reference.child("Img/${arraylistName.get(i)}.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
                val localfile = File.createTempFile("tempImage", "jpg")


                swipe.isRefreshing=true

                fireb.getFile(localfile).addOnCompleteListener {

                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)





                    cache.saveBitmapToCahche(arraylistName[i],bitmap)
                    arrBitMap.add(bitmap)






                    times++


                }.addOnFailureListener {



                    Toast.makeText(this, "${arraylistName }failed to retrieve iamge$extractName", Toast.LENGTH_SHORT).show()
                }

//                dt++
            }







//        }

         val customAdapter =CustomAdapter(modalList, this)



//        docView.adapter = customAdapter

        searchDoc(customAdapter)

         swipe.isRefreshing=false
//        if(call!=0){

    }

    private fun retrieveCache(){
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipe.isRefreshing=true
        val docView=findViewById<GridView>(R.id.gridView)
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





        sortByPro()
     val btnSort=findViewById<ToggleButton>(R.id.sortByName)

     btnSort.setOnCheckedChangeListener { compoundButton, isChecked ->

         if(modalList.isNotEmpty()) {


             if(isChecked) {
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
             }

             else{

                 sortByPro()

             }



         }


        Toast.makeText(this,"${modalList.get(1).docName} ",Toast.LENGTH_SHORT).show()


     }





        val customAdapter =CustomAdapter(modalList, this)

        customAdapter.notifyDataSetChanged()


        if(modalList.size==arraylistName.size){

            docView.adapter = customAdapter
            swipe.isRefreshing=false

        }



        searchDoc(customAdapter)


    }





    private fun retrieveImg(extractName: ArrayList<String>) {
        val arrBitMap = ArrayList<Bitmap>()
//        count=0
        val docView = findViewById<GridView>(R.id.gridView)
        var detect = 0

        if (modalList.size > arraylistData.size) {



        } else {
//            var i=0
            count = arraylistName.size
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


                        val img = findViewById<ImageView>(R.id.imageView)

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
                        Toast.makeText(this, "failed to retrieve iamge", Toast.LENGTH_SHORT).show()
                    }

//                dt++
                }
            }
        }


         val customAdapter = CustomAdapter(modalList, this)



//        docView.adapter = customAdapter

        searchDoc(customAdapter)
    }


    private fun setDoctor() {
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


                Toast.makeText(this, "Successfully added ", Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

                Toast.makeText(this, "Failed to add ", Toast.LENGTH_SHORT).show()
            }


    }


    class CustomAdapter(var itemModel: ArrayList<ModalFormMain>, var context: Context) :
        BaseAdapter(), Filterable{
        var searchString=""
        var pos=0

//        lateinit var viewHolder:ViewHolder
        override fun getCount(): Int {

            return itemModel.size
        }

        override fun getItem(p0: Int): Any {
            return itemModel[p0]
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }

        var arrFilter=ArrayList<String>()
        var queryText=""



        var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

            var view = view
            if (view == null) {


                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false)
            }
//            viewHolder= ViewHolder(view)

            val tvImageName = view?.findViewById<TextView>(R.id.imageName)
            val tvTime = view?.findViewById<TextView>(R.id.docPro)
            val tvPro = view?.findViewById<TextView>(R.id.docTime)
            val hospital= view?.findViewById<TextView>(R.id.docHospital)

            val imageView = view!!.findViewById<ImageView>(R.id.imageView)
            var CheckName = itemModel[position].docName
            var name = itemModel[position].docName
            if (CheckName != null) {
                if (CheckName.length > 10) {
                    var index = CheckName.indexOf(" ", 5, true)
                    CheckName = CheckName.substring(0, index) + "\n" + CheckName.substring(
                        index,
                        CheckName.length
                    )
                    tvImageName?.text = CheckName

                } else {
                    tvImageName?.text = CheckName

                }
            }

//            tvImageName?.text=itemModel[position].docName

                hospital?.text=itemModel[position].hospital


            tvTime?.text = itemModel[position].time
//            val str= sendResult()
//            var i= sendPosition()
            val pro=itemModel[position].pro
            tvPro?.text = pro

            if(pro.toString().contains(searchString,true)&&searchString.isNotBlank()
                &&searchString.isNotEmpty()){

                val str=setColorText(searchString,position)

                tvPro?.text = str

            }



            val doc=itemModel[position].docName.toString()
            val cache= MyCache()
            val bit: Bitmap? =cache.retrieveBitmapFromCache(doc)
            itemModel[position].image=bit
            with(context)
                .load(bit)
                .into(imageView)


            return view!!
        }


         var result=SpannableString("s")
            var i=-1
            var truth=false

            private lateinit var proView:TextView
            @SuppressLint("ResourceType")
           fun  setColorText(searchTarget: String, position: Int): SpannableString {





                val magenta = ForegroundColorSpan(Color.MAGENTA)
//                val spannableString = SpannableString(str)
                val pro=itemModel[position].pro.toString()
                val spannableStringTgt = SpannableString(pro)
//                val str=""





                    val chr=searchTarget[0]
                    val pos= pro.indexOf(chr,0,true)
                    val end =pos+queryText.length

//                    val positon2 = searchTarget.indexOf(str[1])


                    spannableStringTgt.setSpan(
                        magenta,
                        0,
                        pos+1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

//                val tvPro = viewHolder.findViewById<TextView>(R.id.docTime)

//                viewHolder.txtDoc.text=spannableStringTgt




//                docPro.text = arraylistPro[position]
                result= spannableStringTgt

                truth=true

                return spannableStringTgt
            }


            fun sendResult(): SpannableString {

                return result
            }

            fun sendPosition(): Int {

                return i
            }






        private val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                queryText= p0.toString()
                searchString=p0.toString()

                val arrPro=ArrayList<String>()
                for(modal in itemModel){
                    val pro=modal.pro.toString()
                    if(pro.contains(searchString,true)){

                        arrPro.add(pro)

                    }

                }


                val filterResult=FilterResults()
                filterResult.count=arrPro.size
                filterResult.values=arrPro
                return filterResult



            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                val proFilter=p1.toString()
                if(p1!=null&&p1.count>0){



                        arrFilter.add(p1.values.toString())

                        notifyDataSetChanged()


                }

            }

        }

        override fun getFilter(): Filter {

            return  filter
        }


        fun getSearch(s:String) {


            searchString=s

        }

        fun getSpan(): SpannableString{


            return result
        }

    }



    private fun refresh() {
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

//            getDataDoc()
            mainPageViewModel.getDataDoc()

            swipe.isRefreshing = false

        }



    }

    private fun writeUser(appointTime: String, docName: String, loginUser: String) {

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

                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }


//        userNum+=1


    }

    private fun deleteDoc(dtname: String) {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docView = findViewById<GridView>(R.id.gridView)




        docView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->


                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Doctor Alert")
                builder.setMessage("Are you sure to delete Doctor?")


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()


                    val docRef = mFirebaseDatabaseInstance!!.collection("doctor")
                        .document("${modalList.get(i).docName}")

                    val deleteDoc=modalList.get(i).docName

                    if(deleteDoc=="Dr $dtname") {
                                docRef
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "${deleteDoc}\" successfully deleted!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Error deleting document",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                val fireb =
                                    Firebase.storage.reference.child("Img/${modalList.get(i).docName}.jpg")
                                fireb.delete().addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Successfully delete images${deleteDoc}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {

                                }

                                modalList.removeAt(i)



//                                val arr = CustomAdapter(modalList, this)
//                                arr.notifyDataSetChanged()
//                                docView.adapter = arr

                    }

                    else{


                        Toast.makeText(this,"This is Not ur profile to delete",Toast.LENGTH_LONG).show()
                    }



                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT
                    ).show()
                }




                builder.show()
                true
            }




    }


    private fun searchDoc(customAdapter: CustomAdapter) {

        val docView: GridView = findViewById<GridView>(R.id.gridView)
        docView.adapter = customAdapter

//        searchView()

        val temp = ArrayList<String>()
        val tempHos = ArrayList<String>()
        val tempName = ArrayList<String>()
        val searchView = findViewById<SearchView>(R.id.searchDoc)
        searchView.queryHint = " Professional/Hospital/Doc Name"

//        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocomplete_text_view)
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistPro)
//        autoCompleteTextView.setAdapter(adapter)
//        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
//                        val selectedItem = parent.getItemAtPosition(position) as String
//
//
//
//            //            if(autoCompleteTextView.text.isEmpty() || autoCompleteTextView.text.isBlank()){
//            //
//            //
//            //                searchView.setQuery(null,true)
//            //            }
//            //            else{
//
//                        searchView.setQuery(selectedItem, true)
//            //            }
//
//
//
//
//
//        }








        val custom= CustomAdapter(modalList, this)

        val proAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistPro)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                temp.clear()
                tempHos.clear()
                tempName.clear()
                var searchQuery=""
//                val first= p0?.let { p0.toString().indexOf(it[0]) }
                if (p0 != null) {
                    for (i in arraylistPro.indices) {
                        if (arraylistPro[i].contains(p0, true)) {
                            temp.add(arraylistName[i])
                            searchQuery.replace("",p0)
                            custom.filter.filter(p0)
                            customAdapter.getSearch(p0)

                        }

                    }


                    for (i in arraylistHospital.indices) {
                        if (arraylistHospital[i].contains(p0, true)) {

                            tempHos.add(arraylistName[i])
                            searchQuery.replace("", p0)
                        }
                    }
                    for (i in arraylistName.indices) {
                        if (arraylistName[i].contains(p0, true)) {

                            tempName.add(arraylistName[i])
                            searchQuery.replace("", p0)
                        }
                    }

                    if (temp.isNotEmpty()) {
                        callForSearching(temp,searchQuery,docView)
                    }
                    else if(tempHos.isNotEmpty()) {



                        callForSearching(tempHos,searchQuery,docView)

                    }
                    else if(tempName.isNotEmpty()){

                        callForSearching(tempName,searchQuery,docView)

                    }

//                    callForSearching(tempHos,searchQuery,docView)



                } else {
//                    showMsg(docView)
                    docView.adapter = customAdapter

                }




                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                        temp.clear()

                        var searchQuery=""
                        if (p0 != null) {
                            for (i in arraylistPro.indices) {
                                if (arraylistPro[i].contains(p0, true)) {
                                    temp.add(arraylistName[i])
                                    searchQuery=p0
                                    custom.filter.filter(p0)



                                }

                            }


                        } else {
                            docView.adapter = customAdapter

                        }
//                if (p0 != null) {
//                    showSuggestion(p0,adapter)
//                }






                return false
            }


        })


    }


    private fun callForSearching(temp:ArrayList<String>,searchQuery:String,docView:GridView){


        if (temp.isNotEmpty()) {
            dataChanged(temp, searchQuery)
        } else {
//            showMsg(docView)

        }


    }




    private fun showSuggestion(query: String, adapter: ArrayAdapter<String>){


        val suggestions = arraylistPro.filter { it.contains(query) }
        adapter.clear()
        adapter.addAll(suggestions)
        adapter.notifyDataSetChanged()



    }



    private fun showMsg(str:String) {

        val arr=ArrayList<String>()
        arr.add("Doctor is not found")
//        val arrayAdapter = ArrayAdapter(this,
//            android.R.layout.select_dialog_item,arr)
//
//        docView.adapter=arrayAdapter
        Toast.makeText(this, "Doctor is not found $str str", Toast.LENGTH_LONG).show()
    }


    private fun dataChanged(tempName: ArrayList<String>, searchQuery: String) {
        val docView: GridView = findViewById<GridView>(R.id.gridView)
        Toast.makeText(this,"$str",Toast.LENGTH_SHORT).show()

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

                if(searchQuery.isNotBlank()&&searchQuery.isNotEmpty()){

                    val custom=CustomAdapter(modalList, this)
                    custom.filter.filter(searchQuery)
                    custom.setColorText(searchQuery,i)
                }


//                setColorText(i)

            }
            else{
                if(j < size-1){
                    j++
                }

            }

            val customSearch = CustomAdapter(modalListSearch, this)


            customSearch.notifyDataSetChanged()
            docView.adapter = customSearch
//                dt++
        }


        val customSearch = CustomAdapter(modalListSearch, this)

        customSearch.notifyDataSetChanged()

        docView.adapter = customSearch


    }





    private fun sortByPro(){


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

    }

    private fun getGoogleName(): String {



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



    private fun showNavBar(){


        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view=findViewById<NavigationView>(R.id.nav_view)
        toggle= ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_BookAppoint -> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }




                R.id.nav_Pres -> {
                    val intent = Intent(this, PrescriptionDisplay::class.java)
                    startActivity(intent)

                }
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
                R.id.nav_viewAppoint -> {
                    val intent = Intent(this, DoctorAppointment::class.java)
                    startActivity(intent)

                }
                R.id.nav_medicineRecord -> {
                    val  intent = Intent(this, MedicineRecord::class.java)
                    startActivity(intent)

                }
                R.id.nav_OCR -> {
                    val intent = Intent(this, UserMedicine::class.java)
                    startActivity(intent)
                }





            }


            true

        }





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true


        }


        return super.onOptionsItemSelected(item)
    }




    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser




    }







}




