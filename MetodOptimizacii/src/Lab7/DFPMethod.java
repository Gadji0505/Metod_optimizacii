package Lab7;

import java.util.Locale;

public class DFPMethod {

    public static double f(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    public static double[] grad(double x1, double x2) {
        return new double[]{2 * x1 + x2 + 1, x1 + 8 * x2};
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        double[] x = {3.0, 1.0}; // x0
        double[][] A = {{1.0, 0.0}, {0.0, 1.0}}; // A0 = E
        double eps1 = 0.1;
        double eps2 = 0.1;
        int maxIter = 50;

        System.out.println("=== Метод ДФП для f(x) = x1^2 + 4x2^2 + x1x2 + x1 ===");

        for (int k = 0; k < maxIter; k++) {
            double[] g = grad(x[0], x[1]);
            double gNorm = Math.sqrt(g[0] * g[0] + g[1] * g[1]);

            System.out.printf("Ит %d: x=(%.4f, %.4f), f=%.4f, |grad|=%.4f%n", k, x[0], x[1], f(x[0], x[1]), gNorm);

            if (gNorm < eps1) break;

            // Направление S = -A * g
            double[] s = {
                    -(A[0][0] * g[0] + A[0][1] * g[1]),
                    -(A[1][0] * g[0] + A[1][1] * g[1])
            };

            double t = goldenSectionSearch(x, s);

            double[] dx = {t * s[0], t * s[1]};
            double[] nextX = {x[0] + dx[0], x[1] + dx[1]};
            double[] nextG = grad(nextX[0], nextX[1]);
            double[] dg = {nextG[0] - g[0], nextG[1] - g[1]};

            // Проверка условия остановки по приращению
            double xDiff = Math.sqrt(dx[0]*dx[0] + dx[1]*dx[1]);
            double fDiff = Math.abs(f(nextX[0], nextX[1]) - f(x[0], x[1]));
            if (xDiff < eps2 && fDiff < eps2) {
                x = nextX;
                break;
            }

            // Обновление матрицы A по формуле ДФП
            updateA(A, dx, dg);
            x = nextX;
        }

        System.out.printf("%nФинальная точка: x* = (%.5f, %.5f)%n", x[0], x[1]);
        System.out.printf("Минимум f(x*) = %.6f%n", f(x[0], x[1]));
    }

    private static void updateA(double[][] A, double[] dx, double[] dg) {
        double dx_dg = dx[0] * dg[0] + dx[1] * dg[1];

        double[] Adg = {
                A[0][0] * dg[0] + A[0][1] * dg[1],
                A[1][0] * dg[0] + A[1][1] * dg[1]
        };
        double dg_Adg = dg[0] * Adg[0] + dg[1] * Adg[1];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                A[i][j] += (dx[i] * dx[j] / dx_dg) - (Adg[i] * Adg[j] / dg_Adg);
            }
        }
    }

    private static double goldenSectionSearch(double[] x, double[] s) {
        double a = 0, b = 1.0, phi = (1 + Math.sqrt(5)) / 2;
        double resPhi = 2 - phi;
        double t1 = a + resPhi * (b - a);
        double t2 = b - resPhi * (b - a);

        for (int i = 0; i < 40; i++) {
            if (f(x[0] + t1 * s[0], x[1] + t1 * s[1]) < f(x[0] + t2 * s[0], x[1] + t2 * s[1])) {
                b = t2; t2 = t1; t1 = a + resPhi * (b - a);
            } else {
                a = t1; t1 = t2; t2 = b - resPhi * (b - a);
            }
        }
        return (a + b) / 2;
    }
}