package Lab4;

public class Main {
    // Целевая функция: f(x1, x2) = x1^2 + 4x2^2 + x1x2 + x1
    public static double mainFunction(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    public static void main(String[] args) {
        double x1_0 = 3.0, x2_0 = 1.0;

        System.out.println("Функция: f(x1, x2) = x1^2 + 4x2^2 + x1x2 + x1");
        System.out.println("Начальная точка: (" + x1_0 + ", " + x2_0 + ")");
        System.out.println("Начальное значение: " + mainFunction(x1_0, x2_0));

        double x1_analytical = -8.0 / 15.0;
        double x2_analytical = 1.0 / 15.0;
        System.out.printf("\nАналитическое решение: x* = (%.4f, %.4f), f(x*) = %.4f\n\n",
                x1_analytical, x2_analytical, mainFunction(x1_analytical, x2_analytical));

        Optimizer gd = new GradientDescent(Main::mainFunction);
        gd.optimize(x1_0, x2_0);

        Optimizer newton = new Newton(Main::mainFunction);
        newton.optimize(x1_0, x2_0);

        Optimizer nr = new NewtonRaphson(Main::mainFunction);
        nr.optimize(x1_0, x2_0);

        Optimizer fr = new FletcherReeves(Main::mainFunction);
        fr.optimize(x1_0, x2_0);
    }
}
