package com.gs.common.util.photo;


import com.gs.common.entity.Photo;
import com.gs.common.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 图章绘制 长方形
 *
 */
public class RectangleUtil extends Base{

    public RectangleUtil() {
        this.width = 150;
        this.height = 40;
    }

    public RectangleUtil(Photo photo) {
        this(photo.getWidth(), photo.getHeight(), photo.getName(), photo.getNameFontSize(), photo.getNameMarginBottom(),
                photo.getLabel(), photo.getLabelFontSize(), photo.getLabelMarginBottom(),
                photo.getNumber(), photo.getNumberFontSize(), photo.getNumberMarginBottom());
        if (StringUtil.isNotBlank(photo.getColor())) {
            this.setColor(photo.getColor());
        }
    }

    public RectangleUtil(int width, int height, String name, Integer nameSize, Integer nameMarginBottom,
                      String label, Integer labelSize, Integer labelMarginBottom,
                      String number, Integer numberSize, Integer numberMarginBottom) {
        this.width = width;
        this.height = height;

        this.name = name;
        this.nameSize = nameSize;
        this.nameMarginBottom = nameMarginBottom;

        this.label = label;
        this.labelSize = labelSize;
        this.labelMarginBottom = labelMarginBottom;

        this.number = number;
        this.numberSize = numberSize;
        this.numberMarginBottom = numberMarginBottom;
    }

	public byte[] draw() throws Exception {

		int fix = 5;// 宽高修正,如果宽高就为图片宽高,可能边框线被切割
		BufferedImage bi = new BufferedImage(width + fix * 2, height + fix * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.translate(fix, fix);
		this.drawPhoto(g2d);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);

		return baos.toByteArray();
	}

	private void drawPhoto(Graphics2D g2d) {
		// 把绘制起点挪到圆中心点
		g2d.translate(width / 2, height / 2);

		Stroke stroke = g2d.getStroke();// 旧的线性

		// 绘制图章边框
		g2d.setColor(borderColor);
		g2d.setStroke(new BasicStroke(borderWidth));
		g2d.drawRect(-width / 2, -height / 2, width, height);
		g2d.setStroke(stroke);

		// 绘制图章名称
        if (StringUtil.isNotBlank(name)) {
        	drawName(g2d, name, nameSize, nameMarginBottom, nameColor);
        }

        // -------绘制图章副名-------
        if (StringUtil.isNotBlank(label)) {
            drawName(g2d, label, labelSize, labelMarginBottom, labelColor);
        }

        // -------绘制图章编码-------
        if (StringUtil.isNotBlank(number)) {
            drawName(g2d, number, numberSize, numberMarginBottom, numberColor);
        }

	}

}