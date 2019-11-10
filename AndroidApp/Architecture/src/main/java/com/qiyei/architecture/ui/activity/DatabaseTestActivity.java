package com.qiyei.architecture.ui.activity;

import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.qiyei.architecture.R;
import com.qiyei.architecture.data.bean.DataBean;
import com.qiyei.sdk.database.DB;
import com.qiyei.sdk.database.DatabaseManager;
import com.qiyei.sdk.database.IDatabaseSession;
import com.qiyei.sdk.log.LogManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String db = "test.db";
        DatabaseManager.getInstance().initDatabase(db);

//        IDatabaseSession<User> userIDatabaseSession = DatabaseManager.getINSTANCE().openSession(db, User.class);
//        List<User> list = new ArrayList<>();
//
//        for (int i = 0;i < 10;i++){
//            User user = new User(i,"name:" + i);
//            list.add(user);
//        }
//
//        userIDatabaseSession.insert(list);
//        userIDatabaseSession.querySupport();

        IDatabaseSession<DataBean> userIDatabaseSession = DatabaseManager.getInstance().openSession(db, DataBean.class);
        List<DataBean> list = new ArrayList<>();

        for (int i = 0;i < 10;i++){
            DataBean dataBean = new DataBean(i,"name:" + i,"value:" + i);
            list.add(dataBean);
        }

        userIDatabaseSession.insert(list);
        LogManager.i(DB.TAG,"data:" + userIDatabaseSession.queryBuilder().queryAll().toString());

    }

}
