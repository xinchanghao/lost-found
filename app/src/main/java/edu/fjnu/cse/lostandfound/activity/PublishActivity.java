package edu.fjnu.cse.lostandfound.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.view.TimePickerDialog;

public class PublishActivity extends BaseActivity implements TimePickerDialog.TimePickerDialogInterface {
    EditText wpost_etime;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findView();
    }

    private void findView() {
        wpost_etime = (EditText) findViewById(R.id.wpost_etime);
        timePickerDialog = new TimePickerDialog(PublishActivity.this);
        wpost_etime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    timePickerDialog.showDatePickerDialog();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //System.exit(0);
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void positiveListener() {
        wpost_etime.setText(timePickerDialog.getYear() + "-" + timePickerDialog.getMonth() + "-" + timePickerDialog.getDay());
    }

    @Override
    public void negativeListener() {

    }
}
