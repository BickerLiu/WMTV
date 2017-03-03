package com.bluemobi.wanmen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bluemobi.wanmen.R;


/*帮助界面*/
public class HelpActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    /*点击跳转主界面*/
    public void onClick(View view) {
        if (view.getId() == R.id.imageView_help) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
