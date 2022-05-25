package models;

import utils.AritmeticHelpers;
import utils.DataSet;

public class GradientDescent extends Regression {

    private GradientDescent()
    {

    }

    public static Model generateModel(DataSet dataSet) throws Exception {
        GradientDescent gd = new GradientDescent();
        return gd.makeModel(dataSet);
    }

    public static Model generateModel(DataSet dataSet, Params params) throws Exception {
        GradientDescent gd = new GradientDescent();
        return gd.makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet) throws Exception {
        Params params = new Params();
        params.setLearningRate(0.0003);
        params.setBeta0(0);
        params.setBeta1(0);
        params.setIterations(1_000_000);
        return makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet, AbstractParams params) throws Exception {
        validateDataSet(dataSet);
        int idxY = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());
        if ( idxY == -1 ) {
            throw new Exception("Target not found");
        }
        if ( !(params instanceof Params) ) {
            throw new Exception("Params object is invalid");
        }

        int idxX = ( idxY == 1 ) ? 0 : 1;

        double lastError = Double.MAX_VALUE;
        double currentError;
        double beta0 = ((Params) params).beta0;
        double beta1 = ((Params) params).beta1;
        double lastBeta0 = beta0;
        double lastBeta1 = beta1;
        int n = dataSet.getInstances().size();

        double[] XVector = DataSetFunctions.generateNumericVector(dataSet, idxX);
        double[] YVector = DataSetFunctions.generateNumericVector(dataSet, idxY);

        int iteration;

        for ( iteration = 0; iteration < ((Params) params).iterations; iteration++ ) {
            final double finalBeta0 = beta0;
            final double finalBeta1 = beta1;

            // Error
            currentError = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                @Override
                public double result(int pos) {
                    return Math.pow( (YVector[pos] - ( finalBeta0 + (finalBeta1 * XVector[pos]) )), 2);
                }
            }, new double[][]{ XVector, YVector });
            currentError /= n;

            if ( currentError < lastError ) {
                lastError = currentError;
                lastBeta0 = beta0;
                lastBeta1 = beta1;
            } else {
                break;
            }

            // Beta 0
            double beta0Derivative = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                @Override
                public double result(int pos) {
                    return ( YVector[pos] - ( finalBeta0 + ( finalBeta1 * XVector[pos] ) ) );
                }
            }, new double[][]{ XVector, YVector });
            beta0Derivative = -(2.0 * beta0Derivative) / n;
            beta0 = beta0 - (((Params) params).learningRate * beta0Derivative);

            // Beta 1
            double beta1Derivative = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                @Override
                public double result(int pos) {
                    return ( YVector[pos] - ( finalBeta0 + ( finalBeta1 * XVector[pos] ) ) ) * XVector[pos];
                }
            }, new double[][]{ XVector, YVector });
            beta1Derivative = -(2.0 * beta1Derivative) / n;
            beta1 = beta1 - (((Params) params).learningRate * beta1Derivative);
        }

        return new Model(dataSet, lastBeta0, lastBeta1, ((Params) params).learningRate, iteration, idxX);
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

    public static class Params extends AbstractParams {

        private double learningRate;
        private double beta0;
        private double beta1;
        private int iterations;

        public void setLearningRate(double learningRate) {
            this.learningRate = learningRate;
        }

        public void setBeta0(double beta0) {
            this.beta0 = beta0;
        }

        public void setBeta1(double beta1) {
            this.beta1 = beta1;
        }

        public void setIterations(int iterations) {
            this.iterations = iterations;
        }

    }

    public static class Model implements AbstractModel<Double> {

        private DataSet dataSet;
        private double beta0;
        private double beta1;
        private double learningRate;
        private int iterations;
        private int idxX;

        public Model(DataSet dataSet, double beta0, double beta1, double learningRate, int iterations, int idxX) {
            this.dataSet = dataSet;
            this.beta0 = beta0;
            this.beta1 = beta1;
            this.learningRate = learningRate;
            this.iterations = iterations;
            this.idxX = idxX;
        }

        @Override
        public Double predict(Object[] instance) throws Exception {
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
            throw new Exception("Not implemented yet");
        }

        @Override
        public String toString() {
            String formula = dataSet.getHeaders().get( (idxX == 1) ? 0 : 1 ) + " = ";
            formula += beta0 + " + " + beta1 + " * " + dataSet.getHeaders().get(idxX);
            return "Iterations = " + iterations + " | Learning rate = " + learningRate + "\n" + formula + "\ny = " + beta0 + " + " + beta1 + " * x_1";
        }

    }

}
