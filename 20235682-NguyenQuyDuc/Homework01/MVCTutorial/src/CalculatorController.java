import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The Controller coordinates interactions
// between the View and Model

public class CalculatorController {
	
	private CalculatorView theView;
	private CalculatorModel theModel;
	
	public CalculatorController(CalculatorView theView, CalculatorModel theModel) {
		this.theView = theView;
		this.theModel = theModel;
		
		// Tell the View that when ever the calculate button
		// is clicked to execute the actionPerformed method
		// in the CalculateListener inner class
		
		this.theView.addCalculateListener(new CalculateListener());
	}
	
		class CalculateListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
			
			int firstNumber, secondNumber = 0;
			
			// Surround interactions with the view with
			// a try block in case numbers weren't
			// properly entered
			
			try{
			
				String firstText = theView.getFirstNumberText();
				String secondText = theView.getSecondNumberText();
				
				// Kiểm tra chỉ chứa chữ số (hoặc dấu trừ + chữ số)
				if(!firstText.matches("-?\\d+") || !secondText.matches("-?\\d+")){
					throw new NumberFormatException("Not an integer");
				}
				
				// Kiểm tra độ dài để tránh overflow khi parse int
				// (10 chữ số cho số dương, 11 với dấu trừ)
				if(firstText.replace("-", "").length() > 10 || secondText.replace("-", "").length() > 10){
					theView.displayErrorMessage("overflow");
					return;
				}
				
				firstNumber = Integer.parseInt(firstText);
				secondNumber = Integer.parseInt(secondText);
				String operator = theView.getSelectedOperator();
				
				theModel.calculate(firstNumber, secondNumber, operator);
				
				double result = theModel.getCalculationValue();
				
				// Kiểm tra overflow cho kết quả
				if(Double.isInfinite(result) || result > Integer.MAX_VALUE || result < Integer.MIN_VALUE){
					theView.displayErrorMessage("overflow");
					return;
				}
				
				// View đang dùng int nên làm tròn và cast
				theView.setCalcSolution((int) Math.round(result));
			
			}

			catch(NumberFormatException ex){
				
				System.out.println(ex);
				
				theView.displayErrorMessage("You Need to Enter 2 Integers");
				
			}
			
			catch(ArithmeticException ex){
				
				System.out.println(ex);
				
				theView.displayErrorMessage(ex.getMessage());
				
			}
			
		}
		
	}
	
}