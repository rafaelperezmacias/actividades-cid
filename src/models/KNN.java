package models;

import utils.AritmeticHelpers;
import utils.DataSet;

import java.util.*;

public class KNN extends Classification {

    private KNN()
    {

    }

    public static Model generateModel(DataSet dataSet) throws Exception {
        KNN knn = new KNN();
        return knn.makeModel(dataSet);
    }

    public static Model generateModel(DataSet dataSet, Params params) throws Exception {
        KNN knn = new KNN();
        return knn.makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet) throws Exception {
        Params params = new Params();
        params.setK(5);
        return makeModel(dataSet, params);
    }

    @Override
    protected Model makeModel(DataSet dataSet, AbstractParams params) throws Exception {
        validateDataSetAndParams(dataSet, params);
        int idxTarget = DataSetFunctions.getIndexAtAtributeFromDataSet(dataSet.getHeaders(), dataSet.getTarget());

        DataSet standardizedDataSet = DataSet.copyDataSet(dataSet);
        double[] means = new double[dataSet.getHeaders().size() - 1];
        double[] standarDerivations = new double[dataSet.getHeaders().size() - 1];
        standardizationDataSet(standardizedDataSet, means, standarDerivations, idxTarget);

        return new Model(standardizedDataSet, ((Params) params).k, means, standarDerivations, idxTarget);
    }

    private static void standardizationDataSet(DataSet dataSet, double[] means, double[] standarDerivations, int idxTarget) throws Exception {
        int idxAttribute = 0;
        for (int j = 0; j < dataSet.getHeaders().size(); j++ ) {
            if ( j == idxTarget ) {
                continue;
            }
            // Vector attribute
            double[] vectorAttribute = DataSetFunctions.generateNumericVector(dataSet, j);
            // Mean
            double mean = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                @Override
                public double result(int pos) {
                    return vectorAttribute[pos];
                }
            }, new double[][]{ vectorAttribute } );
            means[idxAttribute] = mean / dataSet.getInstances().size();
            // Standar Derivation
            final int idxAttributeFinal = idxAttribute;
            double standarDerivation = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                @Override
                public double result(int pos) {
                    return Math.pow( vectorAttribute[pos] - means[idxAttributeFinal], 2 );
                }
            }, new double[][]{ vectorAttribute } );
            standarDerivations[idxAttribute] = Math.sqrt( standarDerivation / ( dataSet.getInstances().size() - 1) );
            // Standardized
            for ( int i = 0; i < dataSet.getInstances().size(); i++ ) {
                dataSet.getInstances().get(i).set(j, String.valueOf( standardized(vectorAttribute[i], means[idxAttribute], standarDerivations[idxAttribute]) ));
            }
            idxAttribute++;
        }
    }

    public static double standardized(double value, double mean, double standarDerivation) {
        return ( value - mean ) / standarDerivation;
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
        if ( !(params instanceof KNN.Params) ) {
            throw new Exception("Params object is invalid");
        }
        if ( ((Params) params).k < 1 || ((Params) params).k > dataSet.getInstances().size() ) {
            throw new Exception("The parameter K must be in the limits (1 - " + dataSet.getInstances().size() + ")");
        }
    }
    public static class Params extends AbstractParams {

        private int k;

        public void setK(int k) {
            this.k = k;
        }

    }

    public static class Model implements AbstractModel<String> {

        private DataSet dataSet;
        private int k;
        private double[] means;
        private double[] standarDerivations;
        private int idxTarget;

        private Model(DataSet dataSet, int k, double[] means, double[] standarDerivations, int idxTarget) {
            this.dataSet = dataSet;
            this.k = k;
            this.means = means;
            this.standarDerivations = standarDerivations;
            this.idxTarget = idxTarget;
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
                values[i] = standardized((double) instance[i], means[i], standarDerivations[i]);
            }
            ArrayList<Neighbor> neighbors = getNeighbors(dataSet, values, idxTarget);
            neighbors.sort(new Comparator<Neighbor>() {
                @Override
                public int compare(Neighbor o1, Neighbor o2) {
                    return Double.compare(o1.distance, o2.distance);
                }
            });
            // Evaluate classes
            HashMap<String, Integer> classes = new HashMap<>();
            String target = null;
            int kNeighbors = 0;
            while ( target == null ) {
                if ( kNeighbors == dataSet.getInstances().size() ) {
                    break;
                }
                String kTarget = dataSet.getInstances().get( neighbors.get(kNeighbors).idx ).get(idxTarget);
                if ( classes.containsKey(kTarget) ) {
                    classes.put( kTarget, classes.get(kTarget) + 1 );
                } else {
                    classes.put( kTarget, 1 );
                }
                if ( ( kNeighbors + 1) >= k ) {
                    ArrayList<String> targetOut = new ArrayList<>();
                    int maxTargetOut = Integer.MIN_VALUE;
                    for ( Map.Entry<String, Integer> entryClass : classes.entrySet() ) {
                        if ( entryClass.getValue() == maxTargetOut ) {
                            targetOut.add( entryClass.getKey() );
                        }
                        if ( entryClass.getValue() > maxTargetOut ) {
                            targetOut.clear();
                            targetOut.add( entryClass.getKey() );
                            maxTargetOut = entryClass.getValue();
                        }
                    }
                    if ( targetOut.size() == 1 ) {
                        target = targetOut.get(0);
                    }
                }
                kNeighbors++;
            }
            if ( target != null ) {
                return target;
            }
            // Evaluate distance
            kNeighbors = 0;
            HashMap<String, Double> distances = new HashMap<>();
            while ( target == null ) {
                if ( kNeighbors == dataSet.getInstances().size() ) {
                    break;
                }
                String kTarget = dataSet.getInstances().get( neighbors.get(kNeighbors).idx ).get(idxTarget);
                if ( distances.containsKey(kTarget) ) {
                    distances.put( kTarget, distances.get(kTarget) + neighbors.get(kNeighbors).distance );
                } else {
                    distances.put( kTarget, neighbors.get(kNeighbors).distance );
                }
                if ( ( kNeighbors + 1) >= k ) {
                    ArrayList<String> targetOut = new ArrayList<>();
                    double minDistance = Double.MIN_VALUE;
                    for ( Map.Entry<String, Double> entryDistance : distances.entrySet() ) {
                        if ( entryDistance.getValue() == minDistance ) {
                            targetOut.add( entryDistance.getKey() );
                        }
                        if ( entryDistance.getValue() < minDistance ) {
                            targetOut.clear();
                            targetOut.add( entryDistance.getKey() );
                            minDistance = entryDistance.getValue();
                        }
                    }
                    if ( targetOut.size() == 1 ) {
                        target = targetOut.get(0);
                    }
                }
                kNeighbors++;
            }
            if ( target != null ) {
                return target;
            }
            // Random method
            Set<String> randomClasses = new HashSet<>();
            for ( kNeighbors = 1; kNeighbors < k; kNeighbors++ ) {
                randomClasses.add( dataSet.getInstances().get(neighbors.get(kNeighbors - 1).idx).get(idxTarget) );
            }
            Object[] classesOut = randomClasses.toArray();
            return (String) classesOut[ (int) (Math.random() * classesOut.length) ];
        }

        @Override
        public void predict(DataSet dataSet, String classNameOut) throws Exception {
            throw new Exception("Not implemented yet");
        }

        @Override
        public String toString() {
            return "Params model, k = " + k;
        }

        private ArrayList<Neighbor> getNeighbors(DataSet dataSet, double[] instanceToPredict, int idxTarget) throws Exception {
            ArrayList<Neighbor> neighbors = new ArrayList<>();

            for ( int i = 0; i < dataSet.getInstances().size(); i++ ) {
                Neighbor neighbor = new Neighbor();
                neighbor.idx = i;
                double[] instance = getInstanceFromDataSet(dataSet, i, idxTarget);
                double distance = AritmeticHelpers.summation(new AritmeticHelpers.SummationExpression() {
                    @Override
                    public double result(int pos) {
                        return Math.pow( instance[pos] - instanceToPredict[pos], 2);
                    }
                }, new double[][]{ instance, instanceToPredict } );
                neighbor.distance = Math.sqrt(distance);
                neighbors.add(neighbor);
            }

            return neighbors;
        }

        private double[] getInstanceFromDataSet(DataSet dataSet, int idxInstance, int idxTarget) {
            double[] instance = new double[dataSet.getHeaders().size() - 1];
            int posInstance = 0;
            for ( int i = 0; i < dataSet.getHeaders().size(); i++ ) {
                if ( i == idxTarget ) {
                    continue;
                }
                instance[posInstance++] = Double.parseDouble(dataSet.getInstances().get(idxInstance).get(i));
            }
            return instance;
        }

        private static class Neighbor {

            public int idx;
            public double distance;

        }

    }

}
