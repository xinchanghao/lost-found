package edu.fjnu.cse.lostandfound.activity;
/**
 * 主界面
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
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

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.fragment.CardFragment;
import edu.fjnu.cse.lostandfound.fragment.FoundFragment;
import edu.fjnu.cse.lostandfound.fragment.HomeFragment;
import edu.fjnu.cse.lostandfound.fragment.LostFragment;
import edu.fjnu.cse.lostandfound.fragment.MyFragment;
import edu.fjnu.cse.lostandfound.fragment.SearchFragment;
import edu.fjnu.cse.lostandfound.fragment.VoiceFragment;

import static edu.fjnu.cse.lostandfound.R.styleable.NavigationView;

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
    private CardFragment cardFragment;
    private SearchFragment searchFragment;
    private MyFragment myFragment;
    private VoiceFragment voiceFragment;
    private ActionBarDrawerToggle toggle;
    private PendingIntent pi = null;
    private boolean isNFC_support = false;
    private IntentFilter tagDetected = null;
    private int currentFragment = 1;
    private int lastFragment = 1;
    NfcAdapter nfcAdapter;

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
        cardFragment = new CardFragment();
        voiceFragment = new VoiceFragment();
        myFragment = new MyFragment();
        searchFragment = new SearchFragment();
        //Toolbar 设置标题
        toolbar.setTitle(R.string.app_name_long);
        setSupportActionBar(toolbar);

        //右下角按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "敬请期待", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (appContext.isLogined()) {
                    Intent intent = new Intent(MainActivity.this, PublishActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        //汉堡按钮
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toggle.isDrawerIndicatorEnabled())
                    onBackPressed();
            }
        });
        toggle.syncState();


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, new HomeFragment());
        transaction.commit();
    }

    private void startNFC_Listener() {
        // 开始监听NFC设备是否连接，如果连接就发pi意图
        nfcAdapter.enableForegroundDispatch(this, pi,
                new IntentFilter[]{tagDetected}, null);
    }

    private void stopNFC_Listener() {
        // 停止监听NFC设备是否连接
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isNFC_support) {
            // 当前Activity如果不在手机的最前端，就停止NFC设备连接的监听
            stopNFC_Listener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NFCSupport();
        if (isNFC_support) {
            startNFC_Listener();
        }
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
//        getAllChildViews(getWindow().getDecorView(), 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            getSupportFragmentManager().popBackStack();
            toggle.setDrawerIndicatorEnabled(true);
            currentFragment = lastFragment;
            if (currentFragment == 1) {
                toolbar.setTitle(R.string.app_name_long);
            } else if (currentFragment == 2) {
                toolbar.setTitle(R.string.ILost);
            } else if (currentFragment == 3) {
                toolbar.setTitle(R.string.IFound);
            } else if (currentFragment == 4) {
                toolbar.setTitle("查询餐卡");
            } else if (currentFragment == 5) {
                toolbar.setTitle("语音寻物");
            } else if (currentFragment == 6) {
                toolbar.setTitle("我发布的信息");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_ADAPTER_STATE_CHANGED.equals(intent.getAction())) {
            changeToCard();
            cardFragment.readFromCard(intent);
//            readFromCard(intent);
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            changeToCard();
            cardFragment.readFromCard(intent);
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            changeToCard();
            cardFragment.readFromCard(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
//                searchNote(string);
                appContext.setSearchText(string);
                changeToSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String string) {
//                searchNote(string);
//                appContext.setSearchText(string);
//                changeToSearch();
                return true;
            }
        });
        return true;
    }

    public void NFCSupport() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
//            setStatus(2);
            isNFC_support = false;
            appContext.setNFC_support(false);
        } else {
            isNFC_support = true;
            appContext.setNFC_support(true);
            if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
//                setStatus(2);
                appContext.setNfcEnabled(false);
            } else {
                appContext.setNfcEnabled(true);
//                if (textView.getText().equals("请打开NFC功能") || textView.getText().equals("")) {
//                    textView.setTextColor(Color.rgb(41, 128, 185));
//                    textView.setText("等待接触卡片...");
//                }
            }
            if (isNFC_support) {
                init_NFC();
            }
        }
    }

    private void init_NFC() {
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 新建IntentFilter，使用的是第二种的过滤机制
        tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
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
        fab.setVisibility(View.VISIBLE);
        if (id == R.id.nav_home) {
            lastFragment = currentFragment;
            currentFragment = 1;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, homeFragment);
            transaction.commit();
            toolbar.setTitle(R.string.app_name_long);
        } else if (id == R.id.nav_lost) {
            lastFragment = currentFragment;
            currentFragment = 2;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, lostFragment);
            transaction.commit();
            toolbar.setTitle(R.string.ILost);
        } else if (id == R.id.nav_found) {
            lastFragment = currentFragment;
            currentFragment = 3;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, foundFragment);
            transaction.commit();
            toolbar.setTitle(R.string.IFound);
        } else if (id == R.id.nav_card) {
            changeToCard();
        } else if (id == R.id.nav_voice) {
            lastFragment = currentFragment;
            currentFragment = 5;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, voiceFragment);
            transaction.commit();
            toolbar.setTitle("语音寻物");
            fab.setVisibility(View.GONE);
        } else if (id == R.id.nav_my) {
            if (appContext.isLogined()) {
                lastFragment = currentFragment;
                currentFragment = 6;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.content_main, myFragment);
                transaction.commit();
                toolbar.setTitle("我发布的信息");
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeToSearch() {
        fab.setVisibility(View.VISIBLE);
        if (currentFragment == 7) {
            searchFragment.initData();
            toolbar.setTitle("查询 “" + appContext.getSearchText() + "”");
        } else {
            lastFragment = currentFragment;
            currentFragment = 7;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.content_main, searchFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            toolbar.setTitle("查询 “" + appContext.getSearchText() + "”");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void changeToCard() {
        lastFragment = currentFragment;
        currentFragment = 4;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.content_main, cardFragment);
        transaction.commit();
        toolbar.setTitle("查询餐卡");
        navigationView.getMenu().getItem(3).setChecked(true);
        fab.setVisibility(View.GONE);
    }
}
