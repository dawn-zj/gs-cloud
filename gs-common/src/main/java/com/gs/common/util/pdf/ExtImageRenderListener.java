package com.gs.common.util.pdf;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.IOException;

/**
 * @Author Zhang Juan
 * @Date 2024/3/27 15:41
 */
public class ExtImageRenderListener implements RenderListener {
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
