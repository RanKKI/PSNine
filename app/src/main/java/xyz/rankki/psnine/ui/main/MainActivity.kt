package xyz.rankki.psnine.ui.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.Utils
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.sdk25.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.viewPager
import xyz.rankki.psnine.R
import xyz.rankki.psnine.data.http.HttpManager

class MainActivity : AppCompatActivity() {

    companion object {
        const val ID_Toolbar: Int = 1
        const val ID_TabLayout: Int = 2
        const val ID_ViewPager: Int = 3

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            appBarLayout {
                toolbar {
                    id = ID_Toolbar
                    title = "PSNine"
                    backgroundColorResource = R.color.colorAppBarBackground
                    setTitleTextColor(resources.getColor(R.color.colorAppBarText))
                    menuInflater.inflate(R.menu.menu_main, menu)
                    onMenuItemClick { item ->
                        when (item!!.itemId) {
                            R.id.action_settings -> true
                            else -> super.onOptionsItemSelected(item)
                        }
                    }
                }.lparams(matchParent, wrapContent)
                tabLayout {
                    id = ID_TabLayout
                    backgroundColorResource = R.color.colorAppBarBackground
                    setTabTextColors(resources.getColor(R.color.colorTabTextNormal),
                            resources.getColor(R.color.colorTabTextSelected))
                }.lparams(matchParent, wrapContent)
            }
            viewPager {
                id = ID_ViewPager
                adapter = ViewPagerAdapter(supportFragmentManager)
            }.lparams(matchParent, matchParent)
        }

        Utils.init(application)
        HttpManager.init()

        find<TabLayout>(ID_TabLayout).setupWithViewPager(find(ID_ViewPager))
    }
}
