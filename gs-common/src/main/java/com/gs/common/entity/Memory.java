package com.gs.common.entity;

import com.gs.common.util.Arith;

public class Memory {
    /**
     * 内存总量(GB)
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {

        this.total = Arith.div(total, (1024 * 1024 * 1024), 2);
    }

    public double getUsed() {
        return used;
    }

    public void setUsed(double used) {
        this.used = Arith.div(used, (1024 * 1024 * 1024), 2);
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = Arith.div(free, (1024 * 1024 * 1024), 2);
    }

}
