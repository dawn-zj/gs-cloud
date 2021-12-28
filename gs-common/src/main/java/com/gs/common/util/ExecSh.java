package com.gs.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * linux执行sh脚本工具类
 */
public class ExecSh {

    public static String exec(String command) throws Exception {
        BufferedReader bufferedReader = null;
        String tmp, result = "";
        try {
            // 1.Runtime执行命令
            Process pro = Runtime.getRuntime().exec(command);
            pro.waitFor();
            // 2.通过Process获取流拿到结果：getInputStream正确结果，getErrorStream异常信息
            bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((tmp = bufferedReader.readLine()) != null) {
                result += tmp;
            }

            if (StringUtil.isBlank(result)) {
                bufferedReader = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                while ((tmp = bufferedReader.readLine()) != null) {
                    result += tmp;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // 3.关闭流
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return result;
    }

    public static String exec(String[] command) throws Exception {
        BufferedReader bufferedReader = null;
        String tmp, result = "";
        try {
            // 1.Runtime执行命令
            Process pro = Runtime.getRuntime().exec(command);
            pro.waitFor();
            // 2.通过Process获取流拿到结果：getInputStream正确结果，getErrorStream异常信息
            bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((tmp = bufferedReader.readLine()) != null) {
                result += tmp;
            }

            if (StringUtil.isBlank(result)) {
                bufferedReader = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                while ((tmp = bufferedReader.readLine()) != null) {
                    result += tmp;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // 3.关闭流
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return result;
    }
}
