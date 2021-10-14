import matplotlib.pyplot as plt
import matplotlib.ticker as mticker
import math
import numpy as np
import pandas as pd

def main():
    directory = "../output/"
    df = pd.read_csv(directory+"spaceship-error.txt", sep=';')
    fig, ax = plt.subplots()
    ax.plot(df['dt'], df['error']*100, marker = "o")
    plt.xlabel(r'$\Delta t$ $(s)$')
    plt.ylabel(r'$ E(\Delta t)$  $(\%)$')
    plt.xscale('log')
    #plt.yscale('log')
    f = mticker.ScalarFormatter(useOffset=False, useMathText=True)
    g = lambda x,pos : "${}$".format(f._formatSciNotation('%1.10e' % x))
    plt.gca().yaxis.set_major_formatter(mticker.FuncFormatter(g))
    plt.show()

if __name__ == "__main__":
    main()
