package com.gs.common.util.analysis.impl;

import com.gs.common.util.analysis.AnalysisUtil;
import com.gs.common.util.seal.StampUtil;

/**
 * @Author Zhang Juan
 * @Date 2024/3/29 16:59
 */
public class AnalysisStamp extends AnalysisUtil {

    @Override
    public void analysis(byte[] data) throws Exception {
        StampUtil.analysisStamp(data);
    }
}
