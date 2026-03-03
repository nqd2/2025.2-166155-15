public class MVCCalculatorSwing {

    public static void main(String[] args) {

        CalculatorViewSwing theView = new CalculatorViewSwing();
        CalculatorModelSwing theModel = new CalculatorModelSwing();

        new CalculatorControllerSwing(theView, theModel);

        theView.setVisible(true);
    }
}

