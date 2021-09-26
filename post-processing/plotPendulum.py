import matplotlib.pyplot as plt
import pandas as pd

def toarray(file):
	f = open(file, "r")
	lines = f.readlines()
	pos = []
	times = []
	for l in lines:
		array = l.split()
		pos.append(float(array[1]))
		times.append(float(array[0]))
	return times, pos

def main():
	directory = "../output/"
	dfV = pd.read_csv(directory+"outputV.txt", sep=';')
	dfB = pd.read_csv(directory+"outputB.txt", sep=';')
	dfG = pd.read_csv(directory+"outputG.txt", sep=';')
	dfO = pd.read_csv(directory+"outputO.txt", sep=';')
	plt.plot(dfV['t'], dfV['x'], color='red', label="Verlet Original")
	plt.plot(dfB['t'], dfB['x'], color='orange', label="Beeman", linestyle = "dashdot")
	plt.plot(dfG['t'], dfG['x'], color='green', label="Gear Predictor-Corrector", linestyle = "dashed")
	plt.plot(dfO['t'], dfO['x'], color='blue', label="Solución Analitica", linestyle = "dotted")
	plt.ylim(top=1, bottom=-1)
	plt.xlabel('Tiempo (s)')
	plt.ylabel('Posición del pendulo (m)')
	plt.legend()
	plt.show()

if __name__ == "__main__":
    main()
