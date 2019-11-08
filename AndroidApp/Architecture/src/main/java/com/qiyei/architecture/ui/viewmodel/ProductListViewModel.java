package com.qiyei.architecture.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.qiyei.architecture.data.bean.Product;
import com.qiyei.architecture.data.repository.ProductRepository;

import java.util.List;

/**
 * @author Created by qiyei2015 on 2018/1/13.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class ProductListViewModel extends ViewModel {

    private String mId;

    private ProductRepository mRepository;

    public void init(String id){
        mId = id;
        mRepository = new ProductRepository(id);
    }

    public LiveData<List<Product>> getProducts(String id){
        return mRepository.getProducts(id);
    }

    public void setTest(String id){
        mRepository.setTest(id);
    }

}
