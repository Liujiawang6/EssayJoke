package com.qiyei.mall.ui.activity


import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.qiyei.framework.ui.activity.BaseActivity
import com.qiyei.framework.ui.fragment.FragmentHelper
import com.qiyei.mall.R
import com.qiyei.mall.goodsmanager.common.GoodsConstant
import com.qiyei.mall.goodsmanager.event.UpdateCartCountEvent
import com.qiyei.mall.goodsmanager.ui.fragment.CategoryFragment
import com.qiyei.mall.messagemanager.event.UpdateMessageEvent
import com.qiyei.mall.messagemanager.ui.fragment.MessageFragment
import com.qiyei.mall.ordermanager.ui.fragment.CartFragment
import com.qiyei.mall.ui.fragment.HomeFragment
import com.qiyei.mall.usermanager.ui.fragment.UserFragment
import com.qiyei.mall.view.HomeBottomNavigationBar
import com.qiyei.router.path.RouteMall
import com.qiyei.sdk.dc.DataManager
import com.qiyei.sdk.log.LogManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit

class HomeActivity : BaseActivity() {

    private lateinit var fragmentHelper:FragmentHelper

    private val mHomeFragment:HomeFragment by lazy { HomeFragment() }
    private val mCategoryFragment: CategoryFragment by lazy { CategoryFragment() }
    private val mCartFragment: CartFragment by lazy { CartFragment() }
    private val mMessageFragment: MessageFragment by lazy { MessageFragment() }
    private val mUserFragment: UserFragment by lazy { UserFragment() }
    /**
     * fragment列表
     */
    private lateinit var mFragmentList:List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentHelper = FragmentHelper(supportFragmentManager,R.id.mHomeContent)
        initView()
        initObserver()
        initFragment()
        loadData()
        ARouter.getInstance().build(RouteMall.PayManager.cash_pay).navigation()
        LogManager.i(getTAG(),"onCreate")
    }

    override fun getTAG(): String {
        return this::class.java.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }


    private fun initView(){
        mHomeBottomNavigationBar.setTabSelectedListener(object:BottomNavigationBar.OnTabSelectedListener{
            override fun onTabReselected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabSelected(position: Int) {
                fragmentHelper.switchFragment(mFragmentList[position])
            }
        })
        mHomeBottomNavigationBar.setMessageBadgeVisibility(false)
    }

    private fun initObserver(){
        Bus.observe<UpdateCartCountEvent>()
                .subscribe {
                    updateCartCountView()
                }.registerInBus(this)
        Bus.observe<UpdateMessageEvent>()
                .subscribe {
                    updateMessageCountView()
                }.registerInBus(this)
    }

    private fun initFragment(){
        fragmentHelper.switchFragment(mHomeFragment)
        mFragmentList = listOf(mHomeFragment,mCategoryFragment,mCartFragment,mMessageFragment,mUserFragment)
    }

    private fun updateCartCountView(){
        val uri = DataManager.getInstance().getUri(GoodsConstant.javaClass,GoodsConstant.SP_CART_SIZE)
        val value = DataManager.getInstance().getInt(uri,0)
        LogManager.i(getTAG(),"updateCartCountView:$value")
        mHomeBottomNavigationBar.setCartBadgeCount(value)
    }

    private fun updateMessageCountView(){
        mHomeBottomNavigationBar.setMessageBadgeVisibility(true)
    }

    /**
     * 加载数据
     */
    private fun loadData(){
        loadCartData()
    }

    /**
     * 加载购物车数据
     */
    private fun loadCartData(){
        updateCartCountView()
    }
}
