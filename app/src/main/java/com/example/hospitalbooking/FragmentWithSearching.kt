package com.example.hospitalbooking

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.AdminManagementOnAppointment.DoctorViewAppointment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWithSearching.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWithSearching : Fragment(R.layout.fragment_with_searching) {

            lateinit var menuitem:MenuItem
            lateinit var searchview:SearchView


    override fun onCreate(savedInstanceState: Bundle?) {

        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.searchview,menu)

        val temp = ArrayList<String>()
        var mainPage: MainPage = MainPage()
        val doctorViewAppoint: DoctorViewAppointment = DoctorViewAppointment()
//        mainPage.getDataDoc()
//        val getData=mainPage.getDataDoc()
        menuitem=menu.findItem(R.id.app_bar_search)
        searchview=MenuItemCompat.getActionView(menuitem) as SearchView
        searchview.isIconified
        var searchManager:SearchManager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchview.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                temp.clear()

//                p0?.let { mainPage.captureInput(it) }
//                p0?.let { doctorViewAppoint.captureInput(it) }






                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }


        })









    }
    object ConnectivityUtils {
        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }




}