import matplotlib.pyplot as plt
import math
import numpy as np
import pandas as pd

def main():
	directory = "../output/"
	df = pd.read_csv(directory+"spaceship-error.txt", sep=';')

	plt.plot(df['dt'], df['error']*100, marker = "o")

	plt.xlabel(r'$\Delta t$ $(s)$')
	plt.ylabel(r'$ E(\Delta t)$  $(\%)$')
	plt.xscale('log')
	#plt.yscale('log')
	plt.show()

if __name__ == "__main__":
    main()
