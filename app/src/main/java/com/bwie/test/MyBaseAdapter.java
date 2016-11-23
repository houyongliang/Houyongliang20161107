package com.bwie.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwie.test.bean.Bean;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by mis on 2016/11/7.
 */

public class MyBaseAdapter extends BaseAdapter {
    /*适配器设置  内部定义刷新效果好*/
    private  Context context;
    /*list 为使用的*/
    private   List<Bean.ResultBean.DataBean>  list=new ArrayList<Bean.ResultBean.DataBean>();
    /*为外面加载的数据 */
    private   List<Bean.ResultBean.DataBean>  listall;
    public MyBaseAdapter(Context context, List<Bean.ResultBean.DataBean>  listall){
        list.clear();
        this.context=context;
        this.listall=listall;
        list.addAll(listall);
}
    public void addList(List<Bean.ResultBean.DataBean>  listall){
        this.listall=listall;
        list.addAll(listall);
        notifyDataSetChanged();
    }
    public void reflashList(List<Bean.ResultBean.DataBean>  listall){
        list.clear();
        this.listall=listall;
        list.addAll(listall);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Bean.ResultBean.DataBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=View.inflate(context,R.layout.xlv_item,null);
        TextView tv= (TextView) view.findViewById(R.id.tv_item);
        Bean.ResultBean.DataBean item = getItem(i);
        tv.setText("positon"+i+"......"+item.getContent());
        return view;
    }
}
