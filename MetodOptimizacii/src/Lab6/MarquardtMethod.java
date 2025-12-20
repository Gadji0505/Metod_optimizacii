package Lab6;

import java.util.Locale;

public class MarquardtMethod {

    public static double f(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    public static double[] gradient(double x1, double x2) {
        return new double[]{2 * x1 + x2 + 1, x1 + 8 * x2};
    }

    public static double[][] hessian() {
        return new double[][]{{2, 1}, {1, 8}};
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        double x1 = 3.0; // x0 из условия
        double x2 = 1.0;
        double mu = 100.0;
        double eps = 0.1;
        int maxIter = 50;

        System.out.println("=== Лабораторная работа №6: Метод Марквардта ===");
        System.out.printf("x0 = (%.1f, %.1f), f(x0) = %.2f, mu0 = %.1f%n%n", x1, x2, f(x1, x2), mu);
        System.out.printf("%-4s | %-10s | %-10s | %-10s | %-10s | %-10s%n",
                "k", "x1", "x2", "f(x)", "|grad|", "mu");
        System.out.println("-----------------------------------------------------------------------");

        for (int k = 0; k < maxIter; k++) {
            double[] g = gradient(x1, x2);
            double gNorm = Math.sqrt(g[0] * g[0] + g[1] * g[1]);

            System.out.printf("%-4d | %-10.4f | %-10.4f | %-10.4f | %-10.4f | %-10.2f%n",
                    k, x1, x2, f(x1, x2), gNorm, mu);

            if (gNorm < eps) {
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("Критерий остановки выполнен (норма градиента < eps).");
                break;
            }

            // Формируем матрицу (H + mu*E)
            double[][] H = hessian();
            double a11 = H[0][0] + mu;
            double a12 = H[0][1];
            double a21 = H[1][0];
            double a22 = H[1][1] + mu;

            // Решаем систему (H + mu*E)S = -g (метод Крамера)
            double det = a11 * a22 - a12 * a21;
            double s1 = (-g[0] * a22 - a12 * (-g[1])) / det;
            double s2 = (a11 * (-g[1]) - (-g[0]) * a21) / det;

            double nextX1 = x1 + s1;
            double nextX2 = x2 + s2;

            if (f(nextX1, nextX2) < f(x1, x2)) {
                x1 = nextX1;
                x2 = nextX2;
                mu /= 2.0;
            } else {
                mu *= 2.0;
                k--;
            }
        }

        System.out.printf("%nФинальная точка: x* = (%.5f, %.5f)%n", x1, x2);
        System.out.printf("Минимум функции f(x*) = %.6f%n", f(x1, x2));
    }
}