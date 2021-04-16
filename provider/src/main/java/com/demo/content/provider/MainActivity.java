package com.demo.content.provider;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 部分国产手机(如：Vivo X60)，需要Provider应用已启动才能获取到Content数据
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        textView.setText("This is Content Provider!");
        setContentView(textView);
    }
}