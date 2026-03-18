public class CalculatorModel {

    private double result;

    public void calculate(int num1, int num2, String op) {
        if (op.equals("+")) {
            result = num1 + num2;
        } else if (op.equals("-")) {
            result = num1 - num2;
        } else if (op.equals("*")) {
            result = num1 * num2;
        } else if (op.equals("/")) {
            if (num2 == 0) {
                throw new ArithmeticException("Khong the chia cho 0");
            }
            result = (double) num1 / num2;
        }
    }

    public double getResult() {
        return result;
    }
}
