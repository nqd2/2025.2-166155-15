import java.awt.event.ActionListener;

import javax.swing.*;

public class CalculatorViewSwing extends JFrame{

	private JTextField firstNumber  = new JTextField(10);
	private JComboBox<String> operatorBox = new JComboBox<String>(new String[]{"+", "-", "*", "/"});
	private JTextField secondNumber = new JTextField(10);
	private JButton calculateButton = new JButton("Calculate");
	private JTextField calcSolution = new JTextField(10);
	
	CalculatorViewSwing(){
		
		JPanel calcPanel = new JPanel();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 200);
		
		calcPanel.add(firstNumber);
		calcPanel.add(operatorBox);
		calcPanel.add(secondNumber);
		calcPanel.add(calculateButton);
		calcPanel.add(calcSolution);
		
		this.add(calcPanel);
	}
	
	public int getFirstNumber(){
		
		return Integer.parseInt(firstNumber.getText());
		
	}
	
	public int getSecondNumber(){
		
		return Integer.parseInt(secondNumber.getText());
		
	}
	
	public String getFirstNumberText(){
		
		return firstNumber.getText();
		
	}
	
	public String getSecondNumberText(){
		
		return secondNumber.getText();
		
	}
	
	public String getSelectedOperator(){
		
		return (String) operatorBox.getSelectedItem();
		
	}
	
	public void setCalcSolution(int solution){
		
		calcSolution.setText(Integer.toString(solution));
		
	}
	
	void addCalculateListener(ActionListener listenForCalcButton){
		
		calculateButton.addActionListener(listenForCalcButton);
		
	}
	
	void displayErrorMessage(String errorMessage){
		
		JOptionPane.showMessageDialog(this, errorMessage);
		
	}
	
}

