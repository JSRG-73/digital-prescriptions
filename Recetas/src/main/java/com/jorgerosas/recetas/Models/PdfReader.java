package com.jorgerosas.recetas.Models;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class PdfReader {

    private String content;

    public PdfReader(String path) {
        try (PDDocument document = Loader.loadPDF(new File(path))) { 
            PDFTextStripper stripper = new PDFTextStripper();
            String s = stripper.getText(document);

            s = removeHeader(s);

            this.content = s;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String removeHeader(String s) {
        return s.lines().skip(7).collect(Collectors.joining("\n"));
    }

    public String getContent() {
        return content;
    }
}