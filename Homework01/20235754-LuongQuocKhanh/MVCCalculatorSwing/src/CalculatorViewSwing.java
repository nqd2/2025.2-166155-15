import java.awt.event.ActionListener;
import javax.swing.*;

public class CalculatorViewSwing extends JFrame {

    private JTextField txtSo1 = new JTextField(10);
    private JComboBox<String> cboPhepTinh = new JComboBox<>(new String[] { "+", "-", "*", "/" });
    private JTextField txtSo2 = new JTextField(10);
    private JButton btnTinh = new JButton("Tinh");
    private JTextField txtKetQua = new JTextField(10);

    CalculatorViewSwing() {
        txtKetQua.setEditable(false);

        JPanel panel = new JPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(550, 150);
        this.setTitle("Calculator - Swing");

        panel.add(txtSo1);
        panel.add(cboPhepTinh);
        panel.add(txtSo2);
        panel.add(btnTinh);
        panel.add(txtKetQua);

        this.add(panel);
    }

    public int getSo1() {
        return Integer.parseInt(txtSo1.getText());
    }

    public int getSo2() {
        return Integer.parseInt(txtSo2.getText());
    }

    public String getSo1Text() {
        return txtSo1.getText();
    }

    public String getSo2Text() {
        return txtSo2.getText();
    }

    public String getPhepTinh() {
        return (String) cboPhepTinh.getSelectedItem();
    }

    public void setKetQua(int value) {
        txtKetQua.setText(Integer.toString(value));
    }

    void addTinhListener(ActionListener listener) {
        btnTinh.addActionListener(listener);
    }

    void hienThiLoi(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
