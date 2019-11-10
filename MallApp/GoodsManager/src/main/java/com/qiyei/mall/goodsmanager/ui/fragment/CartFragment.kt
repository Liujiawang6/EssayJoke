package com.qiyei.mall.ordermanager.ui.fragment


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.kennyc.view.MultiStateView
import com.qiyei.framework.titlebar.CommonTitleBar
import com.qiyei.framework.ui.fragment.BaseMVPFragment
import com.qiyei.framework.util.YuanFenConverter
import com.qiyei.mall.goodsmanager.R
import com.qiyei.mall.goodsmanager.data.protocol.CartGoods
import com.qiyei.mall.goodsmanager.event.UpdateAllChecked
import com.qiyei.mall.goodsmanager.event.UpdateTotalPriceEvent
import com.qiyei.mall.goodsmanager.ui.adapter.CartGoodsListAdapter
import com.qiyei.mall.ordermanager.injection.component.DaggerCartComponent


import com.qiyei.mall.ordermanager.mvp.presenter.CartManagerPresenter
import com.qiyei.mall.ordermanager.mvp.view.ICartManagerView
import com.qiyei.provider.router.RouteMall
import com.qiyei.provider.router.RouterMallConstant
import com.qiyei.provider.service.mall.IGoodsManagerService
import com.qiyei.provider.service.mall.MallServiceConstant
import com.qiyei.sdk.log.LogManager
import com.qiyei.sdk.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_cart.*



/**
 * @author Created by qiyei2015 on 2018/10/5.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
class CartFragment : BaseMVPFragment<CartManagerPresenter>(),ICartManagerView {

    private lateinit var mCartGoodsListAdapter:CartGoodsListAdapter
    /**
     * 总价格
     */
    private var mTotalPrice: Long = 0

    @Autowired(name = MallServiceConstant.GOODS_MANAGER_SERVICE_PATH)
    lateinit var mGoodsManagerService: IGoodsManagerService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogManager.i(getTAG(),"onViewCreated")
        initView()
        initObserver()
    }


    override fun onStart() {
        super.onStart()
        loadData()
    }

    /**
     * 依赖注入
     */
    override fun initComponentInject() {
        DaggerCartComponent.builder()
                .activityComponent(mActivityComponent)
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when(view.id){
            R.id.mAllCheckBox -> {
                updateData()
                updateView()
            }
            R.id.mSettleAccountsButton -> {
                submitToAccount()
            }
            R.id.mDeleteButton -> {
                //删除所选
                val list:MutableList<Int> = arrayListOf()
                mCartGoodsListAdapter.datas
                        .filter {
                            it.isSelected
                        }.mapTo(list) { it.id }
                mPresenter.deleteCartList(list)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }

    override fun onCartList(goodsList: MutableList<CartGoods>?) {
        mCartGoodsListAdapter.datas = goodsList
        mMultiStateView.viewState = MultiStateView.VIEW_STATE_CONTENT
        updateView()
        //更新数量
        mGoodsManagerService.updateCartGoodsCount(goodsList?.size?:0)
        LogManager.i(getTAG(), "goodsList.size():${goodsList?.size}")
    }

    override fun onDeleteCartList(result: Boolean) {
        LogManager.i(getTAG(), "result:$result")
        ToastUtil.showLongToast("删除成功")
        loadData()
    }

    override fun onSubmitCartList(id: Int) {
        LogManager.i(getTAG(), "orderId:$id")
        ARouter.getInstance().build(RouteMall.OrderManager.ORDER_CONFIRM)
                .withInt(RouterMallConstant.KEY_ORDER_ID,id)
                .withLong(RouterMallConstant.KEY_ORDER_PRICE,mTotalPrice)
                .navigation()
    }

    private fun initView(){
        mTitleBar = CommonTitleBar.Builder(this)
                .setTitle(getString(R.string.common_cart))
                .setRightText(getString(R.string.common_edit))
                .setRightClickListener {
                    val text = if(mTitleBar?.rightText == getString(R.string.common_edit)){
                        switchEditModeView()
                        getString(R.string.common_complete)
                    }
                    else {
                        switchAccountModeView()
                        getString(R.string.common_edit)
                    }
                    mTitleBar?.rightText = text
                }
                .build()

        mAllCheckBox.setOnClickListener(this)
        mSettleAccountsButton.setOnClickListener(this)
        mDeleteButton.setOnClickListener(this)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mCartRecyclerView.layoutManager = layoutManager
        mCartGoodsListAdapter = CartGoodsListAdapter(context!!, arrayListOf())
        mCartRecyclerView.adapter = mCartGoodsListAdapter

        mTotalPriceTextView.text = "合计:${YuanFenConverter.changeF2YWithUnit(mTotalPrice)}"
        //首次是切换为结账模式
        switchAccountModeView()
    }

    private fun initObserver(){
        Bus.observe<UpdateAllChecked>()
                .subscribe {
                    mAllCheckBox.isChecked = it.allChecked
                }.registerInBus(this)
        Bus.observe<UpdateTotalPriceEvent>()
                .subscribe {
            updateTotalPrice()
        }.registerInBus(this)
    }

    private fun loadData(){
        mPresenter.getCartList()
    }

    /**
     * 更新总价格
     */
    private fun updateTotalPrice() {
        updateView()
    }

    private fun calculateTotalPrice() {
        mTotalPrice = mCartGoodsListAdapter.datas
                .filter {
                    it.isSelected
                }.map {
                    it.goodsCount * it.goodsPrice
                }.sum()
    }

    private fun updateView() {
        calculateTotalPrice()
        mTotalPriceTextView.text = "合计:${YuanFenConverter.changeF2YWithUnit(mTotalPrice)}"
    }

    private fun updateData(){
        for (item in mCartGoodsListAdapter.datas){
            item.isSelected = mAllCheckBox.isChecked
        }
        mCartGoodsListAdapter.notifyDataSetChanged()
    }

    /**
     * 切换到编辑模式
     */
    private fun switchEditModeView() {
        mDeleteButton.visibility = View.VISIBLE
        mSettleAccountsButton.visibility = View.GONE
        mTotalPriceTextView.visibility = View.GONE
    }

    /**
     * 切换到结账模式
     */
    private fun switchAccountModeView() {
        mDeleteButton.visibility = View.GONE
        mSettleAccountsButton.visibility = View.VISIBLE
        mTotalPriceTextView.visibility = View.VISIBLE
    }

    /**
     * 去结算
     */
    private fun submitToAccount() {
        val list: List<CartGoods> = mCartGoodsListAdapter.datas
                .filter {
                    it.isSelected
                }
        if (list.isEmpty()) {
            ToastUtil.showLongToast("请选择商品")
            return
        }
        val accountList: MutableList<CartGoods> = arrayListOf()
        list.mapTo(accountList) { it }
        mPresenter.submitCart(accountList, mTotalPrice)
    }
}
