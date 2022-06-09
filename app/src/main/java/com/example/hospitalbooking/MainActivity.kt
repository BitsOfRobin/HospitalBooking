package com.example.hospitalbooking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var modalList=ArrayList<Modal>()


    var images=intArrayOf(R.drawable.appointment,R.drawable.entermedicine,R.drawable.medicine,R.drawable.doc,
        R.drawable.doc3,R.drawable.settime,R.drawable.upload,R.drawable.medicine,R.drawable.dt3
    ,R.drawable.dt2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        userOrAdmin()
        refresh()

    }


    private fun userOrAdmin()
    {
        var names: Array<String>
        modalList.clear()
        names= arrayOf(" ")
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
//
        }

        if(loginUser.contains("@student.tarc"))
        {
            names= arrayOf(
                "Filter Patient Medicine","Login","Set time for Doctors","Upload Images for Doctor","Medicine Recognition"
            )


        }

        else
        {
            names= arrayOf(
                "Filter Patient Medicine" ,"Login","Set time for Doctors","Upload Images for Doctor","Medicine Recognition",
                "Book Appointment", "Medicine Record","View Doctor Appointment","Doctor View Appointment","Patient Prescription"

            )
        }


        for(i in names.indices)
        {

            modalList.add(Modal(names[i],images[i]))
        }
        var customAdapter=CustomAdapter(modalList,this)
        val grid = findViewById<GridView>(R.id.gridView)

//        val arraylist = arrayOf("Book Appointment", "Medicine Record", "Enter Medicine","View Doctor Appointment","User Registration","Login","Set time for Doctors","Upload Images for doctor")

//        id.adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, arraylist)
        grid.adapter=customAdapter

        grid.setOnItemClickListener { adapterView, view, i, l ->

            if (i == 0) {
                val intent = Intent(this, DoctorFilterMedicine::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            } else if (i == 1) {
                val intent = Intent(this,UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)


            } else if (i == 2) {
                val intent = Intent(this, MainPage::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)


            }

            else if (i == 3) {
                val intent = Intent(this, UploadImg::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)


            }

//            else if (i == 4) {
//                val intent = Intent(this, UserRegister::class.java)
////            intent.putExtra("DoctorName", tempListViewClickedValue)
//                startActivity(intent)
//
//
//            }

            else if (i == 4) {
                val intent = Intent(this, UserMedicine::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)


            }

            else if (i == 5) {
                val intent = Intent(this, MainPage::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)


            }

            else if(i==6)
            {
                val intent = Intent(this, MedicineRecord::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)

            }

            else if(i==7)
            {
                val intent = Intent(this, DoctorAppointment::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)

            }

            else if(i==8)
            {
                val intent = Intent(this, DoctorViewAppointment::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)

            }

            else if(i==9)
            {
                val intent = Intent(this, PrescriptionDisplay::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)

            }


        }
    }


    class CustomAdapter(var itemModel: ArrayList<Modal>, var context: Context):BaseAdapter(){
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
                view=layoutInflater.inflate(R.layout.welcomepagegridview,viewGroup,false)
            }
            var tvImageName=view?.findViewById<TextView>(R.id.imageName )
            var imageView=view?.findViewById<ImageView>(R.id.imageView)

            tvImageName?.text=itemModel[position].name
            imageView?.setImageResource(itemModel[position].image!!)

            return view!!
        }


    }


    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {
//            txt.setText(null)
//            arraylistDocSearch.clear()

           userOrAdmin()

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
