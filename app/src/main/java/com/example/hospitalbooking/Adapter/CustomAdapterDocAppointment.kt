package com.example.hospitalbooking.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.KotlinClass.AppointmentDetail
import com.example.hospitalbooking.KotlinClass.ModalFormMain
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.spec.PSource

class CustomAdapterDocAppointment (var context: Context, private var appointmentDetail: ArrayList<AppointmentDetail>,
                               private val listener: OnItemClickListener, private val longClickListener: OnItemLongClickListener
) :
    RecyclerView.Adapter<CustomAdapterDocAppointment.ViewHolder>(){


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.customlistview, parent, false)

        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appointmentDetail[position], listener, longClickListener,context,position)
    }






    override fun getItemCount(): Int {
        return  appointmentDetail.size
    }





      class ViewHolder(row:View):RecyclerView.ViewHolder(row){
        lateinit var txtName:TextView
        lateinit var txtDoc:TextView
        lateinit var txtTime:TextView
        lateinit var txtWarn:TextView
        lateinit var txtNum:TextView
        lateinit var ivImage: ImageView
        lateinit var ivImage2: ImageView
        lateinit var txtcommentStatus: TextView

        init {
           this.txtName= row.findViewById(R.id.txtAppoint) as TextView
            this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
            this.txtTime= row.findViewById(R.id.txtUser) as TextView
            this.txtWarn= row.findViewById(R.id.txtWarn) as TextView
            this.txtNum= row.findViewById(R.id.txtNum) as TextView
            this.txtcommentStatus= row.findViewById(R.id.commentStatus) as TextView
            this.ivImage= row.findViewById(R.id.userImg) as ImageView
            this.ivImage2= row.findViewById(R.id.docImg) as ImageView

        }



        fun bind(
            appointment: AppointmentDetail,
            listener: OnItemClickListener,
            longClickListener: OnItemLongClickListener,
            context: Context,
            position: Int
        ) {


//            var appointment=getItem(p0) as AppointmentDetail
        txtName.text=appointment.AppointmentDetail.toString()+"\n"+appointment.docName.toString()+"\n"+appointment.userName.toString()+"\n"


            var dateInString = appointment.AppointmentDetail.trim()
            dateInString.replace(" ", "-")


//            if (dateInString[0].toString().toInt() < 10&&dateInString[1].toString().contains(" ")) {
//                dateInString = "0$dateInString"
//
//            }
//        else
//            {
//
//                dateInString=dateInString
//            }
            val calendarDate = Calendar.getInstance().time

            val detect=dateInString.indexOf(",")
            val sub1=dateInString.substring(0,detect)
            val sub2=dateInString.substring(detect+2,dateInString.length)
            dateInString= "$sub1,$sub2"
//        val utc = TimeZone.getTimeZone("UTC")
//        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
//        sourceFormat.timeZone =utc

//
//        val convertedDate = sourceFormat.parse(dateInString)
//        destFormat.format(convertedDate)
            val docName=appointment.docName
           txtNum.text =(position+1).toString()
            val formatter = SimpleDateFormat("dd-MMM-yyyy,HH:mm:ss")
            val date = formatter.parse(dateInString.replace(" ","-"))
            if (calendarDate.after(date) ) {
               txtWarn.setTextColor(Color.parseColor("#FFE91E63"))
               txtName.text = appointment.userName.toString()
               txtTime.text = appointment.AppointmentDetail.toString()
               txtDoc.text =  appointment.docName.toString()
              txtcommentStatus.text=appointment.commentStatus
               txtWarn.text = "BOOKING TIME PASSED"
              txtNum.setTextColor(Color.parseColor("#FFE91E63"))


            }


            else{

          txtcommentStatus.text=appointment.commentStatus
             txtName.text = appointment.userName.toString()
                txtTime.text = appointment.AppointmentDetail.toString()
              txtDoc.text =  appointment.docName.toString()
               txtWarn.text = "Be Ready "
            }

            val firebaseAuth= FirebaseAuth.getInstance()
            val firebaseUser=firebaseAuth.currentUser
            val image= firebaseUser?.photoUrl
            Picasso.get().load(image).into(ivImage);
            val cache= MyCache()
            val bit: Bitmap? =cache.retrieveBitmapFromCache(docName)
            Glide.with(context)
                .load(bit)
                .into(ivImage2)



           itemView.setOnClickListener {
                listener.onItemClick(position)
            }

            itemView.setOnLongClickListener {
                longClickListener.onItemLongClick(position)
                true
            }


        }



    }






}

