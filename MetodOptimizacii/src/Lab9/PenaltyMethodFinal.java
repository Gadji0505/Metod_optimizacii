package Lab9;

import java.util.Locale;

public class PenaltyMethodFinal {

    // Исходная целевая функция f(x)
    public static double f(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    // Ограничение g(x) = x1 + x2 - 2
    public static double g(double x1, double x2) {
        return x1 + x2 - 2;
    }

    /**
     * Аналитическое нахождение минимума функции штрафа P(x, r)
     * Решаем систему 2x2 методом Крамера
     */
    public static double[] findMinimumOfPenalty(double r) {
        // Коэффициенты системы:
        // (2 + 2r)x1 + (1 + 2r)x2 = 4r - 1
        // (1 + 2r)x1 + (8 + 2r)x2 = 4r

        double a11 = 2 + 2 * r;
        double a12 = 1 + 2 * r;
        double b1  = 4 * r - 1;

        double a21 = 1 + 2 * r;
        double a22 = 8 + 2 * r;
        double b2  = 4 * r;

        double det = a11 * a22 - a12 * a21;
        if (Math.abs(det) < 1e-12) {
            throw new ArithmeticException("Система вырождена");
        }

        double x1 = (b1 * a22 - a12 * b2) / det;
        double x2 = (a11 * b2 - b1 * a21) / det;

        return new double[]{x1, x2};
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        double r = 1.0;         // Начальный параметр штрафа
        double beta = 10.0;     // Коэффициент увеличения штрафа
        double eps = 0.0001;    // Точность по ограничению
        int maxIter = 10;

        System.out.println("=== Метод штрафных функций ===");
        System.out.println("Задача: f(x) = x1^2 + 4x2^2 + x1x2 + x1 -> min");
        System.out.println("Ограничение: x1 + x2 = 2\n");

        System.out.printf("%-4s | %-10s | %-10s | %-10s | %-10s | %-10s%n",
                "k", "r", "x1", "x2", "f(x)", "|g(x)|");
        System.out.println("----------------------------------------------------------------------");

        for (int k = 1; k <= maxIter; k++) {
            // Находим точку безусловного минимума вспомогательной функции
            double[] x = findMinimumOfPenalty(r);
            double x1 = x[0];
            double x2 = x[1];

            double fVal = f(x1, x2);
            double gVal = g(x1, x2);
            double absG = Math.abs(gVal);

            System.out.printf("%-4d | %-10.1f | %-10.5f | %-10.5f | %-10.5f | %-10.6f%n",
                    k, r, x1, x2, fVal, absG);

            // Проверка условия остановки
            if (absG < eps) {
                System.out.println("----------------------------------------------------------------------");
                System.out.printf("Точность достигнута на итерации %d.%n", k);
                break;
            }

            // Увеличиваем штраф
            r *= beta;
        }

        // Итоговый вывод для отчета
        double[] finalX = findMinimumOfPenalty(r / beta);
        System.out.println("\nФИНАЛЬНЫЙ РЕЗУЛЬТАТ:");
        System.out.printf("Точка минимума x* = (%.4f, %.4f)%n", finalX[0], finalX[1]);
        System.out.printf("Значение f(x*) = %.5f%n", f(finalX[0], finalX[1]));
        System.out.printf("Невязка ограничения g(x*) = %.6f%n", g(finalX[0], finalX[1]));
    }
}