package Lab_3;

import java.util.function.BiFunction;

public class MainClass {
    private BiFunction<Double, Double, Double> f;

    public MainClass(BiFunction<Double, Double, Double> func) {
        this.f = func;
    }

    // численное вычисление производных (через конечные разности)
    public double derivativeX1(double x1, double x2, double h) {
        return (f.apply(x1 + h, x2) - f.apply(x1 - h, x2)) / (2 * h);
    }

    public double derivativeX1(double x1, double x2) {
        return derivativeX1(x1, x2, 1e-8);
    }

    public double derivativeX2(double x1, double x2, double h) {
        return (f.apply(x1, x2 + h) - f.apply(x1, x2 - h)) / (2 * h);
    }

    public double derivativeX2(double x1, double x2) {
        return derivativeX2(x1, x2, 1e-8);
    }

    public double secondDerivativeX1X1(double x1, double x2, double h) {
        return (f.apply(x1 + h, x2) - 2 * f.apply(x1, x2) + f.apply(x1 - h, x2)) / (h * h);
    }

    public double secondDerivativeX1X1(double x1, double x2) {
        return secondDerivativeX1X1(x1, x2, 1e-8);
    }

    // для смешанной производной
    public double secondDerivativeX1X2(double x1, double x2, double h) {
        return (f.apply(x1 + h, x2 + h) - f.apply(x1 + h, x2 - h)
                - f.apply(x1 - h, x2 + h) + f.apply(x1 - h, x2 - h)) / (4 * h * h);
    }

    public double secondDerivativeX1X2(double x1, double x2) {
        return secondDerivativeX1X2(x1, x2, 1e-8);
    }

    public double secondDerivativeX2X2(double x1, double x2, double h) {
        return (f.apply(x1, x2 + h) - 2 * f.apply(x1, x2) + f.apply(x1, x2 - h)) / (h * h);
    }

    public double secondDerivativeX2X2(double x1, double x2) {
        return secondDerivativeX2X2(x1, x2, 1e-8);
    }

    // решение системы методом простой итерации (градиентный спуск) с начальной точкой
    public double[] solveSystem(double startX1, double startX2) {
        double x1 = startX1, x2 = startX2;
        double learningRate = 0.01;

        System.out.println("Градиентный спуск:");
        System.out.printf("Начальная точка: x1 = %.2f, x2 = %.2f%n", startX1, startX2);
        System.out.println("Итерационный процесс:");
        System.out.println("--------------------------------------------------------");

        for (int iter = 0; iter < 100000; ++iter) {
            double grad1 = derivativeX1(x1, x2);
            double grad2 = derivativeX2(x1, x2);
            double norm = Math.sqrt(grad1 * grad1 + grad2 * grad2);

            if (iter < 5 || iter % 1000 == 0 || norm < 1e-12) {
                System.out.printf("Итерация %d: x1 = %.6f, x2 = %.6f, градиент = %.6f%n",
                        iter, x1, x2, norm);
            }

            x1 -= learningRate * grad1;
            x2 -= learningRate * grad2;

            if (norm < 1e-12) {
                System.out.printf("Сходимость достигнута на итерации %d%n", iter);
                break;
            }
        }
        return new double[]{x1, x2};
    }

    public void mainOutput() {
        System.out.println("Функция: f(x1, x2) = x1² + 4x2² + x1x2 + x1" + System.lineSeparator());

        // начальная точка из условия
        double startX1 = 3.0;
        double startX2 = 1.0;

        System.out.printf("Начальная точка: x1 = %.1f, x2 = %.1f%n%n", startX1, startX2);

        // находим стационарные точки
        double[] point = solveSystem(startX1, startX2);
        double x1 = point[0];
        double x2 = point[1];

        System.out.println("\nСтационарная точка:");
        System.out.printf("x1 = %.10f%n", x1);
        System.out.printf("x2 = %.10f%n", x2);
        System.out.printf("f(x1, x2) = %.10f%n%n", f.apply(x1, x2));

        // вычисляем градиент в стационарной точке
        System.out.println("Градиент в стационарной точке:");
        double grad1 = derivativeX1(x1, x2);
        double grad2 = derivativeX2(x1, x2);
        System.out.printf("∂f/∂x1 = %.10f%n", grad1);
        System.out.printf("∂f/∂x2 = %.10f%n", grad2);
        System.out.printf("Норма градиента = %.10f%n%n", Math.sqrt(grad1*grad1 + grad2*grad2));

        // вычисляем вторые производные
        System.out.println("Вторые производные (матрица Гессе):");
        double f11 = secondDerivativeX1X1(x1, x2);
        double f12 = secondDerivativeX1X2(x1, x2);
        double f22 = secondDerivativeX2X2(x1, x2);

        System.out.printf("∂²f/∂x1² = %.2f%n", f11);
        System.out.printf("∂²f/∂x1∂x2 = %.2f%n", f12);
        System.out.printf("∂²f/∂x2∂x1 = %.2f%n", f12);  // по теореме Шварца
        System.out.printf("∂²f/∂x2² = %.2f%n%n", f22);

        // матрица Гессе и определитель
        double det = f11 * f22 - f12 * f12;
        System.out.println("Матрица Гессе:");
        System.out.printf("[%.2f  %.2f]%n", f11, f12);
        System.out.printf("[%.2f  %.2f]%n", f12, f22);
        System.out.printf("Определитель: %.2f%n%n", det);

        // анализ достаточности условия
        System.out.println("Анализ экстремума:");
        if (det > 0 && f11 > 0) {
            System.out.println("det > 0 и ∂²f/∂x1² > 0 -> строгий локальный минимум");
        } else if (det > 0 && f11 < 0) {
            System.out.println("det > 0 и ∂²f/∂x1² < 0 -> строгий локальный максимум");
        } else if (det < 0) {
            System.out.println("det < 0 -> седловая точка");
        } else {
            System.out.println("det = 0 -> требуется дополнительное исследование");
        }

        // Дополнительная информация
        System.out.println("\nДополнительная информация:");
        System.out.println("Для квадратичной функции f(x1,x2) = x1² + 4x2² + x1x2 + x1:");
        System.out.println("- Матрица Гессе постоянна (не зависит от точки)");
        System.out.println("- Все вторые производные постоянны");
        System.out.println("- Функция строго выпуклая (матрица Гессе положительно определена)");
    }

    // Новая функция из условия: f(x1,x2) = x1² + 4x2² + x1x2 + x1
    public static double mainFunction(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    public static void main(String[] args) {
        MainClass solver = new MainClass(MainClass::mainFunction);
        solver.mainOutput();

        // Аналитическое решение для проверки
        System.out.println("\n" + "=".repeat(50));
        System.out.println("АНАЛИТИЧЕСКОЕ РЕШЕНИЕ:");
        System.out.println("Для f(x1,x2) = x1² + 4x2² + x1x2 + x1:");
        System.out.println("∂f/∂x1 = 2x1 + x2 + 1 = 0");
        System.out.println("∂f/∂x2 = 8x2 + x1 = 0");
        System.out.println("Решая систему:");
        System.out.println("2x1 + x2 = -1");
        System.out.println("x1 + 8x2 = 0");
        System.out.println("Решение: x1 = -8/15, x2 = 1/15");
        System.out.printf("Точное решение: x1 = %.6f, x2 = %.6f%n", -8.0/15.0, 1.0/15.0);

        System.out.println("\nМатрица Гессе (постоянная):");
        System.out.println("[2.00  1.00]");
        System.out.println("[1.00  8.00]");
        System.out.println("Определитель: 2×8 - 1×1 = 15.00");
        System.out.println("det > 0 и ∂²f/∂x1² = 2 > 0 -> ГЛОБАЛЬНЫЙ МИНИМУМ");
    }
}