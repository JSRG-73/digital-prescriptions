package com.jorgerosas.recetas.Models;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PrescriptionBuilder {

    private String html;
    private final String path;

    public PrescriptionBuilder(String path) {
        this.path = path;
    }

    public void replaceDataShort(String patient, String date, String description) {
        html = html.replace("{{patient}}", patient);
        html = html.replace("{{date}}", date);
        html = html.replace("{{description}}", description);
    }

    public void readHtmlFromResources() {   //Converts html file to java String

        StringBuilder content = new StringBuilder();
        String line;

        try (InputStream inputStream = PrescriptionBuilder.class.getResourceAsStream(this.path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            while ((line = reader.readLine()) != null) {content.append(line).append("\n");}

        } catch (IOException | NullPointerException e) {
            System.out.println(e); }

        this.html = content.toString();
    }


    public String getHtml() {
        return html;
    }

    public void generateHTML(String filePath) {
        try {
            File dir = new File(filePath).getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(html);
                System.out.println("HTML file generated successfully at: " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Error writing HTML file: " + e.getMessage());
        }
    }

    public void generatePDF(String filePath) {
        try {
            // Create parent directories if they don't exist
            File dir = new File(filePath).getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Fix HTML to ensure proper XML compliance and add print-specific styles
            String fixedHtml = fixHtmlForXmlCompliance(html);

            // Create an output stream for the PDF
            try (OutputStream os = new FileOutputStream(filePath)) {
                // Create a PDF renderer builder
                PdfRendererBuilder builder = new PdfRendererBuilder();

                // Get the base URI for resolving resources
                String baseUri = new File(filePath).getParentFile().toURI().toString();

                // Set the HTML content with base URI
                builder.withHtmlContent(fixedHtml, baseUri);
                
                // Set output stream
                builder.toStream(os);

                // Run the renderer
                builder.run();

                System.out.println("PDF file generated successfully at: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("Error generating PDF file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String fixHtmlForXmlCompliance(String html) {
        // Replace standalone <br> tags with self-closing <br/> tags
        html = html.replaceAll("<br>", "<br/>");
        html = html.replaceAll("<br\\s+>", "<br/>");

        // Replace other common HTML elements that might not be properly closed
        html = html.replaceAll("<hr>", "<hr/>");
        html = html.replaceAll("<img([^>]*)>", "<img$1/>");
        html = html.replaceAll("<input([^>]*)>", "<input$1/>");

        // Make sure meta tags are properly closed
        html = html.replaceAll("<meta([^>]*)>", "<meta$1/>");

        // Make sure link tags are properly closed
        html = html.replaceAll("<link([^>]*)>", "<link$1/>");

        return html;
    }

    public static void main(String[] args) {
        // Initialize PrescriptionBuilder with the path to the HTML template
        PrescriptionBuilder pb = new PrescriptionBuilder("/templates/UC/UC.html");

        // Load the HTML template
        pb.readHtmlFromResources();

        // Replace placeholder data in the HTML
        pb.replaceDataShort("Jorge Rosas", "12/12/12", "Descripción");

        // Define file paths for the generated HTML and PDF
        String htmlFilePath = "templates/UC/UC-1.html";
        String pdfFilePath = "templates/UC/UC-1.pdf";

        // Generate HTML
        pb.generateHTML(htmlFilePath);

        // Generate PDF
        pb.generatePDF(pdfFilePath);
    }
}
