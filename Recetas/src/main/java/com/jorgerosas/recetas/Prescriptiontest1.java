package com.jorgerosas.recetas;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;

public class Prescriptiontest1 {
    public static void generatePrescription(String doctorName, String patientName, String age, String medicine, String dosage, String instructions) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText("Unidad de Cancerología");
                contentStream.endText();

                // Doctor's Name
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 720);
                contentStream.showText("Doctor: " + doctorName);
                contentStream.endText();

                // Line separator
                contentStream.moveTo(50, 710);
                contentStream.lineTo(550, 710);
                contentStream.stroke();

                // Patient Details
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 690);
                contentStream.showText("Paciente: " + patientName);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Edad: " + age);
                contentStream.endText();

                // Medicine and Dosage
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 640);
                contentStream.showText("Medicamento: " + medicine);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Dosis: " + dosage);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Instrucciones: " + instructions);
                contentStream.endText();

                // Footer
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 100);
                contentStream.showText("________________________");
                contentStream.endText();
            }

            document.save("prescription.pdf");
            System.out.println("PDF generated successfully: prescription.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generatePrescription("Dr. Eduardo Barragán Curiel", "Pedro Pérez", "45", "Paracetamol", "500mg", "Tomar dos pastillas cada 8 horas.");
    }
}
