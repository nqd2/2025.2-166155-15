import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorControllerSwing {

    private CalculatorViewSwing view;
    private CalculatorModelSwing model;

    public CalculatorControllerSwing(CalculatorViewSwing view, CalculatorModelSwing model) {
        this.view = view;
        this.model = model;

        this.view.addTinhListener(new TinhListener());
    }

    class TinhListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                String t1 = view.getSo1Text();
                String t2 = view.getSo2Text();

                if (!t1.matches("-?\\d+") || !t2.matches("-?\\d+")) {
                    throw new NumberFormatException();
                }

                if (t1.replace("-", "").length() > 10 || t2.replace("-", "").length() > 10) {
                    view.hienThiLoi("So qua lon!");
                    return;
                }

                int a = Integer.parseInt(t1);
                int b = Integer.parseInt(t2);
                String op = view.getPhepTinh();

                model.calculate(a, b, op);
                double kq = model.getResult();

                if (Double.isInfinite(kq) || kq > Integer.MAX_VALUE || kq < Integer.MIN_VALUE) {
                    view.hienThiLoi("Ket qua qua lon!");
                    return;
                }

                view.setKetQua((int) Math.round(kq));

            } catch (NumberFormatException ex) {
                view.hienThiLoi("Hay nhap 2 so nguyen");
            } catch (ArithmeticException ex) {
                view.hienThiLoi(ex.getMessage());
            }
        }
    }
}
