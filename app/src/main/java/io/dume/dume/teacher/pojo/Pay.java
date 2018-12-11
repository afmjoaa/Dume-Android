package io.dume.dume.teacher.pojo;

public class Pay {
    private int bucks;
    private String buckTitle;
    private int refer;
    private boolean haveDiscount;
    private float discount;

    public Pay(int bucks, String buckTitle, int refer, boolean haveDiscount, float discount) {
        this.bucks = bucks;
        this.buckTitle = buckTitle;
        this.refer = refer;
        this.haveDiscount = haveDiscount;
        this.discount = discount;
    }

    public int getBucks() {
        return bucks;
    }

    public String getBuckTitle() {
        return buckTitle;
    }

    public int getRefer() {
        return refer;
    }

    public boolean isHaveDiscount() {
        return haveDiscount;
    }

    public float getDiscount() {
        return discount;
    }
}
