package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class DataSet {

    private ArrayList<ArrayList<String>> instances;

    private ArrayList<String> headers;

    private ArrayList<String> attributeTypes;

    private String target;

    private final int WIDTH_COLUMN = 16;

    public static final String NUMERIC_TYPE = "Numeric";
    public static final String CATEGORICAL_TYPE = "Categorical";

    private DataSet() {

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
        if (headers != null && headers.size() > 0) {
            headers.forEach(header -> builder.append(textFormatter(header, WIDTH_COLUMN)));
            builder.append("\n");
        }
        if (attributeTypes != null && attributeTypes.size() > 0) {
            attributeTypes.forEach(header -> builder.append(textFormatter(header, WIDTH_COLUMN)));
            builder.append("\n");
        }
        if (instances != null && instances.size() > 0) {
            instances.forEach(instance -> {
                instance.forEach(value -> builder.append(textFormatter(value, WIDTH_COLUMN)));
                builder.append("\n");
            });
        }
        return builder.toString();
    }

    private static String textFormatter(String text, int length) {
        StringBuilder result = new StringBuilder();
        result.append(text);
        if (result.length() > length) {
            result.delete(length - 3, result.length());
            result.append(".").append(".").append(".");
        } else {
            while (result.length() < length) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public static DataSet copyDataSet(DataSet copyDataSet) throws Exception {
        if ( copyDataSet == null ) {
            throw new Exception("The data set cannot be null");
        }
        ArrayList<String> headers = new ArrayList<>(copyDataSet.headers);
        ArrayList<ArrayList<String>> instances = new ArrayList<>();
        for (int i = 0; i < copyDataSet.getInstances().size(); i++) {
            instances.add(new ArrayList<>(copyDataSet.getInstances().get(i)));
        }
        ArrayList<String> atributeTypes = new ArrayList<>(copyDataSet.attributeTypes);
        DataSet newDataSet = new DataSet();
        newDataSet.headers = headers;
        newDataSet.attributeTypes = atributeTypes;
        newDataSet.instances = instances;
        newDataSet.target = copyDataSet.target;
        return newDataSet;
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

    public static DataSet getFourVariablesDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("exam_1", "exam_2","exam_3","final"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Numeric","Numeric"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("73", "80", "75", "152") ),
                new ArrayList<>( Arrays.asList("93", "88", "93", "185") ),
                new ArrayList<>( Arrays.asList("89", "91", "90", "180") ),
                new ArrayList<>( Arrays.asList("96", "98", "100", "196") ),
                new ArrayList<>( Arrays.asList("73", "66", "70", "142") ),
                new ArrayList<>( Arrays.asList("53", "46", "55", "101") ),
                new ArrayList<>( Arrays.asList("69", "74", "77", "149") ),
                new ArrayList<>( Arrays.asList("47", "56", "60", "115") ),
                new ArrayList<>( Arrays.asList("87", "79", "90", "175") ),
                new ArrayList<>( Arrays.asList("79", "70", "88", "164") ),
                new ArrayList<>( Arrays.asList("69", "70", "73", "141") ),
                new ArrayList<>( Arrays.asList("70", "65", "74", "141") ),
                new ArrayList<>( Arrays.asList("93", "95", "91", "184") ),
                new ArrayList<>( Arrays.asList("79", "80", "73", "152") ),
                new ArrayList<>( Arrays.asList("70", "73", "78", "148") ),
                new ArrayList<>( Arrays.asList("93", "89", "96", "192") ),
                new ArrayList<>( Arrays.asList("78", "75", "68", "147") ),
                new ArrayList<>( Arrays.asList("81", "90", "93", "183") ),
                new ArrayList<>( Arrays.asList("88", "92", "86", "177") ),
                new ArrayList<>( Arrays.asList("78", "83", "77", "159") ),
                new ArrayList<>( Arrays.asList("82", "86", "90", "177") ),
                new ArrayList<>( Arrays.asList("86", "82", "89", "175") ),
                new ArrayList<>( Arrays.asList("78", "83", "85", "175") ),
                new ArrayList<>( Arrays.asList("76", "83", "71", "149") ),
                new ArrayList<>( Arrays.asList("96", "93", "95", "192") )
        ) );
        dataSet.target = "final";
        return dataSet;
    }

    public static DataSet getSixVariablesDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("anual-sales", "number sq. ft", "inventory", "advertising", "size of sales", "number of competing stores"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Numeric","Numeric","Numeric","Numeric"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("231", "3", "294", "8.2", "8.2" , "11") ),
                new ArrayList<>( Arrays.asList("156", "2.2", "232", "6.9", "4.1" , "12") ),
                new ArrayList<>( Arrays.asList("10", "0.5", "149", "3", "4.3" , "15") ),
                new ArrayList<>( Arrays.asList("519", "5.5", "600", "12", "16.1" , "1") ),
                new ArrayList<>( Arrays.asList("437", "4.4", "567", "10.6", "14.1" , "5") ),
                new ArrayList<>( Arrays.asList("487", "4.8", "571", "11.8", "12.7" , "4") ),
                new ArrayList<>( Arrays.asList("299", "3.1", "512", "8.1", "10.1" , "10") ),
                new ArrayList<>( Arrays.asList("195", "2.5", "347", "7.7", "8.4" , "12") ),
                new ArrayList<>( Arrays.asList("20", "1.2", "212", "3.3", "2.1" , "15") ),
                new ArrayList<>( Arrays.asList("68", "0.6", "102", "4.9", "4.7" , "8") ),
                new ArrayList<>( Arrays.asList("570", "5.4", "788", "17.4", "12.3" , "1") ),
                new ArrayList<>( Arrays.asList("428", "4.2", "577", "10.5", "15" , "7") ),
                new ArrayList<>( Arrays.asList("464", "4.7", "535", "11.3", "15" , "3") ),
                new ArrayList<>( Arrays.asList("15", "0.6", "163", "2.5", "2.5" , "14") ),
                new ArrayList<>( Arrays.asList("65", "1.2", "168", "4.7", "3.3" , "11") ),
                new ArrayList<>( Arrays.asList("98", "1.6", "151", "4.6", "2.7" , "10") ),
                new ArrayList<>( Arrays.asList("368", "4.3", "342", "5.5", "16" , "4") ),
                new ArrayList<>( Arrays.asList("161", "2.6", "196", "7.2", "6.3" , "13") ),
                new ArrayList<>( Arrays.asList("397", "3.8", "453", "10.4", "13.9" , "7") ),
                new ArrayList<>( Arrays.asList("497", "5.3", "518", "11.5", "16.3" , "1") ),
                new ArrayList<>( Arrays.asList("528", "5.6", "615", "12.3", "16" , "0") ),
                new ArrayList<>( Arrays.asList("99", "0.8", "278", "2.8", "6.5" , "14") ),
                new ArrayList<>( Arrays.asList("0.5", "1.1", "142", "3.1", "1.6" , "12") ),
                new ArrayList<>( Arrays.asList("347", "3.6", "461", "9.6", "11.3" , "6") ),
                new ArrayList<>( Arrays.asList("341", "3.5", "382", "9.8", "11.5" , "5") ),
                new ArrayList<>( Arrays.asList("507", "5.1", "590", "12", "15.7" , "0") ),
                new ArrayList<>( Arrays.asList("400", "8.6", "517", "7", "12" , "8") )
        ) );
        dataSet.target = "anual-sales";
        return dataSet;
    }


    // After 100 iterations => ( -2.707, .9207, .9207 )
    public static DataSet getNBADataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("numberHF","numberAS","winner"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Categorical"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("1","1","0") ),
                new ArrayList<>( Arrays.asList("4","2","1") ),
                new ArrayList<>( Arrays.asList("2","4","1") )
        ) );
        dataSet.target = "winner";
        return dataSet;
    }

    // (admitted 1) (Reject 0)
    public static DataSet getUniversityDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("gmat","gpa","work_experience","admitted"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Numeric","Categorical"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("780","4.0","3","1") ),
                new ArrayList<>( Arrays.asList("750","3.9","4","1") ),
                new ArrayList<>( Arrays.asList("690","3.3","3","0") ),
                new ArrayList<>( Arrays.asList("710","3.7","5","1") ),
                new ArrayList<>( Arrays.asList("680","3.9","4","0") ),
                new ArrayList<>( Arrays.asList("730","3.7","6","1") ),
                new ArrayList<>( Arrays.asList("690","2.3","1","0") ),
                new ArrayList<>( Arrays.asList("720","3.3","4","1") ),
                new ArrayList<>( Arrays.asList("740","3.3","5","1") ),
                new ArrayList<>( Arrays.asList("690","1.7","1","0") ),
                new ArrayList<>( Arrays.asList("610","2.7","3","0") ),
                new ArrayList<>( Arrays.asList("690","3.7","5","1") ),
                new ArrayList<>( Arrays.asList("710","3.7","6","1") ),
                new ArrayList<>( Arrays.asList("680","3.3","4","0") ),
                new ArrayList<>( Arrays.asList("770","3.3","3","1") ),
                new ArrayList<>( Arrays.asList("610","3.0","1","0") ),
                new ArrayList<>( Arrays.asList("580","2.7","4","0") ),
                new ArrayList<>( Arrays.asList("650","3.7","6","1") ),
                new ArrayList<>( Arrays.asList("540","2.7","2","0") ),
                new ArrayList<>( Arrays.asList("590","2.3","3","0") ),
                new ArrayList<>( Arrays.asList("620","3.3","2","1") ),
                new ArrayList<>( Arrays.asList("600","2.0","1","0") ),
                new ArrayList<>( Arrays.asList("550","2.3","4","0") ),
                new ArrayList<>( Arrays.asList("550","2.7","1","0") ),
                new ArrayList<>( Arrays.asList("570","3.0","2","0") ),
                new ArrayList<>( Arrays.asList("670","3.3","6","1") ),
                new ArrayList<>( Arrays.asList("660","3.7","4","1") ),
                new ArrayList<>( Arrays.asList("580","2.3","2","0") ),
                new ArrayList<>( Arrays.asList("650","3.7","6","1") ),
                new ArrayList<>( Arrays.asList("660","3.3","5","1") ),
                new ArrayList<>( Arrays.asList("640","3.0","1","0") ),
                new ArrayList<>( Arrays.asList("620","2.7","2","0") ),
                new ArrayList<>( Arrays.asList("660","4.0","4","1") ),
                new ArrayList<>( Arrays.asList("660","3.3","6","1") ),
                new ArrayList<>( Arrays.asList("680","3.3","5","1") ),
                new ArrayList<>( Arrays.asList("650","2.3","1","0") ),
                new ArrayList<>( Arrays.asList("670","2.7","2","0") ),
                new ArrayList<>( Arrays.asList("580","3.3","1","0") ),
                new ArrayList<>( Arrays.asList("590","1.7","4","0") ),
                new ArrayList<>( Arrays.asList("690","3.7","5","1") )
        ) );
        dataSet.target = "admitted";
        return dataSet;
    }

    public static DataSet getIrisDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.headers = new ArrayList<>(Arrays.asList("Sepal Length","Sepal Width","Species"));
        dataSet.attributeTypes = new ArrayList<>(Arrays.asList("Numeric","Numeric","Categorical"));
        dataSet.instances = new ArrayList<>( Arrays.asList(
                new ArrayList<>( Arrays.asList("5.3","3.7","Setosa") ),
                new ArrayList<>( Arrays.asList("5.1","3.8","Setosa") ),
                new ArrayList<>( Arrays.asList("7.2","3.0","Virginica") ),
                new ArrayList<>( Arrays.asList("5.4","3.4","Setosa") ),
                new ArrayList<>( Arrays.asList("5.1","3.3","Setosa") ),
                new ArrayList<>( Arrays.asList("5.4","3.9","Setosa") ),
                new ArrayList<>( Arrays.asList("7.4","2.8","Virginica") ),
                new ArrayList<>( Arrays.asList("6.1","2.8","Versicolor") ),
                new ArrayList<>( Arrays.asList("7.3","2.9","Virginica") ),
                new ArrayList<>( Arrays.asList("6.0","2.7","Versicolor") ),
                new ArrayList<>( Arrays.asList("5.8","2.8","Virginica") ),
                new ArrayList<>( Arrays.asList("6.3","2.3","Versicolor") ),
                new ArrayList<>( Arrays.asList("5.1","2.5","Versicolor") ),
                new ArrayList<>( Arrays.asList("6.3","2.5","Versicolor") ),
                new ArrayList<>( Arrays.asList("5.5","2.4","Versicolor") )
        ) );
        dataSet.target = "Species";
        return dataSet;
    }

}
