package models;

import utils.DataSet;

import java.util.ArrayList;

public class DataSetFunctions {

    public static void validateDataSet(DataSet dataSet) throws Exception {
        if ( dataSet == null ) {
            throw new Exception("The data set can not be null");
        }
        if ( dataSet.getInstances() == null || dataSet.getInstances().isEmpty() ) {
            throw new Exception("The data set is empty");
        }
        if ( dataSet.getHeaders() == null || dataSet.getHeaders().isEmpty() ) {
            throw new Exception("The data set not contains the headers");
        }
        if ( dataSet.getAttributeTypes() == null || dataSet.getAttributeTypes().isEmpty() ) {
            throw new Exception("The data set not contains the types of atributes");
        }
        if ( dataSet.getTarget() == null ) {
            throw new Exception("The data set not contains the target");
        }
    }

    public static boolean isEqualsTheDataSets(DataSet a, DataSet b) {
        for ( int i = 0; i < a.getHeaders().size(); i++ ) {
            if ( !a.getHeaders().get(i).equalsIgnoreCase(b.getHeaders().get(i)) ) {
                return false;
            }
        }
        return a.getHeaders().size() == b.getHeaders().size();
    }

    public static int getIndexAtAtributeFromDataSet(ArrayList<String> headers, String atribute) {
        for ( int i = 0; i < headers.size(); i++ ) {
            if ( headers.get(i).equals(atribute) ) {
                return i;
            }
        }
        return -1;
    }

    public static double[] generateNumericVector(DataSet dataSet, int idxColumn) throws Exception {
        if ( idxColumn < 0 || idxColumn >= dataSet.getHeaders().size() ) {
            throw new Exception("Column index out of range");
        }
        double[] values = new double[dataSet.getInstances().size()];
        for ( int i = 0; i < dataSet.getInstances().size(); i++ ) {
            values[i] = Double.parseDouble(dataSet.getInstances().get(i).get(idxColumn));
        }
        return values;
    }

    public static double[][] generateNumericMatrix(DataSet dataSet, int... columns) throws Exception {
        for ( int column : columns ) {
            if ( column < 0 || column >= dataSet.getHeaders().size() ) {
                throw new Exception("Column index (" + column + ") out of range (" + (dataSet.getHeaders().size() - 1) +")");
            }
        }
        double[][] matrix = new double[dataSet.getInstances().size()][columns.length];
        for ( int i = 0; i < columns.length; i++ ) {
            for ( int j = 0; j < dataSet.getInstances().size(); j++ ) {
                matrix[j][i] = Double.parseDouble(dataSet.getInstances().get(j).get(columns[i]));
            }
        }
        return matrix;
    }
}
