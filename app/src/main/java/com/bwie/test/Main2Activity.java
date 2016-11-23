package com.bwie.test;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bwie.test.bean.Bean;
import com.bwie.test.utils.XListView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.tag;


public class Main2Activity extends AppCompatActivity {
    private final int IsFirstLoad=0;
    private final int IsUpReflashLoad=1;
    private final int  IsDownReflashLoad=2;

    private String TAG="Main2Activity";
    private int page = 1;
    private int pagesize = 10;
    private String url = getUrl(page,pagesize);
    private XListView xlv;
    private MyBaseAdapter mAdapter;
    HttpUtils httpUtils;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what=msg.what;
            switch (what) {
                case IsFirstLoad:
                    Toast.makeText(Main2Activity.this, "成功" , Toast.LENGTH_SHORT).show();
                    final List<Bean.ResultBean.DataBean> data = (List<Bean.ResultBean.DataBean>) msg.obj;
                   /*设置下拉加载和上拉刷新*/
                    xlv.setPullLoadEnable(true);
                    xlv.setPullRefreshEnable(true);
                    /*设置设配器，及对适配器判断*/
                    initXLV(data,true);
                    /*设置监听*/
                    xlv.setXListViewListener(new XListView.IXListViewListener() {
                        @Override
                        public void onRefresh() {
                            /*数据刷新 请求数据*/
                            page++;
                            url=getUrl(page,pagesize);
                            initData(1);


                        }

                        @Override
                        public void onLoadMore() {
                            /*加载更多  请求数据*/
                            page++;
                            url=getUrl(page,pagesize);
                            initData(0);
//                            initXLV(data,true);
                        }
                    });

                    break;
                /*是否 上拉刷新*/
                case IsUpReflashLoad:
                    final List<Bean.ResultBean.DataBean> data1 = (List<Bean.ResultBean.DataBean>) msg.obj;

                    xlv.setPullLoadEnable(true);
                    xlv.setPullRefreshEnable(true);
                    initXLV(data1,false);
                    break;
            }

        }
    };
    private OkHttpClient okHttpClient;
    public String getUrl(int page,int pagesize){
return "http://japi.juhe.cn/joke/content/list.from?key= 874ed931559ba07aade103eee279bb37" +
        " &page=" + page + "&pagesize=" + pagesize + "&sort=asc&time=1418745237";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        httpUtils =new HttpUtils();
        initView();
        initData(0);
    }
/*网络请求数据 采用 okhttp*/
    private void initData(final int Tag) {
        if(okHttpClient==null){
            okHttpClient = new OkHttpClient.Builder().build();
        }


        Request request = new Request.Builder().url(url).build();
    /*采用 okhttp 异步加载*/
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Main2Activity.this, "网络加载失败。。", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Toast.makeText(Main2Activity.this, "成功" , Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onResponse: "+response.isSuccessful() );
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.e(TAG, "json: "+json );

                    try {
                        /*数据解析*/
                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String content = jsonObject1.getString("content");
                            Log.e(TAG, "data: "+"I="+i+"data[i]"+ content);
                        }
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*数据转为对象处理*/
                    Bean bean = new Gson().fromJson(json, Bean.class);
                    Bean.ResultBean result = bean.getResult();

                    Log.e(TAG, "json: "+json );
                    final List<Bean.ResultBean.DataBean> data = result.getData();
                    /*数据发送主线程处理*/
                    Message msg=Message.obtain();
                    msg.obj=data;
                    msg.what=Tag;
                    mHandler.sendMessageDelayed(msg,2000);
                }

            }
        });



    }

    private void initXLV(List<Bean.ResultBean.DataBean> list,Boolean isAddList) {
        if (mAdapter == null) {
            mAdapter = new MyBaseAdapter(Main2Activity.this, list);
            xlv.setAdapter(mAdapter);
        } else {
            if(isAddList){
                /*加载更多*/
                mAdapter.addList(list);
            }else{
                /*刷新数据*/
                mAdapter.reflashList(list);
            }

        }
        /*停止刷新，停止加载更多*/
        xlv.stopRefresh();
        xlv.stopLoadMore();

    }
/*获取控件*/
    private void initView() {
        xlv = (XListView) findViewById(R.id.xlv);
    }
}
