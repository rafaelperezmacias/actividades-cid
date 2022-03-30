package models;

import utils.AlgebraicHelpers;
import utils.DataSet;

import java.util.ArrayList;

public class MultipleLinearRegression extends Regression {

    private MultipleLinearRegression()
    {

    }

    public static Model generateModel(DataSet dataSet) throws Exception {
        MultipleLinearRegression regression = new MultipleLinearRegression();
        return (Model) regression.makeModel(dataSet);
    }

    @Override
    protected AbstractModel<Double> makeModel(DataSet dataSet) throws Exception {
        validateDataSet(dataSet);
        int idxY = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());
        if ( idxY == -1 ) {
            throw new Exception("Target not found");
        }

        double[][] X = generateAXMatrix(DataSetFunctions.generateNumericMatrix(dataSet, getColumns(dataSet, idxY)));
        double[][] Y = DataSetFunctions.generateNumericMatrix(dataSet, idxY);

        double[][] XTranposed = AlgebraicHelpers.transpose(X);
        double[][] XTmultipliedByX = AlgebraicHelpers.matrixMultiplication(XTranposed, X);
        double[][] inverseXTmultipliedByX = AlgebraicHelpers.inverse(XTmultipliedByX);

        double[][] XTmultipliedByY = AlgebraicHelpers.matrixMultiplication(XTranposed, Y);

        double[][] result = AlgebraicHelpers.matrixMultiplication(inverseXTmultipliedByX, XTmultipliedByY);

        return new Model(dataSet, AlgebraicHelpers.getVectorOfTheMatrix(result, 0), idxY);
    }

    private static double[][] generateAXMatrix(double[][] matrix) {
        double[][] newMatrix = new double[matrix.length][matrix[0].length + 1];
        for ( int i = 0; i < matrix.length; i++ ) {
            newMatrix[i][0] = 1;
        }
        for ( int i = 1; i < newMatrix[0].length; i++ ) {
            for ( int j = 0; j < newMatrix.length; j++ ) {
                newMatrix[j][i] = matrix[j][i - 1];
            }
        }
        return newMatrix;
    }

    private static int[] getColumns(DataSet dataSet, int idxExcept) {
        int[] columns = new int[dataSet.getHeaders().size() - 1];
        for ( int i = 0, j = 0; i < dataSet.getHeaders().size(); i++ ) {
            if ( i == idxExcept ) {
                continue;
            }
            columns[j++] = i;
        }
        return columns;
    }

    private static void validateDataSet(DataSet dataSet) throws Exception {
        DataSetFunctions.validateDataSet(dataSet);
        for ( String atributeType : dataSet.getAttributeTypes() ) {
            if ( !atributeType.equalsIgnoreCase(DataSet.NUMERIC_TYPE) ) {
                throw new Exception("The data set contains non-numeric attributes");
            }
        }
        if ( dataSet.getHeaders().size() <= 1 ) {
            throw new Exception("The data set can not contain 1 or less attributes");
        }
    }

    public static class Model implements AbstractModel<Double> {

        private DataSet dataSet;
        private double[] betas;
        private int idxY;

        public Model(DataSet dataSet, double[] betas, int idxY)
        {
            this.dataSet = dataSet;
            this.betas = betas;
            this.idxY = idxY;
        }

        @Override
        public Double predict(Object[] instance) throws Exception {
            if ( instance.length != dataSet.getHeaders().size() - 1 ) {
                throw new Exception("The instance is not the same size as the data set (" + instance.length + " - " + (dataSet.getHeaders().size() - 1) + ")");
            }
            for ( Object value : instance ) {
                if ( !(value instanceof Double) ) {
                    throw new Exception("Some instance value is not a numerical value");
                }
            }
            double result = betas[0];
            for ( int i = 1; i < betas.length; i++ ) {
                result += (double) betas[i] * (double) instance[i - 1];
            }
            return result;
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
                Object[] values = new Object[instance.size() - 1];
                for ( int i = 0, j = 0; i < instance.size(); i++ ) {
                    if ( i == idxY ) {
                        continue;
                    }
                    values[j++] = Double.parseDouble(instance.get(i));
                }
                instance.add(String.valueOf(predict( values )));
            }
        }

        @Override
        public String toString() {
            StringBuilder formula = new StringBuilder(dataSet.getHeaders().get(idxY) + " = " + betas[0]);
            StringBuilder out = new StringBuilder();
            for ( int i = 1, j = 0; i < betas.length; i++, j++ ) {
                if ( j == idxY ) {
                    j++;
                }
                formula.append(" + ").append(betas[i]).append(" * ").append(dataSet.getHeaders().get(j));
                out.append(" + ").append(betas[i]).append(" * x_").append(i);
            }
            return formula + "\ny = " + betas[0] + out;
        }
    }

}
