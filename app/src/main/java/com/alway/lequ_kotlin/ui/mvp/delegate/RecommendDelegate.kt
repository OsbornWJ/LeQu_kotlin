package com.alway.lequ_kotlin.ui.mvp.delegate

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.alway.lequ_kotlin.R
import com.alway.lequ_kotlin.http.entity.Result
import com.alway.lequ_kotlin.ui.base.LeQuDelegate
import com.alway.lequ_kotlin.ui.mvp.contract.DiscoveryContract
import com.alway.lequ_kotlin.ui.mvp.presenter.DiscoveryPresenter
import com.bumptech.glide.Glide
import com.example.lequ_core.config.LeQu
import com.moment.eyepetizer.home.adapter.MultiTypeAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.comm_recycler_data.*

/**
 * 创建人: Jeven
 * 邮箱:   Osbornjie@163.com
 * 功能:
 */
class RecommendDelegate : LeQuDelegate(), DiscoveryContract.View {

    var page: Int = 1
    var mAdapter: MultiTypeAdapter? = null

    private val mPresenter: DiscoveryPresenter by lazy { DiscoveryPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun setLayout(): Any = R.layout.comm_recycler_data

    override fun onBindView(savedInstanceState: Bundle?, rootView: View) = Unit

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        smart_refreshLayout.setRefreshHeader(ClassicsHeader(_mActivity))
        smart_refreshLayout.setRefreshFooter(ClassicsFooter(_mActivity))
        smart_refreshLayout.setEnableAutoLoadMore(true)
        smart_refreshLayout.autoRefresh()
        smart_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                initData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                initData()
            }

        })

        dataView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    2 -> Glide.with(LeQu.applicationContext).pauseRequests()
                    0, 1 -> Glide.with(LeQu.applicationContext).resumeRequests()
                }
            }
        })
        val list = ArrayList<Result.ItemList>()
        dataView.layoutManager = LinearLayoutManager(_mActivity)
        mAdapter = MultiTypeAdapter(list, _mActivity!!)
        dataView.adapter = mAdapter
    }

    override fun initData() {
        checkNotNull(mPresenter as DiscoveryPresenter) {"${RecommendDelegate::class.java.simpleName} presenter is null"}.allRec(page)
    }

    override fun onDiscoverySucc(result: Result) {
        smart_refreshLayout.finishRefresh()
        smart_refreshLayout.finishLoadMore(0, true, TextUtils.isEmpty(result.nextPageUrl))
        if (result.itemList!!.isEmpty()) return
        val start = mAdapter!!.itemCount
        if (page == 1) {
            mAdapter!!.clearAll()
            mAdapter!!.notifyItemRangeRemoved(0, start)
            mAdapter!!.addAll(result.itemList as ArrayList<Result.ItemList>?)
            mAdapter!!.notifyItemRangeInserted(0, result.itemList!!.size)
        } else {
            mAdapter!!.addAll(result.itemList as ArrayList<Result.ItemList>?)
            mAdapter!!.notifyItemRangeInserted(start, result.itemList!!.size)
        }
    }

    override fun onDiscoveryFail(error: Throwable?) {
        smart_refreshLayout.finishRefresh()
        smart_refreshLayout.finishLoadMore(false)
    }

    override fun onDestroy() {
        mPresenter.dettachView()
        super.onDestroy()
    }
}