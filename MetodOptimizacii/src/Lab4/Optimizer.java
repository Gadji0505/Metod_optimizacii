package Lab4;

import java.util.function.BiFunction;

abstract class Optimizer {
    protected BiFunction<Double, Double, Double> f;
    protected final double h = 1e-8;

    public Optimizer(BiFunction<Double, Double, Double> f) { this.f = f; }

    protected double df_dx1(double x1, double x2) { return (f.apply(x1 + h, x2) - f.apply(x1 - h, x2)) / (2 * h); }
    protected double df_dx2(double x1, double x2) { return (f.apply(x1, x2 + h) - f.apply(x1, x2 - h)) / (2 * h); }

    protected double d2f_dx1x1(double x1, double x2) { return (f.apply(x1 + h, x2) - 2 * f.apply(x1, x2) + f.apply(x1 - h, x2)) / (h * h); }
    protected double d2f_dx2x2(double x1, double x2) { return (f.apply(x1, x2 + h) - 2 * f.apply(x1, x2) + f.apply(x1, x2 - h)) / (h * h); }
    protected double d2f_dx1x2(double x1, double x2) { return (f.apply(x1 + h, x2 + h) - f.apply(x1 + h, x2 - h) - f.apply(x1 - h, x2 + h) + f.apply(x1 - h, x2 - h)) / (4 * h * h); }

    // Точный поиск шага для квадратичной формы: f = x^T A x + b^T x
    // Для f = x1^2 + 4x2^2 + x1x2 + x1 -> A = [[2, 1], [1, 8]], b = [1, 0]
    protected double getExactStep(double x1, double x2, double dx1, double dx2) {
        double num = dx1 * (2 * x1 + x2 + 1) + dx2 * (x1 + 8 * x2);
        double den = dx1 * (2 * dx1 + dx2) + dx2 * (dx1 + 8 * dx2);
        return Math.abs(den) < 1e-12 ? 0.1 : -num / den;
    }

    public abstract double[] optimize(double x1_0, double x2_0);
}