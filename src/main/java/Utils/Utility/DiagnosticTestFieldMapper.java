package Utils.Utility;

import Database.AaModels.TestType;

import java.util.HashMap;
import java.util.Map;

// I need this class in order to translate the fields from the Models to Spanish and
// also assign them the units of each. Also, very useful for new kinds of tests in the future
// It also contains the range of values for each thing

public class DiagnosticTestFieldMapper {
    private static final Map<TestType, String[][]> FIELD_MAPPINGS = new HashMap<>();
    private static final Map<String, double[]> VALUE_RANGES = new HashMap<>();

    static {
        FIELD_MAPPINGS.put(TestType.BLOOD_LAB, new String[][]{
                {"Eritrocitos", "mill/mm³"},
                {"Hemoglobina", "g/dL"},
                {"Hematocrito", "%"},
                {"VCM", "fL"},
                {"HCM", "pg"},
                {"CHCM", "g/dL"},
                {"ADE", "%"},
                {"Plaquetas", "mill/mm³"},
                {"VPM", "fL"},
                {"ADP", "%"},
                {"Plaquetocrito", "%"},
                {"Leucocitos", "mill/mm³"},
                {"Neutrófilos", "%"},
                {"Linfocitos", "%"},
                {"Monocitos", "%"},
                {"Eosinófilos", "%"},
                {"Basófilos", "%"}
        });

        FIELD_MAPPINGS.put(TestType.BIOCHEMISTRY_LAB, new String[][]{
                {"Glucosa", "mg/dL"},
                {"Urea", "mg/dL"},
                {"Creatinina", "mg/dL"},
                {"Ácido Úrico", "mg/dL"},
                {"Colesterol Total", "mg/dL"},
                {"Colesterol HDL", "mg/dL"},
                {"Colesterol LDL", "mg/dL"},
                {"Triglicéridos", "mg/dL"},
                {"GOT (AST)", "U/L"},
                {"GPT (ALT)", "U/L"},
                {"Gamma-GT", "U/L"},
                {"Fosfatasa Alcalina", "U/L"},
                {"Bilirrubina Total", "mg/dL"}
        });

        FIELD_MAPPINGS.put(TestType.IMMUNOLOGY_LAB, new String[][]{
                {"Linfocitos T CD4+", "%"},
                {"Conteo de Linfocitos T CD4+", "cells/μL"},
                {"Linfocitos T CD8+", "%"},
                {"Conteo de Linfocitos T CD8+", "cells/μL"},
                {"Relación CD4/CD8", ""}
        });

        FIELD_MAPPINGS.put(TestType.MICROBIOLOGY_LAB, new String[][]{
                {"Resultado del Cultivo", ""}
        });

        // Hemograma
        VALUE_RANGES.put("Eritrocitos", new double[]{4.2, 5.9});
        VALUE_RANGES.put("Hemoglobina", new double[]{12.0, 16.0});
        VALUE_RANGES.put("Hematocrito", new double[]{37.0, 47.0});
        VALUE_RANGES.put("VCM", new double[]{80.0, 100.0});
        VALUE_RANGES.put("HCM", new double[]{27.0, 31.0});
        VALUE_RANGES.put("CHCM", new double[]{32.0, 36.0});
        VALUE_RANGES.put("ADE", new double[]{11.5, 14.5});
        VALUE_RANGES.put("Plaquetas", new double[]{150.0, 450.0});
        VALUE_RANGES.put("VPM", new double[]{9.0, 13.0});
        VALUE_RANGES.put("ADP", new double[]{10.0, 15.0});
        VALUE_RANGES.put("Plaquetocrito", new double[]{0.2, 0.5});
        VALUE_RANGES.put("Leucocitos", new double[]{4.0, 10.0});
        VALUE_RANGES.put("Neutrófilos", new double[]{40.0, 75.0});
        VALUE_RANGES.put("Linfocitos", new double[]{20.0, 45.0});
        VALUE_RANGES.put("Monocitos", new double[]{2.0, 10.0});
        VALUE_RANGES.put("Eosinófilos", new double[]{1.0, 6.0});
        VALUE_RANGES.put("Basófilos", new double[]{0.5, 1.5});

        // Bioquímica
        VALUE_RANGES.put("Glucosa", new double[]{70.0, 110.0});
        VALUE_RANGES.put("Urea", new double[]{10.0, 50.0});
        VALUE_RANGES.put("Creatinina", new double[]{0.6, 1.3});
        VALUE_RANGES.put("Ácido Úrico", new double[]{2.4, 7.0});
        VALUE_RANGES.put("Colesterol Total", new double[]{125.0, 200.0});
        VALUE_RANGES.put("Colesterol HDL", new double[]{40.0, 60.0});
        VALUE_RANGES.put("Colesterol LDL", new double[]{50.0, 130.0});
        VALUE_RANGES.put("Triglicéridos", new double[]{40.0, 150.0});
        VALUE_RANGES.put("GOT (AST)", new double[]{0.0, 40.0});
        VALUE_RANGES.put("GPT (ALT)", new double[]{0.0, 45.0});
        VALUE_RANGES.put("Gamma-GT", new double[]{0.0, 60.0});
        VALUE_RANGES.put("Fosfatasa Alcalina", new double[]{30.0, 120.0});
        VALUE_RANGES.put("Bilirrubina Total", new double[]{0.2, 1.2});

        // Inmunología
        VALUE_RANGES.put("Linfocitos T CD4+", new double[]{30.0, 60.0});
        VALUE_RANGES.put("Conteo de Linfocitos T CD4+", new double[]{500.0, 1500.0});
        VALUE_RANGES.put("Linfocitos T CD8+", new double[]{20.0, 40.0});
        VALUE_RANGES.put("Conteo de Linfocitos T CD8+", new double[]{200.0, 800.0});
        VALUE_RANGES.put("Relación CD4/CD8", new double[]{0.9, 2.9});
    }

    public static String[][] getFieldMappings(TestType testType) {
        return FIELD_MAPPINGS.getOrDefault(testType, new String[][]{});
    }

    public static double[] getValueRange(String fieldName) {
        return VALUE_RANGES.getOrDefault(fieldName, new double[]{Double.MIN_VALUE, Double.MAX_VALUE});
    }
}
