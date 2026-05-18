package com.app.dinglian.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.dinglian.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dinglian.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String TABLE_PASSWORD = "password";

    // 列名定义
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INPUT_DATE = "input_date";
    private static final String COLUMN_CUSTOMER_CODE = "customer_code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_NEED = "need";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REMARK = "remark";
    private static final String COLUMN_FOLLOW_DATE = "follow_date";
    private static final String COLUMN_FOLLOW_RECORD = "follow_record";
    private static final String COLUMN_FOLLOW_METHOD = "follow_method";
    private static final String COLUMN_FOLLOW_RESULT = "follow_result";
    private static final String COLUMN_FOLLOW_REMARK = "follow_remark";
    private static final String COLUMN_SIGN_DATE = "sign_date";
    private static final String COLUMN_CONTRACT_CODE = "contract_code";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_CONTRACT_AMOUNT = "contract_amount";
    private static final String COLUMN_PAID_AMOUNT = "paid_amount";
    private static final String COLUMN_REMAINING_AMOUNT = "remaining_amount";
    private static final String COLUMN_SIGN_PERSON = "sign_person";
    private static final String COLUMN_SIGN_REMARK = "sign_remark";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_REMEMBER_PASSWORD = "remember_password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建客户表
        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_INPUT_DATE + " TEXT,"
                + COLUMN_CUSTOMER_CODE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_LEVEL + " TEXT,"
                + COLUMN_NEED + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_REMARK + " TEXT,"
                + COLUMN_FOLLOW_DATE + " TEXT,"
                + COLUMN_FOLLOW_RECORD + " TEXT,"
                + COLUMN_FOLLOW_METHOD + " TEXT,"
                + COLUMN_FOLLOW_RESULT + " TEXT,"
                + COLUMN_FOLLOW_REMARK + " TEXT,"
                + COLUMN_SIGN_DATE + " TEXT,"
                + COLUMN_CONTRACT_CODE + " TEXT,"
                + COLUMN_PRODUCT_NAME + " TEXT,"
                + COLUMN_CONTRACT_AMOUNT + " REAL,"
                + COLUMN_PAID_AMOUNT + " REAL,"
                + COLUMN_REMAINING_AMOUNT + " REAL,"
                + COLUMN_SIGN_PERSON + " TEXT,"
                + COLUMN_SIGN_REMARK + " TEXT"
                + ")";
        db.execSQL(CREATE_CUSTOMERS_TABLE);

        // 创建密码表
        String CREATE_PASSWORD_TABLE = "CREATE TABLE " + TABLE_PASSWORD + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_REMEMBER_PASSWORD + " INTEGER"
                + ")";
        db.execSQL(CREATE_PASSWORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);
        onCreate(db);
    }

    // ==================== 密码管理 ====================
    /**
     * 设置密码
     */
    public void setPassword(String password, boolean rememberPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_REMEMBER_PASSWORD, rememberPassword ? 1 : 0);
        
        // 先检查是否已有记录
        Cursor cursor = db.query(TABLE_PASSWORD, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            db.update(TABLE_PASSWORD, values, COLUMN_ID + "=?", new String[]{"1"});
        } else {
            values.put(COLUMN_ID, 1);
            db.insert(TABLE_PASSWORD, null, values);
        }
        cursor.close();
        db.close();
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PASSWORD, new String[]{COLUMN_PASSWORD}, 
                COLUMN_ID + "=?", new String[]{"1"}, null, null, null);
        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return password;
    }

    /**
     * 检查是否记住密码
     */
    public boolean isRememberPassword() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PASSWORD, new String[]{COLUMN_REMEMBER_PASSWORD}, 
                COLUMN_ID + "=?", new String[]{"1"}, null, null, null);
        boolean remember = false;
        if (cursor.moveToFirst()) {
            remember = cursor.getInt(0) == 1;
        }
        cursor.close();
        db.close();
        return remember;
    }

    /**
     * 检查是否已设置密码
     */
    public boolean hasPassword() {
        return getPassword() != null;
    }

    // ==================== 客户管理 ====================
    /**
     * 添加客户
     */
    public long addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INPUT_DATE, customer.getInputDate());
        values.put(COLUMN_CUSTOMER_CODE, customer.getCustomerCode());
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_PHONE, customer.getPhone());
        values.put(COLUMN_ADDRESS, customer.getAddress());
        values.put(COLUMN_LEVEL, customer.getLevel());
        values.put(COLUMN_NEED, customer.getNeed());
        values.put(COLUMN_STATUS, customer.getStatus());
        values.put(COLUMN_REMARK, customer.getRemark());
        values.put(COLUMN_FOLLOW_DATE, customer.getFollowDate());
        values.put(COLUMN_FOLLOW_RECORD, customer.getFollowRecord());
        values.put(COLUMN_FOLLOW_METHOD, customer.getFollowMethod());
        values.put(COLUMN_FOLLOW_RESULT, customer.getFollowResult());
        values.put(COLUMN_FOLLOW_REMARK, customer.getFollowRemark());
        values.put(COLUMN_SIGN_DATE, customer.getSignDate());
        values.put(COLUMN_CONTRACT_CODE, customer.getContractCode());
        values.put(COLUMN_PRODUCT_NAME, customer.getProductName());
        values.put(COLUMN_CONTRACT_AMOUNT, customer.getContractAmount());
        values.put(COLUMN_PAID_AMOUNT, customer.getPaidAmount());
        values.put(COLUMN_REMAINING_AMOUNT, customer.getRemainingAmount());
        values.put(COLUMN_SIGN_PERSON, customer.getSignPerson());
        values.put(COLUMN_SIGN_REMARK, customer.getSignRemark());
        
        long id = db.insert(TABLE_CUSTOMERS, null, values);
        db.close();
        return id;
    }

    /**
     * 获取所有客户
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS + " ORDER BY " + COLUMN_ID;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Customer customer = cursorToCustomer(cursor);
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return customerList;
    }

    /**
     * 根据ID获取客户
     */
    public Customer getCustomerById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, null, COLUMN_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);
        
        Customer customer = null;
        if (cursor.moveToFirst()) {
            customer = cursorToCustomer(cursor);
        }
        
        cursor.close();
        db.close();
        return customer;
    }

    /**
     * 搜索客户
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS + 
                " WHERE " + COLUMN_NAME + " LIKE ?" +
                " OR " + COLUMN_PHONE + " LIKE ?" +
                " ORDER BY " + COLUMN_ID;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        
        if (cursor.moveToFirst()) {
            do {
                Customer customer = cursorToCustomer(cursor);
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return customerList;
    }

    /**
     * 更新客户
     */
    public int updateCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INPUT_DATE, customer.getInputDate());
        values.put(COLUMN_CUSTOMER_CODE, customer.getCustomerCode());
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_PHONE, customer.getPhone());
        values.put(COLUMN_ADDRESS, customer.getAddress());
        values.put(COLUMN_LEVEL, customer.getLevel());
        values.put(COLUMN_NEED, customer.getNeed());
        values.put(COLUMN_STATUS, customer.getStatus());
        values.put(COLUMN_REMARK, customer.getRemark());
        values.put(COLUMN_FOLLOW_DATE, customer.getFollowDate());
        values.put(COLUMN_FOLLOW_RECORD, customer.getFollowRecord());
        values.put(COLUMN_FOLLOW_METHOD, customer.getFollowMethod());
        values.put(COLUMN_FOLLOW_RESULT, customer.getFollowResult());
        values.put(COLUMN_FOLLOW_REMARK, customer.getFollowRemark());
        values.put(COLUMN_SIGN_DATE, customer.getSignDate());
        values.put(COLUMN_CONTRACT_CODE, customer.getContractCode());
        values.put(COLUMN_PRODUCT_NAME, customer.getProductName());
        values.put(COLUMN_CONTRACT_AMOUNT, customer.getContractAmount());
        values.put(COLUMN_PAID_AMOUNT, customer.getPaidAmount());
        values.put(COLUMN_REMAINING_AMOUNT, customer.getRemainingAmount());
        values.put(COLUMN_SIGN_PERSON, customer.getSignPerson());
        values.put(COLUMN_SIGN_REMARK, customer.getSignRemark());
        
        int rowsAffected = db.update(TABLE_CUSTOMERS, values, COLUMN_ID + "=?", 
                new String[]{String.valueOf(customer.getId())});
        db.close();
        return rowsAffected;
    }

    /**
     * 删除客户
     */
    public void deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, COLUMN_ID + "=?", 
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * 获取已签单的客户
     */
    public List<Customer> getSignedCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS + 
                " WHERE " + COLUMN_STATUS + " = '已签单'" +
                " ORDER BY " + COLUMN_ID;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Customer customer = cursorToCustomer(cursor);
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return customerList;
    }

    /**
     * 清除所有客户数据
     */
    public void clearAllCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, null, null);
        db.close();
    }

    /**
     * 将Cursor转换为Customer对象
     */
    private Customer cursorToCustomer(Cursor cursor) {
        Customer customer = new Customer();
        customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        customer.setInputDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INPUT_DATE)));
        customer.setCustomerCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_CODE)));
        customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
        customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
        customer.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
        customer.setNeed(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEED)));
        customer.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
        customer.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMARK)));
        customer.setFollowDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLLOW_DATE)));
        customer.setFollowRecord(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLLOW_RECORD)));
        customer.setFollowMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLLOW_METHOD)));
        customer.setFollowResult(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLLOW_RESULT)));
        customer.setFollowRemark(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLLOW_REMARK)));
        customer.setSignDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIGN_DATE)));
        customer.setContractCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRACT_CODE)));
        customer.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
        customer.setContractAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CONTRACT_AMOUNT)));
        customer.setPaidAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PAID_AMOUNT)));
        customer.setRemainingAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_AMOUNT)));
        customer.setSignPerson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIGN_PERSON)));
        customer.setSignRemark(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIGN_REMARK)));
        return customer;
    }
}
