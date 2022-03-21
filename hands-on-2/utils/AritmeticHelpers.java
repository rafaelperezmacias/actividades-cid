package utils;

public class AritmeticHelpers {

    public static double sumSummation(double[]... vectors) throws Exception {
        for ( int i = 1; i < vectors.length; i++ ) {
            if ( vectors[i].length != vectors[0].length ) {
                throw new Exception("Some of the vectors do not have the same size");
            }
        }
        double result = 0;
        for ( int i = 0; i < vectors[0].length; i++ ) {
            for ( int j = 0; j < vectors.length; j++ ) {
                result += vectors[j][i];
            }
        }
        return result;
    }

    public static double productSummation(double[]... vectors) throws Exception {
        for ( int i = 1; i < vectors.length; i++ ) {
            if ( vectors[i].length != vectors[0].length ) {
                throw new Exception("Some of the vectors do not have the same size");
            }
        }
        double result = 0;
        for ( int i = 0; i < vectors[0].length; i++ ) {
            double product = 1;
            for ( int j = 0; j < vectors.length; j++ ) {
                product *= vectors[j][i];
            }
            result += product;
        }
        return result;
    }

    public static double squareSummation(double[]... vectors) throws Exception {
        for ( int i = 1; i < vectors.length; i++ ) {
            if ( vectors[i].length != vectors[0].length ) {
                throw new Exception("Some of the vectors do not have the same size");
            }
        }
        double result = 0;
        for ( int i = 0; i < vectors[0].length; i++ ) {
            for ( int j = 0; j < vectors.length; j++ ) {
                result += (vectors[j][i] * vectors[j][i]);
            }
        }
        return result;
    }
}
