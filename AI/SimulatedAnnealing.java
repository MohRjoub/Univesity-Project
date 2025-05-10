import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {

    private final HashMap<Integer, Double> schedule;
    private final List<Double> results;
    private final double minTemperature = 1e-10;

    public SimulatedAnnealing(double initialTemperature, double coolingRate) {
        schedule = new HashMap<>();
        results = new ArrayList<>();

        int t = 0;
        while (initialTemperature > minTemperature) {
            schedule.put(t++, initialTemperature);
            // T = T0 / log(t + c)
            initialTemperature *= coolingRate;
        }
        schedule.put(t, 0.0);
    }

    public double optimize(Function function, double[] initialSolution, double minBound, double maxBound) {
        double[] currentSolution = initialSolution.clone();
        double currentValue = function.evaluate(currentSolution);
        double bestValue = currentValue;
        results.add(currentValue);

        for (int t = 0; t < schedule.size(); t++) {
            double T = schedule.get(t);
            if (T == 0.0) break;

            double[] newSolution = generateNeighbors(currentSolution, minBound, maxBound);
            double newValue = function.evaluate(newSolution);
            double deltaE = newValue - currentValue;

            if (deltaE < 0 || Math.exp(-deltaE / T) > ThreadLocalRandom.current().nextDouble()) {
                currentSolution = newSolution;
                currentValue = newValue;
                if (currentValue < bestValue) {
                    bestValue = currentValue;
                }
            }

            results.add(currentValue);
        }

        return bestValue;
    }

    private double[] generateNeighbors(double[] solution, double min, double max) {
        double[] neighbor = solution.clone();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        
        int index = rand.nextInt(solution.length);
        neighbor[index] = rand.nextDouble(min, max);
    
        return neighbor;
    }
    
    public HashMap<Integer, Double> getSchedule() {
        return schedule;
    }

    public double[] getResults() {
        return results.stream().mapToDouble(Double::doubleValue).toArray();
    }
}