package com.app.dinglian.model;

/**
 * 客户数据模型
 */
public class Customer {
    private int id; // 序号
    private String inputDate; // 录入日期
    private String customerCode; // 客户编码
    private String name; // 客户名称
    private String phone; // 联系方式
    private String address; // 现居地址
    private String level; // 客户等级 (A/B/C/D)
    private String need; // 客户需求
    private String status; // 跟进状态
    private String remark; // 客户备注
    private String followDate; // 跟进日期
    private String followRecord; // 跟进记录
    private String followMethod; // 跟进方式
    private String followResult; // 跟进结果
    private String followRemark; // 跟进备注
    private String signDate; // 签约日期
    private String contractCode; // 合同编码
    private String productName; // 产品名称
    private double contractAmount; // 合同金额
    private double paidAmount; // 支付金额
    private double remainingAmount; // 剩余金额
    private String signPerson; // 签约人
    private String signRemark; // 签约备注

    // 构造函数
    public Customer() {
    }

    public Customer(int id, String inputDate, String customerCode, String name, String phone, 
                   String address, String level, String need, String status, String remark,
                   String followDate, String followRecord, String followMethod,
                   String followResult, String followRemark, String signDate,
                   String contractCode, String productName, double contractAmount,
                   double paidAmount, double remainingAmount, String signPerson, String signRemark) {
        this.id = id;
        this.inputDate = inputDate;
        this.customerCode = customerCode;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.level = level;
        this.need = need;
        this.status = status;
        this.remark = remark;
        this.followDate = followDate;
        this.followRecord = followRecord;
        this.followMethod = followMethod;
        this.followResult = followResult;
        this.followRemark = followRemark;
        this.signDate = signDate;
        this.contractCode = contractCode;
        this.productName = productName;
        this.contractAmount = contractAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.signPerson = signPerson;
        this.signRemark = signRemark;
    }

    // Getter和Setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getInputDate() { return inputDate; }
    public void setInputDate(String inputDate) { this.inputDate = inputDate; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getNeed() { return need; }
    public void setNeed(String need) { this.need = need; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getFollowDate() { return followDate; }
    public void setFollowDate(String followDate) { this.followDate = followDate; }
    public String getFollowRecord() { return followRecord; }
    public void setFollowRecord(String followRecord) { this.followRecord = followRecord; }
    public String getFollowMethod() { return followMethod; }
    public void setFollowMethod(String followMethod) { this.followMethod = followMethod; }
    public String getFollowResult() { return followResult; }
    public void setFollowResult(String followResult) { this.followResult = followResult; }
    public String getFollowRemark() { return followRemark; }
    public void setFollowRemark(String followRemark) { this.followRemark = followRemark; }
    public String getSignDate() { return signDate; }
    public void setSignDate(String signDate) { this.signDate = signDate; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getContractAmount() { return contractAmount; }
    public void setContractAmount(double contractAmount) { this.contractAmount = contractAmount; }
    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public double getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(double remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getSignPerson() { return signPerson; }
    public void setSignPerson(String signPerson) { this.signPerson = signPerson; }
    public String getSignRemark() { return signRemark; }
    public void setSignRemark(String signRemark) { this.signRemark = signRemark; }

    /**
     * 获取等级对应的联系周期（周数）
     */
    public int getContactIntervalWeeks() {
        switch (level) {
            case "A": return 1;
            case "B": return 3;
            case "C": return 6;
            case "D": return 8;
            default: return 8;
        }
    }
}
