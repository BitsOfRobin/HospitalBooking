package com.example.hospitalbooking.Adapter

import com.example.hospitalbooking.KotlinClass.MyCache
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.hospitalbooking.KotlinClass.Prescription
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class ListCustomAdapterForPrescription(var context: PrescriptionDisplay, private var pres:ArrayList<Prescription>):
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

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ResourceAsColor", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view:View
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
        viewHolder.txtName.text=prescription.user.toString()
        var appointment=prescription.appointment.toString()
        appointment.replace("\n","")
        viewHolder.txtAppointment.text=appointment
        viewHolder.txtVisitStatus.text=prescription.visitStatus.toString()
//        viewHolder.txtDoc.text=prescription.doc.toString()
        val medi=prescription.medicine.toString()
        val mediStr=prescription.medicine.toString()

        var str1: SpannableString
        var str2=SpannableString(medi)
//        val position=ArrayList<Int>()
        val position1=medi.indexOf("mg")
        val position2=medi.lastIndexOf("mg")
//        position.add(position1)
//        position.add(position2)
        val subStr1=medi.substring(0,position1+2)
        val subStr2=medi.substring(position1+2,medi.length)
        val newPositionSub=subStr2.indexOf("mg")
            str1=setColorText(subStr1,position1-2,position1+2)
            str2=setColorText(subStr2,newPositionSub-2,newPositionSub+2)

        val str= TextUtils.concat(str1, str2);
//        val priceStr=medi.substring(newPositionSub+2,mediStr.length)
//        val fullStr=TextUtils.concat(str, priceStr);

        viewHolder.txtMedi.text=str


//        viewHolder.ratingBar.tag = p0;
//
//        viewHolder.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, boolean ->
//
//            viewHolder.ratingBar=ratingBar
//            val pres:Prescription= getItem(p0) as Prescription
//            pres.setRatingStar(fl)
//
//
//
//        }
//









        var CheckName=prescription.doc.toString()
        var docName=pres[p0].doc.toString()

        if (CheckName != null) {
            if(CheckName.length>10) {
                var index=CheckName.indexOf(" ",5,true)
                CheckName=CheckName.substring(0,index)+"\n"+CheckName.substring(index,CheckName.length)
                viewHolder.txtDoc.text =CheckName

            }

            else{
                viewHolder.txtDoc.text =CheckName

            }
        }
        val firebaseAuth= FirebaseAuth.getInstance()
        val firebaseUser=firebaseAuth.currentUser
        val image= firebaseUser?.photoUrl
        var userG=""
        var email=""
        firebaseUser.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (firebaseUser != null) {
                userG = firebaseUser.displayName.toString()
                email=firebaseUser.email.toString()

            }

            else {
//
                userG = " NOne"
            }

        }

        if(!email.contains("@student.tar",true)){


            Picasso.get().load(image).into(viewHolder.ivImage);
        }

//        val bitmap = BitmapFactory.decodeFile(image!!.path)

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
        return view as View

    }
    private fun setColorText(str: String,start:Int,end:Int): SpannableString {

//        val docPro=findViewById<TextView>(R.id.docPro)


        val yellow = ForegroundColorSpan(Color.MAGENTA)
        val spannableString = SpannableString(str)


        spannableString.setSpan(yellow,
            start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        val num=arraylistPro[position].length

//        Toast.makeText(this,"$spannableString", Toast.LENGTH_SHORT).show()


//        docPro.text = spannableString.toString()
        return spannableString
    }




    private  class ViewHolder(row:View?){
        lateinit var txtName: TextView
        lateinit var txtDoc: TextView
        lateinit var txtMedi: TextView
        lateinit var txtVisitStatus: TextView
        lateinit var txtAppointment: TextView
        lateinit var ivImage: ImageView
        lateinit var ivImage2: ImageView
        lateinit var ratingBar: RatingBar

        init {
            this.txtName=row?.findViewById(R.id.txtUser) as TextView
            this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
            this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
            this.txtVisitStatus= row.findViewById(R.id.txtVisitStatus) as TextView
            this.txtAppointment= row.findViewById(R.id.txtAppoint) as TextView
            this.ivImage= row.findViewById(R.id.userImg) as ImageView
            this.ivImage2= row.findViewById(R.id.imageView3) as ImageView
//            this.ratingBar= row.findViewById(R.id.ratingBarInput) as RatingBar

        }













    }



}