package com.gs.common.util.photo;

import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.StringUtil;

import java.awt.*;

public class Base {
    protected int width = 150;// 图章宽度
    protected int height = 100;// 图章高度
    protected int fix = 5;// 图章修正，画布比图章大一圈

    protected String fontType = "宋体";
    /**
     * 单位字体的宽度缩放比率(百分比).此参数可以使字体看起来瘦长
     */
    protected float companyScale = 0.7F;

    /**
     * 边框线宽
     */
    protected float borderWidth = 4F;
    protected Color borderColor = Color.RED;

    /**
     * 五角星线宽
     */
    protected float starBorderWidth = 3F;
    protected Color starColor = Color.RED;
    protected float starR = 25f;// 五角星外接圆半径

    /**
     * 图章所属单位
     * 起始角度,以6点钟方向为中心,向两个方向平均扩展
     */
    protected String company = "电子签章管理系统";
    protected Color companyColor = Color.RED;
    protected Integer companySize = 20;
    protected Integer companyAngle = 150;

    /**
     * 图章名称
     */
    protected String name = "测试章";
    protected Color nameColor = Color.RED;
    protected Integer nameSize = 12;
    protected Integer nameMarginBottom = 25; // name距离底部距离

    /**
     * 图章副名
     */
    protected String label = "(1)";
    protected Color labelColor = Color.RED;
    protected Integer labelSize = 10;
    protected Integer labelMarginBottom = 15; // 副名距离底部距离

    /**
     * 图章编码
     * 起始角度,以6点钟方向为中心,向两个方向平均扩展
     */
    protected String number = "5301000082888";//5301000082888
    protected Integer numberSize = 12;
    protected Integer numberAngle = 135;
    protected Color numberColor = Color.RED;
    protected Integer numberMarginBottom = 5;

    public Color hexToColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // 确保颜色代码的长度为6或8（对于带有alpha通道的颜色）
        if (hex.length() != 6 && hex.length() != 8) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }

        // 将16进制字符串转换为整数
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        // 如果有alpha通道，则解析它
        int alpha = hex.length() == 8 ? Integer.parseInt(hex.substring(6, 8), 16) : 255;

        return new Color(r, g, b, alpha);
    }

    public void setColor(String colorHex) {
        Color color = hexToColor(colorHex);
        this.borderColor = color;
        this.starColor = color;
        this.companyColor = color;
        this.nameColor = color;
        this.labelColor = color;
        this.numberColor = color;
    }
    /**
     * 以图中心点为对称点，横向画名称，起点是文字左下角
     * @param g2d
     * @param name
     * @param size
     * @param marginBottom
     * @param color
     */
    protected void drawName(Graphics2D g2d, String name, Integer size, Integer marginBottom, Color color) {
        g2d.setFont(new Font(fontType, Font.BOLD, size));
        g2d.setColor(color);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(name);// 名称宽度
        int h = fm.getHeight();// 名称高度
        // System.out.println(StringUtil.format("总高度{}，名称{}，高度{}，宽度{}，距离{}", height, name, h, w, marginBottom));
        g2d.drawString(name, - w / 2, height / 2 - borderWidth - marginBottom); // 字体到底部的距离
    }

    /**
     * 画五角星
     * @param radius 五角星外接圆半径
     * @return
     */
    protected void drawStar(Graphics2D g2d, float radius) {
        if (radius <= 0)
            throw new NetGSRuntimeException("五角星外接圆半径不能小于0");

        float lradius = radius * 0.381966f;// 根据radius求内圆半径
        double halfpi = Math.PI / 180f;
        Point[] points = new Point[10]; // 画10个点，依次连接就是一个五角星
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 1) // 奇数
                points[i] = new Point((int) (Math.sin(halfpi * 36 * i) * radius), (int) (Math.cos(halfpi * 36 * i) * radius));
            else
                points[i] = new Point((int) (Math.sin(halfpi * 36 * i) * lradius), (int) (Math.cos(halfpi * 36 * i) * lradius));
        }
        Polygon polygon = new Polygon();
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }

        if (starColor != null) {
            g2d.setColor(starColor);
            g2d.fill(polygon);
        }

        // 绘制五角星边框
        g2d.setStroke(new BasicStroke(starBorderWidth));
        g2d.setColor(starColor);
        g2d.draw(polygon);
    }
}
