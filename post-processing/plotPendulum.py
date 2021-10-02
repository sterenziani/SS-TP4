import matplotlib.pyplot as plt
import pandas as pd

def main():
	directory = "../output/"
	dfV = pd.read_csv(directory+"outputV-4.txt", sep=';')
	dfB = pd.read_csv(directory+"outputB-4.txt", sep=';')
	dfBPC = pd.read_csv(directory+"outputBPC-4.txt", sep=';')
	dfG = pd.read_csv(directory+"outputG-4.txt", sep=';')
	dfO = pd.read_csv(directory+"outputO-4.txt", sep=';')
	plt.plot(dfV['t'], dfV['x'], color='green', label="Verlet Original")
	plt.plot(dfB['t'], dfB['x'], color='orange', label="Beeman", linestyle = "dashdot")
	plt.plot(dfBPC['t'], dfBPC['x'], color='yellow', label="Beeman Predictor-Corrector", linestyle = "dashdot")
	plt.plot(dfG['t'], dfG['x'], color='blue', label="Gear Predictor-Corrector", linestyle = "dashed")
	plt.plot(dfO['t'], dfO['x'], color='grey', label="Solución Analitica", linestyle = "dotted")
	plt.ylim(top=1.2, bottom=-1.2)
	plt.xlabel('Tiempo (s)')
	plt.ylabel('Posición del péndulo (m)')
	plt.legend()
	plt.show()

if __name__ == "__main__":
    main()
