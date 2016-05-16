package calc1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Calc extends Application {
  
    static Operator currentOperator;
    static boolean operatorSelected;
    static boolean resultDisplayed;

   
    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage stage) {
        BorderPane layout = new BorderPane();

        TextField auxiliary = new TextField();
        auxiliary.setStyle("-fx-font-size: 15; -fx-text-fill: gray");
        auxiliary.setMaxWidth(415); // 415 = total width, including margins of buttons
        auxiliary.setEditable(false);

        TextField result = new TextField();
        result.setStyle("-fx-font-size: 20");
        result.setMaxWidth(415); 
        result.setEditable(false);

        VBox resultLayout = new VBox();
        resultLayout.getChildren().addAll(auxiliary, result);
        layout.setTop(resultLayout);


        GridPane buttonLayout = new GridPane();
        buttonLayout.setPadding(new Insets(10, 10, 10, 10));
        buttonLayout.setHgap(5);
        buttonLayout.setVgap(5);
        layout.setCenter(buttonLayout);

        Button backButton = new Button("\u2190");
        backButton.setMinSize(50, 50);
        backButton.setOnAction(e -> {
            String currentText = result.getText();
            if (!currentText.isEmpty() && !resultDisplayed){
                result.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        buttonLayout.add(backButton, 2, 0);

        Button clearButton = new Button("Clear");
        clearButton.setMinSize(100, 50);
        clearButton.setOnAction(e -> {
            result.clear();
            auxiliary.clear();
            operatorSelected = false;
        });
        GridPane.setColumnSpan(clearButton, 2); 
        buttonLayout.add(clearButton, 0, 0);

        Button[] numberButtons = new Button[10];
        for (int i = 3, target = 1; i >= 1; i--) {
            for (int j = 0; j <= 2; j++) {
                String number = Integer.toString(target);
                
                numberButtons[target] = new Button(number);
                numberButtons[target].setStyle("-fx-color: black");
                numberButtons[target].setMinSize(50, 50);
                numberButtons[target].setOnAction(e -> {
                    if (resultDisplayed) {
                        result.setText(number);
                        resultDisplayed = false;
                    } else {
                        result.appendText(number);
                    }

                    operatorSelected = false;
                });
                buttonLayout.add(numberButtons[target++], j, i);
            }   
        }

        numberButtons[0] = new Button("0");
        numberButtons[0].setMinSize(100, 50);
        numberButtons[0].setOnAction(e -> {
            if (!result.getText().isEmpty() && !resultDisplayed) {
                result.appendText("0");
                operatorSelected = false;
            }
        });
        GridPane.setColumnSpan(numberButtons[0], 2);
        buttonLayout.add(numberButtons[0], 0, 4);


        Button decimalButton = new Button(".");
        decimalButton.setMinSize(50, 50);
        decimalButton.setOnAction(e -> {
            if (result.getText().indexOf('.') == -1) {
                result.appendText(".");
            }
        });
        buttonLayout.add(decimalButton, 2, 4);
        int count=0;
        for (Operator op : Operator.values()) {
            String symbol = op.toString();

            Button button = new Button(symbol);
            
            button.setMinSize(50, 50);
            button.setStyle("-fx-color: orange");
            button.setOnAction(e -> {
                if (auxiliary.getText().isEmpty()) {
                    auxiliary.setText(result.getText().isEmpty() ? "0" : acquireValue(result.getText()));
                    auxiliary.appendText(" " + symbol);
                    currentOperator = op;
                    resultDisplayed = true;
                    operatorSelected = true;
                } else if (operatorSelected) {
                    currentOperator = op;
                    int end = auxiliary.getText().length();
                    auxiliary.replaceText(end - 1, end,  symbol);
                } else {
                    auxiliary.setText(calculate(currentOperator, result, auxiliary) + " " + symbol);
                    result.clear();
                    currentOperator = op;
                    resultDisplayed = true;
                    operatorSelected = true;
                }
            });
            count++;
            
            if(count<5)
            buttonLayout.addColumn(3, button);
            else
                buttonLayout.addColumn(4, button);
        }

        Button equalsButton = new Button("=");
        equalsButton.setStyle("-fx-color: orange");
        equalsButton.setMinSize(100, 50);
        equalsButton.setOnAction(e -> {
            if (!auxiliary.getText().isEmpty()) {
                result.setText(
                    calculate(currentOperator, result, auxiliary)
                );
                resultDisplayed = true;
                operatorSelected = false;
                auxiliary.clear();
            }
        });
        GridPane.setColumnSpan(equalsButton, 2); 
        buttonLayout.add(equalsButton, 3, 4);
        equalsButton.setDefaultButton(true);

        Scene scene = new Scene(layout);
       

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("Calculator FX991");
        stage.show();
    }

    /**
     * @param op
     * @param main
     * @param auxiliary
     * @return
     */
    private static String calculate(Operator op, TextField main, TextField auxiliary) {
    	String d;
    	d=auxiliary.getText();
    	double val1 = Double.parseDouble(auxiliary.getText().replaceAll("[^\\.0-9]", ""));
    	if(d.charAt(0)=='-')
    	{
    		
    		val1 = - val1;
    	}
        double val2 = Double.parseDouble(main.getText());

        if (val2 == 0 && op == Operator.DIVIDE) {
            return "Cannot divide by 0";
        }
        
        double result = op.compute(val1, val2);
        return toCalculatorString(result);
    }

    /**
     * @param val
     * @return
     */
    private static String acquireValue(String val) {
    	
        double result = Double.parseDouble(val);
        return toCalculatorString(result);
    }

    /**
     * @param s
     * @return
     */
    private static String removeDecimalTrailingZeroes(String s) {
        return s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    /**
     * @param input
     * @return
     */
   
     
    private static String toCalculatorString(double input) {
        return input == (int)input ? 
            Integer.toString((int)input) : removeDecimalTrailingZeroes(String.format("%.6f", input));
    }
    
}
