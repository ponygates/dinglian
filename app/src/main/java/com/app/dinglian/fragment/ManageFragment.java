package com.app.dinglian.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dinglian.R;
import com.app.dinglian.activity.CustomerEditActivity;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理页面
 */
public class ManageFragment extends Fragment {
    private RecyclerView recyclerView;
    private ManageAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Customer> customerList;
    private List<Customer> filteredList;
    private Button addButton;
    private int highlightCustomerId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(getContext());

        // 初始化视图
        recyclerView = view.findViewById(R.id.manage_recycler_view);
        addButton = view.findViewById(R.id.add_customer_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化数据
        customerList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ManageAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // 设置添加按钮点击事件
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CustomerEditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomers();
    }

    /**
     * 加载客户
     */
    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(databaseHelper.getAllCustomers());
        filteredList.clear();
        filteredList.addAll(customerList);
        adapter.notifyDataSetChanged();

        // 如果有需要高亮的客户
        if (highlightCustomerId != -1) {
            scrollToCustomer(highlightCustomerId);
            highlightCustomerId = -1;
        }
    }

    /**
     * 搜索客户
     */
    public void searchCustomers(String keyword) {
        filteredList.clear();
        if (keyword == null || keyword.isEmpty()) {
            filteredList.addAll(customerList);
        } else {
            for (Customer customer : customerList) {
                String name = customer.getName() != null ? customer.getName() : "";
                String phone = customer.getPhone() != null ? customer.getPhone() : "";
                if (name.toLowerCase().contains(keyword.toLowerCase()) ||
                        phone.contains(keyword)) {
                    filteredList.add(customer);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 高亮并滚动到指定客户
     */
    public void highlightCustomer(int customerId) {
        highlightCustomerId = customerId;
        if (isResumed()) {
            loadCustomers();
        }
    }

    /**
     * 滚动到指定客户
     */
    private void scrollToCustomer(int customerId) {
        for (int i = 0; i < filteredList.size(); i++) {
            if (filteredList.get(i).getId() == customerId) {
                recyclerView.scrollToPosition(i);
                break;
            }
        }
    }

    /**
     * Adapter
     */
    private class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ViewHolder> {
        private List<Customer> customers;

        public ManageAdapter(List<Customer> customers) {
            this.customers = customers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_customer_manage, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Customer customer = customers.get(position);
            holder.bind(customer);
        }

        @Override
        public int getItemCount() {
            return customers.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView customerName;
            TextView customerLevel;
            TextView customerPhone;
            TextView customerStatus;
            ImageView editButton;
            ImageView deleteButton;

            public ViewHolder(View itemView) {
                super(itemView);
                customerName = itemView.findViewById(R.id.customer_name);
                customerLevel = itemView.findViewById(R.id.customer_level);
                customerPhone = itemView.findViewById(R.id.customer_phone);
                customerStatus = itemView.findViewById(R.id.customer_status);
                editButton = itemView.findViewById(R.id.edit_button);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }

            public void bind(final Customer customer) {
                // 设置客户信息
                customerName.setText(customer.getName() != null ? customer.getName() : "");
                customerPhone.setText(customer.getPhone() != null ? customer.getPhone() : "");

                // 设置等级
                int levelColor = getLevelColor(customer.getLevel());
                String levelStr = getLevelString(customer.getLevel());
                customerLevel.setText(levelStr);
                customerLevel.setBackgroundColor(levelColor);

                // 设置跟进状态
                customerStatus.setText(getStatusString(customer.getStatus()));

                // 点击卡片跳转编辑
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CustomerEditActivity.class);
                        intent.putExtra("customer_id", customer.getId());
                        startActivity(intent);
                    }
                });

                // 编辑按钮
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CustomerEditActivity.class);
                        intent.putExtra("customer_id", customer.getId());
                        startActivity(intent);
                    }
                });

                // 删除按钮
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteConfirmDialog(customer);
                    }
                });
            }

            /**
             * 显示删除确认对话框
             */
            private void showDeleteConfirmDialog(final Customer customer) {
                new AlertDialog.Builder(getContext())
                        .setTitle("确认删除")
                        .setMessage("确定要删除客户 " + customer.getName() + " 吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.deleteCustomer(customer.getId());
                                loadCustomers();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }

            /**
             * 获取等级颜色
             */
            private int getLevelColor(String level) {
                if (level == null) return getResources().getColor(R.color.level_d);
                switch (level) {
                    case "A": return getResources().getColor(R.color.level_a);
                    case "B": return getResources().getColor(R.color.level_b);
                    case "C": return getResources().getColor(R.color.level_c);
                    case "D": return getResources().getColor(R.color.level_d);
                    default: return getResources().getColor(R.color.level_d);
                }
            }

            /**
             * 获取等级字符串
             */
            private String getLevelString(String level) {
                if (level == null) return "D级";
                switch (level) {
                    case "A": return "A级";
                    case "B": return "B级";
                    case "C": return "C级";
                    case "D": return "D级";
                    default: return "D级";
                }
            }

            /**
             * 获取状态字符串
             */
            private String getStatusString(String status) {
                if (status == null) return "接触中";
                switch (status) {
                    case "contacting": return "接触中";
                    case "promoting": return "促成中";
                    case "signed": return "已签单";
                    case "gaveup": return "已放弃";
                    default: return status;
                }
            }
        }
    }
}
