package com.example.hospitalbooking

import MyCache
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.GnssAntennaInfo
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*


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
        var viewHolder:ViewHolder
        if(p1==null)
        {
            var layout= LayoutInflater.from(context)
            view=layout.inflate(R.layout.list_view_prescription,p2,false)
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        }

        else{
            view=p1
            viewHolder=view.tag as ViewHolder
        }

        var prescription=getItem(p0) as Prescription
        viewHolder.txtName.text=prescription.user.toString()
//        viewHolder.txtDoc.text=prescription.doc.toString()
        viewHolder.txtMedi.text=prescription.medicine.toString()

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
//        val bitmap = BitmapFactory.decodeFile(image!!.path)
        Picasso.get().load(image).into(viewHolder.ivImage);
        val cache=MyCache()
//        Glide.with(context)
//            .load(cache.retrieveBitmapFromCache("a"))
//            .into(viewHolder.ivImage2)
//        val cache=MyCache()
//        cache.saveBitmapToCahche(firebaseUser.toString(),bitmap)
//        cache.retrieveBitmapFromCache(firebaseUser.toString())
//        Picasso.get().load(image).into(viewHolder.ivImage);
        val bit: Bitmap? =cache.retrieveBitmapFromCache(docName)
        Glide.with(context)
            .load(bit)
            .into(viewHolder.ivImage2)
        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName: TextView
        lateinit var txtDoc: TextView
        lateinit var txtMedi: TextView
        lateinit var ivImage: ImageView
        lateinit var ivImage2: ImageView
        lateinit var ratingBar: RatingBar

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
            this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
            this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
            this.ivImage= row.findViewById(R.id.userImg) as ImageView
            this.ivImage2= row.findViewById(R.id.imageView3) as ImageView
//            this.ratingBar= row.findViewById(R.id.ratingBarInput) as RatingBar

        }

    }



}