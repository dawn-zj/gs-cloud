package com.gs.common.util.photo;

import com.gs.common.entity.Photo;
import com.gs.common.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 图章绘制 正方形
 * 
 */
public class SquareUtil extends Base{

	public SquareUtil() {
		this.width = 150;
		this.height = 150;
	}

	public SquareUtil(Photo photo) {
		this(photo.getWidth(), photo.getName(), photo.getNameFontSize(), photo.getNameMarginBottom(),
				photo.getLabel(), photo.getLabelFontSize(), photo.getLabelMarginBottom(),
				photo.getNumber(), photo.getNumberFontSize(), photo.getNumberMarginBottom());
		if (StringUtil.isNotBlank(photo.getColor())) {
			this.setColor(photo.getColor());
		}
	}

	public SquareUtil(int r, String name, Integer nameSize, Integer nameMarginBottom,
					  String label, Integer labelSize, Integer labelMarginBottom,
					  String number, Integer numberSize, Integer numberMarginBottom) {
		this.width = r;
		this.height = r;

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

		// 绘制图章边框，起点矩形左上角
		g2d.setColor(borderColor);
		g2d.setStroke(new BasicStroke(borderWidth));
		g2d.drawRect(-width / 2, -height / 2, width, height);
		g2d.setStroke(stroke);

		// -------绘制图章副名，起点名称左下角-------
		if (StringUtil.isNotBlank(label)) {
			drawName(g2d, label, labelSize, labelMarginBottom, labelColor);
		}

		// -------绘制图章编码-------
		if (StringUtil.isNotBlank(number)) {
			drawName(g2d, number, numberSize, numberMarginBottom, numberColor);
		}

		// -------绘制图章名称-------
		FontMetrics fm = g2d.getFontMetrics();
		Font nameFont = new Font(fontType, Font.BOLD, nameSize);
		g2d.setFont(nameFont);
		g2d.setColor(nameColor);
		fm = g2d.getFontMetrics();
		int w = fm.stringWidth(name);// 名称宽度
		int h = fm.getHeight();// 字高度
		int y = fm.getAscent() - h / 2 - 5;

		width = (int) (width - borderWidth * 2);
		int oneW = w / name.length();// 每个字宽度
		int textNum = width / oneW;// 每行字数
		int line = w / width;// 行数
		if (w % width > 0)
			line += 1;
		// System.out.println("fm.getAscent()="+fm.getAscent()+" w="+w+" width="+width+"
		// oneW="+oneW+" textNum="+textNum+" line="+line+" y="+y);
		for (int i = 0; i < line; i++) {
			String subName = "";
			if (((i + 1) * textNum) >= name.length())
				subName = name.substring(i * textNum);
			else
				subName = name.substring(i * textNum, (i + 1) * textNum);
			int check = 0;
			if (i > 0)
				check = 10;
			g2d.drawString(subName, -(oneW * subName.length()) / 2, -width / 2 + (i + 1) * width / (line + 1) + y + check - nameMarginBottom);
		}

		// // 绘制印章编码
		// g2d.setFont(labelFont);
		// g2d.setColor(labelColor);
		// fm = g2d.getFontMetrics();
		// w = fm.stringWidth(label);// 编码宽度
		// h = fm.getHeight();// 编码高度
		//
		// int x1 = -(w / 2);
		// int y1 = height / 2 - h  + 5;
		// g2d.drawString(label, x1, y1);
	}

}