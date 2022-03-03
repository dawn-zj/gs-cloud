package com.gs.common.util.photo;

import com.gs.common.util.FileUtil;

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
    private String company = "电子签章管理系统系统系统";
    private String name = "测试章";
    private String number = "5301000082888";//5301000082888

    private String fontType = "宋体";

    private Integer companySize = 20;
    private Integer nameSize = 12;
    private Integer numberSize = 12;

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

    public Circle() {

    }

    public Circle(int r, String company,Integer companySize, String name, Integer nameSize,
                  String number, Integer numberSize) {
        this.width = r;
        this.height = r;

        this.company = company;
        this.companySize = companySize;

        this.name = name;
        this.nameSize = nameSize;

        this.number = number;
        this.numberSize = numberSize;
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
        // 把原点挪到圆中心点
        g2d.translate((width + fix) / 2, (height + fix) / 2);

        // -------绘制图章边框-------
        g2d.setColor(borderColor);// 边框颜色
        g2d.setStroke(new BasicStroke(borderWidth));// 边框宽度
        g2d.drawOval(- width / 2, - height / 2, width, height);// 起点坐标(永远是左上角)和宽高

        // -------绘制图章名称-------
        g2d.setFont(new Font(fontType, Font.BOLD, nameSize));
        g2d.setColor(nameColor);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(name);// 名称宽度
        int h = fm.getHeight();// 名称高度
        g2d.drawString(name, - w / 2, height / 2 - h - 10); // 字体距离底部10px

        // -------绘制图章单位-------
        g2d.setFont(new Font(fontType, Font.BOLD, companySize));
        g2d.setColor(companyColor);
        fm = g2d.getFontMetrics();
        h = fm.getHeight();// 字高度

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

        // -------绘制图章单位-------
        g2d.setFont(new Font(fontType, Font.BOLD, numberSize));
        g2d.setColor(companyColor);
        fm = g2d.getFontMetrics();
        h = fm.getHeight();// 字高度

        // 印章编码角度区域为8点-4点方向，共120度, 计算每个数字的旋转角度，挨个绘制
        count = number.length();// 字数
        // int r = width / 2;// 半径
        angle = 100 / (count - 1);
        start = 90 + 100 / 2 - angle / 2;// 以x轴正向为0,顺时针旋转
        vr = Math.toRadians(-90);// 垂直旋转弧度
        chars = number.toCharArray();
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
