package Lab4;

import java.util.function.BiFunction;

// 4. Метод Флетчера-Ривса
class FletcherReeves extends Optimizer {
    public FletcherReeves(BiFunction<Double, Double, Double> f) { super(f); }
    public double[] optimize(double x1, double x2) {
        System.out.println("--- Метод Флетчера-Ривса ---");
        double g1 = df_dx1(x1, x2), g2 = df_dx2(x1, x2);
        double dx1 = -g1, dx2 = -g2;
        for (int i = 0; i < 2; i++) { // Для n=2 сходится за 2 шага
            double alpha = getExactStep(x1, x2, dx1, dx2);
            x1 += alpha * dx1; x2 += alpha * dx2;
            double g1_new = df_dx1(x1, x2), g2_new = df_dx2(x1, x2);
            double beta = (g1_new * g1_new + g2_new * g2_new) / (g1 * g1 + g2 * g2);
            dx1 = -g1_new + beta * dx1; dx2 = -g2_new + beta * dx2;
            g1 = g1_new; g2 = g2_new;
            System.out.printf("Итерация %d: (%.4f, %.4f), f = %.4f\n", i+1, x1, x2, f.apply(x1, x2));
        }
        System.out.printf("Результат: (%.4f, %.4f)\n\n", x1, x2);
        return new double[]{x1, x2};
    }
}