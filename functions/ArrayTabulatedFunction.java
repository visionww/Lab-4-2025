package functions;

import java.io.*;

public class ArrayTabulatedFunction  implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points; //массив точек
	private static final double EPSILON = 1e-10;
	
	// Пустой конструктор необходим для Externalizable
    public ArrayTabulatedFunction() {
        this.points = new FunctionPoint[0];
    }
	
	// Конструктор 1:
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше 2");
        }
     
        this.points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1); //вычисляем шаг между точками
     
        // Заполняем массив точками с координатой x и с y=0
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    // Конструктор 2:
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше 2");
        }
     
        this.points = new FunctionPoint[values.length];
        double step = (rightX - leftX) / (values.length - 1);
     
        //заполняем массив заданным значением y
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
	
	
	// Конструктор 3: 
	public ArrayTabulatedFunction(FunctionPoint[] points) {
		if (points.length < 2) {
			throw new IllegalArgumentException("Кол-во точек меньше 2");
		}
    
    // Проверяем упорядоченность точек по X
		for (int i = 1; i < points.length; i++) {
			if (points[i].getX() <= points[i - 1].getX()) {
				throw new IllegalArgumentException("Точки не упорядочены по значению X");
			}
		}
    
    
		this.points = new FunctionPoint[points.length];
		for (int i = 0; i < points.length; i++) {
			this.points[i] = new FunctionPoint(points[i]);
		}
	}
	
	@Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(points.length);
        for (FunctionPoint point : points) {
            out.writeDouble(point.getX());
            out.writeDouble(point.getY());
        }
    }
	
	@Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int count = in.readInt();
        points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }
	
	
	@Override
	public double getLeftDomainBorder() {
        return points[0].getX();
    }
	
	@Override
    public double getRightDomainBorder() {
        return points[points.length - 1].getX();
    }

//Вычисляем значение функции
	@Override
	public double getFunctionValue(double x) {
        
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        
        for (int i = 0; i < points.length - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            double y1 = points[i].getY();
            double y2 = points[i + 1].getY();
            
            
            if (Math.abs(x - x1) < EPSILON) {
                return y1;
            }
            if (Math.abs(x - x2) < EPSILON) {
                return y2;
            }
            
            
            if (x > x1 && x < x2) {
                return y1 + (y2 - y1) / (x2 - x1) * (x - x1);
            }
        }
        
       
        return points[points.length - 1].getY();
    }
	
 // Количество точек
	@Override
	public int getPointsCount() {
        return points.length;
		
	}
	
 
	@Override
	public FunctionPoint getPoint(int index){
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
		}
		return new FunctionPoint(points[index]);
    }

//Копируем точку 
	@Override
	public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        
        if (index > 0 && point.getX() <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть больше, чем координата предыдущей точки X");
        }
        if (index < points.length - 1 && point.getX() >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть меньше координаты X следующей точки");
        }
        points[index] = new FunctionPoint(point);
    }
	@Override
	public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        return points[index].getX();
    }
	@Override
	public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        
        if (index > 0 && x <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть больше, чем координата предыдущей точки X");
        }
        if (index < points.length - 1 && x >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть меньше координаты X следующей точки");
        }
        points[index].setX(x);
    }

	@Override
	public double getPointY(int index) {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        return points[index].getY();
    }
	@Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        points[index].setY(y);
    }

// Удаление точки
	@Override
	public void deletePoint(int index) {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс  : " + index);
        }
        
        if (points.length <= 2) {
            throw new IllegalStateException("Невозможно удалить точку, требуется минимум 2");
        }
        
        FunctionPoint[] newPoints = new FunctionPoint[points.length - 1]; 
        System.arraycopy(points, 0, newPoints, 0, index); 
        System.arraycopy(points, index + 1, newPoints, index, points.length - index - 1);
        points = newPoints;
    }

// Добавление точки
	@Override
	public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionPoint[] newPoints = new FunctionPoint[points.length + 1];
        int i = 0;
        
        while (i < points.length && point.getX() > points[i].getX()) {
            i++;
        }
        
        // Если точка с таким X уже существует - выбрасываем исключение
        if (i < points.length && Math.abs(point.getX() - points[i].getX()) < EPSILON) {
            throw new InappropriateFunctionPointException("Такая точка с координатой X уже существует");
        }
        
        // Копируем старый массив, вставляя новую точку в нужную позицию
        System.arraycopy(points, 0, newPoints, 0, i);
        newPoints[i] = new FunctionPoint(point);
        System.arraycopy(points, i, newPoints, i + 1, points.length - i);
        
        points = newPoints;
    }
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArrayTabulatedFunction{");
        for (int i = 0; i < points.length; i++) {
            sb.append(points[i]);
            if (i < points.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
	
  
}
