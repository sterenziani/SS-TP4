import matplotlib.pyplot as plt
import math
import numpy as np
import pandas as pd

def geterror(aprox, exact):
	n = len(aprox)
	total = 0
	for i in range(n):
		total += math.pow((aprox[i] - exact[i]), 2)
	return total/n

def main():
	errorV = []
	errorB = []
	errorG = []
	errorS = []
	directory = "../output/"
	for i in range(1,7):
		print("Picking up " +directory+"output-" +str(i) +".txt")
		rV = pd.read_csv(directory+"outputV-" +str(i) +".txt", sep=';')
		rB = pd.read_csv(directory+"outputB-" +str(i) +".txt", sep=';')
		rG = pd.read_csv(directory+"outputG-" +str(i) +".txt", sep=';')
		rO = pd.read_csv(directory+"outputO-" +str(i) +".txt", sep=';')
		#rS = pd.read_csv(directory+"outputS-" +str(i) +".txt", sep=';')
		errorV.append(geterror(rV['x'], rO['x']))
		errorB.append(geterror(rB['x'], rO['x']))
		errorG.append(geterror(rG['x'], rO['x']))
		#errorS.append(geterror(rS['x'], rO['x']))

	# Reverse order, from smallest to largest deltaT
	errorV = errorV[::-1]
	errorB = errorB[::-1]
	errorG = errorG[::-1]
	#errorS = errorS[::-1]

	deltas = [10**-6, 10**-5, 10**-4, 10**-3, 10**-2, 10**-1]
	plt.plot(deltas, errorB, marker = "o", color='orange', label="Beeman")
	plt.plot(deltas, errorV, marker = "o", color='green', label="Verlet Original")
	plt.plot(deltas, errorG, marker = "o", color='blue', label="Gear Predictor-Corrector")
	#plt.plot(deltas, errorS, marker = "o", color='yellow', label="SOFIA")

	plt.xlabel(r'$\Delta t$ $(s)$')
	plt.ylabel(r'$ E(\Delta t)$  $(m^2)$')
	plt.xscale('log')
	plt.yscale('log')
	plt.legend()
	plt.show()

if __name__ == "__main__":
    main()