package functions;

import functions.meta.*;

public class Functions {
    
    // Приватный конструктор, чтобы нельзя было создать объект класса
    private Functions() {
    }
    
    //Сдвиг вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    
    //Масштабирование
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    
    //Степень функции
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    
    //Сумма функций
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    
    //Произведение функции
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    
    
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
}