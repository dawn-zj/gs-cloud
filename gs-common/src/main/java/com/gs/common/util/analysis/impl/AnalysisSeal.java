package com.gs.common.util.analysis.impl;

import com.gs.common.util.analysis.AnalysisUtil;
import com.gs.common.util.seal.SealUtil;

/**
 * @Author Zhang Juan
 * @Date 2024/3/29 16:59
 */
public class AnalysisSeal extends AnalysisUtil {

    @Override
    public void analysis(byte[] data) throws Exception {
        SealUtil.analysisSeal(data);
    }
}
