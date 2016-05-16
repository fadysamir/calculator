package calc1;

import java.util.function.BinaryOperator;
/**
 * @author antonyoswilliam
 *
 */
public enum Operator {
    DIVIDE("\u00F7", (x, y) -> x / y),
    MULTIPLY("x", (x, y) -> x * y),
    SUBTRACT("-", (x, y) -> x - y),
    ADD("+", (x, y) -> x + y),
    POWR("^",(x,y)-> Math.pow(x, y)),
    SQRT("√",(x,y)->Math.sqrt(y)),
    MODL("%",(x,y)-> x%y),
    NEG("-/+",(x,y)->y*-1);
    ;

    private final String symbol;
    private final BinaryOperator<Double> equation;


    /**
     * @param symbol
     * @param equation
     */
    Operator(String symbol, BinaryOperator<Double> equation) {
        this.symbol = symbol;
        this.equation = equation;
    }

    /**
     * @param alpha
     * @param beta
     * @return
     */
    public double compute(double alpha, double beta) {
        return equation.apply(alpha, beta);
    }

   
    @Override
    public String toString() {
        return symbol;
    }
}













