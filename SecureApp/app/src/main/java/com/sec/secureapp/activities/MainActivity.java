package com.sec.secureapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sec.secureapp.R;
import com.sec.secureapp.databinding.ActivityMainBinding;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.general.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    //variable to store auctions
    ArrayList<HashMap<String, String>> auctionList;

    // wait for answer from server with data
    AuctionReceiver runningAuctionReceiver;
    AuctionReceiver openAuctionReceiver;

    // string with json got from server
    String runningAuctions = "";
    String openAuctions = "";
    String finishedAuctions = "";

    // the tab titles
    String[] tabTitles = {"OPEN", "RUNNING"};

    ViewPagerAdapter adapter;

    // progress dialog showing until data is fetched
    MaterialDialog progressDialog;

    int received_open = 0;
    int received_running = 0;

    int tab_position = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        sendMessages();
        createDialog();
        //createReceivers();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        // change actionbar title
        binding.toolbar.setTitle("Auctions");
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(this);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //binding.fab.hide(); // Hide animation
                        break;
                    /*case ViewPager.SCROLL_STATE_IDLE:
                        switch (binding.viewPager.getCurrentItem()) {
                            case 0:
                                //fragment2.shareFab(null); // Remove FAB from fragment
                                //fragment1.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                            case 1:
                            default:
                                //fragment1.shareFab(null); // Remove FAB from fragment
                                //fragment2.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                        }
                        binding.fab.show(); // Show animation
                        break;*/
                    default:
                        break;
                }
            }
        });

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                tab_position = tab.getPosition();
            }
        });
        waitRefresh();
    }

    // refresh viewpager
    public void refresh() {
        waitRefresh();
        received_open = 0;
        received_running = 0;
        sendMessages();
        //createReceivers();
    }

    private void createReceivers() {
        // filter used for receiver
        IntentFilter filter = new IntentFilter();

        // create a receiver for open auctions and wait response from server
        openAuctionReceiver = new AuctionReceiver();
        filter.addAction(getString(R.string.open_receiver));
        registerReceiver(openAuctionReceiver, filter);

        // create a receiver for running auctions and wait response from server
        runningAuctionReceiver = new AuctionReceiver();
        filter.addAction(getString(R.string.running_receiver));
        registerReceiver(runningAuctionReceiver, filter);
    }

    private void createDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    private void sendMessages() {
        // send message to server for open and running auctions
        new InfoMessage(this, T.OPEN_AUCTIONS, new UserInfo(null, null, null, null, null)).start();
        new InfoMessage(this, T.RUNNING_AUCTIONS, new UserInfo(T.USER_ID, null, null, null, null)).start();
        new InfoMessage(this, T.EXCHANGE, null).start();
        new InfoMessage(this, T.BALANCE, new UserInfo(T.USER_ID, null, null, null, null)).start();
    }

    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabTitles, tabTitles.length, openAuctions, runningAuctions);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent mIntent = new Intent(MainActivity.this, CreateAuctionActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    // options menu top right of screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_profile:
                intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("auctions", openAuctions); //send finished auctions
                this.startActivity(intent);
                break;
            case R.id.action_logout:
                intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void unregisterReceivers() {
        unregisterReceiver(openAuctionReceiver);
        unregisterReceiver(runningAuctionReceiver);
    }

    class AuctionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //TODO: Add finished
            if (intent.getAction() != null && intent.getAction().equals(getString(R.string.open_receiver)) && received_open == 0) {
                openAuctions = intent.getStringExtra("getAuctions");
                received_open++;
            } else if ((intent.getAction() != null && intent.getAction().equals(getString(R.string.running_receiver)) && received_running == 0)) {
                runningAuctions = intent.getStringExtra("getAuctions");
                received_running++;
            }

            if (received_running == 1 && received_open == 1) {

                //unregisterReceivers();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

                int tab = tab_position;
                setupViewPager(binding.viewPager);
                binding.tabLayout.setupWithViewPager(binding.viewPager);//setting tab over viewpager
                binding.tabLayout.getTabAt(tab).select();
            }
        }
    }

    public void waitRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();

                    new MaterialDialog.Builder(MainActivity.this)
                            .title(R.string.error)
                            .content(R.string.went_wrong)
                            .cancelable(false)
                            .positiveText(R.string.refresh)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.cancel();
                                    createDialog();
                                    refresh();
                                }
                            })
                            .negativeText(R.string.cancel)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }

                if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

            }
        }, 5000);
    }
}
