package Lab4;

import java.util.function.BiFunction;

// 3. Метод Ньютона-Рафсона (с регулировкой шага)
class NewtonRaphson extends Optimizer {
    public NewtonRaphson(BiFunction<Double, Double, Double> f) { super(f); }
    public double[] optimize(double x1, double x2) {
        System.out.println("--- Метод Ньютона-Рафсона ---");
        double g1 = df_dx1(x1, x2), g2 = df_dx2(x1, x2);
        double h11 = d2f_dx1x1(x1, x2), h22 = d2f_dx2x2(x1, x2), h12 = d2f_dx1x2(x1, x2);
        double det = h11 * h22 - h12 * h12;
        double dx1 = -(h22 * g1 - h12 * g2) / det, dx2 = -(-h12 * g1 + h11 * g2) / det;
        double alpha = getExactStep(x1, x2, dx1, dx2); // В идеале alpha=1 для квадратичной
        x1 += alpha * dx1; x2 += alpha * dx2;
        System.out.printf("Результат: (%.4f, %.4f), f = %.4f\n\n", x1, x2, f.apply(x1, x2));
        return new double[]{x1, x2};
    }
}