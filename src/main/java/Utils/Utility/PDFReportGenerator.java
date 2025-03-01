package Utils.Utility;

import Components.NotificationPopUp;
import Database.Models.Enums.TestType;
import Database.Models.MedicalReport;
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
import com.itextpdf.layout.property.VerticalAlignment;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFReportGenerator {
    private static final Logger LOGGER = CustomLogger.getLogger(PDFReportGenerator.class);
    private static final String LOGO_PATH = "C:\\Users\\andyl\\IdeaProjects\\Cuidarte\\src\\main\\resources\\General\\white-app-logo.png";
    private static final String PDF_FONT = "Helvetica";

    public static void generateMedicalReport(MedicalReport report, String destFolder) {
        String fileName = generateUniqueFilename("medicalreport", ".pdf");
        String dest = destFolder + File.separator + fileName;
        generateReport(report, dest, null, null);
    }


    public static void generateDiagnosticTest(Object detailedTest, String destFolder, List<Method> methods, TestType testType) {
        String testName = testType.getValue().toLowerCase();
        String fileName = generateUniqueFilename(testName, ".pdf");
        String dest = destFolder + File.separator + fileName;
        generateReport(detailedTest, dest, methods, testType);
    }

    private static void generateReport(Object report, String dest, List<Method> methods, TestType testType) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            PdfFont font = PdfFontFactory.createFont(PDF_FONT);

            addHeader(document, font, report);

            if (report instanceof MedicalReport medicalReport) {
                generateMedicalReportContent(document, medicalReport, font);
            } else {
                generateDiagnosticTestContent(document, report, methods, testType, font);
            }

            addFooter(document, pdf, font);
            document.close();

            openPDF(dest);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating the PDF file at: " + dest, e);
            NotificationPopUp.showErrorMessage(
                    null,
                    "Error",
                    "Error generando el PDF."
            );
        }
    }

    private static void addHeader(Document document, PdfFont font, Object report) throws IOException {
        Table table = new Table(2).useAllAvailableWidth();
        table.setBackgroundColor(new DeviceRgb(0, 150, 136));

        Cell imageCell = new Cell().add(new Image(ImageDataFactory.create(LOGO_PATH)))
                .setBorder(Border.NO_BORDER);
        table.addCell(imageCell);

        String title = (report instanceof MedicalReport) ? "HISTORIA CLÍNICA" : "INFORME DE PRUEBA DIAGNÓSTICA";

        Cell textCell = new Cell().add(new Paragraph(title)
                        .setFont(font)
                        .setFontSize(16)
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE))
                        .setBorder(Border.NO_BORDER)
                        .setHeight(60)
                        .setPaddingTop(50) // Adjust to center it manually
                        .setPaddingBottom(10);

        table.addCell(textCell);
        document.add(table);
    }

    private static void generateMedicalReportContent(Document document, MedicalReport report, PdfFont font) {
        document.add(new Paragraph("\nInformación del Paciente").setFont(font).setBold());
        document.add(new Paragraph("DNI del Paciente: " + report.getPatientDNI()));
        document.add(new Paragraph("Nombre del Médico: " + report.getDoctorDNI()));
        document.add(new Paragraph("Fecha de Consulta: " + formatDate(report.getVisitDate())));
        document.add(new LineSeparator(new SolidLine()));

        addTextSection(document, "\nMotivo de consulta", report.getAppointmentMotive(), font);
        addTextSection(document, "\nAlergias", report.getAllergies(), font);
        addTextSection(document, "\nAntecedentes", report.getMedicalHistory(), font);
        addTextSection(document, "\nExploración física", report.getPhysicalExploration(), font);
        addTextSection(document, "\nDiagnóstico", report.getDiagnosis(), font);
        addTextSection(document, "\nTratamiento", report.getTreatment(), font);
    }

    private static void generateDiagnosticTestContent(Document document, Object detailedTest,
                                                      List<Method> methods, TestType testType,
                                                      PdfFont font) {
        document.add(new Paragraph("\nInformación de la Prueba Diagnóstica\n").setFont(font).setBold());

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        String[][] fieldMapping = DiagnosticTestFieldMapper.getFieldMappings(testType);
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            String displayName = fieldMapping[i][0];
            String units = fieldMapping[i][1];

            try {
                Object value = method.invoke(detailedTest);

                if (value == null) continue;

                table.addCell(new Cell().add(new Paragraph(displayName)
                                .setFont(font)
                                .setBold())
                        .setBackgroundColor(new DeviceRgb(173, 216, 230))
                        .setTextAlignment(TextAlignment.CENTER));

                table.addCell(new Cell().add(new Paragraph(value.toString() + " " + units)
                                .setFont(font))
                        .setTextAlignment(TextAlignment.CENTER));
            } catch (Exception e) {
                LOGGER.severe("Error retrieving field: " + method + " - " + e.getMessage());
            }
        }
        document.add(table);
    }

    private static void addTextSection(Document document, String title, String content, PdfFont font) {
        document.add(new Paragraph(title).setFont(font).setBold());
        document.add(new Paragraph(content != null ? content : "Sin descripción."));
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
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening generated file", e);
        }
    }

    private static String generateUniqueFilename(String baseName, String extension) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = formatter.format(new Date());

        return baseName + "_" + timestamp + extension;
    }
}
