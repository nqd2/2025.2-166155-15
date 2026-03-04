import java.awt.event.ActionListener;
import javax.swing.*;

public class CalculatorView extends JFrame {

    private JTextField txtNum1 = new JTextField(10);
    private JComboBox<String> cboOperator = new JComboBox<>(new String[] { "+", "-", "*", "/" });
    private JTextField txtNum2 = new JTextField(10);
    private JButton btnCalculate = new JButton("Tinh");
    private JTextField txtResult = new JTextField(10);

    CalculatorView() {
        txtResult.setEditable(false);

        JPanel panel = new JPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(550, 150);
        this.setTitle("May Tinh MVC");

        panel.add(txtNum1);
        panel.add(cboOperator);
        panel.add(txtNum2);
        panel.add(btnCalculate);
        panel.add(txtResult);

        this.add(panel);
    }

    public int getFirstNumber() {
        return Integer.parseInt(txtNum1.getText());
    }

    public int getSecondNumber() {
        return Integer.parseInt(txtNum2.getText());
    }

    public String getNum1Text() {
        return txtNum1.getText();
    }

    public String getNum2Text() {
        return txtNum2.getText();
    }

    public String getOperator() {
        return (String) cboOperator.getSelectedItem();
    }

    public void setResult(int value) {
        txtResult.setText(Integer.toString(value));
    }

    void addCalculateListener(ActionListener listener) {
        btnCalculate.addActionListener(listener);
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
