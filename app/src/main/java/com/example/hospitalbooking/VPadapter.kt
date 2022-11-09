package com.example.hospitalbooking
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hospitalbooking.databinding.FragmentViewPatientBinding


class VPadapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private val  fragmentList=ArrayList<Fragment>()
    private val  fragmentTittle=ArrayList<String>()


    override fun getCount(): Int {
       return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
      return fragmentList.get(position)
    }

    fun addFragment(fragment:Fragment,title:String){

        fragmentList.add(fragment)
        fragmentTittle.add(title)


    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTittle.get(position)
    }



}