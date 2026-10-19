package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @author Administator
 */
@Data
public class PhotoTo {
    /**
     * 印章样式：1圆形，2椭圆形，3方形，4长方形
     * @mock 1
     */
    private Integer stampStyle;

    /**
     * 宽
     * @mock 150
     */
    private Integer width;

    /**
     * 高
     * @mock 150
     */
    private Integer height;

    /**
     * 字体
     * @mock 宋体
     */
    private String fontType;

    /**
     * 印章名称
     * @mock 测试专用章
     */
    private String name;
    /**
     * 印章名称字号
     * @mock 12
     */
    private Integer nameFontSize;
    /**
     * 印章名称距离底部距离
     * @mock 28
     */
    private Integer nameMarginBottom;

    /**
     * 印章副名
     * @mock (1)
     */
    private String label;
    /**
     * 印章副名字号
     * @mock 10
     */
    private Integer labelFontSize;
    /**
     * 印章副名距离底部距离
     * @mock 15
     */
    private Integer labelMarginBottom;

    /**
     * 印章单位
     * @mock 电子签章系统测试
     */
    private String company;
    /**
     * 印章单位字号
     * @mock 20
     */
    private Integer companyFontSize;

    /**
     * 印章编号
     * @mock 1234567890123
     */
    private String number;
    /**
     * 印章编号字号
     * @mock 13
     */
    private Integer numberFontSize;
    /**
     * 印章编号距离底部距离
     * @mock 0
     */
    private Integer numberMarginBottom;

    /**
     * 十六进制颜色
     */
    private String color;
}
