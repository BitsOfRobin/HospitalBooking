package com.example.hospitalbooking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class ListCustomAdapterForPrescription(var context: Context, private var pres:ArrayList<String>):
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
            view=layout.inflate(R.layout.customlistview,p2,false)
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        }

        else{
            view=p1
            viewHolder=view.tag as ViewHolder
        }

        var prescription=getItem(p0) as String
        viewHolder.txtName.text=prescription


        return view as View

    }

    private  class ViewHolder(row:View?){
        lateinit var txtName: TextView
//        lateinit var ivImage:ImageView

        init {
            this.txtName=row?.findViewById(R.id.txtAppoint) as TextView
//            this.ivImage=row?.findViewById(R.id.imgAppoint) as ImageView

        }

    }



}