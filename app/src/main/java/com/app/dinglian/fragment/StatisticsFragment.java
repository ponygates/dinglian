package com.app.dinglian.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dinglian.R;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 统计页面
 */
public class StatisticsFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private TextView totalCountText;
    private TextView needContactCountText;
    private PieChart levelPieChart;
    private PieChart statusPieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(getContext());

        // 初始化视图
        totalCountText = view.findViewById(R.id.total_count);
        needContactCountText = view.findViewById(R.id.need_contact_count);
        levelPieChart = view.findViewById(R.id.level_pie_chart);
        statusPieChart = view.findViewById(R.id.status_pie_chart);

        // 配置图表
        setupPieChart(levelPieChart);
        setupPieChart(statusPieChart);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }

    /**
     * 配置饼图
     */
    private void setupPieChart(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(45f);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(true);

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
    }

    /**
     * 加载统计数据
     */
    private void loadStatistics() {
        List<Customer> allCustomers = databaseHelper.getAllCustomers();

        // 更新统计数字
        totalCountText.setText(String.valueOf(allCustomers.size()));

        // 计算需要联系的客户数
        int needContactCount = 0;
        int countA = 0, countB = 0, countC = 0, countD = 0;
        int statusContacting = 0, statusPromoting = 0, statusSigned = 0, statusGaveup = 0;

        for (Customer customer : allCustomers) {
            if (isNeedContact(customer)) {
                needContactCount++;
            }

            // 统计等级
            String level = customer.getLevel();
            if (level != null) {
                switch (level) {
                    case "A": countA++; break;
                    case "B": countB++; break;
                    case "C": countC++; break;
                    case "D": countD++; break;
                }
            } else {
                countD++;
            }

            // 统计状态
            String status = customer.getStatus();
            if (status != null) {
                switch (status) {
                    case "contacting": statusContacting++; break;
                    case "promoting": statusPromoting++; break;
                    case "signed": statusSigned++; break;
                    case "gaveup": statusGaveup++; break;
                    default: statusContacting++;
                }
            } else {
                statusContacting++;
            }
        }

        needContactCountText.setText(String.valueOf(needContactCount));

        // 设置等级饼图
        setLevelPieChart(countA, countB, countC, countD);

        // 设置状态饼图
        setStatusPieChart(statusContacting, statusPromoting, statusSigned, statusGaveup);
    }

    /**
     * 检查是否需要联系
     */
    private boolean isNeedContact(Customer customer) {
        String followDateStr = customer.getFollowDate();
        if (followDateStr == null || followDateStr.isEmpty()) {
            return true;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date followDate = sdf.parse(followDateStr);
            Calendar followCalendar = Calendar.getInstance();
            followCalendar.setTime(followDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long diffInMillis = today.getTimeInMillis() - followCalendar.getTimeInMillis();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

            int weeks = customer.getContactIntervalWeeks();
            long days = weeks * 7;

            return diffInDays >= days;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 设置等级饼图数据
     */
    private void setLevelPieChart(int countA, int countB, int countC, int countD) {
        List<PieEntry> entries = new ArrayList<>();
        if (countA > 0) entries.add(new PieEntry(countA, "A级"));
        if (countB > 0) entries.add(new PieEntry(countB, "B级"));
        if (countC > 0) entries.add(new PieEntry(countC, "C级"));
        if (countD > 0) entries.add(new PieEntry(countD, "D级"));

        if (entries.isEmpty()) {
            levelPieChart.setNoDataText("暂无数据");
            levelPieChart.invalidate();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(getResources().getColor(R.color.level_a),
                getResources().getColor(R.color.level_b),
                getResources().getColor(R.color.level_c),
                getResources().getColor(R.color.level_d));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.0f", value);
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        levelPieChart.setData(data);
        levelPieChart.invalidate();
    }

    /**
     * 设置状态饼图数据
     */
    private void setStatusPieChart(int contacting, int promoting, int signed, int gaveup) {
        List<PieEntry> entries = new ArrayList<>();
        int needContactCount = contacting + promoting;
        if (needContactCount > 0) entries.add(new PieEntry(needContactCount, "待联系"));
        if (signed > 0) entries.add(new PieEntry(signed, "已签单"));
        if (gaveup > 0) entries.add(new PieEntry(gaveup, "已放弃"));

        if (entries.isEmpty()) {
            statusPieChart.setNoDataText("暂无数据");
            statusPieChart.invalidate();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(getResources().getColor(R.color.level_a),
                getResources().getColor(R.color.level_c),
                getResources().getColor(R.color.gray));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.0f", value);
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        statusPieChart.setData(data);
        statusPieChart.invalidate();
    }
}
