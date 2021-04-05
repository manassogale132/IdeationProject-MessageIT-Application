package com.example.blogit.Activities.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.blogit.Fragments.MessageFragment
import com.example.blogit.Fragments.ProfileFragment
import com.example.blogit.Fragments.StatusFragment
import com.example.blogit.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = MessageFragment()
            }
            1 -> {
                fragment = StatusFragment()
            }
            2 -> {
                fragment = ProfileFragment()
            }
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Message"
            1 -> return "Status"
            2 -> return "Profile"
        }
        return null
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}