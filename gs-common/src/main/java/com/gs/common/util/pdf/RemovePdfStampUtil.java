package com.gs.common.util.pdf;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.gs.common.util.FileUtil;
import com.itextpdf.text.pdf.*;

public class RemovePdfStampUtil {

	public static byte[] removeStamp(byte[] pdfData) throws Exception {
		List<Integer> rangeList = getPdfStampByteRange(pdfData);

		switch (rangeList.size()) {
		case 0:
			throw new Exception("没有找到签章信息");
		case 1:
			return removeOneOrAll(pdfData);
		default:
			int length = rangeList.get(rangeList.size() - 2);
			return removeLast(pdfData, length);
		}
	}

	private static byte[] removeOneOrAll(byte[] pdfData) throws Exception {
		PdfReader reader = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			reader = new PdfReader(pdfData);
			PdfStamper stamper = new PdfStamper(reader, baos);
			AcroFields acroFields = reader.getAcroFields();

			ArrayList<String> names = acroFields.getSignatureNames();
			for (String name : names)
				acroFields.removeField(name);

			stamper.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
	}

	private static byte[] removeLast(byte[] pdfData, int length) throws Exception {
		// [offset1,len1,offset2,len2]   offset2 + len2 = length
		byte[] data = new byte[length];
		System.arraycopy(pdfData, 0, data, 0, length);
		return data;
	}

	private static List<Integer> getPdfStampByteRange(byte[] pdfData) throws Exception {
		PdfReader reader = null;
		List<Integer> rangeList = new ArrayList<>();

		try {
			reader = new PdfReader(pdfData);
			AcroFields af = reader.getAcroFields();

			ArrayList<String> names = af.getSignatureNames();
			// 获取每一个签名域的 byteRange
			for (String name : names) {
				PdfDictionary dictionary = af.getSignatureDictionary(name);
				PdfArray ro = dictionary.getAsArray(PdfName.BYTERANGE);
				// [offset1,len1,offset2,len2]
				rangeList.add(ro.getAsNumber(2).intValue() + ro.getAsNumber(3).intValue());
			}

			return rangeList;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String pdfStamp = "f:/temp/stamp_2.pdf";
		byte[] data = RemovePdfStampUtil.removeStamp(FileUtil.getFile(pdfStamp));
		FileUtil.storeFile("f:/temp/stamp_remove.pdf", data);
	}
}
