package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
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














        var CheckName=prescription.doc.toString()
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
        Picasso.get().load(image).into(viewHolder.ivImage);
        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName: TextView
        lateinit var txtDoc: TextView
        lateinit var txtMedi: TextView
        lateinit var ivImage: ImageView

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
            this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
            this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
            this.ivImage=row?.findViewById(R.id.userImg) as ImageView

        }

    }



}