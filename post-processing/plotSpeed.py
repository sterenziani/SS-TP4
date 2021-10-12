import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter
import pandas as pd
import os
import numpy as np

dfV = pd.read_csv('./../output/marsLanding.txt', sep='\t')

minV = 100000
maxV = 0
for index, row in dfV.iterrows():
    if row['d'] <= 1500 and minV > row['v']:
        minV = row['v']
        minTime = row['t']/(24*3600)
    if row['d'] <= 1500 and maxV < row['v']:
        maxV = row['v']
        maxTime = row['t']/(24*3600)
print(minTime, ' ', maxTime)

fig1, ax1 = plt.subplots()
t = dfV['t']/(24*3600)
ax1.plot(dfV['v'], t, color='r')
ax1.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
ax1.xaxis.set_major_formatter(FormatStrFormatter('%.3f'))
plt.xticks(np.arange(min(dfV['v']), max(dfV['v'] + 0.001), 0.001))
print(minV, ' ', maxV)
print(np.arange(minTime, maxTime, 0.01))
plt.yticks(np.arange(minTime, maxTime, 0.01))
plt.xlabel("$\mathregular{V_0}$ (km/s)")
plt.ylabel("Tiempo de viaje (días)")

if not os.path.exists('plots'):
    os.mkdir('plots')
plt.savefig('./plots/time.png', bbox_inches='tight')

fig2, ax2 = plt.subplots()
ax2.plot(dfV['v'], dfV['d'], color='r')
ax2.xaxis.set_major_formatter(FormatStrFormatter('%.3f'))
plt.xlabel("$\mathregular{V_0}$ (km/s)")
plt.ylabel("Distancia mínima a Marte (km)")
plt.xticks(np.arange(min(dfV['v']), max(dfV['v'] + 0.001), 0.001))
plt.savefig('./plots/distance.png', bbox_inches='tight')
plt.show()
