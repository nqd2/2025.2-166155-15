public class MVCCalculatorSwing {

    public static void main(String[] args) {
        CalculatorViewSwing view = new CalculatorViewSwing();
        CalculatorModelSwing model = new CalculatorModelSwing();
        new CalculatorControllerSwing(view, model);
        view.setVisible(true);
    }
}
