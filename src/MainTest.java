import java.util.List;
import java.util.Random;

public class MainTest {

    public static void main(String[] args) {
        int numCities = 5; // Số lượng thành phố
        int[][] distanceMatrix = generateDistanceMatrix(numCities);

        System.out.println("Ma trận khoảng cách:");
        printMatrix(distanceMatrix);

        // Test Genetic Algorithm
        System.out.println("\nChạy thuật toán Genetic Algorithm...");
        int populationSize = 10;
        double mutationRate = 0.05;
        int generations = 100;

        List<int[]> population = GenAlgorithm.initializePopulation(numCities, populationSize);
        int[] bestSolutionGA = null;
        double bestFitnessGA = 0;

        for (int generation = 0; generation < generations; generation++) {
            double[] fitnesses = new double[population.size()];
            for (int i = 0; i < population.size(); i++) {
                fitnesses[i] = GenAlgorithm.calculateFitness(population.get(i), distanceMatrix);
                if (fitnesses[i] > bestFitnessGA) {
                    bestFitnessGA = fitnesses[i];
                    bestSolutionGA = population.get(i).clone();
                }
            }

            // Tạo thế hệ mới
            List<int[]> newPopulation = population.subList(0, populationSize / 2);
            Random random = new Random();
            while (newPopulation.size() < populationSize) {
                int[] parent1 = GenAlgorithm.rouletteWheelSelection(population, fitnesses);
                int[] parent2 = GenAlgorithm.rouletteWheelSelection(population, fitnesses);
                int[] child = GenAlgorithm.crossover(parent1, parent2);
                GenAlgorithm.mutate(child, mutationRate);
                newPopulation.add(child);
            }
            population = newPopulation;
        }

        System.out.println("Giải pháp tốt nhất Genetic Algorithm: ");
        printSolution(bestSolutionGA);
        System.out.println("Chi phí: " + (1.0 / bestFitnessGA));

        // Test Simulated Annealing
        System.out.println("\nChạy thuật toán Simulated Annealing...");
        double initialTemp = 1000;
        double alpha = 0.995;
        int maxIterations = 1000;

        int[] bestSolutionSA = GenAlgorithm.simulatedAnnealing(distanceMatrix, initialTemp, alpha, maxIterations);
        double bestEnergySA = GenAlgorithm.energy(bestSolutionSA, distanceMatrix);

        System.out.println("Giải pháp tốt nhất Simulated Annealing: ");
        printSolution(bestSolutionSA);
        System.out.println("Chi phí: " + bestEnergySA);
    }

    private static int[][] generateDistanceMatrix(int n) {
        Random random = new Random();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = random.nextInt(50) + 1; // Khoảng cách ngẫu nhiên từ 1 đến 50
                matrix[i][j] = distance;
                matrix[j][i] = distance;
            }
        }
        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%4d", value);
            }
            System.out.println();
        }
    }

    private static void printSolution(int[] solution) {
        for (int city : solution) {
            System.out.print(city + " -> ");
        }
        System.out.println(solution[0]); // Quay về thành phố xuất phát
    }
}
