package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    double x;
    double y;

    
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    
    public FunctionPoint(FunctionPoint p) {
        x = p.x;
        y = p.y;
    }

 
    public FunctionPoint() {
        x = 0;
        y = 0;
    }

   public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

}
