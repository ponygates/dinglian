package com.app.dinglian.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dinglian.R;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.fragment.ManageFragment;
import com.app.dinglian.fragment.SignFragment;
import com.app.dinglian.fragment.StatisticsFragment;
import com.app.dinglian.fragment.TrackFragment;
import com.app.dinglian.receiver.ReminderReceiver;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {
    // 导航栏
    private LinearLayout navManage;
    private LinearLayout navTrack;
    private LinearLayout navSign;
    private LinearLayout navStatistics;
    private ImageView navManageIcon;
    private ImageView navTrackIcon;
    private ImageView navSignIcon;
    private ImageView navStatisticsIcon;
    private TextView navManageText;
    private TextView navTrackText;
    private TextView navSignText;
    private TextView navStatisticsText;

    // 搜索框
    private EditText searchEditText;

    // 设置按钮
    private ImageButton settingsButton;

    // Fragments
    private TrackFragment trackFragment;
    private ManageFragment manageFragment;
    private SignFragment signFragment;
    private StatisticsFragment statisticsFragment;
    private Fragment currentFragment;

    // 数据库
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(this);

        // 初始化视图
        initViews();

        // 初始化Fragments
        initFragments();

        // 检查是否需要设置密码
        checkPasswordSetup();

        // 设置搜索框
        setupSearch();

        // 设置定时提醒
        setupDailyReminder();
    }

    /**
     * 设置每日提醒
     */
    private void setupDailyReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // 设置每天上午9点提醒
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 9);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);

        // 如果当前时间已过，则设置为明天
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }

        // 设置定时任务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        // 导航栏
        navManage = findViewById(R.id.nav_manage);
        navTrack = findViewById(R.id.nav_track);
        navSign = findViewById(R.id.nav_sign);
        navStatistics = findViewById(R.id.nav_statistics);
        navManageIcon = findViewById(R.id.nav_manage_icon);
        navTrackIcon = findViewById(R.id.nav_track_icon);
        navSignIcon = findViewById(R.id.nav_sign_icon);
        navStatisticsIcon = findViewById(R.id.nav_statistics_icon);
        navManageText = findViewById(R.id.nav_manage_text);
        navTrackText = findViewById(R.id.nav_track_text);
        navSignText = findViewById(R.id.nav_sign_text);
        navStatisticsText = findViewById(R.id.nav_statistics_text);

        // 搜索框
        searchEditText = findViewById(R.id.search_edit_text);

        // 设置按钮
        settingsButton = findViewById(R.id.settings_button);

        // 设置点击事件
        navManage.setOnClickListener(navClickListener);
        navTrack.setOnClickListener(navClickListener);
        navSign.setOnClickListener(navClickListener);
        navStatistics.setOnClickListener(navClickListener);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化Fragments
     */
    private void initFragments() {
        trackFragment = new TrackFragment();
        manageFragment = new ManageFragment();
        signFragment = new SignFragment();
        statisticsFragment = new StatisticsFragment();

        // 默认显示跟踪页面
        currentFragment = trackFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, trackFragment)
                .commit();
    }

    /**
     * 导航栏点击监听
     */
    private View.OnClickListener navClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment selectedFragment = null;
            int id = v.getId();

            if (id == R.id.nav_manage) {
                selectedFragment = manageFragment;
            } else if (id == R.id.nav_track) {
                selectedFragment = trackFragment;
            } else if (id == R.id.nav_sign) {
                selectedFragment = signFragment;
            } else if (id == R.id.nav_statistics) {
                selectedFragment = statisticsFragment;
            }

            if (selectedFragment != null && selectedFragment != currentFragment) {
                switchFragment(selectedFragment);
                updateNavState(id);
            }
        }
    };

    /**
     * 切换Fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.fragment_container, fragment);
        }
        transaction.hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }

    /**
     * 更新导航栏状态
     */
    private void updateNavState(int selectedId) {
        // 重置所有为未选中状态
        resetNavState();

        // 设置选中状态
        if (selectedId == R.id.nav_manage) {
            navManageIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
            navManageText.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selectedId == R.id.nav_track) {
            navTrackIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
            navTrackText.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selectedId == R.id.nav_sign) {
            navSignIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
            navSignText.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selectedId == R.id.nav_statistics) {
            navStatisticsIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
            navStatisticsText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    /**
     * 重置导航栏状态
     */
    private void resetNavState() {
        navManageIcon.setColorFilter(getResources().getColor(R.color.gray));
        navTrackIcon.setColorFilter(getResources().getColor(R.color.gray));
        navSignIcon.setColorFilter(getResources().getColor(R.color.gray));
        navStatisticsIcon.setColorFilter(getResources().getColor(R.color.gray));
        navManageText.setTextColor(getResources().getColor(R.color.gray));
        navTrackText.setTextColor(getResources().getColor(R.color.gray));
        navSignText.setTextColor(getResources().getColor(R.color.gray));
        navStatisticsText.setTextColor(getResources().getColor(R.color.gray));
    }

    /**
     * 检查是否需要设置密码
     */
    private void checkPasswordSetup() {
        if (!databaseHelper.hasPassword()) {
            showPasswordSetupDialog();
        }
    }

    /**
     * 显示密码设置对话框
     */
    private void showPasswordSetupDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_password_setup, null);
        final EditText passwordEditText = dialogView.findViewById(R.id.password_edit_text);
        final EditText confirmPasswordEditText = dialogView.findViewById(R.id.confirm_password_edit_text);
        final android.widget.CheckBox rememberPasswordCheckBox = dialogView.findViewById(R.id.remember_password_check_box);

        // 默认勾选记住密码
        rememberPasswordCheckBox.setChecked(true);

        new AlertDialog.Builder(this)
                .setTitle(R.string.password_title)
                .setView(dialogView)
                .setMessage(R.string.password_warning)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = passwordEditText.getText().toString();
                        String confirmPassword = confirmPasswordEditText.getText().toString();

                        if (password.isEmpty()) {
                            // 密码为空，不设置
                            return;
                        }

                        if (!password.equals(confirmPassword)) {
                            // 两次密码不一致
                            return;
                        }

                        // 保存密码
                        databaseHelper.setPassword(password, rememberPasswordCheckBox.isChecked());
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 设置搜索框
     */
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 搜索时切换到管理页面并进行搜索
                if (currentFragment == manageFragment) {
                    manageFragment.searchCustomers(s.toString());
                } else {
                    // 先切换到管理页面
                    switchFragment(manageFragment);
                    updateNavState(R.id.nav_manage);
                    // 延迟搜索
                    searchEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            manageFragment.searchCustomers(s.toString());
                        }
                    }, 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 跳转到管理页面并定位到指定客户
     */
    public void navigateToCustomer(int customerId) {
        switchFragment(manageFragment);
        updateNavState(R.id.nav_manage);
        manageFragment.highlightCustomer(customerId);
    }
}
