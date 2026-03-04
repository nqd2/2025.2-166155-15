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
        cbOperator.getItems().setAll("+", "-", "*", "/");
        cbOperator.getSelectionModel().selectFirst();
    }

    @FXML
    private void onCalculate(ActionEvent event) {
        lblError.setText("");
        txtResult.clear();

        try {
            String t1 = txtFirst.getText();
            String t2 = txtSecond.getText();

            if (!t1.matches("-?\\d+") || !t2.matches("-?\\d+")) {
                throw new NumberFormatException();
            }

            if (t1.replace("-", "").length() > 10 || t2.replace("-", "").length() > 10) {
                lblError.setText("So qua lon!");
                return;
            }

            int a = Integer.parseInt(t1);
            int b = Integer.parseInt(t2);
            String op = cbOperator.getSelectionModel().getSelectedItem();

            model.calculate(a, b, op);
            double kq = model.getResult();

            if (Double.isInfinite(kq) || kq > Integer.MAX_VALUE || kq < Integer.MIN_VALUE) {
                lblError.setText("Ket qua qua lon!");
                return;
            }

            txtResult.setText(Integer.toString((int) Math.round(kq)));

        } catch (NumberFormatException ex) {
            lblError.setText("Hay nhap 2 so nguyen");
        } catch (ArithmeticException ex) {
            lblError.setText(ex.getMessage());
        }
    }
}
