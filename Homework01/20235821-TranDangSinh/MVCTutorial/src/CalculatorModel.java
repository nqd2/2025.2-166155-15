// The Model performs all the calculations needed
// and that is it. It doesn't know the View 
// exists

public class CalculatorModel {

	// Holds the value of the sum of the numbers
	// entered in the view
	
	private double calculationValue;
	
	public void calculate(int firstNumber, int secondNumber, String operator){
		
		if("+".equals(operator)){
			calculationValue = firstNumber + secondNumber;
		} else if("-".equals(operator)){
			calculationValue = firstNumber - secondNumber;
		} else if("*".equals(operator)){
			calculationValue = firstNumber * secondNumber;
		} else if("/".equals(operator)){
			// simple check to avoid division by zero
			if(secondNumber == 0){
				throw new ArithmeticException("Division by zero");
			}
			calculationValue = (double) firstNumber / (double) secondNumber;
		} else {
			throw new IllegalArgumentException("Unknown operator: " + operator);
		}
		
	}
	
	public double getCalculationValue(){
		
		return calculationValue;
		
	}
	
}