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
            fixedHtml = addPrintStylesheet(fixedHtml);

            // Create an output stream for the PDF
            try (OutputStream os = new FileOutputStream(filePath)) {
                // Create a PDF renderer builder
                PdfRendererBuilder builder = new PdfRendererBuilder();

                // Get the base URI for resolving resources
                String baseUri = new File(filePath).getParentFile().toURI().toString();

                // Set the HTML content with base URI
                builder.withHtmlContent(fixedHtml, baseUri);

                // Enable CSS support (helps with some CSS properties)
                builder.useCacheStore(new com.openhtmltopdf.cache.FSCacheEx() {});

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

    private String addPrintStylesheet(String html) {
        String printCss = "<style type=\"text/css\" media=\"print\">\n" +
                "/* Print-specific styles that avoid flexbox */\n" +
                "body {\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "    font-family: 'Poppins', sans-serif;\n" +
                "}\n" +
                ".container {\n" +
                "    width: 100%;\n" +
                "    margin: 0 auto;\n" +
                "    padding: 20px;\n" +
                "}\n" +
                ".header {\n" +
                "    width: 100%;\n" +
                "    position: relative;\n" +
                "    margin-bottom: 20px;\n" +
                "    overflow: hidden;\n" +
                "}\n" +
                ".logo {\n" +
                "    float: left;\n" +
                "    width: 100px;\n" +
                "    margin-right: 20px;\n" +
                "}\n" +
                ".doctor-info {\n" +
                "    float: left;\n" +
                "    width: 60%;\n" +
                "}\n" +
                ".patient-info {\n" +
                "    clear: both;\n" +
                "    margin-top: 20px;\n" +
                "    padding-top: 20px;\n" +
                "    border-top: 1px solid #ccc;\n" +
                "}\n" +
                ".content {\n" +
                "    margin-top: 20px;\n" +
                "    padding: 20px 0;\n" +
                "    border-top: 1px solid #ccc;\n" +
                "}\n" +
                ".footer {\n" +
                "    margin-top: 30px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                ".signature {\n" +
                "    margin-top: 50px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "</style>";

        // Insert print stylesheet before the closing head tag
        return html.replace("</head>", printCss + "</head>");
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
