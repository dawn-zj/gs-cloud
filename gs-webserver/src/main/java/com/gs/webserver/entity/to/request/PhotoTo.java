package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @author Administator
 */
@Data
public class PhotoTo {
    /**
     * 印章样式：1圆形，2椭圆形，3方形，4长方形
     */
    private Integer stampStyle;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 字体
     */
    private String fontType;

    /**
     * 印章名称
     */
    private String name;
    /**
     * 印章名称字号
     */
    private Integer nameFontSize;
    /**
     * 印章名称距离底部距离
     */
    private Integer nameMarginBottom;

    /**
     * 印章副名
     */
    private String label;
    /**
     * 印章副名字号
     */
    private Integer labelFontSize;
    /**
     * 印章副名距离底部距离
     */
    private Integer labelMarginBottom;

    /**
     * 印章单位
     */
    private String company;
    /**
     * 印章单位字号
     */
    private Integer companyFontSize;

    /**
     * 印章编号
     */
    private String number;
    /**
     * 印章编号字号
     */
    private Integer numberFontSize;
    /**
     * 印章编号距离底部距离
     */
    private Integer numberMarginBottom;

}
