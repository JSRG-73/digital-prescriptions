package com.jorgerosas.recetas.Models;

import com.jorgerosas.recetas.AppConfig;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class PdfReader {

    private String content;

    public PdfReader(String path){

        try (PDDocument document = PDDocument.load(new File(path))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String s = stripper.getText(document);

            s=removeHeader(s);
            s=removeFooter(s);

            this.content = s;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String removeHeader(String s) {
        return s.lines().skip(7).collect(Collectors.joining("\n"));
    }

    public String removeFooter(String s) {
        int l = s.length();
        return s.substring(0, l - 88);
    }

    public String getContent() {
        return content;
    }

    public static void main(String[] args) {
        String baseDir = AppConfig.getInstance().getBaseDirectory();
        String htmlFilePath = baseDir + File.separator + "Recetas" + File.separator + "a8 28-abril-2025 01-46-01 pm.pdf";

        PdfReader reader = new PdfReader(htmlFilePath);

        System.out.println("\nparchado: " + reader.getContent());
        System.out.println(reader.getContent().length());
    }
}