package com.example.blogit.Activities.ui.main

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.blogit.Fragments.*
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
    FragmentPagerAdapter(fm , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = ChatsFragment()
            }
            1 -> {
                fragment = MessageFragment()
            }
            2 -> {
                fragment = GroupFragment()
            }
            3 -> {
                fragment = StatusFragment()
            }
            4 -> {
                fragment = ProfileFragment()
            }
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return  ""
            1 -> return  ""//context.getString(R.string.users)
            2 -> return  ""//context.getString(R.string.groups)
            3 -> return  ""//context.getString(R.string.status)
            4 -> return  ""//context.getString(R.string.profile)

        }
        return null
    }

    override fun getCount(): Int {
        // Show 4 total pages.
        return 5
    }
}