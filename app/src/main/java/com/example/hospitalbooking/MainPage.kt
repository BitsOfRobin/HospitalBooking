package com.example.hospitalbooking



import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class MainPage : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var docDetail:String?=null
//    private lateinit var binding: ActivityMainBinding
    private var modalList=ArrayList<Modal>()
    var images= intArrayOf(R.drawable.dt1,R.drawable.dt2,R.drawable.dt3)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
//        binding= ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        var imageId= intArrayOf(
//
//            R.drawable.doc1
//
//        )


        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
//        setDoctor()
        getDataDoc()
        refresh()

    }
    private fun getDataDoc()
    {
        modalList.clear()
        val doctor= FirebaseAuth.getInstance().currentUser
        docDetail=doctor?.uid
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
        Toast.makeText(this,"Enter getData", Toast.LENGTH_SHORT).show()
        val arraylistName= ArrayList<String>()
        val arraylistTime= ArrayList<String>()
        val arraylistPro= ArrayList<String>()
        val arraylist= ArrayList<String>()
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView=findViewById<GridView>(R.id.gridView)
//        val txt=findViewById<TextView>(R.id.txtV)
//        val name=findViewById<TextView>(R.id.txtName)
//        val pro=findViewById<TextView>(R.id.txtPro)
        val docRef=mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.get()?.addOnSuccessListener { querySnapshot ->


////                val doc=it.toString()
//            val result:StringBuffer= StringBuffer()
//            val result2:StringBuffer= StringBuffer()
//

            var docName=querySnapshot.documents
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
            for (document in querySnapshot) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                var time=document.get("Time").toString()
//                var time2 = document.get("Time2") as com.google.firebase.Timestamp
//                val date2 = time2.toDate()
                val date = time
                arraylistTime.add(date.toString())
                var name=document.get("name").toString()
                var pro=document.get("pro").toString()
//                var dateFormat=date.toString()
//                val list=dateFormat.split("G")
//                var dateTime=list[0]+"\nG"+list[1]
//                var dateFormat2=date2.toString()
//                val list2=dateFormat2.split("G")
//                var dateTime2=list2[0]+"\nG"+list2[1]
                arraylistName.add(name)
                arraylistPro.add(pro)
                arraylistTime.add(date)
//                arraylist.add("Name: $name \nProfessional:\n $pro \nAvailable Date:\n$date \n")

//                Toast.makeText(this, "Enter the first read ${arraylistTime.toString()} ", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Enter the firebase id ${document.id.toString()} ", Toast.LENGTH_SHORT).show()
            }


//                Toast.makeText(this, "Enter the firebase${docName.toString()} ",Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Enter the firebase data ${ .toString()} ",Toast.LENGTH_SHORT).show()
//
//
//                Toast.makeText(this, "Enter the firebase${arraylist[0].toString()} ",Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Enter the firebase data ${arraylistPro[0].toString()} ",Toast.LENGTH_SHORT).show()







//
//                val docArr=ArrayList<doctor>()
//
//
//                for(i in arraylist.indices){
//
//                    val doctor=doctor(arraylist[i],arraylistPro[i], imageId[i])
//                    docArr.add(doctor)
//
//
////
//                    Toast.makeText(this, "Enter the firebase${arraylist[i].toString()} ",Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, "Enter the firebase data ${arraylistPro[i].toString()} ",Toast.LENGTH_SHORT).show()
////                    name.setText(arraylist[i])
////                    pro.setText(arraylistPro[i])
////                    docView.addView(name)
////                    docView.addView(pro)
//                }
//                val arrAdapter =RecycleViewClass(docArr)
//                val arrAdapter =RecycleViewClass(docArr)
//
//
//
//                docView.layoutManager=LinearLayoutManager(this)
//                docView.adapter=arrAdapter
//
//                val doc= listOf(arraylist)
            var imageArr=ArrayList<Bitmap>()
//            var images= intArrayOf()
//            var dt=1
            var arrBitMap=ArrayList<Bitmap>()
            val file = File.createTempFile("img","jpg")
            for(i in arraylist.indices)
            {

                val fireb= Firebase.storage.reference.child("Img/dt$i.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
                val localfile= File.createTempFile("tempImage","jpg")
                var bitmap:Bitmap
                fireb.getFile(localfile).addOnSuccessListener {
//                 bitmap=BitmapFactory.decodeFile(file.absolutePath)
//                imageArr.add(bitmap)
                    bitmap=BitmapFactory.decodeFile(localfile.absolutePath)
//                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
//                val x: Int = bitmap.width
//                val y: Int = bitmap.height
//                val intArray = IntArray(x * y)
//                var imgArr=bitmap.getPixels(intArray, 0, x, 0, 0, x, y)

//                val mDrawable: Drawable = BitmapDrawable(resources, bitmap)
//                imageArr.add(bitmap)
                    val img=findViewById<ImageView>(R.id.imageView)
//                    arrBitMap.add(bitmap)
//                    modalList.add(docModal(arraylist[i],))

                    Toast.makeText(this,"success to retrieve iamge",Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{

                    Toast.makeText(this,"failed to retrieve iamge",Toast.LENGTH_SHORT).show()
                }

//                dt++
            }


//            var images= intArrayOf()
//            for( i in imageArr)
//            {
//                images= intArrayOf()
//
//
//            }

//            var images= 0
//            var images=intArrayOf(R.drawable.dt1,R.drawable.dt2)
            for(i in arraylist.indices)
            {

//                val image: MutableList<Int> = ArrayList(imageArr.size)
//                images=imageArr[i].toString().toInt()
                modalList.add(Modal(arraylistName[i],arraylistTime[i],arraylistPro[i],images[i]))
//                modalList.add(arraylist[i],images[i]))

            }
            //                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()
            var customAdapter= CustomAdapter(modalList, this)



            docView.adapter = customAdapter

            docView.setOnItemClickListener { adapterView, view, i, l ->
                val tempListViewClickedValue = arraylistName[i].toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
                val intent= Intent(this,AppointmentSelect::class.java)
                intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()



            }


            var user=" "
            val userGoogle = Firebase.auth.currentUser
            userGoogle.let {
                // Name, email address, and profile photo Url
//                    val name = user.displayName
                if (userGoogle != null) {
                    user = userGoogle.displayName.toString()

                } else {

                    val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                    startActivity(intent)
                }

            }

            if(user=="ZHONG LEAN LOW")
            {

                docView.setOnItemClickListener { adapterView, view, i, l ->
                    val tempListViewClickedValue = arraylistName[i].toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
                    val intent= Intent(this,CalendarTimePicker::class.java)
                    intent.putExtra("DoctorName", tempListViewClickedValue)
                    startActivity(intent)
//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()



                }

            }







//                docView.layoutManager=LinearLayoutManager(this)
//                docView.adapter=MyAdapter(this, docArr)
//
//                val adapter=MyAdapter(this,docArr)
//                binding.listViewDoc.adapter=adapter
////
//                binding.listView.adapter=MyAdapter(this,docArr)
//                binding.apply {
//                    listViewDoc.apply {
//                        adapter=MyAdapter(this,docArr)
//
//
//
//
//                    }
//
//
//                }
//                val adapter = ArrayAdapter(this, android.R.layout.activity_list_item, arraylist)
////                docView.adapter = adapter
//                txt.text = arraylist[0].toString()












        }

//        docRef?.get()?.addOnSuccessListener { documents ->
//            Log.d(ContentValues.TAG, "${documents.id}=>${documents.data}")
//            arrlist.add(documents.toString())
//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, arrlist)
//            list.adapter=adapter
//            txt.text=arrlist.toString()
//        }

            ?.addOnFailureListener {
                Toast.makeText(this,"Failed ", Toast.LENGTH_SHORT).show()
            }




    }


    private fun setDoctor()
    {
        Toast.makeText(this,"enter set Doctor ", Toast.LENGTH_SHORT).show()
        val docName="Mr Liew"


//        val doctor=FirebaseAuth.getInstance()
        val doctor= hashMapOf(
            "name" to docName




        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("doctor")?.document( "docDetail")?.set(doctor)?.addOnSuccessListener {


            Toast.makeText(this,"Successfully added ", Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add ", Toast.LENGTH_SHORT).show()
            }





    }


    class CustomAdapter(var itemModel:ArrayList<Modal>,var context: Context): BaseAdapter(){
        override fun getCount(): Int {

            return itemModel.size
        }

        override fun getItem(p0: Int): Any {
            return itemModel[p0]
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }
        private var layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

            var view=view
            if(view==null)
            {
                view=layoutInflater.inflate(R.layout.row_items,viewGroup,false)
            }
            var tvImageName=view?.findViewById<TextView>(R.id.name )
//            var tvTime=view?.findViewById<TextView>(R.id.date )
//            var tvPro=view?.findViewById<TextView>(R.id.Pro )

            var imageView=view?.findViewById<ImageView>(R.id.imageView)

            tvImageName?.text=itemModel[position].name
//            tvTime?.text=itemModel[position].date
//            tvPro?.text=itemModel[position].pro

            itemModel[position].image?.let { imageView?.setImageResource(it) }

            return view!!
        }


    }


    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {
//            txt.setText(null)
//            arraylistDocSearch.clear()

            getDataDoc()
//           readDoc(txt)
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }



}