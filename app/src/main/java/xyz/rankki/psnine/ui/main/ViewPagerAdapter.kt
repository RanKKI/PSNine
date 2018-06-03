package xyz.rankki.psnine.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.rankki.psnine.model.Gene
import xyz.rankki.psnine.model.Topics
import xyz.rankki.psnine.ui.topics.TopicsFragment

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private var fragments: ArrayList<Fragment> = ArrayList()

    init {
        fragments.add(TopicsFragment.newInstance<Gene, Gene.Topic>(Gene::class.java))
        fragments.add(TopicsFragment.newInstance<Topics, Topics.Topic>(Topics::class.java))

    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].arguments?.getString("name")

}