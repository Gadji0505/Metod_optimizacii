package Lab8;

import java.util.Locale;

public class LagrangeMethodFinal {

    // Исходная функция f(x)
    public static double objectiveFunction(double x1, double x2) {
        return x1 * x1 + 4 * x2 * x2 + x1 * x2 + x1;
    }

    // Ограничение g(x) = x1 + x2 - 2
    public static double constraintFunction(double x1, double x2) {
        return x1 + x2 - 2;
    }

    /**
     * Решение системы 3x3 методом Крамера
     * Системный вид:
     * 2*x1 + 1*x2 + 1*L = -1
     * 1*x1 + 8*x2 + 1*L = 0
     * 1*x1 + 1*x2 + 0*L = 2
     */
    public static void solveLagrange() {
        double[][] A = {
                {2, 1, 1},
                {1, 8, 1},
                {1, 1, 0}
        };
        double[] B = {-1, 0, 2};

        double mainDet = calculateDet3x3(A);

        if (Math.abs(mainDet) < 1e-10) {
            System.out.println("Система не имеет однозначного решения (детерминант равен 0).");
            return;
        }

        // Вычисляем x1
        double x1 = calculateDet3x3(replaceColumn(A, B, 0)) / mainDet;
        // Вычисляем x2
        double x2 = calculateDet3x3(replaceColumn(A, B, 1)) / mainDet;
        // Вычисляем Lambda
        double lambda = calculateDet3x3(replaceColumn(A, B, 2)) / mainDet;

        // Вывод результатов
        System.out.println("--- Результаты метода Лагранжа ---");
        System.out.printf("Точка минимума: x1 = %.4f, x2 = %.4f%n", x1, x2);
        System.out.printf("Множитель Лагранжа: lambda = %.4f%n", lambda);
        System.out.printf("Значение функции f(x*) = %.4f%n", objectiveFunction(x1, x2));
        System.out.printf("Проверка ограничения g(x*) = %.4f%n", constraintFunction(x1, x2));

        // Проверка достаточного условия (Окаймленный Гессиан)
        checkSecondOrderCondition();
    }

    // Вспомогательная функция для определителя 3x3
    private static double calculateDet3x3(double[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
                - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
                + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }

    // Замена столбца в матрице для метода Крамера
    private static double[][] replaceColumn(double[][] matrix, double[] b, int colIndex) {
        double[][] newMatrix = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newMatrix[i][j] = (j == colIndex) ? b[i] : matrix[i][j];
            }
        }
        return newMatrix;
    }

    // Проверка определителя окаймленного Гессиана
    private static void checkSecondOrderCondition() {
        // H_border = [ 0  g1  g2 ]
        //            [ g1 L11 L12 ]
        //            [ g2 L21 L22 ]
        // Для нашего случая: g1=1, g2=1, L11=2, L12=1, L22=8
        double[][] hBorder = {
                {0, 1, 1},
                {1, 2, 1},
                {1, 1, 8}
        };
        double det = calculateDet3x3(hBorder);
        System.out.println("\n--- Проверка достаточных условий ---");
        System.out.printf("Определитель окаймленного Гессиана: %.1f%n", det);
        if (det < 0) {
            System.out.println("Так как det(H) < 0, найденная точка является точкой МИНИМУМА.");
        } else {
            System.out.println("Так как det(H) > 0, найденная точка является точкой МАКСИМУМА.");
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.out.println("Решение задачи оптимизации:");
        System.out.println("f(x) = x1^2 + 4*x2^2 + x1*x2 + x1 -> min");
        System.out.println("при условии: x1 + x2 = 2\n");
        solveLagrange();
    }
}