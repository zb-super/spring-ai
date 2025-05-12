package com.zb.example.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;

import java.util.List;

public class MyPagePdfDocumentReader {
    public static List<Document> getDocsFromPdf() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/海阳市政府采购招标文件.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        return pdfReader.read();
    }

    public static void main(String[] args) {
        List<Document> docsFromPdf = MyPagePdfDocumentReader.getDocsFromPdf();
        System.out.println(1);
    }
}
