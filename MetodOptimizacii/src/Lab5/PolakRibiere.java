package Lab5;

import java.util.function.Function;

public class PolakRibiere {

    public double[] optimize(Function<double[], Double> func, double[] x0, double tol, int maxIter) {
        int n = x0.length;
        double[] x = x0.clone();

        System.out.println("\nЗАПУСК МЕТОДА ПОЛЛАКА-РИБЬЕРА...");

        double[] g = gradient(x);
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = -g[i];

        for (int k = 0; k < maxIter; k++) {
            double gNorm = Math.sqrt(dotProduct(g, g));
            if (gNorm < tol) break;

            double lambda = goldenSection(func, x, d);
            double[] xOld = x.clone();
            double[] gOld = g.clone();

            for (int i = 0; i < n; i++) x[i] += lambda * d[i];

            g = gradient(x);

            // Коэффициент Поллака-Рибьера
            double betaNum = 0;
            for (int i = 0; i < n; i++) betaNum += g[i] * (g[i] - gOld[i]);
            double beta = Math.max(0, betaNum / dotProduct(gOld, gOld));

            for (int i = 0; i < n; i++) d[i] = -g[i] + beta * d[i];

            System.out.printf("Итерация %d: f = %.6f, |grad| = %.6e\n", k+1, func.apply(x), gNorm);
        }
        return x;
    }

    private double[] gradient(double[] x) {
        // Производные для f = x1^2 + 4x2^2 + x1x2 + x1
        return new double[]{
                2 * x[0] + x[1] + 1,
                8 * x[1] + x[0]
        };
    }

    private double dotProduct(double[] a, double[] b) {
        double res = 0;
        for (int i = 0; i < a.length; i++) res += a[i] * b[i];
        return res;
    }

    // Метод золотого сечения аналогичен методу в классе Powell
    private double goldenSection(Function<double[], Double> func, double[] x0, double[] dir) {
        double a = -2, b = 2, eps = 1e-8;
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
        for (int i = 0; i < x.length; i++) nextX[i] = x[i] + lambda * dir[i];
        return func.apply(nextX);
    }
}