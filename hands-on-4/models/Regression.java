package models;

import utils.DataSet;

public abstract class Regression {

     protected abstract AbstractModel<Double> makeModel(DataSet dataSet) throws Exception;

}
