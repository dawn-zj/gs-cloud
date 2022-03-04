package com.gs.common.util.photo;

import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author Administator
 * @date 2021-11-15 17:45
 * @Description 图章绘制 圆形
 */
public class Circle {
    private int width = 150;// 图章宽度
    private int height = 150;// 图章高度
    private int fix = 5;// 图章修正，画布比图章大一圈

    private String fontType = "宋体";
    /**
     * 单位字体的宽度缩放比率(百分比).此参数可以使字体看起来瘦长
     */
    private float companyScale = 0.7F;

    /**
     * 边框线宽
     */
    private float borderWidth = 4F;
    private Color borderColor = Color.RED;

    /**
     * 五角星线宽
     */
    private float starBorderWidth = 3F;
    private Color starColor = Color.RED;
    private float starR = 25f;// 五角星外接圆半径

    /**
     * 图章所属单位
     * 起始角度,以6点钟方向为中心,向两个方向平均扩展
     */
    private String company = "电子签章管理系统系统系统";
    private Color companyColor = Color.RED;
    private Integer companySize = 20;
    private Integer companyAngle = 150;

    /**
     * 图章名称
     */
    private String name = "测试章";
    private Color nameColor = Color.RED;
    private Integer nameSize = 12;
    private Integer nameMarginBottom = 25; // name距离底部距离

    /**
     * 图章副名
     */
    private String label = "(1)";
    private Color labelColor = Color.RED;
    private Integer labelSize = 10;
    private Integer labelMarginBottom = 15; // 副名距离底部距离

    /**
     * 图章编码
     * 角度范围,以6点钟方向为中心,向两个方向平均扩展
     */
    private String number = "5301000082888";//5301000082888
    private Integer numberSize = 12;
    private Integer numberAngle = 100;


    public Circle() {

    }

    public Circle(int r, String company,Integer companySize,
                  String name, Integer nameSize, Integer nameMarginBottom,
                  String label, Integer labelSize, Integer labelMarginBottom,
                  String number, Integer numberSize) {
        this.width = r;
        this.height = r;

        this.company = company;
        this.companySize = companySize;

        this.number = number;
        this.numberSize = numberSize;

        this.name = name;
        this.nameSize = nameSize;
        this.nameMarginBottom = nameMarginBottom;

        this.label = label;
        this.labelSize = labelSize;
        this.labelMarginBottom = labelMarginBottom;
    }


    /**
     * 画圆
     */
    public byte[] draw() throws Exception {
        BufferedImage bi = new BufferedImage(width + fix * 2, height + fix * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        drawPhoto(g2d);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);

        return baos.toByteArray();
    }

    private void drawPhoto(Graphics2D g2d) {
        // 线条更光滑流畅
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        // 绘制矩形白画布(左上角为原点)
        g2d.fillRect(0, 0, width + fix * 2, height + fix * 2);
        // 把原点挪到圆中心点，translate：以当前点为基础开始x/y递增
        g2d.translate((width + fix) / 2, (height + fix) / 2);

        // -------绘制图章边框-------
        g2d.setColor(borderColor);// 边框颜色
        g2d.setStroke(new BasicStroke(borderWidth));// 边框宽度
        g2d.drawOval(- width / 2, - height / 2, width, height);// 起点坐标(永远是左上角)和宽高

        // -------绘制五角星-------
        drawStar(g2d, starR);

        // -------绘制图章名称-------
        if (StringUtil.isNotBlank(name)) {
            drawName(g2d, name, nameSize, nameMarginBottom, nameColor);
        }

        // -------绘制图章副名-------
        if (StringUtil.isNotBlank(label)) {
            drawName(g2d, label, labelSize, labelMarginBottom, labelColor);
        }

        // -------绘制图章单位-------
        if (StringUtil.isNotBlank(company)) {
            drawCompany(g2d);
        }

        // -------绘制图章编码-------
        if (StringUtil.isNotBlank(number)) {
            drawNumber(g2d);
        }
    }

    /**
     * 画五角星
     * @param radius 五角星外接圆半径
     * @return
     */
    private void drawStar(Graphics2D g2d, float radius) {
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

    private void drawName(Graphics2D g2d, String name, Integer size, Integer marginBottom, Color color) {
        g2d.setFont(new Font(fontType, Font.BOLD, size));
        g2d.setColor(color);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(name);// 名称宽度
        int h = fm.getHeight();// 名称高度
        g2d.drawString(name, - w / 2, height / 2 - h - marginBottom); // 字体到底部的距离
    }

    private void drawCompany(Graphics2D g2d) {
        g2d.setFont(new Font(fontType, Font.BOLD, companySize));
        g2d.setColor(companyColor);
        FontMetrics fm = g2d.getFontMetrics();
        int h = fm.getHeight();// 字高度

        // 公司名称角度区域为8点-4点方向，共240度, 根据公司名称的字数，计算每个字的旋转角度，挨个绘制
        int count = company.length();// 字数
        int r = width / 2;// 半径
        int angle = (360 - companyAngle) / (count - 1);
        float start = 90 + companyAngle / 2;// 以x轴正向为0,顺时针旋转
        double vr = Math.toRadians(90);// 垂直旋转弧度
        char[] chars = company.toCharArray();
        for (int i = 0; i < count; i++) {
            char c = chars[i];// 需要绘制的字符
            int cw = fm.charWidth(c);// 此字符宽度

            float a = start + angle * i;// 现在角度
            double radians = Math.toRadians(a);
            g2d.rotate(radians);// 旋转坐标系,让要绘制的字符处于x正轴

            float x = r - h;// 绘制字符的x坐标为半径减去字高度
            g2d.translate(x, 0);// 移动到此位置,此时字的上下方向和x轴垂直
            g2d.rotate(vr);// 旋转90度,让字平行于x轴
            g2d.scale(companyScale, 1);// 缩放字体宽度
            g2d.drawString(String.valueOf(c), -cw / 2, 0);// 此点为字的中心点

            // 将所有设置还原,等待绘制下一个
            g2d.scale(1 / companyScale, 1);
            g2d.rotate(-vr);
            g2d.translate(-x, 0);
            g2d.rotate(-radians);
        }
    }

    private void drawNumber(Graphics2D g2d) {
        g2d.setFont(new Font(fontType, Font.BOLD, numberSize));
        g2d.setColor(companyColor);
        FontMetrics fm = g2d.getFontMetrics();
        int h = fm.getHeight();// 字高度

        // 印章编码角度区域为8点-4点方向，共120度, 计算每个数字的旋转角度，挨个绘制
        int count = number.length();// 字数
        int r = width / 2;// 半径
        int angle = numberAngle / (count - 1);
        int start = 90 + numberAngle / 2 - angle / 2;// 以x轴正向为0,顺时针旋转
        double vr = Math.toRadians(-90);// 垂直旋转弧度
        char[] chars = number.toCharArray();
        // for (int i = count - 1; i >= 0; i--) {
        for (int i = 0; i < count; i++) {
            char c = chars[i];// 需要绘制的字符
            int cw = fm.charWidth(c);// 此字符宽度

            float a = start - angle * i;// 现在角度
            double radians = Math.toRadians(a);
            g2d.rotate(radians);// 旋转坐标系,让要绘制的字符处于x正轴

            float x = r - 5;// 绘制字符的x坐标为半径减去字高度
            g2d.translate(x, 0);// 移动到此位置,此时字的上下方向和x轴垂直
            g2d.rotate(vr);// 逆时针旋转90度,让字平行于x轴
            g2d.scale(companyScale, 1);// 缩放字体宽度
            g2d.drawString(String.valueOf(c), -cw / 2, 0);// 此点为字的中心点

            // 将所有设置还原,等待绘制下一个
            g2d.scale(1 / companyScale, 1);
            g2d.rotate(-vr);
            g2d.translate(-x, 0);
            g2d.rotate(-radians);
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] data = new Circle().draw();
        FileUtil.storeFile("F:/circle.png", data);
    }
}
