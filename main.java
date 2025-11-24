import functions.basic.*;
import functions.*;
import functions.meta.*;

import java.io.*;

public class main {
    public static void main(String[] args) {
        try {
            //Создание объектов Sin и Cos, вывод значений от 0 до π с шагом 0.1
            System.out.println("Исходные функции Sin и Cos");
            Sin sinFunc = new Sin();
            Cos cosFunc = new Cos();
            
            System.out.println("x\tSin(x)\tCos(x)");
            for (double x = 0; x <= Math.PI; x += 0.1) {
                System.out.printf("%.1f\t%.6f\t%.6f%n", x, sinFunc.getFunctionValue(x), cosFunc.getFunctionValue(x));
            }
            System.out.println();
            
            //Создание табулированных аналогов с 10 точками
            System.out.println("Табулированные функции (10 точек)");
            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 10);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 10);
            
            System.out.println("x\tTabSin(x)\tTabCos(x)");
            for (double x = 0; x <= Math.PI; x += 0.1) {
                System.out.printf("%.1f\t%.6f\t%.6f%n", x, tabulatedSin.getFunctionValue(x), tabulatedCos.getFunctionValue(x));
            }
            System.out.println();
            
            //Сумма квадратов табулированных функций
            System.out.println("Сумма квадратов табулированных функций");
            Function sinSquared = Functions.power(tabulatedSin, 2);
            Function cosSquared = Functions.power(tabulatedCos, 2);
            Function sumOfSquares = Functions.sum(sinSquared, cosSquared);
            
            System.out.println("x\tSin2+Cos2");
            for (double x = 0; x <= Math.PI; x += 0.5) {
                System.out.println(x + "\t" + sumOfSquares.getFunctionValue(x));
            }
            System.out.println();
            
            //Работа с разным количеством точек
            System.out.println("Исследование с разным количеством точек");
            int[] pointCounts = {5, 10, 15, 20};
            for (int count : pointCounts) {
                TabulatedFunction sinTab = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, count);
                TabulatedFunction cosTab = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, count);
                Function sum = Functions.sum(Functions.power(sinTab, 2), Functions.power(cosTab, 2));
                
                System.out.println("Точек: " + count + ", значение при x=pi/2: " + sum.getFunctionValue(Math.PI/2));
            }
            System.out.println();
            
            //Работа с экспонентой - текстовый формат
            System.out.println("Экспонента - текстовый формат");
            Exp expFunc = new Exp();
            TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(expFunc, 0, 10, 11);
            
            // Запись в файл
            try (FileWriter writer = new FileWriter("exp_function.txt")) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
            }
            
            // Чтение из файла
            TabulatedFunction readExp;
            try (FileReader reader = new FileReader("exp_function.txt")) {
                readExp = TabulatedFunctions.readTabulatedFunction(reader);
            }
            
            System.out.println("x\tИсходная\tСчитанная");
            for (double x = 0; x <= 10; x += 1) {
                System.out.println(x + "\t" + tabulatedExp.getFunctionValue(x) + "\t" + readExp.getFunctionValue(x));
            }
            System.out.println();
            
            //Работа с логарифмом - бинарный формат
            System.out.println("Логарифм - бинарный формат");
            Log logFunc = new Log(Math.E); // Натуральный логарифм
            TabulatedFunction tabulatedLog = TabulatedFunctions.tabulate(logFunc, 0.1, 10, 11); // начинаем с 0.1, т.к. ln(0) не определен
            
            // Запись в файл
            try (FileOutputStream out = new FileOutputStream("log_function.bin")) {
                TabulatedFunctions.outputTabulatedFunction(tabulatedLog, out);
            }
            
            // Чтение из файла
            TabulatedFunction readLog;
            try (FileInputStream in = new FileInputStream("log_function.bin")) {
                readLog = TabulatedFunctions.inputTabulatedFunction(in);
            }
            
            System.out.println("x\tИсходная\tСчитанная");
            for (double x = 0.1; x <= 10; x += 1) {
                System.out.println(x + "\t" + tabulatedLog.getFunctionValue(x) + "\t" + readLog.getFunctionValue(x));
            }
            System.out.println();
            
            //Анализ файлов
            System.out.println("Анализ форматов хранения");
            File textFile = new File("exp_function.txt");
            File binaryFile = new File("log_function.bin");
            
            System.out.println("Размер текстового файла: " + textFile.length() + " байт");
            System.out.println("Размер бинарного файла: " + binaryFile.length() + " байт");
			
			// Тестирование сериализации
            System.out.println("=== Тестирование сериализации ===");
            
            // Создаем композицию функций: ln(exp(x)) = x
            Exp expFunc2 = new Exp();
            Log logFunc2 = new Log(Math.E);
            Function composition = Functions.composition(logFunc2, expFunc2); // ln(exp(x))
            
            // Создаем табулированную версию
            TabulatedFunction tabulatedComp = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            
            System.out.println("Исходная функция ln(exp(x)) = x:");
            System.out.println("x\tИсходная");
            for (double x = 0; x <= 10; x += 1) {
                System.out.println(x + "\t" + tabulatedComp.getFunctionValue(x));
            }
            System.out.println();
            
            // Создаем массив точек из табулированной функции
            FunctionPoint[] points = new FunctionPoint[tabulatedComp.getPointsCount()];
            for (int i = 0; i < tabulatedComp.getPointsCount(); i++) {
                points[i] = tabulatedComp.getPoint(i);
            }
            
            // Тестируем сериализацию ArrayTabulatedFunction (Externalizable)
            System.out.println(" ArrayTabulatedFunction (Externalizable)");
            
            // Создаем ArrayTabulatedFunction через массив точек
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);
            
            // Сериализуем в файл 
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("array_function.dat"))) {
                out.writeObject(arrayFunc);
                System.out.println("ArrayTabulatedFunction сериализован в array_function.dat");
            }
            
            // Десериализуем из файла 
            TabulatedFunction deserializedArrayFunc;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("array_function.dat"))) {
                deserializedArrayFunc = (TabulatedFunction) in.readObject();
                System.out.println("ArrayTabulatedFunction десериализован из array_function.dat");
            }
            
            System.out.println("x\tИсходная\tДесериализованная");
            for (double x = 0; x <= 10; x += 1) {
                System.out.println(x + "\t" + arrayFunc.getFunctionValue(x) + "\t" + deserializedArrayFunc.getFunctionValue(x));
            }
            System.out.println();
            
            // Тестируем сериализацию LinkedListTabulatedFunction (Serializable)
            System.out.println(" LinkedListTabulatedFunction (Serializable)");
            
            // Создаем LinkedListTabulatedFunction через массив точек
            TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(points);
            
            // Сериализуем в файл 
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("linkedlist_function.dat"))) {
                out.writeObject(linkedListFunc);
                System.out.println("LinkedListTabulatedFunction сериализован в linkedlist_function.dat");
            }
            
            // Десериализуем из файла 
            TabulatedFunction deserializedLinkedListFunc;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("linkedlist_function.dat"))) {
                deserializedLinkedListFunc = (TabulatedFunction) in.readObject();
                System.out.println("LinkedListTabulatedFunction десериализован из linkedlist_function.dat");
            }
            
            System.out.println("x\tИсходная\tДесериализованная");
            for (double x = 0; x <= 10; x += 1) {
                System.out.println(x + "\t" + linkedListFunc.getFunctionValue(x) + "\t" + deserializedLinkedListFunc.getFunctionValue(x));
            }
            System.out.println();
            
            // Анализ файлов сериализации
            System.out.println(" Анализ файлов сериализации ");
            File externalizableFile = new File("array_function.dat");
            File serializableFile = new File("linkedlist_function.dat");
            
            System.out.println("Размер файла Externalizable (Array): " + externalizableFile.length() + " байт");
            System.out.println("Размер файла Serializable (LinkedList): " + serializableFile.length() + " байт");
            System.out.println();
     
			
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}