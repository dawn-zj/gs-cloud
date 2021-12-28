package com.gs.common.util.photo;

import com.gs.common.util.FileUtil;
import sun.awt.image.IntegerInterleavedRaster;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author Administator
 * @date 2021-11-17 14:22
 * @Description 绘制椭圆图章
 */
public class Ellipse {
    private int width = 150;// 图章宽度
    private int height = 100;// 图章高度
    private int fix = 5;// 图章修正，画布比图章大一圈
    private int bottom = 5;// 图章名称距离底部圆弧的距离
    private String company = "电子签章管理系统";
    private String name = "测试章";

    private String fontType = "宋体";

    private Integer companySize = 20;
    private Integer nameSize = 12;

    private Color borderColor = Color.RED;
    private Color companyColor = Color.RED;
    private Color nameColor = Color.RED;
    private Color starColor = Color.RED;

    /**
     * 图章所属单位的起始角度,以6点钟方向为中心,向两个方向平均扩展
     */
    private Integer companyAngle = 120;
    /**
     * 边框线宽
     */
    private float borderWidth = 4F;
    /**
     * 单位字体的宽度缩放比率(百分比).此参数可以使字体看起来瘦长
     */
    private float companyScale = 0.7F;

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
        // 把原点挪到圆中心点
        g2d.translate((width + fix) / 2, (height + fix) / 2);

        // -------绘制图章边框-------
        g2d.setColor(borderColor);// 边框颜色
        g2d.setStroke(new BasicStroke(borderWidth));// 边框宽度
        g2d.drawOval(-width / 2, -height / 2, width, height);// 起点坐标(永远是左上角)和宽高

        // -------绘制图章名称-------
        g2d.setFont(new Font(fontType, Font.BOLD, nameSize));
        g2d.setColor(nameColor);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(name);// 名称宽度
        int h = fm.getHeight();// 名称高度
        g2d.drawString(name, - w / 2, height / 2 - h - bottom); // 字体距离底部10px

        // -------绘制图章单位-------
        Font companyFont = new Font(fontType, Font.BOLD, companySize);
        g2d.setFont(companyFont);
        g2d.setColor(companyColor);
        fm = g2d.getFontMetrics();

        // 公司名称角度区域为8点-4点方向，120-360°，共240度, 根据公司名称的字数，计算每个字的旋转角度，挨个绘制
        int count = company.length();// 字数
        int rx = width / 2;// 长半轴
        int ry = height / 2;// 短半轴
        int angle = (360 - companyAngle) / (count - 1);
        int startAngle = 90 + companyAngle / 2;// 以x轴正向为0,顺时针旋转
        startAngle = -90 - (360 - companyAngle) / 2;
        double vr = Math.toRadians(90);// 垂直旋转弧度
        char[] chars = company.toCharArray();

        // 与画圆不同的是，很难得出原点到椭圆任意点的距离，放弃旋转坐标轴的方法
        // 思路：计算8点-4点方向的总弧长，分尽可能多的点，计算两点之间的线段，最后总和即为总弧长
        double len = 0.0;
        // 步进2,240°分120份
        int step = 2;
        double angR = startAngle * Math.PI / 180.0;
        double lastX = rx * Math.cos(angR);
        double laxtY =ry * Math.sin(angR);
        double accArcLen = 0.0;
        // System.out.println(String.format("角度：%s，坐标：(%f, %f)", startAngle, lastX, laxtY));

        // int num = 0;
        // for (int i = startAngle + step; num < 120; i+=step) {
        //     // 弧长=角度 x π / 180
        //     angR = i * Math.PI / 180.0;
        //     double x1 = rx * Math.cos(angR);
        //     double y1 = ry * Math.sin(angR);
        //     // System.out.println(String.format("角度：%s，坐标：(%f, %f)", i, x1, y1));
        //     accArcLen += Math.sqrt((lastX - x1) * (lastX - x1) + (laxtY - y1) * (laxtY - y1));
        //     lastX = x1;
        //     laxtY = y1;
        //     num++;
        // }
        // System.out.println(accArcLen);

        // 每个字对应的弧度
        // double arcPer = accArcLen / count;

        Point center = new Point(width / 100, width / 100);// 中心点

        int num = 0;
        for (int i = 0; i < count; i++) {
            float ang = startAngle + angle * i;// 现在角度
            // 弧长=角度 x π / 180
            angR = ang * Math.PI / 180.0;
            float x = (float) (rx * Math.cos(angR));
            float y = (float) (ry * Math.sin(angR));
            double qxang = Math.atan2(rx * Math.cos(angR), -rx * Math.sin(angR)),
                    fxang = qxang + Math.PI / 2.0;
            System.out.println(String.format("角度：%s，弧度：%s，坐标：(%s, %s)", ang, angR, x, y));

            String c = String.valueOf(chars[num]);// 需要绘制的字符
            int cw = fm.stringWidth(c);// 此字符宽度
            int ch = fm.getHeight();// 字高度

            // 此时x2,y2字体绘制完在圆弧外测，同比例缩放一下
            x += ch * 0.95f * (float) Math.cos(fxang);
            y += ch * 0.95f * (float) Math.sin(fxang);
            x += -cw / 2f * (float) Math.cos(qxang);
            y += -cw / 2f * (float) Math.sin(qxang);



            // 旋转
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
            Font f2 = companyFont.deriveFont(affineTransform);
            g2d.setFont(f2);
            g2d.drawString(c, x, y);// 此点为字的中心点

            // 将所有设置还原,等待绘制下一个
            // g2d.scale(1 / companyScale, 1);

            num++;
        }

        // for (int i = 1; i <= count; i++) {
        //     char c = chars[i-1];// 需要绘制的字符
        //     int cw = fm.charWidth(c);// 此字符宽度
        //
        //     double arcL = i * arcPer;
        //     Float x2 = (float) (rx * (float) Math.cos(arcL));
        //     Float y2 = (float) (ry * (float) Math.sin(arcL));
        //     System.out.println(String.format("文字：%s，坐标：(%f, %f)", i, x2, y2));
        //     float x = x2 > 0 ? x2 - h : x2 + h;// 绘制字符的x坐标为半径减去字高度
        //     float y = y2 > 0 ? y2 - h : y2 + h;// 绘制字符的x坐标为半径减去字高度
        //     g2d.scale(companyScale, 1);// 缩放字体宽度
        //     g2d.drawString(String.valueOf(c), x, y);// 此点为字的中心点
        //
        //     // 将所有设置还原,等待绘制下一个
        //     g2d.scale(1 / companyScale, 1);
        // }
    }

    public static void main(String[] args) throws Exception {
        byte[] data = new Ellipse().draw();
        FileUtil.storeFile("F:/ellipse.png", data);
    }
}
