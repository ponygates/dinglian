package com.app.dinglian.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.dinglian.R;
import com.app.dinglian.database.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        databaseHelper = new DatabaseHelper(this);

        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 修改密码
        LinearLayout llChangePassword = findViewById(R.id.ll_change_password);
        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        // 导出数据
        LinearLayout llExportData = findViewById(R.id.ll_export_data);
        llExportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "导出功能开发中", Toast.LENGTH_SHORT).show();
            }
        });

        // 导入数据
        LinearLayout llImportData = findViewById(R.id.ll_import_data);
        llImportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "导入功能开发中", Toast.LENGTH_SHORT).show();
            }
        });

        // 清除数据
        LinearLayout llClearData = findViewById(R.id.ll_clear_data);
        llClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearDataDialog();
            }
        });

        // 使用说明
        LinearLayout llHelpDoc = findViewById(R.id.ll_help_doc);
        llHelpDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                intent.putExtra("type", "help");
                startActivity(intent);
            }
        });

        // 反馈建议
        LinearLayout llFeedback = findViewById(R.id.ll_feedback);
        llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        // 关于
        LinearLayout llAbout = findViewById(R.id.ll_about);
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                intent.putExtra("type", "about");
                startActivity(intent);
            }
        });
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_password_setup, null);
        final EditText passwordEditText = dialogView.findViewById(R.id.password_edit_text);
        final EditText confirmPasswordEditText = dialogView.findViewById(R.id.confirm_password_edit_text);
        final android.widget.CheckBox rememberPasswordCheckBox = dialogView.findViewById(R.id.remember_password_check_box);
        rememberPasswordCheckBox.setVisibility(View.GONE);

        new AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = passwordEditText.getText().toString();
                        String confirmPassword = confirmPasswordEditText.getText().toString();

                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(SettingsActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!password.equals(confirmPassword)) {
                            Toast.makeText(SettingsActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        databaseHelper.setPassword(password, databaseHelper.isRememberPassword());
                        Toast.makeText(SettingsActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 显示清除数据对话框
     */
    private void showClearDataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确认清除")
                .setMessage("确定要清除所有数据吗？此操作不可恢复！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.clearAllCustomers();
                        Toast.makeText(SettingsActivity.this, "数据已清除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 发送反馈邮件
     */
    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:xponey@sina.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "定联管理系统反馈");
        try {
            startActivity(Intent.createChooser(intent, "请选择邮件应用"));
        } catch (Exception e) {
            Toast.makeText(this, "未找到邮件应用，请手动发送邮件至 xponey@sina.com", Toast.LENGTH_LONG).show();
        }
    }
}
