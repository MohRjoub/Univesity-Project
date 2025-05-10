
public class Main {
    public static void main(String[] args) {
        int n = 15;
        double minBound = -2;
        double maxBound = 2;
        double initialTemperature = 1000.0;
        double coolingRate = 0.95;

        Rastrigin function = new Rastrigin(n);
        SimulatedAnnealing sa = new SimulatedAnnealing(initialTemperature, coolingRate);

        double[] initialSolution = new double[n];
        for (int i = 0; i < n; i++) {
            initialSolution[i] = Math.random() * (maxBound - minBound) + minBound;
        }

        double result = sa.optimize(function, initialSolution, minBound, maxBound);
        System.out.println("Optimized value: " + result);
    }
    
}
