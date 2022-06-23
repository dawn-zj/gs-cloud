package com.gs.common.entity;

public class Memory {
    /**
     * 内存总量(GB)
     */
    private double total;

    /**
     * 已用内存(GB)
     */
    private double used;

    /**
     * 剩余内存(GB)
     */
    private double free;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {

        this.total = total;
    }

    public double getUsed() {
        return used;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

}
