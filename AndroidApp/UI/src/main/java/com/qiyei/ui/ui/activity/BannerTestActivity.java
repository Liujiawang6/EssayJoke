package com.qiyei.ui.ui.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.qiyei.framework.data.protocol.DiscoverListResp;
import com.qiyei.sdk.http.HttpManager;
import com.qiyei.sdk.http.base.HttpRequest;
import com.qiyei.sdk.http.base.INetCallback;
import com.qiyei.sdk.http.base.RequestMethod;
import com.qiyei.sdk.image.ImageManager;
import com.qiyei.sdk.log.LogManager;
import com.qiyei.sdk.util.ToastUtil;
import com.qiyei.sdk.view.banner.BannerAdapter;
import com.qiyei.sdk.view.banner.BannerViewPager;
import com.qiyei.ui.R;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerTestActivity extends AppCompatActivity {

    private final static String TAG = "BannerTestActivity";

    private BannerViewPager mBannerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_test);
        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner_view_pager);


        new HttpManager().execute(getSupportFragmentManager(),buildRequest(), new INetCallback<DiscoverListResp>() {
            @Override
            public void onSuccess(DiscoverListResp result) {
                LogManager.d(TAG,"name --> "+result.getData().getCategories().getName());
                //ToastUtil.showLongToast(result.getData().getCategories().getName());
                initBanner(result.getData().getRotate_banner().getBanners());
            }

            @Override
            public void onFail(Exception e) {
                LogManager.d(TAG,e.getMessage());
                ToastUtil.showLongToast(e.getMessage());
            }
        });

    }

    private void addCommonParams(Map<String,Object> params){
        params.put("app_name","joke_essay");
        params.put("version_name","5.7.0");
        params.put("ac","wifi");
        params.put("device_id","30036118478");
        params.put("device_brand","Xiaomi");
        params.put("update_version_code","5701");
        params.put("manifest_version_code","570");
        params.put("longitude","113.000366");
        params.put("latitude","28.171377");
        params.put("device_platform","android");
    }


    private HttpRequest buildRequest(){

        HttpRequest request = new HttpRequest();

        request.setUrl("http://is.snssdk.com/2/essay/discovery/v3/");
        Map<String,Object> params = new HashMap<>();

        params.put("iid","6152551759");
        params.put("aid","7");
        params.put("channel",360);

        addCommonParams(params);

        request.setParams(params);
        request.setRequestMethod(RequestMethod.GET);
        request.setUseCache(true);
        return request;
    }

    private void initBanner(final List<DiscoverListResp.DataBean.RotateBannerBean.BannersBean> list){
        mBannerViewPager.setAdapter(new BannerAdapter() {
            @Override
            public int getCount() {
                //LogManager.d(TAG,"banner size  --> " + list.size());
                return list.size();
            }

            @Override
            public View getView(int position,View convertView) {
                ImageView bannerIv = null;
                if (convertView != null){
                    bannerIv = (ImageView) convertView;
                }else {
                    bannerIv = new ImageView(BannerTestActivity.this);
                }

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.TOP;
                bannerIv.setLayoutParams(layoutParams);
                bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
                String url = list.get(position).getBanner_url().getUrl_list().get(0).getUrl();

                ImageManager.getInstance().loadImage(bannerIv,url);
                return bannerIv;
            }

        });

        mBannerViewPager.startLoop();
    }

}
