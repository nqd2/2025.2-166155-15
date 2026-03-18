import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorController {

    private CalculatorView view;
    private CalculatorModel model;

    public CalculatorController(CalculatorView view, CalculatorModel model) {
        this.view = view;
        this.model = model;

        this.view.addCalculateListener(new CalculateListener());
    }

    class CalculateListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                String text1 = view.getNum1Text();
                String text2 = view.getNum2Text();

                if (!text1.matches("-?\\d+") || !text2.matches("-?\\d+")) {
                    throw new NumberFormatException();
                }

                if (text1.replace("-", "").length() > 10 || text2.replace("-", "").length() > 10) {
                    view.showError("So qua lon!");
                    return;
                }

                int num1 = Integer.parseInt(text1);
                int num2 = Integer.parseInt(text2);
                String op = view.getOperator();

                model.calculate(num1, num2, op);
                double kq = model.getResult();

                if (Double.isInfinite(kq) || kq > Integer.MAX_VALUE || kq < Integer.MIN_VALUE) {
                    view.showError("Ket qua qua lon!");
                    return;
                }

                view.setResult((int) Math.round(kq));

            } catch (NumberFormatException ex) {
                view.showError("Ban can nhap 2 so nguyen");
            } catch (ArithmeticException ex) {
                view.showError(ex.getMessage());
            }
        }
    }
}
