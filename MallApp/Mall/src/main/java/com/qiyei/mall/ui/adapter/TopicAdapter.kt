package com.qiyei.mall.ui.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.qiyei.framework.extend.loadUrl
import com.qiyei.mall.R
import com.qiyei.sdk.log.LogManager
import kotlinx.android.synthetic.main.layout_topic_item.view.*

/*
    话题数据
 */
class TopicAdapter(private val context: Context, private val list: List<String>) : PagerAdapter() {

    companion object {
        const val TAG = "TopicAdapter"
    }

    override fun destroyItem(parent: ViewGroup, paramInt: Int, paramObject: Any) {
        parent.removeView(paramObject as View)
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val rooView = LayoutInflater.from(this.context).inflate(R.layout.layout_topic_item, null)
        LogManager.i(TAG,"loadUrl:" + list[position])
        rooView.mTopicIv.loadUrl(list[position])
        parent.addView(rooView)
        return rooView
    }

    override fun isViewFromObject(paramView: View, paramObject: Any): Boolean {
        return paramView === paramObject
    }
}
