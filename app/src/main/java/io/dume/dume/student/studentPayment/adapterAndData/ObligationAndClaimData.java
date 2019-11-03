package io.dume.dume.student.studentPayment.adapterAndData;

import androidx.annotation.Keep;

@Keep
public class ObligationAndClaimData {
    public String stuName;
    public String menaName;
    public String salaryInDemand;
    public String subjectInDemand;
    public float stuRating;
    public float menRating;
    public String stuDP;
    public String menDP;
    public Number dueAmount;
    public Number paidAmount;

    public String startingDate;
    public String finishingDate;
    public String productName;
    public String categoryName;

    public Number salary;

    public ObligationAndClaimData(String stuName, String menaName, String salaryInDemand, String subjectInDemand, float stuRating, float menRating, String stuDP, String menDP, Number dueAmount, Number paidAmount, String startingDate, String finishingDate, String productName, String categoryName, Number salary) {
        this.stuName = stuName;
        this.menaName = menaName;
        this.salaryInDemand = salaryInDemand;
        this.subjectInDemand = subjectInDemand;
        this.stuRating = stuRating;
        this.menRating = menRating;
        this.stuDP = stuDP;
        this.menDP = menDP;
        this.dueAmount = dueAmount;
        this.paidAmount = paidAmount;
        this.startingDate = startingDate;
        this.finishingDate = finishingDate;
        this.productName = productName;
        this.categoryName = categoryName;
        this.salary = salary;
    }

    public ObligationAndClaimData() {
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getMenaName() {
        return menaName;
    }

    public void setMenaName(String menaName) {
        this.menaName = menaName;
    }

    public String getSalaryInDemand() {
        return salaryInDemand;
    }

    public void setSalaryInDemand(String salaryInDemand) {
        this.salaryInDemand = salaryInDemand;
    }

    public String getSubjectInDemand() {
        return subjectInDemand;
    }

    public void setSubjectInDemand(String subjectInDemand) {
        this.subjectInDemand = subjectInDemand;
    }

    public float getStuRating() {
        return stuRating;
    }

    public void setStuRating(float stuRating) {
        this.stuRating = stuRating;
    }

    public float getMenRating() {
        return menRating;
    }

    public void setMenRating(float menRating) {
        this.menRating = menRating;
    }

    public String getStuDP() {
        return stuDP;
    }

    public void setStuDP(String stuDP) {
        this.stuDP = stuDP;
    }

    public String getMenDP() {
        return menDP;
    }

    public void setMenDP(String menDP) {
        this.menDP = menDP;
    }

    public Number getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Number dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Number getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Number paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(String finishingDate) {
        this.finishingDate = finishingDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Number getSalary() {
        return salary;
    }

    public void setSalary(Number salary) {
        this.salary = salary;
    }
}
