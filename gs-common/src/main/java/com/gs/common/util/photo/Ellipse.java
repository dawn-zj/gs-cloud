package com.gs.common.util.photo;

import com.gs.common.entity.Photo;
import com.gs.common.util.FileUtil;
import com.gs.common.util.ImageUtil;
import com.gs.common.util.StringUtil;

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
public class Ellipse extends Base {

    public Ellipse() {
        super.starR = 15;
        super.nameMarginBottom = 0;
    }

    public Ellipse(Photo photo) {
        this(photo.getWidth(), photo.getHeight(), photo.getCompany(), photo.getCompanyFontSize(),
                photo.getName(), photo.getNameFontSize(), photo.getNameMarginBottom(),
                photo.getLabel(), photo.getLabelFontSize(), photo.getLabelMarginBottom(),
                photo.getNumber(), photo.getNumberFontSize(), photo.getNumberMarginBottom());
        if (StringUtil.isNotBlank(photo.getColor())) {
            this.setColor(photo.getColor());
        }
    }

    public Ellipse(int width, int height, String company, Integer companySize,
                  String name, Integer nameSize, Integer nameMarginBottom,
                  String label, Integer labelSize, Integer labelMarginBottom,
                  String number, Integer numberSize, Integer numberMarginBottom) {
        this.width = width;
        this.height = height;

        this.company = company;
        this.companySize = companySize;

        this.number = number;
        this.numberSize = numberSize;
        this.numberMarginBottom = numberMarginBottom;

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
        // 把原点挪到圆中心点
        g2d.translate((width + fix) / 2, (height + fix) / 2);

        // -------绘制图章边框-------
        g2d.setColor(borderColor);// 边框颜色
        g2d.setStroke(new BasicStroke(borderWidth));// 边框宽度
        g2d.drawOval(-width / 2, -height / 2, width, height);// 起点坐标(永远是左上角)和宽高

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
        Font companyFont = new Font(fontType, Font.BOLD, companySize);
        g2d.setFont(companyFont);
        g2d.setColor(companyColor);
        FontMetrics fm = g2d.getFontMetrics();

        // 公司名称角度区域为8点-4点方向，120-360°，共240度, 根据公司名称的字数，计算每个字的旋转角度，挨个绘制
        int count = company.length();// 字数
        int rx = width / 2;// 长半轴
        int ry = height / 2;// 短半轴
        int angle = (360 - (companyAngle - 90) * 2)  / (count - 1);
        int startAngle = companyAngle;
        char[] chars = company.toCharArray();

        int num = 0;
        for (int i = 0; i < count; i++) {
            float ang = startAngle + angle * i;// 现在角度
            // 弧长 = 角度 * π / 180
            double angR = ang * Math.PI / 180.0;
            float x = (float) (rx * Math.cos(angR));
            float y = (float) (ry * Math.sin(angR));
            double qxang = Math.atan2(rx * Math.cos(angR), -rx * Math.sin(angR)),
                    fxang = qxang + Math.PI / 2.0;
            // System.out.println(String.format("角度：%s，弧度：%s，坐标：(%s, %s)", ang, angR, x, y));

            String c = String.valueOf(chars[num]);// 需要绘制的字符
            int cw = fm.stringWidth(c);// 此字符宽度
            int ch = fm.getHeight();// 字高度

            // 此时x2,y2字体绘制完在圆弧外测，同比例缩放一下，重点1
            x += ch * 0.95f * (float) Math.cos(fxang);
            y += ch * 0.95f * (float) Math.sin(fxang);
            x += -cw / 2f * (float) Math.cos(qxang);
            y += -cw / 2f * (float) Math.sin(qxang);

            // 旋转，重点1
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
            Font f2 = companyFont.deriveFont(affineTransform);
            g2d.setFont(f2);

            g2d.drawString(c, x, y);// 此点为字的中心点

            num++;
        }

    }

    public static void main(String[] args) throws Exception {
        byte[] data = new Ellipse().draw();
        FileUtil.storeFile("F:/ellipse.png", data);
    }
}
