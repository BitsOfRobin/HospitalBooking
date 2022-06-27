package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class listCustomAdapter(var context: Context, private var appointmentDetail:ArrayList<AppointmentDetail>):
    BaseAdapter() {
    override fun getCount(): Int {
       return appointmentDetail.count()
    }

    override fun getItem(p0: Int): Any {
        return appointmentDetail.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view:View
        var viewHolder:ViewHolder
        if(p1==null)
        {
            var layout=LayoutInflater.from(context)
            view=layout.inflate(R.layout.customlistview,p2,false)
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        }

        else{
            view=p1
            viewHolder=view.tag as ViewHolder
        }

        var appointment=getItem(p0) as AppointmentDetail
        viewHolder.txtName.text=appointment.AppointmentDetail.toString()+"\n"+appointment.docName.toString()+"\n"+appointment.userName.toString()+"\n"


        var dateInString = appointment.AppointmentDetail.trim()
        dateInString.replace(" ", "-")


            if (dateInString[0].toString().toInt() < 10&&dateInString[1].toString().contains(" ")) {
                dateInString = "0$dateInString"

            }
        else
            {

                dateInString=dateInString
            }
            val calendarDate = Calendar.getInstance().time


//        val utc = TimeZone.getTimeZone("UTC")
//        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
//        sourceFormat.timeZone =utc

//
//        val convertedDate = sourceFormat.parse(dateInString)
//        destFormat.format(convertedDate)

        viewHolder.txtNum.text =(p0+1).toString()
            val formatter = SimpleDateFormat("dd-MMM-yyyy")
            val date = formatter.parse(dateInString.replace(" ","-"))
            if (calendarDate.after(date) ) {
                viewHolder.txtWarn.setTextColor(Color.parseColor("#FFE91E63"))
                viewHolder.txtName.text = appointment.userName.toString()
                viewHolder.txtTime.text = appointment.AppointmentDetail.toString()
                viewHolder.txtDoc.text =  appointment.docName.toString()
                viewHolder.txtWarn.text = "BOOKING TIME PASSED"
                viewHolder.txtNum.setTextColor(Color.parseColor("#FFE91E63"))


            }


        else{


                viewHolder.txtName.text = appointment.userName.toString()
                viewHolder.txtTime.text = appointment.AppointmentDetail.toString()
                viewHolder.txtDoc.text =  appointment.docName.toString()
                viewHolder.txtWarn.text = "Be Ready "
            }

        val firebaseAuth= FirebaseAuth.getInstance()
        val firebaseUser=firebaseAuth.currentUser
        val image= firebaseUser?.photoUrl
        Picasso.get().load(image).into(viewHolder.ivImage);

//        viewHolder.ivImage.setImageResource((appointment.image))

        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName:TextView
        lateinit var txtDoc:TextView
        lateinit var txtTime:TextView
        lateinit var txtWarn:TextView
        lateinit var txtNum:TextView
        lateinit var ivImage: ImageView

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
            this.txtDoc=row?.findViewById(R.id.txtDoc) as TextView
            this.txtTime=row?.findViewById(R.id.txtUser) as TextView
            this.txtWarn=row?.findViewById(R.id.txtWarn) as TextView
            this.txtNum=row?.findViewById(R.id.txtNum) as TextView
            this.ivImage=row?.findViewById(R.id.userImg) as ImageView

        }

    }



}