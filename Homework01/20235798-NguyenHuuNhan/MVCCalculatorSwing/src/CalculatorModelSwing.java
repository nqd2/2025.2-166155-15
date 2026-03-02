public class CalculatorModelSwing {

	private double calculationValue;
	
	public void calculate(int firstNumber, int secondNumber, String operator){
		
		if("+".equals(operator)){
			calculationValue = firstNumber + secondNumber;
		} else if("-".equals(operator)){
			calculationValue = firstNumber - secondNumber;
		} else if("*".equals(operator)){
			calculationValue = firstNumber * secondNumber;
		} else if("/".equals(operator)){
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

