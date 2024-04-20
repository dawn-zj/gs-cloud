package com.gs.common.util.seal;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.crypto.OidUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.*;

/**
 * @Author Zhang Juan
 * @Date 2024/3/28 13:14
 */
@Slf4j
public class StampUtil {

    /**
     * 解析签章结构(不解析印章)
     * @param data 签章数据
     * @throws Exception
     */
    public static void analysisStamp(byte[] data) throws Exception {
        analysisStamp(data, false);
    }

    /**
     * 解析签章结构
     * @param data 签章数据
     * @param analysisSeal 是否解析印章
     * @throws Exception
     */
    public static void analysisStamp(byte[] data, boolean analysisSeal) throws Exception {
        ASN1InputStream asn1InputStream = new ASN1InputStream(data);
        ASN1Primitive asn1Primitive;
        log.debug("开始解析签章");
        while ((asn1Primitive = asn1InputStream.readObject()) != null) {
            ASN1Sequence asn1Sequence = (ASN1Sequence) asn1Primitive;
            int size = asn1Sequence.size();
            log.debug("共{}个部分", size);

            if (size == 2) {
                analysisStamp_GM0031(asn1Sequence, analysisSeal);
            } else if (size == 4 || size == 5) {
                analysisStamp_GBT38540(asn1Sequence, analysisSeal);
            }
        }
        log.debug("解析签章结束");
    }

    private static void analysisStamp_GM0031(ASN1Sequence stamp, boolean analysisSeal) throws Exception {
        log.debug("按照《GM/T 0031-2014》规范解析");
        // 1. ==============================================待签章数据
        log.debug("--SEQUENCE：待签章数据");
        ASN1Sequence stampInfo_obj = (ASN1Sequence) stamp.getObjectAt(0);

        // 1.1
        // ==============================================电子签章版本信息==============================================
        log.debug("----INTEGER：电子签章版本号 = {}", stampInfo_obj.getObjectAt(0));
        // 1.2
        // ==============================================电子印章==============================================
        log.debug("----SEQUENCE：电子印章");
        ASN1Sequence sealInfo_obj = (ASN1Sequence) stampInfo_obj.getObjectAt(1);
        if (analysisSeal) {
            log.debug("————————————————————————————————————————————");
            SealUtil.analysisSeal(sealInfo_obj.getEncoded());
            log.debug("————————————————————————————————————————————");
        }
        // 1.3
        // ==============================================签章时间==============================================
        // 可能是时间戳，也可能是UTCTime
        ASN1BitString stampTime_obj = (ASN1BitString) stampInfo_obj.getObjectAt(2);
        String derTimeCn = "未知类型";
        String time = "无法解析";
        log.debug("----BIT STRING：签章时间 = {}({}:{})", stampTime_obj, derTimeCn, time);
        // 1.4
        // ==============================================原文杂凑值==============================================
        log.debug("----BIT STRING：原文杂凑值 = {}", stampInfo_obj.getObjectAt(3));
        // 1.5
        // ==============================================原文属性==============================================
        log.debug("----IA5String：原文属性 = {}", stampInfo_obj.getObjectAt(4));
        // 1.6
        // ==============================================签章人签名证书==============================================
        log.debug("----OCTET STRING：制章人签名证书 = {}", stampInfo_obj.getObjectAt(5));
        // 1.7
        // ==============================================签名算法标识==============================================
        String signHashOid = stampInfo_obj.getObjectAt(6).toString();
        log.debug("----OBJECT IDENTIFIER：签名算法标识 = {}({})", signHashOid, OidUtil.getSignHash(signHashOid));

        // 2.
        // ==============================================签名值==============================================
        log.debug("--BIT STRING：签名值 = {}", stamp.getObjectAt(1));
    }

    private static void analysisStamp_GBT38540(ASN1Sequence stamp, boolean analysisSeal) throws Exception {
        log.debug("按照《GB/T 38540-2020》规范解析");
        // 1. ==============================================签章信息
        log.debug("--SEQUENCE：签章信息");
        ASN1Sequence stampInfo_obj = (ASN1Sequence) stamp.getObjectAt(0);

        // 1.1
        // ==============================================电子签章版本信息==============================================
        log.debug("----INTEGER：电子签章版本号 = {}", stampInfo_obj.getObjectAt(0));
        // 1.2
        // ==============================================电子印章==============================================
        log.debug("----SEQUENCE：电子印章");
        ASN1Sequence sealInfo_obj = (ASN1Sequence) stampInfo_obj.getObjectAt(1);
        if (analysisSeal) {
            log.debug("————————————————————————————————————————————");
            SealUtil.analysisSeal(sealInfo_obj.getEncoded());
            log.debug("————————————————————————————————————————————");
        }

        // 1.3
        // ==============================================签章时间==============================================
        ASN1GeneralizedTime stampTime = (ASN1GeneralizedTime)stampInfo_obj.getObjectAt(2);
        log.debug("----GeneralizedTime：签章时间 = {}", stampTime.getTimeString());
        // 1.4
        // ==============================================原文杂凑值==============================================
        log.debug("----BIT STRING：原文杂凑值 = {}", stampInfo_obj.getObjectAt(3));
        // 1.5
        // ==============================================原文属性==============================================
        log.debug("----IA5String：原文属性 = {}", stampInfo_obj.getObjectAt(4));
        // 1.6
        // ==============================================自定义数据==============================================
        if (stampInfo_obj.size() == 6) {
            log.debug("----SEQUENCE：自定义数据，暂不解析");
        }

        // 2.
        // ==============================================签章人签名证书==============================================
        log.debug("--OCTET STRING：制章人签名证书 = {}", stamp.getObjectAt(1));
        // 3.
        // ==============================================签名算法标识==============================================
        String signHashOid = stamp.getObjectAt(2).toString();
        log.debug("--OBJECT IDENTIFIER：签名算法标识 = {}({})", signHashOid, OidUtil.getSignHash(signHashOid));

        // 4.
        // ==============================================签名值==============================================
        log.debug("--BIT STRING：签名值 = {}", stamp.getObjectAt(3));

        if (stamp.size() == 5) {
            log.debug("----BIT STRING：时间戳信息，暂不解析");
        }

    }

    public static void main(String[] args) throws Exception {
        String path1 = Constants.FILE_PATH + "seal/电子签章_GM0031.asn1";
        analysisStamp(FileUtil.getFile(path1));
        String path2 = Constants.FILE_PATH + "seal/电子签章_GB38540.asn1";
        analysisStamp(FileUtil.getFile(path2));
    }
}
