package com.gs.webserver.entity.to;

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


    private String company;
    private Integer companyFontSize;// 印章单位字体大小

    private String number;
    private Integer numberFontSize;// 印章编号字体大小

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
}
