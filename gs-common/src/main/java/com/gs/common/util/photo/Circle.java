package com.gs.common.util.photo;

import com.gs.common.entity.Photo;
import com.gs.common.util.FileUtil;
import com.gs.common.util.ImageUtil;
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
public class Circle extends Base {

    public Circle() {
        this.width = 150;
        this.height = 150;
    }

    public Circle(Photo photo) {
        this(photo.getWidth(), photo.getCompany(), photo.getCompanyFontSize(),
                photo.getName(), photo.getNameFontSize(), photo.getNameMarginBottom(),
                photo.getLabel(), photo.getLabelFontSize(), photo.getLabelMarginBottom(),
                photo.getNumber(), photo.getNumberFontSize());
    }

    public Circle(int r, String company, Integer companySize,
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

        // 去白色背景
        byte[] data = ImageUtil.cleanBGColor(bi);
        return data;

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(bi, "png", baos);
//        return baos.toByteArray();
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

    private void drawCompany(Graphics2D g2d) {
        g2d.setFont(new Font(fontType, Font.BOLD, companySize));
        g2d.setColor(companyColor);
        FontMetrics fm = g2d.getFontMetrics();
        int h = fm.getHeight();// 字高度

        // 公司名称角度区域为8点-4点方向, 根据公司名称的字数，计算每个字的旋转角度，挨个绘制
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
        // int angle = numberAngle / (count - 1);
        int angle = (numberAngle - 90) * 2 / (count - 1);
        int start = numberAngle - angle / 2;// 以x轴正向为0,顺时针旋转
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
