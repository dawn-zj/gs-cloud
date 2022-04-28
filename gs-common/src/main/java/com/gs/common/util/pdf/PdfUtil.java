package com.gs.common.util.pdf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.gs.common.define.Constants;
import com.gs.common.exception.BaseException;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.resource.ErrCode;
import com.gs.common.util.FileUtil;
import com.gs.common.util.date.DateUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

public class PdfUtil {

	// 需要pdfbox
//	public void pdfToWord() {
//		try {
//			String pdfFile = "D:/1.pdf";
//			PDDocument doc = PDDocument.load(new File(pdfFile));
//			int pagenumber = doc.getNumberOfPages();
//			pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
//			String fileName = pdfFile + ".doc";
//			File file = new File(fileName);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			FileOutputStream fos = new FileOutputStream(fileName);
//			Writer writer = new OutputStreamWriter(fos, "UTF-8");
//			PDFTextStripper stripper = new PDFTextStripper();
//			stripper.setSortByPosition(true);// 排序
//			stripper.setStartPage(1);
//			;// 设置转换的开始页
//			stripper.setEndPage(pagenumber);// 设置转换的结束页
//			stripper.writeText(doc, writer);
//			writer.close();
//			doc.close();
//			System.out.println("pdf转换word成功！");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static List getTemplateFields(byte[] pdfTemplateData) throws Exception {
		PdfReader reader = null;
		PdfStamper stamper = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			// 读取pdf模板
			reader = new PdfReader(pdfTemplateData);

			// 获取模板文本域
			stamper = new PdfStamper(reader, bos);
			AcroFields fields = stamper.getAcroFields();

			Set<String> fieldNames = fields.getFields().keySet();
			return new ArrayList<>(fieldNames);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (stamper != null)
				stamper.close();

			if (reader != null)
				reader.close();
		}
	}

	/**
	 * 根据模板和内容生成新的pdf
	 *
	 * @param pdfTemplateData
	 * @param pro
	 */
	public static byte[] genPdfByTemplate(byte[] pdfTemplateData, Properties pro) throws Exception {
		PdfReader reader = null;
		PdfStamper stamper = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			// 读取pdf模板
			reader = new PdfReader(pdfTemplateData);

			// 获取模板文本域
			stamper = new PdfStamper(reader, bos);
			AcroFields fields = stamper.getAcroFields();

			// 使用中文字体 使用 AcroFields填充值的不需要在程序中设置字体，在模板文件中设置字体为中文字体 Adobe 宋体 std L
			fields.addSubstitutionFont(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false));

			// 遍历给文本域赋值
			for (Object o : pro.keySet()) {
				// 插入的数据都为字符类型
				fields.setField((String) o, pro.getProperty((String) o));
			}

			// 如果为false那么生成的PDF文件还能编辑，一定要设为true
			stamper.setFormFlattening(true);
			stamper.close();
			stamper = null;

			return bos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (stamper != null)
					stamper.close();
			} catch (Exception e) {
			}

			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
	}

	public static byte[] pdfAddImage(byte[] pdfData, byte[] photoData, int pageNumber, float x, float y, float w, float h, int type) throws Exception {
		switch (type) {
			case Constants.PHOTO_UNIT_CM:
				// 厘米转像素
				w = w / 2.54f * 72f;
				h = h / 2.54f * 72f;
				return pdfAddImage(pdfData, photoData, pageNumber, x, y, w, h);
			case Constants.PHOTO_UNIT_PX:
				return pdfAddImage(pdfData, photoData, pageNumber, x, y, w, h);
			default:
				throw new BaseException("不支持的图片单位：" + type);
		}
	}

	/**
	 * 给pdf的指定页码和坐标添加图片
	 *
	 * @param pdfData    pdf文件
	 * @param photoData  图片
	 * @param pageNumber 页码
	 * @param x          x坐标，像素
	 * @param y          y坐标，像素
	 * @param w          图片宽度，单位像素
	 * @param h          图片高度，单位像素
	 */
	private static byte[] pdfAddImage(byte[] pdfData, byte[] photoData, int pageNumber, float x, float y, float w, float h) throws Exception {
		PdfReader reader = null;
		PdfStamper stamper = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			reader = new PdfReader(pdfData);
			stamper = new PdfStamper(reader, baos);

			// 获取pdf总页数
			int totalPageNum = reader.getNumberOfPages();
			if (pageNumber > totalPageNum || pageNumber < 1)
				throw new NetGSRuntimeException(ErrCode.PAGE_NUM_OVER_LIMIT_IN_PDF, "page number over limit in pdf");


			// 获取该页的宽高
			Document document = new Document(reader.getPageSize(pageNumber));
			float widthPage = document.getPageSize().getWidth();
			float heightPage = document.getPageSize().getHeight();

			/**
			 * 通常手机、电脑上照片默认分辨率是72或96
			 * 实际尺寸(英寸)=像素/分辨率
			 * 1 英寸 = 2.54 厘米 = 72磅
			 * 1 厘米 = 1/2.54 英寸
			 */
			// Image处理
			// todo 图片宽高入参支持厘米，由厘米转为英寸
			Image img = Image.getInstance(photoData);
			if (w == 0) {
				w = img.getWidth() * 72f / 96; // 分辨率从72改为96
			}
			if (h == 0) {
				h = img.getHeight() * 72f / 96;
			}
			// 自定义图片大小
			img.scaleAbsoluteWidth(w);
			img.scaleAbsoluteHeight(h);
			// 坐标越界处理
			x = x < 0 ? 0 : x;
			x = x >= (widthPage - w) ? widthPage - w : x;
			y = y < 0 ? 0 : y;
			y = y > (heightPage - h) ? heightPage - h : y;

			img.setAbsolutePosition(x, y);

			// 获取该页码的内容
			PdfContentByte content = stamper.getOverContent(pageNumber);
			if(content == null) {
				throw new NetGSRuntimeException("page number " + pageNumber + " out of range");
			}
			// 添加图片
			content.addImage(img);
			stamper.close();

			return baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (stamper != null)
					stamper.close();
			} catch (Exception e) {
			}

			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
	}


	public static byte[] genPdfTest(String pdfTemplatePath) throws Exception {
		byte[] pdfTemplateData = FileUtil.getFile(pdfTemplatePath);

		Properties properties = new Properties();
		properties.setProperty("cert", "CN=Test");
		properties.setProperty("businessCodeCn", "新办");
		properties.setProperty("name", "张女士");
		properties.setProperty("phone", "15712345678");
		properties.setProperty("idType", "身份证");
		properties.setProperty("idNum", "411328****");
		properties.setProperty("other", "无");

		properties.setProperty("ag_name", "张女士");
		properties.setProperty("ag_sex", "女");
		properties.setProperty("ag_idNum", "411328****");
		properties.setProperty("ag_idType", "身份证");
		properties.setProperty("ag_phone", "15712345678");
		properties.setProperty("ag_telPhone", "0377-66666666");
		properties.setProperty("ag_email", "867096367@qq.com");
		properties.setProperty("ag_other", "无");

		properties.setProperty("req_time", DateUtil.getDateTime());
		properties.setProperty("remark", "无");

		byte[] pdfData = genPdfByTemplate(pdfTemplateData, properties);
		return pdfData;
	}

	public static void main(String[] args) throws Exception {
		// 根据模板制作pdf
		// byte[] pdfData = genPdfTest(Constants.FILE_PATH + "req_con.pdf");
		// FileUtil.storeFile(Constants.FILE_OUT_PATH + "个人信息.pdf", pdfData);
		//
		// byte[] photoData = FileUtil.getFile(Constants.FILE_OUT_PATH + "gs_去底色.png");
		// byte[] addImagePdf = pdfAddImage(pdfData, photoData, 1, 100, 100, 100, 100);
		// FileUtil.storeFile(Constants.FILE_OUT_PATH + "个人信息_addImage.pdf", addImagePdf);

		List list = getTemplateFields(FileUtil.getFile(Constants.FILE_PATH + "req_con.pdf"));
		System.out.println(list);

	}
}
