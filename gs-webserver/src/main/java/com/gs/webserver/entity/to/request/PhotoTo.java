package com.gs.webserver.entity.to.request;

import lombok.Data;

@Data
public class PhotoTo {
    /**
     * 印章样式 1圆形 2椭圆形 3方形 4长方形
     */
    private Integer stampStyle;

    /**
     * 宽高
     */
    private Integer width;

    private Integer height;

    private String fontType;

    private String name;
    private Integer nameFontSize;// 印章名称字体大小
    private Integer nameMarginBottom;// 印章名称距离底部距离

    private String label;
    private Integer labelFontSize;// 印章副名字体大小
    private Integer labelMarginBottom;// 印章副名距离底部距离

    private String company;
    private Integer companyFontSize;// 印章单位字体大小

    private String number;
    private Integer numberFontSize;// 印章编号字体大小
    private Integer numberMarginBottom;// 印章编号字体大小

}
