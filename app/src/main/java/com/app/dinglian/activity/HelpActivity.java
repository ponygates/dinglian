package com.app.dinglian.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dinglian.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String type = getIntent().getStringExtra("type");

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvContent = findViewById(R.id.tv_content);
        ImageView ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if ("about".equals(type)) {
            tvTitle.setText("关于");
            tvContent.setText(getAboutContent());
        } else {
            tvTitle.setText("使用说明");
            tvContent.setText(getHelpContent());
        }
    }

    /**
     * 获取使用说明内容
     */
    private String getHelpContent() {
        return "一、客户分级\n\n" +
                "A级（挺你到底）：已送保险建议书，需每周联系一次\n\n" +
                "B级（好友）：可聊个人心事，需每3周联系一次\n\n" +
                "C级（三分熟）：曾经很熟但许久未见，需每6周联系一次\n\n" +
                "D级（认识有联络方式）：需每8周联系一次\n\n\n" +
                "二、主要功能\n\n" +
                "1. 跟踪页面：显示需要联系的客户，可直接拨打或复制微信\n\n" +
                "2. 管理页面：查看、添加、编辑、删除所有客户信息\n\n" +
                "3. 签单页面：查看已签单客户的详细信息\n\n" +
                "4. 统计页面：查看客户统计数据和分布图表\n\n\n" +
                "三、操作说明\n\n" +
                "1. 添加客户：在管理页面点击添加按钮，填写客户信息\n\n" +
                "2. 导入联系人：在编辑客户页面，姓名旁边的图标可导入手机联系人\n\n" +
                "3. 更新跟进：联系客户后，在客户详情中更新跟进日期\n\n" +
                "4. 搜索客户：在主页搜索框输入姓名或电话可快速查找\n\n\n" +
                "四、数据安全\n\n" +
                "请妥善保管您的数据库密码，忘记密码可能导致数据无法访问。\n";
    }

    /**
     * 获取关于内容
     */
    private String getAboutContent() {
        return "定联管理系统\n\n" +
                "版本：1.0\n\n" +
                "新华保险 司马君\n\n" +
                "® 基于张瀞心《定联》开发\n\n\n" +
                "这是一款专门为保险代理人设计的客户定期联系管理工具，帮助您维护良好的客户关系。\n\n\n" +
                "如有问题或建议，请通过\"反馈建议\"功能联系我们。\n";
    }
}
