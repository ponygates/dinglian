package com.app.dinglian.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dinglian.R;
import com.app.dinglian.activity.MainActivity;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 跟踪页面
 */
public class TrackFragment extends Fragment {
    private RecyclerView recyclerView;
    private TrackAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Customer> customerList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(getContext());

        // 初始化视图
        recyclerView = view.findViewById(R.id.track_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化数据
        customerList = new ArrayList<>();
        adapter = new TrackAdapter(customerList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomers();
    }

    /**
     * 加载需要联系的客户
     */
    private void loadCustomers() {
        List<Customer> allCustomers = databaseHelper.getAllCustomers();
        customerList.clear();

        for (Customer customer : allCustomers) {
            if (isNeedContact(customer)) {
                customerList.add(customer);
            }
        }

        // 按等级排序：A级 > B级 > C级 > D级
        sortByLevel(customerList);

        adapter.notifyDataSetChanged();
    }

    /**
     * 检查是否需要联系
     */
    private boolean isNeedContact(Customer customer) {
        String followDateStr = customer.getFollowDate();
        if (followDateStr == null || followDateStr.isEmpty()) {
            // 没有跟进日期，默认为需要联系
            return true;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date followDate = sdf.parse(followDateStr);
            Calendar followCalendar = Calendar.getInstance();
            followCalendar.setTime(followDate);

            // 计算当前日期
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            // 计算间隔天数
            long diffInMillis = today.getTimeInMillis() - followCalendar.getTimeInMillis();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

            // 获取等级对应的周数
            int weeks = customer.getContactIntervalWeeks();
            long days = weeks * 7;

            return diffInDays >= days;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 按等级排序
     */
    private void sortByLevel(List<Customer> customers) {
        java.util.Collections.sort(customers, new java.util.Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                // A < B < C < D
                int level1 = getLevelOrder(c1.getLevel());
                int level2 = getLevelOrder(c2.getLevel());
                return level1 - level2;
            }
        });
    }

    /**
     * 获取等级顺序
     */
    private int getLevelOrder(String level) {
        if (level == null) return 4;
        switch (level) {
            case "A": return 1;
            case "B": return 2;
            case "C": return 3;
            case "D": return 4;
            default: return 4;
        }
    }

    /**
     * Adapter
     */
    private class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
        private List<Customer> customers;

        public TrackAdapter(List<Customer> customers) {
            this.customers = customers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_customer_card, parent, false);
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
            LinearLayout levelBar;
            TextView levelText;
            TextView customerName;
            TextView customerWechat;
            TextView customerPhone;
            LinearLayout wechatButton;
            LinearLayout phoneButton;

            public ViewHolder(View itemView) {
                super(itemView);
                levelBar = itemView.findViewById(R.id.level_bar);
                levelText = itemView.findViewById(R.id.level_text);
                customerName = itemView.findViewById(R.id.customer_name);
                customerWechat = itemView.findViewById(R.id.customer_wechat);
                customerPhone = itemView.findViewById(R.id.customer_phone);
                wechatButton = itemView.findViewById(R.id.wechat_button);
                phoneButton = itemView.findViewById(R.id.phone_button);
            }

            public void bind(final Customer customer) {
                // 设置等级颜色
                int levelColor = getLevelColor(customer.getLevel());
                levelBar.setBackgroundColor(levelColor);
                customerName.setTextColor(levelColor);

                // 设置等级文本
                String levelStr = getLevelString(customer.getLevel());
                levelText.setText(levelStr);

                // 设置客户信息
                customerName.setText(customer.getName() != null ? customer.getName() : "");
                customerWechat.setText(customer.getRemark() != null ? customer.getRemark() : "");
                customerPhone.setText(customer.getPhone() != null ? customer.getPhone() : "");

                // 点击卡片跳转详情
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).navigateToCustomer(customer.getId());
                        }
                    }
                });

                // 微信联系
                wechatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWechat(customer);
                    }
                });

                // 电话联系
                phoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makePhoneCall(customer);
                    }
                });
            }

            /**
             * 打开微信
             */
            private void openWechat(Customer customer) {
                String wechat = customer.getRemark() != null ? customer.getRemark() : customer.getName();
                if (wechat == null || wechat.isEmpty()) {
                    Toast.makeText(getContext(), "没有微信号", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 复制到剪贴板
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("wechat", wechat);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();

                // 尝试打开微信
                try {
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    if (intent != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "请打开微信粘贴查找", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "请打开微信粘贴查找", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 拨打电话
             */
            private void makePhoneCall(Customer customer) {
                String phone = customer.getPhone();
                if (phone == null || phone.isEmpty()) {
                    Toast.makeText(getContext(), "没有电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 打开拨号界面
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
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
        }
    }
}
