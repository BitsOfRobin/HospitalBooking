package com.example.hospitalbooking



import MyCache
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide.with

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class MainPage : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    private var docDetail: String? = null
    private var fragmentInput:String?=null
    //    private lateinit var binding: ActivityMainBinding
    private var modalList = ArrayList<ModalFormMain>()
    var images = intArrayOf(R.drawable.dt1, R.drawable.dt2, R.drawable.dt3)
    private var arraylistName = ArrayList<String>()
    private val arraylistData = ArrayList<String>()
    private val arraylistTime = ArrayList<String>()
    private val arraylistTime2 = ArrayList<String>()
    private val arraylistPro = ArrayList<String>()

    //    private val docView: GridView =findViewById<GridView>(R.id.gridView)
    private var count = 0

    //    private lateinit var binding:AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_page)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Appointment")

        val docView = findViewById<GridView>(R.id.gridView)

//        binding= ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        var imageId= intArrayOf(
//
//            R.drawable.doc1
//
//        )

        val fragment = FragmentWithSearching()
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragment,fragment)
//            commit()
//
//
//        }

        val drawer = findViewById<BottomNavigationView>(R.id.naviBtm)
        drawer.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home -> mainAc()


            }

            true

            Toast.makeText(this, "Enter the layout ", Toast.LENGTH_SHORT).show()


        }

//        drawer.setOnClickListener {
//
//            mainAc()
//
//        }


        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        setDoctor()
        getDataDoc()
//        getDataDoc()
//        getDataDoc()
        refresh()
//        getDataDoc()



    }

    private fun refreshMain(){

        val refresh = Intent(this, MainPage::class.java)
        startActivity(refresh) //Start the same Activity

        finish()
    }

    private fun mainAc() {
//        val fragment=FragmentWithSearching()
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.gridView,fragment)
//            commit()
//
//
//        }
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
//        modalList.clear()
//        arraylistData.clear()
//        var modalList=ArrayList<ModalFormMain>()
        val docView = findViewById<GridView>(R.id.gridView)
        val doctor = FirebaseAuth.getInstance().currentUser
        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

//        val arraylistName= ArrayList<String>()
//        val arraylistTime= ArrayList<String>()
//        val arraylistPro= ArrayList<String>()
//        val arraylistData= ArrayList<String>()

//        val docView=findViewById<RecyclerView>(R.id.Rview)
//        val docView=findViewById<GridView>(R.id.gridView)
//        val txt=findViewById<TextView>(R.id.txtV)
//        val name=findViewById<TextView>(R.id.txtName)
//        val pro=findViewById<TextView>(R.id.txtPro)
        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.get()?.addOnSuccessListener {


////                val doc=it.toString()
//            val result:StringBuffer= StringBuffer()
//            val result2:StringBuffer= StringBuffer()
//


//                for(doc in it.documents ){
////

//                    result.append(doc?.get("name")).append(" ")
//                        result2.append(doc.get("pro")).append("\n\n")
//
//                    arraylist.add(result.toString())
//                    arraylistPro.add(result2.toString())
//
//
//
//
//                    Toast.makeText(this, "Enter the firebase${it.documents.toString()} ",Toast.LENGTH_SHORT).show()
////                    Toast.makeText(this, "Enter the firebase data ${doc.toString()} ",Toast.LENGTH_SHORT).show()


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
//                var dateFormat=date.toString()
//                val list=dateFormat.split("G")
//                var dateTime=list[0]+"\nG"+list[1]
//                var dateFormat2=date2.toString()
//                val list2=dateFormat2.split("G")
//                var dateTime2=list2[0]+"\nG"+list2[1]


                if (name.contains("Dr")) {

                    arraylistName.add(name)


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
            val cache=MyCache()
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

//        docRef?.get()?.addOnSuccessListener { documents ->
//            Log.d(ContentValues.TAG, "${documents.id}=>${documents.data}")
//            arrlist.add(documents.toString())
//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, arrlist)
//            list.adapter=adapter
//            txt.text=arrlist.toString()
//        }

            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }


//        getImg()

//        getImg()

//        while(modalList.size!=arraylistName.size){
//
//            getImg()
//        }

        var user = " "
        var userEmail = " "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                userEmail = userGoogle.email.toString()

            } else {

                val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            }

        }






        if (userEmail.contains("@student.tar")) {

            docView.setOnItemClickListener { adapterView, view, i, l ->
                val tempListViewClickedValue = modalList.get(i).docName.toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
                val intent = Intent(this, CalendarTimePicker::class.java)
                intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()


            }

            deleteDoc()

        } else {

            docView.setOnItemClickListener { adapterView, view, i, l ->

                val name = modalList.get(i).docName.toString()
                val time = modalList.get(i).time.toString()
//            val time = arraylistTime[i].toString()

                if (time.contains('0')) {
//                    writeUser(time,name,user)
                    val intent = Intent(this, AppointmentSelect::class.java)
                    intent.putExtra("DoctorName", name)
                    startActivity(intent)
//                    Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, "Enter the click listener$arraylistTime ", Toast.LENGTH_SHORT).show()

                }

//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()


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
         val cache=MyCache()
         val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
         var times=0
//        extractName.clear()
//        modalList.clear()
//        Toast.makeText(this,"name=$arraylistName",Toast.LENGTH_SHORT).show()
//        if (modalList.size > arraylistData.size) {
////            for(i in arraylistData.size..modalList.size)
////            {
////                modalList.clear()
//
////            }
//
//
//        } else {
//            var i=0
            count = arraylistName.size
//             Toast.makeText(this, "${arraylistName}",Toast.LENGTH_SHORT).show()
            for (i in arraylistName.indices) {

                val fireb = Firebase.storage.reference.child("Img/${arraylistName.get(i)}.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
                val localfile = File.createTempFile("tempImage", "jpg")


                swipe.isRefreshing=true
//                swipe.setOnRefreshListener {
////            txt.setText(null)
////            arraylistDocSearch.clear()
//
//                    getDataDoc()

//           readDoc(txt)
//            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()

//
//                }

                fireb.getFile(localfile).addOnCompleteListener {
//                 bitmap=BitmapFactory.decodeFile(file.absolutePath)
//                imageArr.add(bitmap)
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)


                    val img = findViewById<ImageView>(R.id.imageView)


                    cache.saveBitmapToCahche(arraylistName[i],bitmap)
                    arrBitMap.add(bitmap)

                    var time = arraylistTime[i] + "\n" + arraylistTime2[i]
//                    if (modalList.isEmpty()) {
                        modalList.add(
                            ModalFormMain(
                                arraylistPro[i],
                                bitmap,
                                arraylistName[i],
                                time
                            )
                        )






                    times++
//                    Toast.makeText(this,"name=$times",Toast.LENGTH_SHORT).show()

//                    count=0

//                    modalList.add(docModal(arraylist[i],))

//                    Toast.makeText(this,"success to retrieve iamge $bitmap",Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {



                    Toast.makeText(this, "${arraylistName }failed to retrieve iamge$extractName", Toast.LENGTH_SHORT).show()
                }

//                dt++
            }







//        }

         val customAdapter = CustomAdapter(modalList, this)



//        docView.adapter = customAdapter

        searchDoc(customAdapter)

         swipe.isRefreshing=false
//        if(call!=0){

    }

    private fun retrieveCache(){
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipe.isRefreshing=true
        val docView=findViewById<GridView>(R.id.gridView)
        val cache=MyCache()
        for (i in arraylistName.indices) {
            val bitmap: Bitmap? =cache.retrieveBitmapFromCache(arraylistName[i])
            var time = arraylistTime[i] + "\n" + arraylistTime2[i]

                bitmap?.let {
                    ModalFormMain(
                        arraylistPro[i],
                        it,
                        arraylistName[i],
                        time
                    )
                }?.let {
                    modalList.add(
                        it
                    )
                }

//            Toast.makeText(this,"b$bitmap", Toast.LENGTH_SHORT ).show()

        }


//            val customAdapter= CustomAdapter(modalList, this)
//
//
//
//            docView.adapter = customAdapter
//
//            searchDoc(customAdapter)
//        if(modalList.isNotEmpty()) {
//            Collections.sort(modalList, object :
//
//
//                java.util.Comparator<ModalFormMain> {
//                override fun compare(p0: ModalFormMain?, p1: ModalFormMain?): Int {
//                    var num = 0
//
//                    if (p0 != null) {
//                        if (p1 != null) {
//                            num = p0.pro.toString() compareTo (p1.pro.toString())
//                        }
//                    }
//
//                    return num
//                }
//
//
//            })
//
//        }



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





    val customAdapter = CustomAdapter(modalList, this)
        customAdapter.notifyDataSetChanged()


        if(modalList.size==arraylistName.size){

            docView.adapter = customAdapter
            swipe.isRefreshing=false

        }




//        customAdapter.notifyDataSetChanged()
//    if(modalList.size==arraylistName.size){
//        docView.adapter = customAdapter
//        customAdapter.notifyDataSetChanged()
//    }


    searchDoc(customAdapter)


    }





    private fun retrieveImg(extractName: ArrayList<String>) {
        val arrBitMap = ArrayList<Bitmap>()
//        count=0
        val docView = findViewById<GridView>(R.id.gridView)
        var detect = 0
//        val extractName = ArrayList<String>()
//        extractName.clear()
//        modalList.clear()
//        Toast.makeText(this,"name=$arraylistName",Toast.LENGTH_SHORT).show()
        if (modalList.size > arraylistData.size) {
//            for(i in arraylistData.size..modalList.size)
//            {
//                modalList.clear()

//            }


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
//                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
//                val x: Int = bitmap.width
//                val y: Int = bitmap.height
//                val intArray = IntArray(x * y)
//                var imgArr=bitmap.getPixels(intArray, 0, x, 0, 0, x, y)

//                val mDrawable: Drawable = BitmapDrawable(resources, bitmap)
//                imageArr.add(bitmap)

                        val img = findViewById<ImageView>(R.id.imageView)
//                    if(name.isNotEmpty())
//                    {
//
//                        for(i in arraylistName.indices)
//                        {
//                            if(arraylistName[i].equals(name,true))
//                            {
                        var time = arraylistTime[i] + "\n" + arraylistTime2[i]
                        if (modalList.isEmpty()) {
                            modalList.add(
                                ModalFormMain(
                                    arraylistPro[i],
                                    bitmap,
                                    arraylistName[i],
                                    time
                                )
                            )

                        } else {
//                            for (m in modalList.indices) {
//
//                                extractName.add(modalList.elementAt(m).docName.toString())
//
//
//                            }
                            if (!extractName.contains(arraylistName[i])||extractName.isEmpty()) {
                                modalList.add(
                                    ModalFormMain(
                                        arraylistPro[i],
                                        bitmap,
                                        arraylistName[i],
                                        time
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
                        val cache=MyCache()
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
        BaseAdapter() {
        override fun getCount(): Int {

            return itemModel.size
        }

        override fun getItem(p0: Int): Any {
            return itemModel[p0]
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }

          var layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

            var view = view
            if (view == null) {
                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false)
            }
            val tvImageName = view?.findViewById<TextView>(R.id.imageName)
            val tvTime = view?.findViewById<TextView>(R.id.docPro)
            val tvPro = view?.findViewById<TextView>(R.id.docTime)

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


            tvTime?.text = itemModel[position].time
            tvPro?.text = itemModel[position].pro


//            itemModel[position].image?.let { imageView?.setImageBitmap(it) }

//            with(context)
//                .load(itemModel[position].image)
//                .into(imageView)
            val doc=itemModel[position].docName.toString()
            val cache=MyCache()
            val bit: Bitmap? =cache.retrieveBitmapFromCache(doc)
            itemModel[position].image=bit
            with(context)
                .load(bit)
                .into(imageView)

//            val fire= Firebase.storage.reference.child("Img/$name.jpg")
//                    Glide.with(context)
//                        .load(fire)
//                        .into(imageView)

//            if(count!=itemModel.size)
//            {
////                try{

////                    Toast.makeText(context,"image saved",Toast.LENGTH_SHORT).show()
//
//
//
//
//
//
//          fire.downloadUrl
//                .addOnSuccessListener(OnSuccessListener<Any> { uri -> // Got the download URL for 'users/me/profile.png'
//                    // Pass it to Picasso to download, show in ImageView and caching
//                    Picasso.with(context).load(uri.toString()).into(imageView)
//
//                    Toast.makeText(context,"image saved",Toast.LENGTH_SHORT).show()
//                }).addOnFailureListener(OnFailureListener {
//                  Toast.makeText(context,"image failed to save ",Toast.LENGTH_SHORT).show()
//                })
//

//                catch (e:NullPointerException){
//                    Toast.makeText(context,"image not found",Toast.LENGTH_SHORT).show()
//                }


//            }

//            else{
//                itemModel[position].image?.let { imageView?.setImageBitmap(it) }
//                Toast.makeText(context,"fully retrieved",Toast.LENGTH_SHORT).show()
//
//            }
//


            return view!!
        }


    }


    private fun refresh() {
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {
//            txt.setText(null)
//            arraylistDocSearch.clear()

            getDataDoc()

//           readDoc(txt)
//            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing = false

        }


//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }

    private fun writeUser(appointTime: String, docName: String, loginUser: String) {

//        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val arraylist = java.util.ArrayList<String>()
        val arraylistPro = java.util.ArrayList<String>()
        //        val docView=findViewById<RecyclerView>(R.id.Rview)
//        val docView=findViewById<ListView>(R.id.Rview)
        //        val txt=findViewById<TextView>(R.id.txtV)
        //        val name=findViewById<TextView>(R.id.txtName)
        //        val pro=findViewById<TextView>(R.id.txtPro)

//        var loginUser=" "
//        val userGoogle = Firebase.auth.currentUser
//        userGoogle.let {
//            // Name, email address, and profile photo Url
////                    val name = user.displayName
//            if (userGoogle != null) {
//                loginUser = userGoogle.displayName.toString()
//            }
//
//            else{
//
//                loginUser=" NOne"
//            }
////                    val photoUrl = user.photoUrl
////
////                    // Check if user's email is verified
////                    val emailVerified = user.isEmailVerified
////
////                    // The user's ID, unique to the Firebase project. Do NOT use this value to
////                    // authenticate with your backend server, if you have one. Use
////                    // FirebaseUser.getToken() instead.
////                    val uid = user.uid
//        }
//        val loginUser=readUser()
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

    private fun deleteDoc() {

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

// Remove the 'capital' field from the document
                    val updates = hashMapOf<String, Any>(
                        "name" to FieldValue.delete(),
                        "pro" to FieldValue.delete(),
                        "time" to FieldValue.delete()
                    )

                    docRef.update(updates).addOnCompleteListener {

                        Toast.makeText(this, "Success delete the doctor ", Toast.LENGTH_SHORT)
                            .show()

                    }

                    docRef.collection("userAppointment").document("${modalList.get(i).docName}")
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "${modalList.get(i).docName}\" successfully deleted!",
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

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
                    val fireb = Firebase.storage.reference.child("Img/${modalList.get(i).docName}.jpg")
                    fireb.delete().addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Successfully delete images${modalList.get(i).docName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {

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

//        { adapterView, view, i, l ->


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


//            val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDel.elementAt(i)}")
//
//// Remove the 'capital' field from the document
//            val updates = hashMapOf<String, Any>(
//                "user" to FieldValue.delete(),
//                "doctorAppoint" to FieldValue.delete(),
//                "docName" to FieldValue.delete()
//            )
//
//            docRef.update(updates).addOnCompleteListener {
//
//                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
//
//            docRef.collection("userAppointment").document("${arrayDel.elementAt(i)}")
//                .delete()
//                .addOnSuccessListener {  Toast.makeText( this,"${arrayDel.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
//                .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }
//
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


    }


    private fun searchDoc(customAdapter: CustomAdapter) {

        val docView: GridView = findViewById<GridView>(R.id.gridView)
        docView.adapter = customAdapter

//        searchView()
        val temp = ArrayList<String>()
        val searchView = findViewById<SearchView>(R.id.searchDoc)
        searchView.queryHint = "search Doctor Professional"

        val proAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylistPro)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                temp.clear()

                if (p0 != null) {
                    for (i in arraylistPro.indices) {
                        if (arraylistPro[i].contains(p0, true)) {
                            temp.add(arraylistName[i])

                        }

                    }
                    if (temp.isNotEmpty()) {
                        dataChanged(temp)
                    } else {
                        showMsg()

                    }


                } else {
                    docView.adapter = customAdapter

                }




                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                        temp.clear()

                        if (p0 != null) {
                            for (i in arraylistPro.indices) {
                                if (arraylistPro[i].contains(p0, true)) {
                                    temp.add(arraylistName[i])

                                }

                            }
                            if (temp.isNotEmpty()) {
                                dataChanged(temp)
                            } else {
                                showMsg()

                            }


                        } else {
                            docView.adapter = customAdapter

                        }

                return false
            }


        })


    }


//   private fun matchSearch(temp: ArrayList<String>, searchView: SearchView)
//   {
//
//       val adapter=ArrayAdapter(this, android.R.layout.simple_list_item_1,temp)
//       searchView.adapter
//
//   }
//    private fun suggestion()
////    {
////
////        val searchView = findViewById<SearchView>(R.id.searchDoc)
////
////    searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
////        override fun onSuggestionSelect(position: Int): Boolean {
////            return true
////        }
////
////        override fun onSuggestionClick(position: Int): Boolean {
////            val cursor: Cursor = searchView.suggestionsAdapter.cursor
////            cursor.moveToPosition(position)
////            var suggestion=""
////            for (i in arraylistPro.indices) {
////                if (arraylistPro[i].equals(cursor.getString(position), true)) {
////                    suggestion=arraylistPro[i].toString()
////                }
////
////            }
////
//////                        val suggestion: String =
//////                            cursor.getString(2) //2 is the index of col containing suggestion name.
////            searchView.setQuery(suggestion, true) //setting suggestion
////            return true
////        }
////    })
////    }
//

    private fun showMsg() {


        Toast.makeText(this, "Doctor is not found", Toast.LENGTH_SHORT).show()
    }


    private fun dataChanged(tempName: ArrayList<String>) {
        val docView: GridView = findViewById<GridView>(R.id.gridView)

        val modalListSearch = ArrayList<ModalFormMain>()
        modalListSearch.clear()
        var j = 0
//        Toast.makeText(this,"$count",Toast.LENGTH_SHORT).show()
//             Toast.makeText(this, "${arraylistName}",Toast.LENGTH_SHORT).show()
        for (i in arraylistName.indices) {
            if (arraylistName[i] == tempName[j]) {
//                val fireb = Firebase.storage.reference.child("Img/${arraylistName.get(i)}.jpg")
//
//                val localfile = File.createTempFile("tempImage", "jpg")
//                var bitmap: Bitmap
//                fireb.getFile(localfile).addOnCompleteListener{
//
//                    bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    val cache=MyCache()
                    val bitmap: Bitmap? =cache.retrieveBitmapFromCache(tempName[j])
                    val time = arraylistTime[i] + "\n" + arraylistTime2[i]
                bitmap?.let {
                    ModalFormMain(
                        arraylistPro[i],
                        it,
                        arraylistName[i],
                        time
                    )
                }?.let {
                    modalListSearch.add(
                        it
                    )
                }


//                }.addOnFailureListener {
//
//                    Toast.makeText(this, "failed to retrieve iamge", Toast.LENGTH_SHORT).show()
//                }

                if (j < tempName.size - 1) {
                    j++

                }



            }

            val customSearch = CustomAdapter(modalListSearch, this)



            docView.adapter = customSearch
//                dt++
        }


        val customSearch = CustomAdapter(modalListSearch, this)



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





}




