package group15;

public class CalculatorModelJFX {

    private double result;

    public void calculate(int a, int b, String op) {
        if ("+".equals(op)) {
            result = a + b;
        } else if ("-".equals(op)) {
            result = a - b;
        } else if ("*".equals(op)) {
            result = a * b;
        } else if ("/".equals(op)) {
            if (b == 0) {
                throw new ArithmeticException("Khong the chia cho 0");
            }
            result = (double) a / b;
        }
    }

    public double getResult() {
        return result;
    }
}
