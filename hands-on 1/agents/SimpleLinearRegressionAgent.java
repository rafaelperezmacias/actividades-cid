package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;
import models.SimpleLinearRegression;
import utils.DataSet;

public class SimpleLinearRegressionAgent extends Agent {

  private SimpleLinearRegression.Model model;

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    Object[] args = getArguments();
    if ( args != null && args.length > 0 ) {
      ArrayList<Double> values = new ArrayList<>();
      for (int i = 0; i < args.length; i++) {
        try {
          values.add(Double.parseDouble(args[i].toString()));
        } catch (Exception ex) { }
      }
      addBehaviour(new SimpleLinearRegressionBehaviour(values));
    } else {
      doDelete();
    }
  } 

  private class SimpleLinearRegressionBehaviour extends Behaviour {

    private ArrayList<Double> valuesToPredict;

    public SimpleLinearRegressionBehaviour(ArrayList<Double> valuesToPredict)
    {
      this.valuesToPredict = valuesToPredict;
    } 

    public void action() {
      DataSet dataSet = DataSet.getBenettonDataSetForSLR();
      try {
        model = SimpleLinearRegression.generateModel(dataSet);
        System.out.println(model);
        for ( Double x : valuesToPredict ) {
          System.out.println("x = " + x + ", y ---> " + model.predict(x));
        }
      } catch ( Exception ex ) {
        ex.printStackTrace();
        myAgent.doDelete();
      }
    } 

    public boolean done() {
      return true;
    }
    
    public int onEnd() {
      myAgent.doDelete();
      return super.onEnd();
    } 

  }

}
