package demo;

import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.ImageUtil;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author Zhang Juan
 * @Date 2024/3/27 14:35
 */
public class PDFTest {
    /**
     * 获取签名域的方法：
     * 1.获取所有域，按名字找：reader.getAcroFields();
     */
    @Test
    public void getStampFromPDF1() throws Exception {
        String pdfStampPath = "F:/file/stamp/stamp-rsa.pdf";

        byte[] pdfData = FileUtil.getFile(pdfStampPath);
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfData);
            AcroFields af = reader.getAcroFields();

            ArrayList<String> signNames = af.getSignatureNames();
            // 获取每一个签名域的 Contents
            for (String signName : signNames) {
                System.out.println(signName);
                // 获取 /Annots ->/V的值
                PdfDictionary dictionary = af.getSignatureDictionary(signName);
                String name = dictionary.getAsString(PdfName.NAME).toString();
                String hexContents = HexUtil.byte2Hex(dictionary.getAsString(PdfName.CONTENTS).getBytes());
                String byteRange = dictionary.getAsArray(PdfName.BYTERANGE).toString();
                String filter = dictionary.getAsName(PdfName.FILTER).toString();
                String subFilter = dictionary.getAsName(PdfName.SUBFILTER).toString();
                String time = dictionary.getAsString(PdfName.M).toString();
                System.out.println("---- /Name = " + name);
                System.out.println("---- /Contents = " + hexContents);
                System.out.println("---- /ByteRange = " + byteRange);
                System.out.println("---- /Filter = " + filter);
                System.out.println("---- /SubFilter = " + subFilter);
                System.out.println("---- /M = " + time);
                System.out.println("-----------------------");
            }


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

    /**
     * 获取签名域的方法：
     * 2.遍历每一页，找有/Annots数组的页：reader.getPageN();
     */
    @Test
    public void getStampFromPDF2() throws Exception {
        String pdfStampPath = "F:/file/stamp/stamp-sm2-template.pdf";

        byte[] pdfData = FileUtil.getFile(pdfStampPath);
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfData);
            PdfDictionary pageN = reader.getPageN(1);
            PdfArray annotArray = pageN.getAsArray(PdfName.ANNOTS);
            for (int i = 0; i < annotArray.size(); ++i) {
                // 获取 /Annots ->/T的值
                PdfDictionary signDictionary = annotArray.getAsDict(i);
                PdfString signName = signDictionary.getAsString(PdfName.T);
                System.out.println(signName);

                // 获取 /Annots ->/V的值
                PdfDictionary vDictionary = signDictionary.getAsDict(PdfName.V);
                String name = vDictionary.getAsString(PdfName.NAME).toString();
                String hexContents = HexUtil.byte2Hex(vDictionary.getAsString(PdfName.CONTENTS).getBytes());
                String byteRange = vDictionary.getAsArray(PdfName.BYTERANGE).toString();
                String filter = vDictionary.getAsName(PdfName.FILTER).toString();
                String subFilter = vDictionary.getAsName(PdfName.SUBFILTER).toString();
                String time = vDictionary.getAsString(PdfName.M).toString();
                System.out.println("--V-- /Name = " + name);
                System.out.println("--V-- /Contents = " + hexContents);
                System.out.println("--V-- /ByteRange = " + byteRange);
                System.out.println("--V-- /Filter = " + filter);
                System.out.println("--V-- /SubFilter = " + subFilter);
                System.out.println("--V-- /M = " + time);

                // 获取 /Annots ->/AP的值
                PdfDictionary apDictionary = signDictionary.getAsDict(PdfName.AP);
                PdfStream normalAppearance = apDictionary.getAsStream(PdfName.N);
                PdfDictionary resourcesDic = normalAppearance.getAsDict(PdfName.RESOURCES);

                ExtImageRenderListener listener = new ExtImageRenderListener();
                PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
                processor.processContent(ContentByteUtils.getContentBytesFromContentObject(normalAppearance), resourcesDic);
                PdfImageObject image = listener.image;

                byte[] cleanBGColor = ImageUtil.cleanBGColor(image.getImageAsBytes());
                String photoPath = "F:/temp/pdfParse/"+ signName + ".png";
                FileUtil.storeFile(photoPath, cleanBGColor);
                System.out.println("--AP-- 图片路径 = " + photoPath);
                System.out.println("-----------------------");
            }

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

    class ExtImageRenderListener implements RenderListener {
        public PdfImageObject image = null;
        @Override
        public void beginTextBlock() {

        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {

        }

        @Override
        public void endTextBlock() {

        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
            try {
                image = renderInfo.getImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
