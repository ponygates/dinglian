package com.app.dinglian.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dinglian.R;
import com.app.dinglian.activity.CustomerEditActivity;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 签单页面
 */
public class SignFragment extends Fragment {
    private RecyclerView recyclerView;
    private SignAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Customer> customerList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(getContext());

        // 初始化视图
        recyclerView = view.findViewById(R.id.sign_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化数据
        customerList = new ArrayList<>();
        adapter = new SignAdapter(customerList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomers();
    }

    /**
     * 加载已签单客户
     */
    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(databaseHelper.getSignedCustomers());
        adapter.notifyDataSetChanged();
    }

    /**
     * Adapter
     */
    private class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {
        private List<Customer> customers;

        public SignAdapter(List<Customer> customers) {
            this.customers = customers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_customer_sign, parent, false);
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
            TextView productName;
            TextView contractAmount;
            TextView paidAmount;
            TextView remainingAmount;
            TextView contractCode;
            TextView signDate;
            ImageView editButton;

            public ViewHolder(View itemView) {
                super(itemView);
                customerName = itemView.findViewById(R.id.customer_name);
                productName = itemView.findViewById(R.id.product_name);
                contractAmount = itemView.findViewById(R.id.contract_amount);
                paidAmount = itemView.findViewById(R.id.paid_amount);
                remainingAmount = itemView.findViewById(R.id.remaining_amount);
                contractCode = itemView.findViewById(R.id.contract_code);
                signDate = itemView.findViewById(R.id.sign_date);
                editButton = itemView.findViewById(R.id.edit_button);
            }

            public void bind(final Customer customer) {
                // 设置客户信息
                customerName.setText(customer.getName() != null ? customer.getName() : "");
                productName.setText(customer.getProductName() != null ? customer.getProductName() : "-");

                // 设置金额
                contractAmount.setText(String.format(Locale.getDefault(), "¥%.2f", customer.getContractAmount()));
                paidAmount.setText(String.format(Locale.getDefault(), "¥%.2f", customer.getPaidAmount()));
                remainingAmount.setText(String.format(Locale.getDefault(), "¥%.2f", customer.getRemainingAmount()));

                // 设置合同和日期信息
                String code = customer.getContractCode() != null ? customer.getContractCode() : "-";
                contractCode.setText("合同编号：" + code);
                String date = customer.getSignDate() != null ? customer.getSignDate() : "-";
                signDate.setText("签约日期：" + date);

                // 点击卡片或编辑按钮跳转编辑
                View.OnClickListener editClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CustomerEditActivity.class);
                        intent.putExtra("customer_id", customer.getId());
                        startActivity(intent);
                    }
                };
                itemView.setOnClickListener(editClickListener);
                editButton.setOnClickListener(editClickListener);
            }
        }
    }
}
