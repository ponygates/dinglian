package com.app.dinglian.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dinglian.R;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerEditActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS_PERMISSION = 1001;
    private static final int REQUEST_PICK_CONTACT = 1002;

    // 数据库
    private DatabaseHelper databaseHelper;
    private int customerId = -1;
    private Customer currentCustomer;

    // 基本信息控件
    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private RadioGroup rgLevel;
    private RadioButton rbLevelA, rbLevelB, rbLevelC, rbLevelD;
    private EditText etNeed;
    private EditText etRemark;
    private ImageView ivImportContacts;

    // 跟进信息控件
    private Spinner spStatus;
    private TextView tvFollowDate;
    private EditText etFollowRecord;
    private Spinner spFollowMethod;
    private EditText etFollowResult;
    private EditText etFollowRemark;

    // 签单信息控件
    private TextView tvSignDate;
    private EditText etContractCode;
    private EditText etProductName;
    private EditText etContractAmount;
    private EditText etPaidAmount;
    private EditText etSignPerson;
    private EditText etSignRemark;

    // 操作按钮
    private Button btnSave;
    private Button btnCancel;

    // 日期选择
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(this);

        // 获取传递的客户ID
        if (getIntent() != null) {
            customerId = getIntent().getIntExtra("customer_id", -1);
        }

        // 初始化视图
        initViews();

        // 初始化数据
        initData();

        // 加载客户信息
        if (customerId != -1) {
            loadCustomerData();
        }
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        // 基本信息
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        rgLevel = findViewById(R.id.rg_level);
        rbLevelA = findViewById(R.id.rb_level_a);
        rbLevelB = findViewById(R.id.rb_level_b);
        rbLevelC = findViewById(R.id.rb_level_c);
        rbLevelD = findViewById(R.id.rb_level_d);
        etNeed = findViewById(R.id.et_need);
        etRemark = findViewById(R.id.et_remark);
        ivImportContacts = findViewById(R.id.iv_import_contacts);

        // 跟进信息
        spStatus = findViewById(R.id.sp_status);
        tvFollowDate = findViewById(R.id.tv_follow_date);
        etFollowRecord = findViewById(R.id.et_follow_record);
        spFollowMethod = findViewById(R.id.sp_follow_method);
        etFollowResult = findViewById(R.id.et_follow_result);
        etFollowRemark = findViewById(R.id.et_follow_remark);

        // 签单信息
        tvSignDate = findViewById(R.id.tv_sign_date);
        etContractCode = findViewById(R.id.et_contract_code);
        etProductName = findViewById(R.id.et_product_name);
        etContractAmount = findViewById(R.id.et_contract_amount);
        etPaidAmount = findViewById(R.id.et_paid_amount);
        etSignPerson = findViewById(R.id.et_sign_person);
        etSignRemark = findViewById(R.id.et_sign_remark);

        // 操作按钮
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // 初始化日历
        calendar = Calendar.getInstance();

        // 设置监听器
        setListeners();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 跟进状态下拉框
        List<String> statusList = new ArrayList<>();
        statusList.add("接触中");
        statusList.add("促成中");
        statusList.add("已签单");
        statusList.add("已放弃");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, statusList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        // 跟进方式下拉框
        List<String> methodList = new ArrayList<>();
        methodList.add("微信");
        methodList.add("上门");
        methodList.add("电话");
        methodList.add("短信");
        methodList.add("邮箱");
        methodList.add("其他");
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, methodList);
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFollowMethod.setAdapter(methodAdapter);

        // 默认选中D级
        rbLevelD.setChecked(true);

        // 设置当前日期为跟进日期
        updateDateDisplay(tvFollowDate, calendar.getTime());
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        // 导入联系人
        ivImportContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactsPermission();
            }
        });

        // 选择跟进日期
        tvFollowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvFollowDate);
            }
        });

        // 选择签约日期
        tvSignDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvSignDate);
            }
        });

        // 保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer();
            }
        });

        // 取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 检查联系人权限
     */
    private void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_CONTACTS}, 
                    REQUEST_CONTACTS_PERMISSION);
        } else {
            pickContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContact();
            } else {
                Toast.makeText(this, "需要联系人权限才能导入", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 选择联系人
     */
    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_CONTACT && resultCode == RESULT_OK && data != null) {
            importContact(data);
        }
    }

    /**
     * 导入联系人信息
     */
    private void importContact(Intent data) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(data.getData(), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // 获取联系人姓名
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);
                if (!TextUtils.isEmpty(name)) {
                    etName.setText(name);
                }

                // 获取联系人电话
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phone = phoneCursor.getString(phoneIndex);
                    if (!TextUtils.isEmpty(phone)) {
                        etPhone.setText(phone);
                    }
                    phoneCursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "导入失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 显示日期选择对话框
     */
    private void showDatePickerDialog(final TextView textView) {
        new DatePickerDialog(this, 
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        updateDateDisplay(textView, c.getTime());
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 更新日期显示
     */
    private void updateDateDisplay(TextView textView, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        textView.setText(sdf.format(date));
    }

    /**
     * 加载客户数据
     */
    private void loadCustomerData() {
        currentCustomer = databaseHelper.getCustomerById(customerId);
        if (currentCustomer != null) {
            // 基本信息
            etName.setText(currentCustomer.getName());
            etPhone.setText(currentCustomer.getPhone());
            etAddress.setText(currentCustomer.getAddress());
            etNeed.setText(currentCustomer.getNeed());
            etRemark.setText(currentCustomer.getRemark());

            // 客户等级
            if ("A".equals(currentCustomer.getLevel())) {
                rbLevelA.setChecked(true);
            } else if ("B".equals(currentCustomer.getLevel())) {
                rbLevelB.setChecked(true);
            } else if ("C".equals(currentCustomer.getLevel())) {
                rbLevelC.setChecked(true);
            } else {
                rbLevelD.setChecked(true);
            }

            // 跟进状态
            String status = currentCustomer.getStatus();
            if (status != null) {
                switch (status) {
                    case "contacting":
                        spStatus.setSelection(0);
                        break;
                    case "promoting":
                        spStatus.setSelection(1);
                        break;
                    case "signed":
                        spStatus.setSelection(2);
                        break;
                    case "gaveup":
                        spStatus.setSelection(3);
                        break;
                }
            }

            // 跟进日期
            if (currentCustomer.getFollowDate() != null) {
                tvFollowDate.setText(currentCustomer.getFollowDate());
            }

            // 跟进记录
            etFollowRecord.setText(currentCustomer.getFollowRecord());

            // 跟进方式
            String method = currentCustomer.getFollowMethod();
            if (method != null) {
                String[] methods = {"微信", "上门", "电话", "短信", "邮箱", "其他"};
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].equals(method)) {
                        spFollowMethod.setSelection(i);
                        break;
                    }
                }
            }

            // 跟进结果和备注
            etFollowResult.setText(currentCustomer.getFollowResult());
            etFollowRemark.setText(currentCustomer.getFollowRemark());

            // 签单信息
            if (currentCustomer.getSignDate() != null) {
                tvSignDate.setText(currentCustomer.getSignDate());
            }
            etContractCode.setText(currentCustomer.getContractCode());
            etProductName.setText(currentCustomer.getProductName());
            if (currentCustomer.getContractAmount() > 0) {
                etContractAmount.setText(String.valueOf(currentCustomer.getContractAmount()));
            }
            if (currentCustomer.getPaidAmount() > 0) {
                etPaidAmount.setText(String.valueOf(currentCustomer.getPaidAmount()));
            }
            etSignPerson.setText(currentCustomer.getSignPerson());
            etSignRemark.setText(currentCustomer.getSignRemark());
        }
    }

    /**
     * 保存客户信息
     */
    private void saveCustomer() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入客户姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(etPhone.getText().toString().trim());
        customer.setAddress(etAddress.getText().toString().trim());
        customer.setNeed(etNeed.getText().toString().trim());
        customer.setRemark(etRemark.getText().toString().trim());

        // 客户等级
        if (rbLevelA.isChecked()) {
            customer.setLevel("A");
        } else if (rbLevelB.isChecked()) {
            customer.setLevel("B");
        } else if (rbLevelC.isChecked()) {
            customer.setLevel("C");
        } else {
            customer.setLevel("D");
        }

        // 跟进状态
        int statusPosition = spStatus.getSelectedItemPosition();
        String[] statusValues = {"contacting", "promoting", "signed", "gaveup"};
        customer.setStatus(statusValues[statusPosition]);

        // 跟进日期
        customer.setFollowDate(tvFollowDate.getText().toString());
        customer.setFollowRecord(etFollowRecord.getText().toString().trim());

        // 跟进方式
        customer.setFollowMethod(spFollowMethod.getSelectedItem().toString());
        customer.setFollowResult(etFollowResult.getText().toString().trim());
        customer.setFollowRemark(etFollowRemark.getText().toString().trim());

        // 签单信息
        customer.setSignDate(tvSignDate.getText().toString());
        customer.setContractCode(etContractCode.getText().toString().trim());
        customer.setProductName(etProductName.getText().toString().trim());
        
        try {
            String contractAmountStr = etContractAmount.getText().toString().trim();
            customer.setContractAmount(!TextUtils.isEmpty(contractAmountStr) 
                    ? Double.parseDouble(contractAmountStr) : 0);
                    
            String paidAmountStr = etPaidAmount.getText().toString().trim();
            customer.setPaidAmount(!TextUtils.isEmpty(paidAmountStr) 
                    ? Double.parseDouble(paidAmountStr) : 0);
                    
            customer.setRemainingAmount(customer.getContractAmount() - customer.getPaidAmount());
        } catch (NumberFormatException e) {
            customer.setContractAmount(0);
            customer.setPaidAmount(0);
            customer.setRemainingAmount(0);
        }
        
        customer.setSignPerson(etSignPerson.getText().toString().trim());
        customer.setSignRemark(etSignRemark.getText().toString().trim());

        // 设置录入日期（如果是新增）
        if (customerId == -1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            customer.setInputDate(sdf.format(new Date()));
            // 生成客户编码
            customer.setCustomerCode("C" + System.currentTimeMillis());
        }

        boolean success;
        if (customerId == -1) {
            // 新增
            long id = databaseHelper.addCustomer(customer);
            success = id != -1;
        } else {
            // 更新
            customer.setId(customerId);
            if (currentCustomer != null) {
                customer.setInputDate(currentCustomer.getInputDate());
                customer.setCustomerCode(currentCustomer.getCustomerCode());
            }
            int rows = databaseHelper.updateCustomer(customer);
            success = rows > 0;
        }

        if (success) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
