import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenAlgorithm {
    public static List<int[]> initializePopulation(int numCities, int populationSize) {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            int[] individual = new int[numCities];
            for (int j = 0; j < numCities; j++) {
                individual[j] = j;
            }
            // Trộn ngẫu nhiên để tạo các cá thể ban đầu
            shuffleArray(individual);
            population.add(individual);
        }
        return population;
    }

    private static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static double calculateFitness(int[] individual, int[][] distanceMatrix) {
        double totalDistance = 0;
        for (int i = 0; i < individual.length - 1; i++) {
            totalDistance += distanceMatrix[individual[i]][individual[i + 1]];
        }
        totalDistance += distanceMatrix[individual[individual.length - 1]][individual[0]];
        return 1.0 / totalDistance; // Giá trị fitness cao khi quãng đường ngắn
    }

    public static int[] rouletteWheelSelection(List<int[]> population, double[] fitnesses) {
        double totalFitness = 0;
        for (double fitness : fitnesses) {
            totalFitness += fitness;
        }

        Random random = new Random();
        double pick = random.nextDouble() * totalFitness;
        double currentSum = 0;

        for (int i = 0; i < population.size(); i++) {
            currentSum += fitnesses[i];
            if (currentSum > pick) {
                return population.get(i);
            }
        }
        return population.getLast();
    }

    public static int[] crossover(int[] parent1, int[] parent2) {
        int n = parent1.length;
        int[] child = new int[n];
        boolean[] visited = new boolean[n];
        int start = new Random().nextInt(n);
        int end = new Random().nextInt(n);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        for (int i = start; i <= end; i++) {
            child[i] = parent1[i];
            visited[parent1[i]] = true;
        }
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[parent2[i]]) {
                while (index >= start && index <= end) index++;
                child[index++] = parent2[i];
            }
        }
        return child;
    }

    public static void mutate(int[] individual, double mutationRate) {
        Random random = new Random();
        if (random.nextDouble() < mutationRate) {
            int i = random.nextInt(individual.length);
            int j = random.nextInt(individual.length);
            int temp = individual[i];
            individual[i] = individual[j];
            individual[j] = temp;
        }
    }

    public static int[] initializeSolution(int numCities) {
        int[] solution = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            solution[i] = i;
        }
        shuffleArray(solution); // Hàm đã được định nghĩa ở trên
        return solution;
    }

    public static double energy(int[] solution, int[][] distanceMatrix) {
        double totalDistance = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            totalDistance += distanceMatrix[solution[i]][solution[i + 1]];
        }
        totalDistance += distanceMatrix[solution[solution.length - 1]][solution[0]];
        return totalDistance;
    }

    public static double coolingSchedule(double temperature, double alpha) {
        return temperature * alpha; // Nhiệt độ giảm dần theo alpha
    }

    public static int[] simulatedAnnealing(int[][] distanceMatrix, double initialTemp, double alpha, int maxIterations) {
        int[] currentSolution = initializeSolution(distanceMatrix.length);
        double currentEnergy = energy(currentSolution, distanceMatrix);
        int[] bestSolution = currentSolution.clone();
        double bestEnergy = currentEnergy;
        Random random = new Random();

        for (int i = 0; i < maxIterations; i++) {
            int[] newSolution = currentSolution.clone();
            int index1 = random.nextInt(newSolution.length);
            int index2 = random.nextInt(newSolution.length);
            // Hoán đổi
            int temp = newSolution[index1];
            newSolution[index1] = newSolution[index2];
            newSolution[index2] = temp;

            double newEnergy = energy(newSolution, distanceMatrix);
            if (newEnergy < currentEnergy || random.nextDouble() < Math.exp((currentEnergy - newEnergy) / initialTemp)) {
                currentSolution = newSolution;
                currentEnergy = newEnergy;
            }

            if (newEnergy < bestEnergy) {
                bestSolution = newSolution.clone();
                bestEnergy = newEnergy;
            }

            initialTemp = coolingSchedule(initialTemp, alpha);
        }
        return bestSolution;
    }

}
