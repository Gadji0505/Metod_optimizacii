package Lab_2;

import java.util.ArrayList;
import java.util.List;

public class OptimizationMethods {

    // Основная функция
    public static double functionMain(double a, double b, double c, double x) {
        return a * x * x + b * x + c;
    }

    public static void dichotomyMethod(double a, double b, double c, double minGr, double maxGr, double epsilon) {
        System.out.println("\nМетод Дихотомии");

        // объявляем границы здесь, чтобы не вызывать их каждый раз как параметр функции
        double left = minGr;
        double right = maxGr;

        double delta = epsilon / (right - left); // малый параметр
        int it = 0; // количество итераций
        int count = 0; // если нужно подсчитать сложность алгоритма

        System.out.printf("Начальный отрезок: [%.6f, %.6f]\n", left, right);
        System.out.printf("Точность: %.10f\n", epsilon);
        System.out.printf("Дельта: %.10f\n", delta);
        System.out.println("Итерационный процесс:");
        System.out.println("--------------------------------------------------------");

        while ((right - left) > epsilon) {
            it++;

            // вычисляем две симметричные точки
            double y0 = (left + right - delta) / 2.0;
            double z0 = (left + right + delta) / 2.0;

            // вычисляем значения функции
            double f1 = functionMain(a, b, c, y0);
            double f2 = functionMain(a, b, c, z0);
            count += 2;

            System.out.printf("Итерация %d:\n", it);
            System.out.printf("  Отрезок: [%.6f, %.6f], длина: %.10f\n", left, right, (right - left));
            System.out.printf("  y = %.6f, f(y) = %.6f\n", y0, f1);
            System.out.printf("  z = %.6f, f(z) = %.6f\n", z0, f2);

            // сравниваем значения функции и сужаем отрезок
            if (f1 < f2) {
                right = z0;
                System.out.printf("  f(y) < f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            } else {
                left = y0;
                System.out.printf("  f(y) >= f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            }

            System.out.println("--------------------------------------------------------");
        }

        System.out.printf("\nУсловие выполнено: L = %.10f  <  e = %.10f\n", (right - left), epsilon);
        double xMin = (left + right) / 2.0;
        double fMin = functionMain(a, b, c, xMin);
        count++;

        System.out.println("\nРЕЗУЛЬТАТЫ МЕТОДА ДИХОТОМИИ:");
        System.out.printf("Найденный минимум: x = %.10f\n", xMin);
        System.out.printf("Значение функции в минимуме: f(x) = %.10f\n", fMin);
        System.out.println("Количество итераций: " + it);
        System.out.println("Количество вычислений функции: " + count);
    }

    public static void goldenSectionMethod(double a, double b, double c, double minGr, double maxGr, double epsilon) {
        System.out.println("\nМетод золотого сечения");

        final double phi = (Math.sqrt(5.0) - 1.0) / 2.0; // ~ 0.618 (золотое сечение)
        int it = 0;
        int count = 0;

        double left = minGr;
        double right = maxGr;

        System.out.printf("Начальный отрезок: [%.6f, %.6f]\n", left, right);
        System.out.printf("Точность: %.10f, phi: %.10f\n", epsilon, phi);
        System.out.println("Итерационный процесс:");
        System.out.println("--------------------------------------------------------");

        // Вычисляем начальные точки
        double y0 = right - phi * (right - left);
        double z0 = left + phi * (right - left);

        double f1 = functionMain(a, b, c, y0);
        double f2 = functionMain(a, b, c, z0);
        count += 2;

        System.out.println("Начальные точки:");
        System.out.printf("y = %.6f, f(y) = %.6f\n", y0, f1);
        System.out.printf("z = %.6f, f(z) = %.6f\n", z0, f2);
        System.out.println("--------------------------------------------------------");

        while ((right - left) > epsilon) {
            it++;
            System.out.printf("Итерация %d:\n", it);
            System.out.printf("  Отрезок: [%.6f, %.6f], длина: %.10f\n", left, right, (right - left));
            System.out.printf("  y = %.6f, f(y) = %.6f\n", y0, f1);
            System.out.printf("  z = %.6f, f(z) = %.6f\n", z0, f2);

            if (f1 < f2) {
                // минимум слева - отбрасываем правую часть
                right = z0;
                z0 = y0;
                f2 = f1;
                y0 = right - phi * (right - left);
                f1 = functionMain(a, b, c, y0);
                count++;
                System.out.printf("  f(y) < f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            } else {
                // минимум справа - отбрасываем левую часть
                left = y0;
                y0 = z0;
                f1 = f2;
                z0 = left + phi * (right - left);
                f2 = functionMain(a, b, c, z0);
                count++;
                System.out.printf("  f(y) >= f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            }

            System.out.printf("  Новые точки: y = %.6f, z = %.6f\n", y0, z0);
            System.out.println("--------------------------------------------------------");
        }

        System.out.printf("\nУсловие выполнено: L = %.10f  <  e = %.10f\n", (right - left), epsilon);
        double xMin = (left + right) / 2.0;
        double fMin = functionMain(a, b, c, xMin);
        count++;

        System.out.println("\nРЕЗУЛЬТАТЫ МЕТОДА ЗОЛОТОГО СЕЧЕНИЯ:");
        System.out.printf("Найденный минимум: x = %.10f\n", xMin);
        System.out.printf("Значение функции в минимуме: f(x) = %.10f\n", fMin);
        System.out.println("Количество итераций: " + it);
        System.out.println("Количество вычислений функции: " + count);
    }

    // вычисление чисел Фибоначчи
    public static List<Long> generateFibonacciSequence(int n) {
        List<Long> fib = new ArrayList<>(n + 2);
        fib.add(1L);
        fib.add(1L);
        for (int i = 2; i <= n + 1; i++) {
            fib.add(fib.get(i - 1) + fib.get(i - 2));
        }
        return fib;
    }

    public static void fibonacciMethod(double a, double b, double c, double minGr, double maxGr, double epsilon) {
        System.out.println("\nМетод Фибоначчи");

        int it = 0;
        int count = 0;
        double left = minGr;
        double right = maxGr;
        double L0 = right - left;

        System.out.printf("Начальный отрезок: [%.6f, %.6f], длина: %.6f\n", left, right, L0);
        System.out.printf("Точность: %.10f\n", epsilon);

        // определяем необходимое число итераций
        int n = 1;
        while (generateFibonacciSequence(n).get(n) < (L0 / epsilon)) {
            n++;
        }

        List<Long> fib = generateFibonacciSequence(n);

        System.out.printf("Число Фибоначчи F[%d] = %d\n", n, fib.get(n));
        System.out.println("Планируемое количество итераций: " + n);
        System.out.println("Итерационный процесс:");
        System.out.println("--------------------------------------------------------");

        // Начальные точки
        double y = left + (double)fib.get(n - 2) / fib.get(n) * (right - left);
        double z = left + (double)fib.get(n - 1) / fib.get(n) * (right - left);

        double f1 = functionMain(a, b, c, y);
        double f2 = functionMain(a, b, c, z);
        count += 2;

        System.out.println("Начальные точки:");
        System.out.printf("y = %.6f, f(y) = %.6f (F[%d]/F[%d] = %.6f)\n",
                y, f1, n - 2, n, (double)fib.get(n - 2) / fib.get(n));
        System.out.printf("z = %.6f, f(z) = %.6f (F[%d]/F[%d] = %.6f)\n",
                z, f2, n - 1, n, (double)fib.get(n - 1) / fib.get(n));
        System.out.println("--------------------------------------------------------");

        // Основной цикл
        for (int k = 1; k <= n - 2; k++) {
            it++;

            System.out.printf("Итерация %d (k = %d):\n", it, k);
            System.out.printf("  Отрезок: [%.6f, %.6f], длина: %.10f\n", left, right, (right - left));
            System.out.printf("  y = %.6f, f(y) = %.6f\n", y, f1);
            System.out.printf("  z = %.6f, f(z) = %.6f\n", z, f2);
            System.out.printf("  Коэффициенты: F[%d]/F[%d] = %.6f, F[%d]/F[%d] = %.6f\n",
                    n - k - 1, n - k + 1, (double)fib.get(n - k - 1) / fib.get(n - k + 1),
                    n - k, n - k + 1, (double)fib.get(n - k) / fib.get(n - k + 1));

            if (f1 < f2) {
                // Минимум слева
                right = z;
                z = y;
                f2 = f1;
                y = left + (double)fib.get(n - k - 2) / fib.get(n - k) * (right - left);
                f1 = functionMain(a, b, c, y);
                count++;
                System.out.printf("  f(y) < f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            } else {
                // Минимум справа
                left = y;
                y = z;
                f1 = f2;
                z = left + (double)fib.get(n - k - 1) / fib.get(n - k) * (right - left);
                f2 = functionMain(a, b, c, z);
                count++;
                System.out.printf("  f(y) >= f(z) => новый отрезок: [%.6f, %.6f]\n", left, right);
            }

            System.out.printf("  Новые точки: y = %.6f, z = %.6f\n", y, z);
            System.out.println("--------------------------------------------------------");
        }

        // Финальная итерация (k = n-1)
        it++;
        System.out.printf("Финальная итерация %d (k = %d):\n", it, n - 1);

        // На последней итерации добавляем δ для различения точек
        double delta = epsilon / 10.0;
        z = y + delta;
        f2 = functionMain(a, b, c, z);
        count++;

        System.out.printf("  y = %.6f, f(y) = %.6f\n", y, f1);
        System.out.printf("  z = %.6f, f(z) = %.6f\n", z, f2);

        if (f1 < f2) {
            right = z;
            System.out.printf("  f(y) < f(z) => финальный отрезок: [%.6f, %.6f]\n", left, right);
        } else {
            left = y;
            System.out.printf("  f(y) >= f(z) => финальный отрезок: [%.6f, %.6f]\n", left, right);
        }

        double xMin = (left + right) / 2.0;
        double fMin = functionMain(a, b, c, xMin);
        count++;

        System.out.println("--------------------------------------------------------");
        System.out.println("\nРЕЗУЛЬТАТЫ МЕТОДА ФИБОНАЧЧИ:");
        System.out.printf("Найденный минимум: x = %.10f\n", xMin);
        System.out.printf("Значение функции в минимуме: f(x) = %.10f\n", fMin);
        System.out.println("Количество итераций: " + it);
        System.out.println("Количество вычислений функции: " + count);
        System.out.printf("Длина конечного отрезка: %.10f\n", (right - left));
    }
}