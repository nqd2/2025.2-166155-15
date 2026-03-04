public class CalculatorModelSwing {

    private double result;

    public void calculate(int a, int b, String op) {
        if (op.equals("+")) {
            result = a + b;
        } else if (op.equals("-")) {
            result = a - b;
        } else if (op.equals("*")) {
            result = a * b;
        } else if (op.equals("/")) {
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
