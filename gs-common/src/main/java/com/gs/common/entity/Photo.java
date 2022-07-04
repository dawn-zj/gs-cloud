package com.gs.common.entity;

public class Photo {
    /**
     * 印章样式 1圆形 2椭圆形 3方形 4长方形
     */
    private Integer stampStyle;

    /**
     * 宽高，单位像素
     */
    private Integer width;

    private Integer height;

    private String fontType;

    private String name;
    private Integer nameFontSize;// 印章名称字体大小，单位磅?
    private Integer nameMarginBottom;// 印章名称距离底部距离，单位像素

    private String label;
    private Integer labelFontSize;// 印章副名字体大小
    private Integer labelMarginBottom;// 印章副名距离底部距离

    private String company;
    private Integer companyFontSize;// 印章单位字体大小

    private String number;
    private Integer numberFontSize;// 印章编号字体大小
    private Integer numberMarginBottom;// 印章编号距离底部距离

    public Integer getStampStyle() {
        return stampStyle;
    }

    public void setStampStyle(Integer stampStyle) {
        this.stampStyle = stampStyle;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNameFontSize() {
        return nameFontSize;
    }

    public void setNameFontSize(Integer nameFontSize) {
        this.nameFontSize = nameFontSize;
    }

    public Integer getNameMarginBottom() {
        return nameMarginBottom;
    }

    public void setNameMarginBottom(Integer nameMarginBottom) {
        this.nameMarginBottom = nameMarginBottom;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(Integer labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public Integer getLabelMarginBottom() {
        return labelMarginBottom;
    }

    public void setLabelMarginBottom(Integer labelMarginBottom) {
        this.labelMarginBottom = labelMarginBottom;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCompanyFontSize() {
        return companyFontSize;
    }

    public void setCompanyFontSize(Integer companyFontSize) {
        this.companyFontSize = companyFontSize;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getNumberFontSize() {
        return numberFontSize;
    }

    public void setNumberFontSize(Integer numberFontSize) {
        this.numberFontSize = numberFontSize;
    }

    public Integer getNumberMarginBottom() {
        return numberMarginBottom;
    }

    public void setNumberMarginBottom(Integer numberMarginBottom) {
        this.numberMarginBottom = numberMarginBottom;
    }
}
