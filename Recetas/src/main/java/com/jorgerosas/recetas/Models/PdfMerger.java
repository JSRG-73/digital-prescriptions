package com.jorgerosas.recetas.Models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jorgerosas.recetas.AppConfig;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessStreamCache;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PdfMerger {

    public static void merge(List<String> fileNames, String folderPath) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();

        // destination
        String destination = folderPath + File.separator + "MergedPdf.pdf";
        merger.setDestinationFileName(destination);

        // sources
        for (String name : fileNames) {
            File src = new File(folderPath, name);
            if (!src.exists() || !src.isFile()) {
                throw new IOException("Not found: " + src.getAbsolutePath());
            }
            merger.addSource(src);
        }

        // use memory-only caching
        RandomAccessStreamCache.StreamCacheCreateFunction cacheFactory =
                IOUtils.createMemoryOnlyStreamCache();
        merger.mergeDocuments(cacheFactory);

        System.out.println("Merged " + fileNames.size() + " files into " + destination);
    }

}
