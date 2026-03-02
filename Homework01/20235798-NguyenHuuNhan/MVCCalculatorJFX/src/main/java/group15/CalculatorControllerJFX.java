package group15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculatorControllerJFX {

    @FXML
    private TextField txtFirst;

    @FXML
    private TextField txtSecond;

    @FXML
    private ComboBox<String> cbOperator;

    @FXML
    private TextField txtResult;

    @FXML
    private Label lblError;

    private final CalculatorModelJFX model = new CalculatorModelJFX();

    @FXML
    private void initialize() {
        // Initialize operator options
        cbOperator.getItems().setAll("+", "-", "*", "/");
        cbOperator.getSelectionModel().selectFirst();
    }

    @FXML
    private void onCalculate(ActionEvent event) {

        lblError.setText("");
        txtResult.clear();

        try {
            String firstText = txtFirst.getText();
            String secondText = txtSecond.getText();

            // Validate that input contains only digits (optionally starting with '-')
            if(!firstText.matches("-?\\d+") || !secondText.matches("-?\\d+")){
                throw new NumberFormatException("Not an integer");
            }

            // Limit length to avoid overflow when parsing to int
            // (10 digits for positive numbers, 11 including minus sign)
            if(firstText.replace("-", "").length() > 10 || secondText.replace("-", "").length() > 10){
                lblError.setText("overflow");
                return;
            }

            int firstNumber = Integer.parseInt(firstText);
            int secondNumber = Integer.parseInt(secondText);
            String operator = cbOperator.getSelectionModel().getSelectedItem();

            model.calculate(firstNumber, secondNumber, operator);

            double result = model.getCalculationValue();

            // Check for overflow in the calculated result
            if(Double.isInfinite(result) || result > Integer.MAX_VALUE || result < Integer.MIN_VALUE){
                lblError.setText("overflow");
                return;
            }

            // The view uses int, so round and cast the double result
            txtResult.setText(Integer.toString((int) Math.round(result)));

        } catch (NumberFormatException ex) {
            lblError.setText("You need to enter 2 integers");
        } catch (ArithmeticException ex) {
            lblError.setText(ex.getMessage());
        }
    }
}

