package xyz.rankki.psnine.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.rankki.psnine.model.topics.Gene
import xyz.rankki.psnine.model.topics.Home
import xyz.rankki.psnine.model.topics.Plus
import xyz.rankki.psnine.ui.topics.TopicsFragment

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private var fragments: ArrayList<Fragment> = ArrayList()

    init {
        fragments.add(TopicsFragment.newInstance(Gene::class.java, Gene.Topic::class.java))
        fragments.add(TopicsFragment.newInstance(Plus::class.java, Plus.Topic::class.java))
        fragments.add(TopicsFragment.newInstance(Home::class.java, Home.Topic::class.java))
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].arguments?.getString("name")

}