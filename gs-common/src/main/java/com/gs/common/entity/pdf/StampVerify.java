package com.gs.common.entity.pdf;


import java.util.List;

public class StampVerify {
    private boolean result;
    private List<StampInfo> signList;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<StampInfo> getSignList() {
        return signList;
    }

    public void setSignList(List<StampInfo> signList) {
        this.signList = signList;
    }
}
