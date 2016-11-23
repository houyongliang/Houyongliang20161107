package com.bwie.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bwie.test.view.CircleView;

public class MainActivity extends AppCompatActivity {

    private CircleView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*获取视图控件*/
        cv = (CircleView) findViewById(R.id.cv);

        Button bt= (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        /*设置圆形视图的点击事件*/
        cv.setOnItemSelectListener(new CircleView.OnItemSelectListener() {
            @Override
            public void onItemSelect(int index, String indexString) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
