package functions.basic;

public class Tan extends TrigonometricFunction {
    
    public double getFunctionValue(double x) {
        // Если косинус равен 0, то тангенс не определен 
        double cosValue = Math.cos(x);
        if (Math.abs(cosValue) < 1e-10) {
            return Double.NaN;
        }
        return Math.tan(x);
    }
}