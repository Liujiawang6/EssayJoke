package com.qiyei.framework.extend


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.qiyei.framework.data.protocol.BaseResp
import com.qiyei.framework.rx.BaseFunction
import com.qiyei.framework.rx.BaseFunctionBoolean
import com.qiyei.framework.rx.BaseObserver
import com.qiyei.sdk.image.ImageManager
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Created by qiyei2015 on 2018/9/22.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 用于kotlin 公共的扩展方法
 */

/**
 * 扩展Observable执行,部分公共逻辑进行封装
 */
fun <T> Observable<T>.execute(observer:BaseObserver<T>,lifecycleProvider: LifecycleProvider<*>){
    this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribe(observer)
}

/**
 * 将Observer<BaseResp<T>>转换成Observer<T>类型
 */
fun <T> Observable<BaseResp<T>>.baseRespConvert():Observable<T>{
    return this.flatMap(BaseFunction())
}

/**
 * 将Observer<BaseResp<T>>转换成Observer<Boolean>类型
 */
fun <T> Observable<BaseResp<T>>.baseRespConvertBoolean():Observable<Boolean>{
    return this.flatMap(BaseFunctionBoolean())
}

/**
 * Button使能
 */
fun Button.enable(editText: EditText,method:() -> Boolean){
    editText.addTextChangedListener(object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            this@enable.isEnabled = method()
        }
        override fun afterTextChanged(s: Editable?) {

        }
    })
}

/**
 * 扩展点击事件
 */
fun View.onClick(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}

/**
 * 扩展点击事件，参数为方法
 */
fun View.onClick(method:() -> Unit): View {
    setOnClickListener { method() }
    return this
}

/**
 * ImageView加载图片
 */
fun ImageView.loadUrl(url: String) {
    ImageManager.getInstance().loadImage(this, url)
}