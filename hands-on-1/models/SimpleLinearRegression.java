package models;

import utils.AritmeticHelpers;
import utils.DataSet;

import java.util.ArrayList;

public class SimpleLinearRegression extends Regression {

    private SimpleLinearRegression()
    {

    }

    public static Model generateModel(DataSet dataSet) throws Exception {
        SimpleLinearRegression slr = new SimpleLinearRegression();
        return (Model) slr.makeModel(dataSet);
    }

    @Override
    protected AbstractModel<Double> makeModel(DataSet dataSet) throws Exception {
        validateDataSet(dataSet);
        int idxY = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());
        if ( idxY == -1 ) {
            throw new Exception("Target not found");
        }

        int idxX = ( idxY == 1 ) ? 0 : 1;

        double[] XVector = DataSetFunctions.generateNumericVector(dataSet, idxX);
        double[] YVector = DataSetFunctions.generateNumericVector(dataSet, idxY);

        double summationXY = AritmeticHelpers.productSummation(XVector, YVector);
        double summationX = AritmeticHelpers.sumSummation(XVector);
        double summationY = AritmeticHelpers.sumSummation(YVector);
        double summationSquareX = AritmeticHelpers.squareSummation(XVector);

        int n = dataSet.getInstances().size();

        double beta1 = ( (n * summationXY) - (summationX * summationY) ) / ((n * summationSquareX) - (summationX * summationX));
        double beta0 = ( summationY - ( beta1 * summationX ) ) / n;

        return new SimpleLinearRegression.Model(dataSet,beta0,beta1, idxX);
    }

    private static void validateDataSet(DataSet dataSet) throws Exception {
        DataSetFunctions.validateDataSet(dataSet);
        for ( String atributeType : dataSet.getAttributeTypes() ) {
            if ( !atributeType.equalsIgnoreCase(DataSet.NUMERIC_TYPE) ) {
                throw new Exception("The data set contains non-numeric attributes");
            }
        }
        if ( dataSet.getHeaders().size() != 2 ) {
            throw new Exception("The data set can have only two attributes");
        }
    }

    public static class Model implements AbstractModel<Double> {

        private DataSet dataSet;
        private double beta0;
        private double beta1;
        private int idxX;

        public Model(DataSet dataSet, double beta0, double beta1, int idxX)
        {
            this.dataSet = dataSet;
            this.beta0 = beta0;
            this.beta1 = beta1;
            this.idxX = idxX;
        }

        @Override
        public Double predict(Object... instance) throws Exception {
            if ( instance.length != dataSet.getHeaders().size() - 1) {
                throw new Exception("The instance is not the same size as the data set (" + instance.length + " - " + ( dataSet.getHeaders().size() - 1) + ")");
            }
            if ( !(instance[0] instanceof Double) ) {
                throw new Exception("The prediction admits only numerical values");
            }
            return beta0 + ((double) instance[0] * beta1);
        }

        @Override
        public void predict(DataSet dataSet, String classNameOut) throws Exception {
            validateDataSet(dataSet);
            if ( !DataSetFunctions.isEqualsTheDataSets(this.dataSet, dataSet) ) {
                throw new Exception("The data sets are not equal");
            }
            dataSet.getHeaders().add(classNameOut);
            dataSet.getAttributeTypes().add(DataSet.NUMERIC_TYPE);
            for ( ArrayList<String> instance : dataSet.getInstances() ) {
                double x = Double.parseDouble(instance.get(idxX));
                instance.add(String.valueOf(predict(x)));
            }
        }

        @Override
        public String toString() {
            String formula = dataSet.getHeaders().get( (idxX == 1) ? 0 : 1 ) + " = ";
            formula += beta0 + " + " + beta1 + " * " + dataSet.getHeaders().get(idxX);
            return formula + "\ny = " + beta0 + " + " + beta1 + " * x_1";
        }
    }

}
