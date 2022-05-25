package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.LogisticRegression;
import utils.DataSet;

public class LogisticRegressionAgent extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    addBehaviour(new LogisticRegressionBehaviour());
  } 

  private class LogisticRegressionBehaviour extends Behaviour {

    public void action() {
      DataSet dataSet = DataSet.getNBADataSet();
      try {
       LogisticRegression.Params params = new LogisticRegression.Params();
        params.setLearningRate(0.1);
        params.setIterations(100);
        params.setWeigths( new double[] {0, 0, 0} );
        params.forceMaxIterarions(true);
        LogisticRegression.Model model = LogisticRegression.generateModel(dataSet, params);
        System.out.println(model);
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Ingrese los valores de la instancia separados por una coma");
            if ( input == null || input.equals("q") ) {
              break;
            }
            String[] variables = input.split(",");
            if ( variables.length != dataSet.getHeaders().size() - 1 ) {
                System.out.println("No se pudo realizar la predicion, numero de parametros invalido");
                continue;
            }
            try {
                Object[] values = new Object[variables.length];
                for ( int i = 0; i < variables.length; i++ ) {
                  values[i] = Double.parseDouble(variables[i]);
                }
                System.out.println("y ---> " + model.predict(values) );
            } catch (Exception ex) {
                System.out.println("Los parametros no son numericos");
                continue;
            }
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