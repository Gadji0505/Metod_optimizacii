package Lab4;

import java.util.function.BiFunction;

// 1. Метод наискорейшего спуска
class GradientDescent extends Optimizer {
    public GradientDescent(BiFunction<Double, Double, Double> f) { super(f); }
    public double[] optimize(double x1, double x2) {
        System.out.println("--- Метод наискорейшего спуска ---");
        for (int i = 0; i < 100; i++) {
            double g1 = df_dx1(x1, x2), g2 = df_dx2(x1, x2);
            if (Math.sqrt(g1*g1 + g2*g2) < 0.001) break;
            double alpha = getExactStep(x1, x2, -g1, -g2);
            x1 -= alpha * g1; x2 -= alpha * g2;
            if (i < 3) System.out.printf("Итерация %d: (%.4f, %.4f), f = %.4f\n", i+1, x1, x2, f.apply(x1, x2));
        }
        System.out.printf("Результат: (%.4f, %.4f)\n\n", x1, x2);
        return new double[]{x1, x2};
    }
}