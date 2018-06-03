package xyz.rankki.psnine.ui.topics

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import xyz.rankki.psnine.base.BaseModel

@Suppress("UNCHECKED_CAST")
class TopicsAdapter<T>(context: Context) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    private val ankoContext = AnkoContext.createReusable(context, this)

    var data: ArrayList<T> = ArrayList()

    fun updateData(newData: ArrayList<*>) {
        doAsync {
            val start: Int = data.size - 1
            data.addAll(newData as Collection<T>)
            uiThread {
                notifyItemRangeChanged(start, data.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TopicsAdapterUI<T>().createView(ankoContext))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = data[position] as BaseModel.BaseItem
        holder.tvTitle.text = topic.getTitle()
        holder.tvUsername.text = topic.getUsername()
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.find(TopicsAdapterUI.ID_Title)
        val tvUsername: TextView = view.find(TopicsAdapterUI.ID_Username)
    }
}