package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.TextView


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
        viewHolder.txtDoc.text=prescription.doc.toString()
        viewHolder.txtMedi.text=prescription.medicine.toString()


        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName: TextView
        lateinit var txtDoc: TextView
        lateinit var txtMedi: TextView
//        lateinit var ivImage:ImageView

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
            this.txtDoc= row.findViewById(R.id.txtDoc) as TextView
            this.txtMedi= row.findViewById(R.id.txtMedi) as TextView
//            this.ivImage=row?.findViewById(R.id.imgAppoint) as ImageView

        }

    }



}