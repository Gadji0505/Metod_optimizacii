package Lab_1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class QuadraticAnalysis {

    // Основная функция
    public static double functionMain(double a, double b, double c, double x) {
        return a * x * x + b * x + c;
    }

    // Вычисление производной
    public static double numericalDerivative(double a, double b, double c, double x, double h) {
        return (functionMain(a, b, c, x + h) - functionMain(a, b, c, x - h)) / (2 * h);
    }

    // Перегруженный метод с default значением h
    public static double numericalDerivative(double a, double b, double c, double x) {
        return numericalDerivative(a, b, c, x, 0.0001);
    }

    public static void classicalMain(double a, double b, double c, float minGr, float maxGr) {
        double xMin = -b / (2.0 * a);

        System.out.println("Производная функция: ");

        try (FileWriter outFile = new FileWriter("outFile.txt", true)) {
            outFile.write("\n");

            for (int i = (int)minGr; i <= maxGr; i++) {
                double derivNum = numericalDerivative(a, b, c, i);
                System.out.printf("f'(%d) = %.6f\n", i, derivNum);
                outFile.write(String.format(Locale.US, "%.6f ", derivNum));
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
            return;
        }

        double derivMin = numericalDerivative(a, b, c, minGr);
        double derivMax = numericalDerivative(a, b, c, maxGr);

        System.out.printf("\nf'(%.1f) = %.6f", minGr, derivMin);
        if (derivMin < 0)
            System.out.println(" < 0 => функция убывает");
        else if (derivMin > 0)
            System.out.println(" > 0 => функция возрастает");
        else
            System.out.println(" = 0 => экстремум");

        System.out.printf("f'(%.1f) = %.6f", maxGr, derivMax);
        if (derivMax < 0)
            System.out.println(" < 0 => функция убывает");
        else if (derivMax > 0)
            System.out.println(" > 0 => функция возрастает");
        else
            System.out.println(" = 0 => экстремум");

        System.out.printf("f'(x) = 0 при x = %.6f (минимум)\n", xMin);
        System.out.println("Так как функция имеет 1 экстремум то, она является унимодальной!");
    }

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru"));
        Scanner scanner = new Scanner(System.in);

        float minGr = -3.0f, maxGr = 7.0f;
        double a, b, c;

        System.out.print("Введите коэффициент a: ");
        a = scanner.nextDouble();
        System.out.print("Введите коэффициент b: ");
        b = scanner.nextDouble();
        System.out.print("Введите коэффициент c: ");
        c = scanner.nextDouble();

        // Записываем значения исходной функции
        try (FileWriter outFile = new FileWriter("outFile.txt")) {
            System.out.println("\nЗначения исходной функции: ");

            for (int i = (int)minGr; i <= maxGr; i++) {
                double res = functionMain(a, b, c, i);
                System.out.printf("f(%d) = %.6f\n", i, res);
                outFile.write(String.format(Locale.US, "%.6f ", res));
            }
        } catch (IOException e) {
            System.err.println("Файл не найден или не открывается! " + e.getMessage());
            return;
        }

        classicalMain(a, b, c, minGr, maxGr);

        // Находим точку, где производная = 0 (минимум)
        double xMin = -b / (2.0 * a);
        System.out.printf("\nВ точке минимума x = %.6f:\n", xMin);
        System.out.printf("Производная: %.6f\n", numericalDerivative(a, b, c, xMin));
        System.out.printf("Значение функции: %.6f\n", functionMain(a, b, c, xMin));

        scanner.close();
    }
}