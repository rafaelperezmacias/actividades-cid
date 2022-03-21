package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class DataSet {

    private ArrayList<ArrayList<String>> instances;

    private ArrayList<String> headers;

    private ArrayList<String> attributeTypes;

    private String target;

    private final int WIDTH_COLUMN = 12;

    public static final String NUMERIC_TYPE = "Numeric";

    private DataSet()
    {

    }

    public ArrayList<ArrayList<String>> getInstances() {
        return instances;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    public ArrayList<String> getAttributeTypes() {
        return attributeTypes;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if ( headers != null && headers.size() > 0 ) {
            headers.forEach(header -> builder.append(textFormatter(header, WIDTH_COLUMN)) );
            builder.append("\n");
        }
        if ( attributeTypes != null && attributeTypes.size() > 0 ) {
            attributeTypes.forEach(header -> builder.append(textFormatter(header, WIDTH_COLUMN)) );
            builder.append("\n");
        }
        if ( instances != null && instances.size() > 0 ) {
            instances.forEach(instance -> {
                instance.forEach(value -> builder.append(textFormatter(value, WIDTH_COLUMN)) );
                builder.append("\n");
            });
        }
        return builder.toString();
    }

    private static String textFormatter(String text, int length) {
        StringBuilder result = new StringBuilder();
        result.append(text);
        if (result.length() > length ) {
            result.delete(length - 3, result.length());
            result.append(".").append(".").append(".");
        } else {
            while ( result.length() < length ) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    // Sales = 168 + 23 Advertising
    public static DataSet getBenettonDataSetForSLR() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("Sales", "Advertising"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("651", "23") ),
                new ArrayList<>( Arrays.asList("762", "26") ),
                new ArrayList<>( Arrays.asList("856", "30") ),
                new ArrayList<>( Arrays.asList("1063", "34") ),
                new ArrayList<>( Arrays.asList("1190", "43") ),
                new ArrayList<>( Arrays.asList("1298", "48") ),
                new ArrayList<>( Arrays.asList("1421", "52") ),
                new ArrayList<>( Arrays.asList("1440", "57") ),
                new ArrayList<>( Arrays.asList("1518", "58") )
        ) );
        dataSet.target = "Sales";
        return dataSet;
    }

    // Sales = 323 + 14 Advertising + 47 Year
    public static DataSet getBennetonDataSetForMLR() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("Year", "Advertising", "Sales"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Numeric"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("1", "23", "651") ),
                new ArrayList<>( Arrays.asList("2", "26", "762") ),
                new ArrayList<>( Arrays.asList("3", "30", "856") ),
                new ArrayList<>( Arrays.asList("4", "34", "1063") ),
                new ArrayList<>( Arrays.asList("5", "43", "1190") ),
                new ArrayList<>( Arrays.asList("6", "48", "1298") ),
                new ArrayList<>( Arrays.asList("7", "52", "1421") ),
                new ArrayList<>( Arrays.asList("8", "57", "1440") ),
                new ArrayList<>( Arrays.asList("9", "58", "1518") )
        ) );
        dataSet.target = "Sales";
        return dataSet;
    }

    // y = -153.51 + 1.24 * x_1 + 12.08 * x_2
    public static DataSet getExperimentDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("Factor_1", "Factor_2", "Yield"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Numeric"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("41.9", "29.1", "251.3") ),
                new ArrayList<>( Arrays.asList("43.4", "29.3", "251.3") ),
                new ArrayList<>( Arrays.asList("43.9", "29.5", "248.3") ),
                new ArrayList<>( Arrays.asList("44.5", "29.7", "267.5") ),
                new ArrayList<>( Arrays.asList("47.3", "29.9", "273.0") ),
                new ArrayList<>( Arrays.asList("47.5", "30.3", "276.5") ),
                new ArrayList<>( Arrays.asList("47.9", "30.5", "270.3") ),
                new ArrayList<>( Arrays.asList("50.2", "30.7", "274.9") ),
                new ArrayList<>( Arrays.asList("52.8", "30.8", "285.0") ),
                new ArrayList<>( Arrays.asList("53.2", "30.9", "290.0") ),
                new ArrayList<>( Arrays.asList("56.7", "31.5", "297.0") ),
                new ArrayList<>( Arrays.asList("57.0", "31.7", "302.5") ),
                new ArrayList<>( Arrays.asList("63.5", "31.9", "304.5") ),
                new ArrayList<>( Arrays.asList("65.3", "32.0", "309.3") ),
                new ArrayList<>( Arrays.asList("71.1", "32.1", "321.7") ),
                new ArrayList<>( Arrays.asList("77.0", "32.5", "330.7") ),
                new ArrayList<>( Arrays.asList("77.8", "32.9", "349.0") )
        ) );
        dataSet.target = "Yield";
        return dataSet;
    }

}
