package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.MultipleLinearRegression;
import utils.DataSet;

public class MultipleLinearRegressionAgent extends Agent {

  private MultipleLinearRegression.Model model;

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    addBehaviour(new MultipleLinearRegressionBehaviour());
  } 

  private class MultipleLinearRegressionBehaviour extends Behaviour {

    public void action() {
      DataSet dataSet = DataSet.getExperimentDataSet();
      try {
        model = MultipleLinearRegression.generateModel(dataSet);
        System.out.println(model);
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Ingrese los valores de x_1 y x_2 separados por una coma");
            if ( input == null || input.equals("q") ) {
              break;
            }
            String[] variables = input.split(",");
            if ( variables.length != 2 ) {
                System.out.println("No se pudo realizar la predicion, numero de parametros invalido");
                continue;
            }
            try {
                double x1 = Double.parseDouble(variables[0]);
                double x2 = Double.parseDouble(variables[1]);
                System.out.println("y ---> " + model.predict(x1,x2) );
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
