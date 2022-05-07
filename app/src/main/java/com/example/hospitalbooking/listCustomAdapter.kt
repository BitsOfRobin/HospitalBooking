package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
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

    @SuppressLint("ResourceAsColor")
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


        var dateInString = appointment.AppointmentDetail.toString().replace(" ", "-")

        if(dateInString[0].toString().toInt()<10)
        {
            dateInString="0$dateInString"

        }
        val calendarDate= Calendar.getInstance().time



//        val utc = TimeZone.getTimeZone("UTC")
//        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
//        sourceFormat.timeZone =utc

//
//        val convertedDate = sourceFormat.parse(dateInString)
//        destFormat.format(convertedDate)


        val formatter = SimpleDateFormat("dd-MMM-yyyy")
        val date = formatter.parse(dateInString)
        if(calendarDate<date)
        {



        }

        else
        {
            viewHolder.txtName.setTextColor(Color.parseColor("#FF0000"))
            viewHolder.txtName.text=appointment.AppointmentDetail.toString()+"\n"+appointment.docName.toString()+"\n"+appointment.userName.toString()+"\n"+"BOOKING TIME PASSED"+"\n"

        }



//        viewHolder.ivImage.setImageResource((appointment.image))

        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName:TextView
//        lateinit var ivImage:ImageView

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
//            this.ivImage=row?.findViewById(R.id.imgAppoint) as ImageView

        }

    }



}