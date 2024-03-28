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
public class SealUtil {

    public static void analysisSeal(byte[] data) throws Exception {
        ASN1InputStream asn1InputStream = new ASN1InputStream(data);
        ASN1Primitive asn1Primitive;
        log.debug("开始解析印章");
        while ((asn1Primitive = asn1InputStream.readObject()) != null) {
            ASN1Sequence asn1Sequence = (ASN1Sequence) asn1Primitive;
            int size = asn1Sequence.size();
            log.debug("共{}个部分", size);

            if (size == 2) {
                analysisSeal_GM0031(asn1Sequence);
            } else if (size == 4) {
                analysisSeal_GBT38540(asn1Sequence);
            }
        }
        log.debug("解析印章结束");
    }

    private static void analysisSeal_GM0031(ASN1Sequence seal) throws Exception {
        log.debug("按照《GM/T 0031-2014》规范解析");
        // 1. ==============================================印章信息
        log.debug("--SEQUENCE：印章信息");
        ASN1Sequence sealInfo_obj = (ASN1Sequence) seal.getObjectAt(0);

        // 1.1
        // ==============================================头信息==============================================
        log.debug("----SEQUENCE：印章头信息");
        ASN1Sequence header_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(0);
        log.debug("------IA5String：电子印章数据标识 = {}", header_obj.getObjectAt(0));
        log.debug("------INTEGER：电子印章数据版本号标识 = {}", header_obj.getObjectAt(1));
        log.debug("------IA5String：电子印章厂商ID = {}", header_obj.getObjectAt(2));
        // 1.2
        // ==============================================电子印章标识==============================================
        log.debug("----IA5String：电子印章标识 = {}", sealInfo_obj.getObjectAt(1));

        // 1.3
        // ==============================================印章属性信息==============================================
        log.debug("----SEQUENCE：印章属性信息");
        ASN1Sequence property_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(2);
        log.debug("------INTEGER：印章类型 = {}", property_obj.getObjectAt(0));
        log.debug("------UTF8String：印章名称 = {}", property_obj.getObjectAt(1));
        log.debug("------SEQUENCE：签章人证书列表 = {}", property_obj.getObjectAt(2));
        log.debug("------UTCTime：印章制作日期 = {}", property_obj.getObjectAt(3));
        log.debug("------UTCTime：印章有效起始日期 = {}", property_obj.getObjectAt(4));
        log.debug("------UTCTime：印章有效终止日期 = {}", property_obj.getObjectAt(5));
        // 1.4
        // ==============================================电子印章图片数据==============================================
        log.debug("----SEQUENCE：电子印章图片数据");
        ASN1Sequence picture_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(3);
        log.debug("------IA5String：图片类型 = {}", picture_obj.getObjectAt(0));
        log.debug("------OCTET STRING：图片数据 = {}", picture_obj.getObjectAt(1));
        log.debug("------INTEGER：图片宽度 = {}", picture_obj.getObjectAt(2));
        log.debug("------INTEGER：图片高度 = {}", picture_obj.getObjectAt(3));
        // 1.5
        // ==============================================自定义数据==============================================
        if (sealInfo_obj.size() == 5) {
            log.debug("----SEQUENCE：自定义数据，暂不解析");
        }

        // 2.
        // ==============================================印章签名信息==============================================
        log.debug("--SEQUENCE：签名信息");
        ASN1Sequence signInfo_obj = (ASN1Sequence) seal.getObjectAt(1);
        log.debug("----OCTET STRING：制章人签名证书 = {}", signInfo_obj.getObjectAt(0));
        String signHashOid = signInfo_obj.getObjectAt(1).toString();
        log.debug("----OBJECT IDENTIFIER：签名算法标识 = {}({})", signHashOid, OidUtil.getSignHash(signHashOid));
        log.debug("----BIT STRING：签名值 = {}", signInfo_obj.getObjectAt(2));
    }

    private static void analysisSeal_GBT38540(ASN1Sequence seal) throws Exception {
        log.debug("按照《GB/T 38540-2020》规范解析");
        // 1. ==============================================印章信息
        log.debug("--SEQUENCE：印章信息");
        ASN1Sequence sealInfo_obj = (ASN1Sequence) seal.getObjectAt(0);

        // 1.1
        // ==============================================头信息==============================================
        log.debug("----SEQUENCE：印章头信息");
        ASN1Sequence header_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(0);
        log.debug("------IA5String：电子印章数据标识 = {}", header_obj.getObjectAt(0));
        log.debug("------INTEGER：电子印章数据版本号标识 = {}", header_obj.getObjectAt(1));
        log.debug("------IA5String：电子印章厂商ID = {}", header_obj.getObjectAt(2));
        // 1.2
        // ==============================================电子印章标识==============================================
        log.debug("----IA5String：电子印章标识 = {}", sealInfo_obj.getObjectAt(1));

        // 1.3
        // ==============================================印章属性信息==============================================
        log.debug("----SEQUENCE：印章属性信息");
        ASN1Sequence property_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(2);
        log.debug("------INTEGER：印章类型 = {}", property_obj.getObjectAt(0));
        log.debug("------UTF8String：印章名称 = {}", property_obj.getObjectAt(1));
        String type = property_obj.getObjectAt(2).toString();
        String typeCn = "1".equals(type) ? "数字证书" : "数字证书杂凑值";
        log.debug("------INTEGER：签章人证书列表类型 = {}({})", type, typeCn);
        log.debug("------SEQUENCE：签章人证书列表 = {}", property_obj.getObjectAt(3));

        ASN1GeneralizedTime genTime = (ASN1GeneralizedTime)property_obj.getObjectAt(4);
        ASN1GeneralizedTime startTime = (ASN1GeneralizedTime)property_obj.getObjectAt(5);
        ASN1GeneralizedTime endTime = (ASN1GeneralizedTime)property_obj.getObjectAt(6);
        log.debug("------GeneralizedTime：印章制作日期 = {}", genTime.getTimeString());
        log.debug("------GeneralizedTime：印章有效起始日期 = {}", startTime.getTimeString());
        log.debug("------GeneralizedTime：印章有效终止日期 = {}", endTime.getTimeString());
        // 1.4
        // ==============================================电子印章图片数据==============================================
        log.debug("----SEQUENCE：电子印章图片数据");
        ASN1Sequence picture_obj = (ASN1Sequence) sealInfo_obj.getObjectAt(3);
        log.debug("------IA5String：图片类型 = {}", picture_obj.getObjectAt(0));
        log.debug("------OCTET STRING：图片数据 = {}", picture_obj.getObjectAt(1));
        log.debug("------INTEGER：图片宽度 = {}", picture_obj.getObjectAt(2));
        log.debug("------INTEGER：图片高度 = {}", picture_obj.getObjectAt(3));
        // 1.5
        // ==============================================自定义数据==============================================
        if (sealInfo_obj.size() == 5) {
            log.debug("----SEQUENCE：自定义数据，暂不解析");
        }

        // 2.
        // ==============================================制章人签名证书==============================================
        log.debug("--OCTET STRING：制章人签名证书 = {}", seal.getObjectAt(1));
        // 3.
        // ==============================================印章签名信息==============================================
        String signHashOid = seal.getObjectAt(2).toString();
        log.debug("--OBJECT IDENTIFIER：签名算法标识 = {}({})", signHashOid, OidUtil.getSignHash(signHashOid));

        // 4.
        // ==============================================印章签名信息==============================================
        log.debug("--BIT STRING：签名值 = {}", seal.getObjectAt(3));


    }

    public static void main(String[] args) throws Exception {
        String path1 = Constants.FILE_PATH + "seal/电子印章_GM0031.seal";
        String path2 = Constants.FILE_PATH + "seal/电子印章_GB38540.seal";
        analysisSeal(FileUtil.getFile(path1));
        analysisSeal(FileUtil.getFile(path2));
    }
}
