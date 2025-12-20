package Lab5;

import java.util.Arrays;

public class Main {

    // Целевая функция: f(x1, x2) = x1^2 + 4x2^2 + x1x2 + x1
    public static double mainFunction(double[] x) {
        return Math.pow(x[0], 2) + 4 * Math.pow(x[1], 2) + x[0] * x[1] + x[0];
    }

    public static void main(String[] args) {
        double[] x0 = {3.0, 1.0}; // Начальная точка из вашего примера
        double tol = 1e-6;

        System.out.println("--- ОПТИМИЗАЦИЯ ФУНКЦИИ МНОГИХ ПЕРЕМЕННЫХ ---");
        System.out.println("Начальная точка: " + Arrays.toString(x0));

        // 1. Метод Пауэлла
        Powell powell = new Powell();
        double[] resPowell = powell.optimize(Main::mainFunction, x0, tol, 100);
        System.out.printf("\nФинальный результат (Пауэлл):\nМинимум: %s\nf(x) = %.10f\n",
                Arrays.toString(resPowell), mainFunction(resPowell));

        // 2. Метод Поллака-Рибьера
        PolakRibiere pr = new PolakRibiere();
        double[] resPR = pr.optimize(Main::mainFunction, x0, tol, 100);
        System.out.printf("\nФинальный результат (Поллак-Рибьер):\nМинимум: %s\nf(x) = %.10f\n",
                Arrays.toString(resPR), mainFunction(resPR));
    }
}