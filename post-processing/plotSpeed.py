import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter
import pandas as pd
import os
import numpy as np
import math


def round_decimals_up(number: float, decimals: int = 2):
    """
    Returns a value rounded up to a specific number of decimal places.
    """
    if not isinstance(decimals, int):
        raise TypeError("decimal places must be an integer")
    elif decimals < 0:
        raise ValueError("decimal places has to be 0 or more")
    elif decimals == 0:
        return math.ceil(number)

    factor = 10 ** decimals
    return math.ceil(number * factor) / factor


dfV = pd.read_csv('./../output/marsLanding.txt', sep='\t')

minV = 100000
maxV = 0
speed = []
time = []
for index, row in dfV.iterrows():
    if row['d'] <= 1500 and minV > row['v']:
        minV = row['v']
    if row['d'] <= 1500 and maxV < row['v']:
        maxV = row['v']
    if row['d'] == 0.0:
        time.append(row['t']/(3600))
        speed.append(row['v'])
    if row['v'] == 8.0:
        meanTime = row['t']/(3600)
print(min(time), ' ', meanTime, ' ', max(time))

fig1, ax1 = plt.subplots()
t = dfV['t']/(24*3600)
ax1.scatter(speed, time)
ax1.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
ax1.xaxis.set_major_formatter(FormatStrFormatter('%.4f'))
plt.xticks([np.min(speed), 8.0, np.max(speed)])
print(minV, ' ', maxV)
print(np.max(time))
plt.yticks(np.arange(np.min(time), np.max(time) + 0.001, step=(np.max(
    time)-np.min(time))/4))
plt.setp(ax1.get_xticklabels(), rotation=30, horizontalalignment='right')
plt.xlabel("$\mathregular{V_0}$ (km/s)", fontsize=11)
plt.ylabel("Tiempo de viaje (horas)", fontsize=11)

if not os.path.exists('plots'):
    os.mkdir('plots')
plt.savefig('./plots/time.png', bbox_inches='tight')

fig2, ax2 = plt.subplots()
ax2.plot(dfV['v'], dfV['d'])
ax2.xaxis.set_major_formatter(FormatStrFormatter('%.3f'))
plt.xlabel("$\mathregular{V_0}$ (km/s)")
plt.ylabel("Distancia mínima a Marte (km)")
plt.xticks(np.arange(min(dfV['v']), max(dfV['v']), .001))
plt.savefig('./plots/distance.png', bbox_inches='tight')
plt.show()
