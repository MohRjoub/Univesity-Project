import jpype
import matplotlib.pyplot as plt
import numpy as np


jpype.startJVM("C:/Users/ACTC/AppData/Local/Programs/Eclipse Adoptium/jdk-17.0.10.7-hotspot/bin/server/jvm.dll", classpath=["."])

n = 15
minBound = -2
maxBound = 2
initialTemperature = 1000
coolingRate = 0.99

# Import and use a Java class
Rastrigin = jpype.JClass("Rastrigin")
SimulatedAnnealing = jpype.JClass("SimulatedAnnealing")
rastrigin = Rastrigin(n)
simulated_annealing = SimulatedAnnealing(initialTemperature, coolingRate)

initialSolution = []
for i in range(n):
    initialSolution.append(minBound + (maxBound - minBound) * jpype.JDouble(jpype.java.lang.Math.random()))

result = simulated_annealing.optimize(rastrigin, initialSolution, minBound, maxBound)
results = simulated_annealing.getResults()
# plotting the results
results_list = list(results)

# Plot it
y = np.array(results)
x = np.arange(len(y))

x_smooth = np.linspace(x.min(), x.max(), 500)  # 500 points for smoothness
y_smooth = np.interp(x_smooth, x, y)  # Linear interpolation

# Plot smooth curve
plt.plot(x, y, color='blue')
plt.title("Smoothed Simulated Annealing Results")
plt.xlabel("Iteration")
plt.ylabel("Value")
plt.grid(True)
plt.tight_layout()
print("Best solution found: ", result)
print("table length:", len(simulated_annealing.getSchedule()))
plt.show()


jpype.shutdownJVM()
