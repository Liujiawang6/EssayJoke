package com.qiyei.ui.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;


import com.qiyei.sdk.view.xrecycler.XRecyclerAdapter;
import com.qiyei.sdk.view.xrecycler.XRecyclerListener;
import com.qiyei.sdk.view.xrecycler.XRecyclerView;
import com.qiyei.sdk.view.xrecycler.base.BaseViewHolder;
import com.qiyei.sdk.view.xrecycler.base.CategoryItemDecoration;
import com.qiyei.ui.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTestActivity extends AppCompatActivity implements XRecyclerListener{

    private XRecyclerView mRecyclerView;

    private List<String> mDatas;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                mRecyclerView.stopRefresh();
                return;
            }else if (msg.what == 2){
                mRecyclerView.stopLoadMore();
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        mRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("RecyclerTest");

        initData();

        initView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CategoryItemDecoration(getDrawable(R.drawable.recyclerview_decoration)));
        //mRecyclerView.setAdapter(new CommonAdapter(this,R.layout.recyclerview_item));
        mRecyclerView.setXRecyclerListener(this);
        mRecyclerView.setAdapter(new XRecyclerAdapter<String>(this,mDatas,R.layout.recyclerview_item) {
            @Override
            public void convert(BaseViewHolder holder, String data, int position) {

                holder.setText(R.id.tv,data)
                        .setTextColor(R.id.tv,Color.RED)
                        .setTextSize(R.id.tv,28);
            }
        });



        for (int i = 0;i < 5 ;i++){
            TextView header = new TextView(this);
            header.setText("头部 " + i);
            header.setTextSize(30);

            TextView footer = new TextView(this);
            footer.setText("底部 "+ i);
            footer.setTextSize(30);

            mRecyclerView.addHeaderView(header);
            mRecyclerView.addFooterView(footer);
        }

        mRecyclerView.setPullRefresh(true);

        mRecyclerView.setPullLoad(true);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlag = ItemTouchHelper.LEFT;

                return makeMovementFlags(0,swipeFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onRefresh() {
        Message msg = Message.obtain();
        msg.what = 1;
        mHandler.sendMessageDelayed(msg,3000);
    }

    @Override
    public void onLoadMore() {
        Message msg = Message.obtain();
        msg.what = 2;
        mHandler.sendMessageDelayed(msg,3000);
    }

    private void initData(){
//        CommonTitleBar titleBar = new CommonTitleBar.Builder(this)
//                .setTitle("recyclerView下拉刷新上拉加载测试")
//                .setRightText("待添加")
//                .build();
        mDatas = new ArrayList<>();
        for (int i = 0;i < 50;i++){
            String s = new String("测试 " + i);
            mDatas.add(s);
        }
    }

}
