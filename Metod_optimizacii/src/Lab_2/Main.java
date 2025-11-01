package Lab_2;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru"));
        Scanner scanner = new Scanner(System.in);

        double minGr, maxGr;
        double a, b, c, e;

        // ввод условия
        System.out.print("Введите коэффициент a: ");
        a = scanner.nextDouble();
        System.out.print("Введите коэффициент b: ");
        b = scanner.nextDouble();
        System.out.print("Введите коэффициент c: ");
        c = scanner.nextDouble();
        System.out.print("\nВведите нижнюю границу: ");
        minGr = scanner.nextDouble();
        System.out.print("Введите верхнюю границу: ");
        maxGr = scanner.nextDouble();
        System.out.print("\nВведите точность: ");
        e = scanner.nextDouble();

        System.out.println("\nВыберите метод для которого нужно производить вычисления: ");
        System.out.println("Метод Дихотомии - 1 ");
        System.out.println("Метод золотого сечения - 2 ");
        System.out.println("Метод Фибоначчи - 3 ");
        System.out.print("Введите цифру: ");
        int n = scanner.nextInt();

        if (n == 1)
            OptimizationMethods.dichotomyMethod(a, b, c, minGr, maxGr, e);
        else if (n == 2)
            OptimizationMethods.goldenSectionMethod(a, b, c, minGr, maxGr, e);
        else if (n == 3)
            OptimizationMethods.fibonacciMethod(a, b, c, minGr, maxGr, e);
        else {
            System.out.println("Некорректный запрос");
            return;
        }

        scanner.close();
    }
}