package Utils.Utility;


import Models.MedicalReport;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import LandingPage.Components.NotificationPopUp;
import com.itextpdf.layout.property.VerticalAlignment;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

// https://stackoverflow.com/questions/61441950/how-to-fill-and-export-simple-jasperreports-report-as-pdf-file

public class PDFReportGenerator {
    private static final Logger LOGGER = CustomLogger.getLogger(PDFReportGenerator.class);
    private static final String LOGO_PATH = "C:\\Users\\andyl\\IdeaProjects\\Cuidarte\\src\\main\\resources\\General\\app-logo.png";
    private static final String PDF_FONT = "Helvetica";

    public static void generateMedicalReport(MedicalReport report, String dest) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            PdfFont font = PdfFontFactory.createFont(PDF_FONT);

            addHeader(document, font);
            addPatientInfo(document, report, font);
            addVitalsTable(document, report, font);
            addTextSection(document, "\nMotivo de consulta", report.getAppointmentMotive(), font);
            addTextSection(document, "\nAlergias", report.getAllergies(), font);
            addTextSection(document, "\nAntecedentes", report.getMedicalHistory(), font);
            addTextSection(document, "\nExploración física", report.getPhysicalExploration(), font);
            addTextSection(document, "\nDiagnóstico", report.getDiagnosis(), font);
            addTextSection(document, "\nTratamiento", report.getTreatment(), font);
            addFooter(document, pdf, font);

            document.close();

            openPDF(dest);
        } catch (IOException | com.itextpdf.io.IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating the PDF file at: " + dest, e);
            e.printStackTrace();
            NotificationPopUp.showErrorMessage(
                    null,
                    "Error",
                    "No se ha podido generar el informe en formato PDF."
            );
        }
    }

    private static void addHeader(Document document, PdfFont font) throws IOException {
        Table table = new Table(2).useAllAvailableWidth();
        table.setBackgroundColor(new DeviceRgb(MAIN_APP_COLOUR));

        Cell imageCell = new Cell().add(new Image(ImageDataFactory.create(LOGO_PATH)))
                .setBorder(Border.NO_BORDER);
        table.addCell(imageCell);

        Cell textCell = new Cell().add(new Paragraph("HISTORIA CLÍNICA")
                        .setFont(font)
                        .setFontSize(16)
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE))
                .setBorder(Border.NO_BORDER)
                .setHeight(60)
                .setPaddingTop(60) // Adjust to center it manually
                .setPaddingBottom(10);

        table.addCell(textCell);


        document.add(table);
    }


    private static void addPatientInfo(Document document, MedicalReport report, PdfFont font) {
        document.add(new Paragraph("\nInformación del Paciente").setFont(font).setBold());
        document.add(new Paragraph("DNI del Paciente: " + report.getPatientDNI()));
        document.add(new Paragraph("Nombre del Médico: " + report.getDoctorDNI()));
        document.add(new Paragraph("Fecha de Consulta: " + formatDate(report.getVisitDate())));
        document.add(new LineSeparator(new SolidLine()));
    }

    private static void addVitalsTable(Document document, MedicalReport report, PdfFont font) {
        document.add(new Paragraph("\nConstantes vitales").setFont(font).setBold());
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

        addVitalsRow(table, "Temperatura", report.getTemperature() + "°C", font);
        addVitalsRow(table, "Peso", report.getWeight() + " kg", font);
        addVitalsRow(table, "Altura", report.getHeight() + " cm", font);
        addVitalsRow(table, "Sist.", report.getSystolic() + " mmHg", font);
        addVitalsRow(table, "Diast.", report.getDiastolic() + " mmHg", font);
        addVitalsRow(table, "Frecuencia cardiaca", report.getHeartRate() + " bpm", font);
        addVitalsRow(table, "Saturación", report.getSaturation() + "%", font);

        document.add(table);
        document.add(new LineSeparator(new SolidLine()));
    }

    private static void addVitalsRow(Table table, String label, String value, PdfFont font) {
        Cell headerCell = new Cell().add(new Paragraph(label).setFont(font).setBold())
                .setBackgroundColor(new DeviceRgb(173, 216, 230))
                .setTextAlignment(TextAlignment.CENTER);
        Cell valueCell = new Cell().add(new Paragraph(value).setFont(font))
                .setTextAlignment(TextAlignment.CENTER);

        table.addCell(headerCell);
        table.addCell(valueCell);
    }

    private static void addTextSection(Document document, String title, String content, PdfFont font) {
        document.add(new Paragraph(title).setFont(font).setBold());
        document.add(new Paragraph(Objects.requireNonNullElse(content, "Sin descripción.")));
        document.add(new LineSeparator(new SolidLine()));
    }

    private static void addFooter(Document document, PdfDocument pdf, PdfFont font) {
        document.add(new Paragraph("Fecha de generación del documento: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))
                .setFont(font).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Página " + pdf.getNumberOfPages()).setFont(font).setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
    }

    private static String formatDate(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp);
    }

    private static void openPDF(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    LOGGER.warning("Desktop is not supported");
                }
            } else {
                LOGGER.warning("File does not exist. Path: " + filePath);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening generated file", e);
        }
    }
}
