public class Rastrigin implements Function {
    private int n;
    private double A = 10.0;

    public Rastrigin(int n) {
        this.n = n;
    }

    @Override
    public double evaluate(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += x[i] * x[i] - A * Math.cos(2 * Math.PI * x[i]);
        }
        return A * n + sum;
    }
    
}
