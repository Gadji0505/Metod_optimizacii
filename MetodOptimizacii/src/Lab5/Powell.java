package Lab5;

import java.util.function.Function;

public class Powell {

    public double[] optimize(Function<double[], Double> func, double[] x0, double tol, int maxIter) {
        // ИСПРАВЛЕНО: .length вместо .size()
        int n = x0.length;
        double[] x = x0.clone();
        double[][] directions = new double[n][n];

        // Инициализация базисными векторами
        for (int i = 0; i < n; i++) {
            directions[i][i] = 1.0;
        }

        System.out.println("\nЗАПУСК МЕТОДА ПАУЭЛЛА...");

        for (int iter = 0; iter < maxIter; iter++) {
            double[] xStart = x.clone();

            // Последовательный поиск по направлениям
            for (int i = 0; i < n; i++) {
                double lambda = goldenSection(func, x, directions[i]);
                for (int j = 0; j < n; j++) {
                    x[j] += lambda * directions[i][j];
                }
            }

            // Новое сопряженное направление
            double[] conjugateDir = new double[n];
            for (int i = 0; i < n; i++) {
                conjugateDir[i] = x[i] - xStart[i];
            }

            double norm = 0;
            for (double d : conjugateDir) norm += d * d;
            norm = Math.sqrt(norm);

            if (norm < tol) break;

            // Поиск вдоль сопряженного направления
            double lambda = goldenSection(func, x, conjugateDir);
            for (int j = 0; j < n; j++) {
                x[j] += lambda * conjugateDir[j];
            }

            // Обновление набора направлений
            for (int i = 0; i < n - 1; i++) {
                directions[i] = directions[i + 1].clone();
            }
            directions[n - 1] = conjugateDir.clone();

            System.out.printf("Итерация %d: f = %.6f, x = [%.4f, %.4f]\n",
                    iter + 1, func.apply(x), x[0], x[1]);
        }
        return x;
    }

    private double goldenSection(Function<double[], Double> func, double[] x0, double[] dir) {
        double a = -10, b = 10, eps = 1e-8;
        double phi = (Math.sqrt(5) - 1) / 2;
        double x1 = b - phi * (b - a);
        double x2 = a + phi * (b - a);

        while (Math.abs(b - a) > eps) {
            if (applyStep(func, x0, dir, x1) < applyStep(func, x0, dir, x2)) {
                b = x2; x2 = x1; x1 = b - phi * (b - a);
            } else {
                a = x1; x1 = x2; x2 = a + phi * (b - a);
            }
        }
        return (a + b) / 2;
    }

    private double applyStep(Function<double[], Double> func, double[] x, double[] dir, double lambda) {
        double[] nextX = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            nextX[i] = x[i] + lambda * dir[i];
        }
        return func.apply(nextX);
    }
}