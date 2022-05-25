package models;

import utils.AlgebraicHelpers;
import utils.AritmeticHelpers;
import utils.DataSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LogisticRegression extends Classification {

    private LogisticRegression()
    {

    }

    public static Model generateModel(DataSet dataSet) throws Exception {
        LogisticRegression slr = new LogisticRegression();
        return slr.makeModel(dataSet);
    }

    public static Model generateModel(DataSet dataSet, Params params) throws Exception {
        LogisticRegression slr = new LogisticRegression();
        return slr.makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet) throws Exception {
        Params params = new Params();
        params.setLearningRate(0.0003);
        params.setIterations(1_000_000);
        params.forceMaxIterarions(false);
        if ( dataSet != null && dataSet.getHeaders() != null ) {
            double[] weights = new double[dataSet.getHeaders().size()];
            Arrays.fill(weights, 0);
            params.setWeigths(weights);
        }
        return makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet, AbstractParams params) throws Exception {
        validateDataSetAndParams(dataSet, params);
        int idxTarget = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());

        double[][] X = generateAnXMatrix(DataSetFunctions.generateNumericMatrix(dataSet, getColumns(dataSet, idxTarget)));
        double[] Y = DataSetFunctions.generateNumericVector(dataSet, idxTarget);

        double[] weights = ((Params) params).weigths;
        double[] lastWeights = new double[weights.length];

        int iteration;

        for (iteration = 0; iteration < ((Params) params).iterations; iteration++ ) {
            if ( !((Params) params).forceMaxIterarions && allInstancesClassifiedRigth(dataSet, X, idxTarget, weights) ) {
                break;
            }
            System.arraycopy(weights, 0, lastWeights, 0, weights.length);
            for ( int j = 0; j < weights.length; j++ ) {
                final int finalJ = j;
                double result = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                    @Override
                    public double result(int pos) {
                        double[] values = getValuesFromInstanceForSigmoidFunction(X, pos);
                        return (Y[pos] - sigmoidFuntion(values, lastWeights)) * X[pos][finalJ];
                    }
                }, new double[][]{ Y, AlgebraicHelpers.getVectorOfTheMatrix(X, 0) } );
                weights[j] = weights[j] + (((Params) params).learningRate * result);
            }
        }

        return new Model(dataSet, weights, idxTarget, ((Params) params).learningRate, iteration, allInstancesClassifiedRigth(dataSet, X, idxTarget, weights));
    }

    private static double[] getValuesFromInstanceForSigmoidFunction(double[][] X, int idxInstance) {
        double[] values = new double[X[idxInstance].length - 1];
        System.arraycopy(X[idxInstance], 1, values, 0, X[idxInstance].length - 1);
        return values;
    }

    private static boolean allInstancesClassifiedRigth(DataSet dataSet, double[][] X, int idxTarget, double[] weights) {
        for ( int i = 0; i < dataSet.getInstances().size(); i++ ) {
            double[] values = getValuesFromInstanceForSigmoidFunction(X, i);
            if ( sigmoidFuntion(values, weights) > 0.5 ) {
                if ( !dataSet.getInstances().get(i).get(idxTarget).equals("1") ) {
                    return false;
                }
            } else {
                if ( !dataSet.getInstances().get(i).get(idxTarget).equals("0") ) {
                    return false;
                }
            }
        }
        return true;
    }

    private static double sigmoidFuntion(double[] values, double[] weights) {
        double result = weights[0];
        for ( int i = 0; i < values.length; i++ ) {
            result += ( values[i] * weights[i + 1] );
        }
        return 1 / ( 1 + Math.pow( Math.E, -result ) );
    }

    private static void validateDataSetAndParams(DataSet dataSet, AbstractParams params) throws Exception {
        // DataSet
        DataSetFunctions.validateDataSet(dataSet);
        int idxTarget = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());
        if ( idxTarget == -1 ) {
            throw new Exception("Target not found");
        }
        if ( !dataSet.getAttributeTypes().get(idxTarget).equals(DataSet.CATEGORICAL_TYPE) ) {
            throw new Exception("The target must be categorical");
        }
        for ( int i = 0; i < dataSet.getAttributeTypes().size(); i++ ) {
            if ( i == idxTarget ) {
                continue;
            }
            if ( !dataSet.getAttributeTypes().get(i).equals(DataSet.NUMERIC_TYPE) ) {
                throw new Exception("The attribute " + dataSet.getHeaders().get(i) + " must be numeric");
            }
        }
        // Params
        if ( params == null ) {
            throw new Exception("Object params can not be null");
        }
        if ( !(params instanceof LogisticRegression.Params) ) {
            throw new Exception("Params object is invalid");
        }
        if ( ((Params) params).weigths == null ) {
            throw new Exception("The weights of the parameters can not be null");
        }
        if ( ((Params) params).weigths.length != dataSet.getHeaders().size() ) {
            throw new Exception("The weights indicated do not match those of the data set");
        }
        // Target
        Set<String> targetsFromDataSet = new HashSet<>();
        for ( int i = 0; i < dataSet.getInstances().size(); i++ ) {
            targetsFromDataSet.add( dataSet.getInstances().get(i).get(idxTarget) );
        }
        if ( targetsFromDataSet.size() != 2 ) {
            throw new Exception("There are not two types of target");
        }
        for ( String target : targetsFromDataSet ) {
            if ( !target.equals("1") && !target.equals("0") ) {
                throw new Exception("Class values are not allowed, only 0 and 1 are allowed");
            }
        }
    }

    private static double[][] generateAnXMatrix(double[][] matrix) {
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

    public static class Params extends AbstractParams {

        private double learningRate;
        private double[] weigths;
        private int iterations;
        private boolean forceMaxIterarions;

        public void setLearningRate(double learningRate) {
            this.learningRate = learningRate;
        }

        public void setWeigths(double[] weigths) {
            this.weigths = weigths;
        }

        public void setIterations(int iterations) {
            this.iterations = iterations;
        }

        public void forceMaxIterarions(boolean forceMaxIterarions) {
            this.forceMaxIterarions = forceMaxIterarions;
        }
    }

    public static class Model implements AbstractModel<String> {

        private DataSet dataSet;
        private double[] weights;
        private int idxTarget;
        private double learningRate;
        private int iterations;
        private boolean allClasified;

        public Model(DataSet dataSet, double[] weights, int idxTarget, double learningRate, int iterations, boolean allClasified)
        {
            this.dataSet = dataSet;
            this.weights = weights;
            this.idxTarget = idxTarget;
            this.learningRate = learningRate;
            this.iterations = iterations;
            this.allClasified = allClasified;
        }

        @Override
        public String predict(Object[] instance) throws Exception {
            if ( instance.length != dataSet.getHeaders().size() - 1 ) {
                throw new Exception("The instance is not the same size as the data set (" + instance.length + " - " + (dataSet.getHeaders().size() - 1) + ")");
            }
            for ( Object value : instance ) {
                if ( !(value instanceof Double) ) {
                    throw new Exception("Some instance value is not a numerical value");
                }
            }
            double[] values = new double[instance.length];
            for ( int i = 0; i < instance.length; i++ ) {
                values[i] = (double) instance[i];
            }
            return ( sigmoidFuntion(values, weights) > 0.5 ) ? "1" : "0";
        }

        @Override
        public void predict(DataSet dataSet, String classNameOut) throws Exception {
            throw new Exception("Not implemented yet");
        }

        @Override
        public String toString() {
            StringBuilder formula = new StringBuilder(dataSet.getHeaders().get(idxTarget) + " = " + weights[0]);
            StringBuilder out = new StringBuilder();
            for ( int i = 1, j = 0; i < weights.length; i++, j++ ) {
                if ( j == idxTarget ) {
                    j++;
                }
                formula.append(" + ").append(weights[i]).append(" * ").append(dataSet.getHeaders().get(j));
                out.append(" + ").append(weights[i]).append(" * x_").append(i);
            }
            return "Iterations = " + iterations + " | Learning rate = " + learningRate + " | Instances classified rigth = " + allClasified + "\n" + formula + "\ny = " + weights[0] + out;
        }
    }

}
