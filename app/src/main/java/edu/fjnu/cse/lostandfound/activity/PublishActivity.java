package edu.fjnu.cse.lostandfound.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.fjnu.cse.lostandfound.R;

public class PublishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
