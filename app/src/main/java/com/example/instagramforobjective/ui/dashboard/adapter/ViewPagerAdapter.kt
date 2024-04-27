package com.example.instagramforobjective.ui.dashboard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.processNextEventInCurrentThread

class ViewPagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }
    fun addFragments(fragment: Fragment,title:String){
        fragmentList.add(fragment)
        titleList.add(title)

    }
}