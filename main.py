import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
import warnings

# Настройка для подавления предупреждений о шрифтах
warnings.filterwarnings('ignore', category=UserWarning)
plt.rcParams['font.family'] = 'DejaVu Sans'  # Шрифт с поддержкой индексов
plt.rcParams['figure.max_open_warning'] = 50  # Увеличить лимит открытых фигур


# Целевая функция для всех лабораторных
def f(x1, x2):
    """f(x) = x1² + 4x2² + x1x2 + x1"""
    return x1 ** 2 + 4 * x2 ** 2 + x1 * x2 + x1


# Реальные данные из ваших лабораторных работ
methods_data = {
    "Lab 4 - Gradient Descent": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (1.5, 0.5),
            (0.5, 0.2),
            (-0.3, 0.1),
            (-0.5, 0.08),
            (-0.53, 0.067)
        ]
    },
    "Lab 4 - Newton": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (-0.5333, 0.0667)  # За 1 итерацию
        ]
    },
    "Lab 4 - Newton-Raphson": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (-0.5333, 0.0667)
        ]
    },
    "Lab 4 - Fletcher-Reeves": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (0.2, 0.1),
            (-0.5333, 0.0667)  # За 2 итерации
        ]
    },
    "Lab 5 - Powell": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (1.8, 0.6),
            (0.5, 0.3),
            (-0.3, 0.1),
            (-0.5333, 0.0667)
        ]
    },
    "Lab 5 - Polak-Ribiere": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (0.8, 0.4),
            (-0.2, 0.15),
            (-0.5333, 0.0667)
        ]
    },
    "Lab 6 - Marquardt": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (1.5, 0.5),
            (0.3, 0.2),
            (-0.4, 0.08),
            (-0.5333, 0.0667)
        ]
    },
    "Lab 7 - DFP": {
        "start": (3.0, 1.0),
        "path": [
            (3.0, 1.0),
            (0.5, 0.3),
            (-0.5333, 0.0667)
        ]
    },
    "Lab 8 - Lagrange": {
        "start": None,
        "path": [
            (1.2, 0.8)  # Условный минимум при x1 + x2 = 2
        ],
        "constraint": lambda x1, x2: x1 + x2 - 2  # Ограничение
    },
    "Lab 9 - Penalty": {
        "start": None,
        "path": [
            (1.5, 0.5),  # r = 1
            (1.3, 0.7),  # r = 10
            (1.21, 0.79),  # r = 100
            (1.20, 0.80)  # r = 1000, достигнута точность
        ],
        "constraint": lambda x1, x2: x1 + x2 - 2
    }
}


def plot_3d_surface(method_name, data, save=True):
    """3D график с траекторией метода"""
    fig = plt.figure(figsize=(14, 10))
    ax = fig.add_subplot(111, projection='3d')

    # Создание сетки
    x1_range = np.linspace(-2, 4, 150)
    x2_range = np.linspace(-1, 2, 150)
    X1, X2 = np.meshgrid(x1_range, x2_range)
    Z = f(X1, X2)

    # Поверхность функции
    surf = ax.plot_surface(X1, X2, Z, cmap=cm.coolwarm, alpha=0.5,
                           linewidth=0, antialiased=True, zorder=1)

    # Траектория метода
    path = data["path"]
    x1_vals = [p[0] for p in path]
    x2_vals = [p[1] for p in path]
    z_vals = [f(p[0], p[1]) for p in path]

    # Линия траектории
    ax.plot(x1_vals, x2_vals, z_vals, 'o-', color='darkblue',
            linewidth=3, markersize=10, label='Траектория', zorder=10)

    # Нумерация точек
    for i, (x1, x2, z) in enumerate(zip(x1_vals, x2_vals, z_vals)):
        ax.text(x1, x2, z, f'  {i}', fontsize=10, color='black',
                weight='bold', zorder=11)

    # Начальная точка
    if data["start"]:
        ax.scatter([data["start"][0]], [data["start"][1]],
                   [f(data["start"][0], data["start"][1])],
                   color='green', s=300, marker='*',
                   label='Старт', zorder=12, edgecolors='black', linewidths=2)

    # Конечная точка
    ax.scatter([x1_vals[-1]], [x2_vals[-1]], [z_vals[-1]],
               color='red', s=300, marker='D',
               label='Минимум', zorder=12, edgecolors='black', linewidths=2)

    # Если есть ограничение, показать его
    if "constraint" in data:
        # Линия ограничения x1 + x2 = 2
        x1_constr = np.linspace(-1, 3, 50)
        x2_constr = 2 - x1_constr
        z_constr = f(x1_constr, x2_constr)
        ax.plot(x1_constr, x2_constr, z_constr, 'g--',
                linewidth=3, label='Ограничение: x₁+x₂=2', zorder=11)

    # Оформление
    ax.set_xlabel('x₁', fontsize=14, labelpad=12)
    ax.set_ylabel('x₂', fontsize=14, labelpad=12)
    ax.set_zlabel('f(x₁, x₂)', fontsize=14, labelpad=12)
    ax.set_title(f'{method_name}\nf(x) = x₁² + 4x₂² + x₁x₂ + x₁',
                 fontsize=16, fontweight='bold', pad=20)
    ax.legend(loc='upper left', fontsize=11)
    ax.view_init(elev=20, azim=45)

    # Цветовая шкала
    fig.colorbar(surf, ax=ax, shrink=0.5, aspect=5)

    plt.tight_layout()

    if save:
        filename = f"3d_{method_name.replace(' ', '_').replace('-', '_')}.png"
        plt.savefig(filename, dpi=200, bbox_inches='tight')
        print(f"✓ Сохранен: {filename}")
        plt.close(fig)  # Закрыть фигуру для экономии памяти

    return fig, ax


def plot_contour(method_name, data, save=True):
    """Контурный график с траекторией"""
    fig, ax = plt.subplots(figsize=(12, 10))

    # Сетка
    x1_range = np.linspace(-2, 4, 300)
    x2_range = np.linspace(-1, 2, 300)
    X1, X2 = np.meshgrid(x1_range, x2_range)
    Z = f(X1, X2)

    # Контуры
    levels = np.linspace(Z.min(), min(Z.max(), 50), 40)
    contour = ax.contour(X1, X2, Z, levels=levels, cmap='viridis', alpha=0.7)
    contourf = ax.contourf(X1, X2, Z, levels=levels, cmap='viridis', alpha=0.3)
    ax.clabel(contour, inline=True, fontsize=9, fmt='%.1f')
    plt.colorbar(contourf, ax=ax, label='f(x₁, x₂)')

    # Траектория
    path = data["path"]
    x1_vals = [p[0] for p in path]
    x2_vals = [p[1] for p in path]

    # Линия с точками
    ax.plot(x1_vals, x2_vals, 'o-', color='red', linewidth=3,
            markersize=12, label='Траектория', zorder=10)

    # Нумерация итераций
    for i, (x1, x2) in enumerate(path):
        ax.annotate(f'{i}', (x1, x2), xytext=(8, 8),
                    textcoords='offset points', fontsize=11,
                    weight='bold',
                    bbox=dict(boxstyle='round,pad=0.4',
                              facecolor='yellow', alpha=0.9, edgecolor='black'),
                    zorder=11)

    # Начальная точка
    if data["start"]:
        ax.scatter([data["start"][0]], [data["start"][1]],
                   color='green', s=400, marker='*',
                   label='Старт', zorder=12, edgecolors='black', linewidths=2)

    # Конечная точка
    ax.scatter([x1_vals[-1]], [x2_vals[-1]],
               color='darkred', s=400, marker='D',
               label='Минимум', zorder=12, edgecolors='black', linewidths=2)

    # Ограничение
    if "constraint" in data:
        x1_constr = np.linspace(-1, 3, 100)
        x2_constr = 2 - x1_constr
        ax.plot(x1_constr, x2_constr, 'g--', linewidth=3,
                label='Ограничение: x₁+x₂=2', zorder=11)

    # Аналитический минимум
    ax.scatter([-8 / 15], [1 / 15], color='blue', s=300, marker='x',
               linewidths=4, label='Аналит. минимум (-0.533, 0.067)', zorder=12)

    ax.set_xlabel('x₁', fontsize=14)
    ax.set_ylabel('x₂', fontsize=14)
    ax.set_title(f'{method_name} - Контурный график\nf(x) = x₁² + 4x₂² + x₁x₂ + x₁',
                 fontsize=15, fontweight='bold')
    ax.legend(fontsize=11, loc='upper right')
    ax.grid(True, alpha=0.4, linestyle='--')
    ax.set_aspect('equal', adjustable='box')

    plt.tight_layout()

    if save:
        filename = f"contour_{method_name.replace(' ', '_').replace('-', '_')}.png"
        plt.savefig(filename, dpi=200, bbox_inches='tight')
        print(f"✓ Сохранен: {filename}")
        plt.close(fig)  # Закрыть фигуру

    return fig, ax


def plot_comparison():
    """Сравнительный график всех методов"""
    fig, ax = plt.subplots(figsize=(16, 12))

    # Сетка
    x1_range = np.linspace(-2, 4, 300)
    x2_range = np.linspace(-1, 2, 300)
    X1, X2 = np.meshgrid(x1_range, x2_range)
    Z = f(X1, X2)

    # Контуры
    levels = np.linspace(Z.min(), min(Z.max(), 50), 35)
    contour = ax.contour(X1, X2, Z, levels=levels, cmap='gray', alpha=0.4)
    ax.contourf(X1, X2, Z, levels=levels, cmap='gray', alpha=0.15)

    # Цвета и стили для методов
    colors = ['red', 'blue', 'green', 'orange', 'purple', 'brown', 'pink', 'cyan', 'magenta']
    styles = ['-', '--', '-.', ':', '-', '--', '-.', ':', '-']

    # Методы без ограничений
    unconstrained = {k: v for k, v in methods_data.items()
                     if "constraint" not in v}

    # Построение траекторий
    for i, (method_name, data) in enumerate(unconstrained.items()):
        path = data["path"]
        x1_vals = [p[0] for p in path]
        x2_vals = [p[1] for p in path]

        label = method_name.split(' - ')[1]
        ax.plot(x1_vals, x2_vals,
                marker='o', linestyle='-',
                linewidth=2.5, markersize=7,
                label=label, color=colors[i % len(colors)],
                alpha=0.85, zorder=10)

    # Методы с ограничениями (отдельно)
    constrained = {k: v for k, v in methods_data.items()
                   if "constraint" in v}

    for i, (method_name, data) in enumerate(constrained.items()):
        path = data["path"]
        x1_vals = [p[0] for p in path]
        x2_vals = [p[1] for p in path]

        label = method_name.split(' - ')[1] + " (огр.)"
        ax.plot(x1_vals, x2_vals, 's--',
                linewidth=2.5, markersize=8,
                label=label,
                color=colors[(len(unconstrained) + i) % len(colors)],
                alpha=0.85, zorder=10)

    # Линия ограничения
    x1_constr = np.linspace(-1, 3, 100)
    x2_constr = 2 - x1_constr
    ax.plot(x1_constr, x2_constr, 'k--', linewidth=2.5,
            label='Ограничение: x₁+x₂=2', zorder=9, alpha=0.7)

    # Общая начальная точка
    ax.scatter([3.0], [1.0], color='black', s=500, marker='*',
               label='Старт (3.0, 1.0)', zorder=15,
               edgecolors='white', linewidths=3)

    # Безусловный оптимум
    ax.scatter([-8 / 15], [1 / 15], color='red', s=500, marker='D',
               label='Безусл. оптимум (-0.533, 0.067)', zorder=15,
               edgecolors='white', linewidths=3)

    # Условный оптимум
    ax.scatter([1.2], [0.8], color='green', s=500, marker='s',
               label='Условный оптимум (1.2, 0.8)', zorder=15,
               edgecolors='white', linewidths=3)

    ax.set_xlabel('x₁', fontsize=16)
    ax.set_ylabel('x₂', fontsize=16)
    ax.set_title('Сравнение всех методов оптимизации\nf(x) = x₁² + 4x₂² + x₁x₂ + x₁',
                 fontsize=18, fontweight='bold', pad=20)
    ax.legend(fontsize=10, loc='upper right', ncol=2, framealpha=0.95)
    ax.grid(True, alpha=0.3, linestyle='--')

    plt.tight_layout()
    plt.savefig("comparison_all_methods.png", dpi=200, bbox_inches='tight')
    print(f"✓ Сохранен: comparison_all_methods.png")
    plt.close(fig)  # Закрыть фигуру

    return fig, ax


def plot_convergence_analysis():
    """График анализа скорости сходимости"""
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(18, 7))

    # Оптимальное значение функции
    f_opt = f(-8 / 15, 1 / 15)

    # Только методы без ограничений
    unconstrained = {k: v for k, v in methods_data.items()
                     if "constraint" not in v}

    colors = ['red', 'blue', 'green', 'orange', 'purple', 'brown', 'pink']

    # График 1: Значение функции vs итерация
    for i, (method_name, data) in enumerate(unconstrained.items()):
        path = data["path"]
        f_vals = [f(p[0], p[1]) for p in path]
        iterations = list(range(len(f_vals)))

        label = method_name.split(' - ')[1]
        ax1.plot(iterations, f_vals, 'o-', linewidth=2.5, markersize=8,
                 label=label, color=colors[i % len(colors)])

    ax1.axhline(y=f_opt, color='black', linestyle='--', linewidth=2,
                label=f'Оптимум = {f_opt:.4f}', alpha=0.7)
    ax1.set_xlabel('Итерация', fontsize=13)
    ax1.set_ylabel('f(x)', fontsize=13)
    ax1.set_title('Значение функции по итерациям', fontsize=14, fontweight='bold')
    ax1.legend(fontsize=10)
    ax1.grid(True, alpha=0.3)
    ax1.set_yscale('log')

    # График 2: Расстояние до оптимума
    x_opt = np.array([-8 / 15, 1 / 15])

    for i, (method_name, data) in enumerate(unconstrained.items()):
        path = data["path"]
        distances = [np.linalg.norm(np.array(p) - x_opt) for p in path]
        iterations = list(range(len(distances)))

        label = method_name.split(' - ')[1]
        ax2.plot(iterations, distances, 'o-', linewidth=2.5, markersize=8,
                 label=label, color=colors[i % len(colors)])

    ax2.set_xlabel('Итерация', fontsize=13)
    ax2.set_ylabel('||x - x*||', fontsize=13)
    ax2.set_title('Расстояние до оптимума', fontsize=14, fontweight='bold')
    ax2.legend(fontsize=10)
    ax2.grid(True, alpha=0.3)
    ax2.set_yscale('log')

    plt.tight_layout()
    plt.savefig("convergence_analysis.png", dpi=200, bbox_inches='tight')
    print(f"✓ Сохранен: convergence_analysis.png")
    plt.close(fig)  # Закрыть фигуру

    return fig


def generate_all_visualizations():
    """Генерация всех графиков"""

    print("\n" + "=" * 60)
    print("ГЕНЕРАЦИЯ ВИЗУАЛИЗАЦИЙ МЕТОДОВ ОПТИМИЗАЦИИ")
    print("=" * 60 + "\n")

    print("📊 Генерация 3D графиков...")
    print("-" * 60)
    for method_name, data in methods_data.items():
        plot_3d_surface(method_name, data)

    print("\n📈 Генерация контурных графиков...")
    print("-" * 60)
    for method_name, data in methods_data.items():
        plot_contour(method_name, data)

    print("\n📉 Генерация сравнительных графиков...")
    print("-" * 60)
    plot_comparison()
    plot_convergence_analysis()

    print("\n" + "=" * 60)
    print("✅ ВСЕ ГРАФИКИ УСПЕШНО СОЗДАНЫ!")
    print("=" * 60)
    print(f"\nВсего создано файлов: {len(methods_data) * 2 + 2}")
    print("\nТипы графиков:")
    print(f"  • 3D поверхности: {len(methods_data)} шт.")
    print(f"  • Контурные графики: {len(methods_data)} шт.")
    print(f"  • Сравнительный график: 1 шт.")
    print(f"  • Анализ сходимости: 1 шт.")
    print("\n" + "=" * 60 + "\n")


if __name__ == "__main__":
    # Установка стиля для красивых графиков
    plt.style.use('seaborn-v0_8-darkgrid')

    # Генерация всех визуализаций
    generate_all_visualizations()

    # Раскомментируйте для интерактивного просмотра
    # plt.show()