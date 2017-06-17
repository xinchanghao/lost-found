package hk.path.lf.activity;
/**
 * 主界面
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import hk.path.lf.R;
import hk.path.lf.fragment.FoundFragment;
import hk.path.lf.fragment.HomeFragment;
import hk.path.lf.fragment.LostFragment;
///
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppContext appContext;
    private NavigationView navigationView;
    private Button navButton;
    private TextView navSID;
    private TextView navName;
    private DrawerLayout drawer;
    private View headerView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private LostFragment lostFragment;
    private FoundFragment foundFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (AppContext) getApplication();
        setContentView(R.layout.activity_main);
        findView();

        //全透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        init();
    }

    private void findView() {
        fragmentManager = getFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navButton = (Button) headerView.findViewById(R.id.navButton);
        navSID = (TextView) headerView.findViewById(R.id.navSID);
        navName = (TextView) headerView.findViewById(R.id.navName);
    }

    private void init() {
        homeFragment = new HomeFragment();
        lostFragment = new LostFragment();
        foundFragment = new FoundFragment();

        //Toolbar 设置标题
        toolbar.setTitle(R.string.app_name_long);
        setSupportActionBar(toolbar);

        //右下角按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "敬请期待", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        //汉堡按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, new HomeFragment());
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        //判定登录 设置nav
        if (appContext.isLogined()) {
            navSID.setText(appContext.getSID());
            navName.setText(appContext.getName());
            navButton.setText(R.string.logout);
            navButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appContext.setLogined(false);
                    onResume();
                }
            });
        } else {
            navButton.setText(R.string.loginnow);
            navSID.setText(R.string.nav_tip);
            navName.setText(R.string.nav_welcome);
            navButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }
        getAllChildViews(getWindow().getDecorView(), 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    private String getNone(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
            sb.append(" ");
        return sb.toString();
    }

    private void getAllChildViews(View view, int len) {
        Log.d("asd", getNone(len) + "View " + view.getClass().getName() + " h" + view.getMeasuredHeight()
                + " w" + view.getMeasuredWidth());
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            //XposedBridge.log(String.valueOf(vp.getChildCount()));
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                getAllChildViews(viewchild, len + 1);
            }

        } else if (view instanceof ViewStub) {
            ViewStub vs = (ViewStub) view;
            //vs.getv
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, homeFragment);
            transaction.commit();
            toolbar.setTitle(R.string.app_name_long);
        } else if (id == R.id.nav_lost) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, lostFragment);
            transaction.commit();
            toolbar.setTitle(R.string.ILost);
        } else if (id == R.id.nav_found) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, foundFragment);
            transaction.commit();
            toolbar.setTitle(R.string.IFound);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
