import matplotlib.pyplot as plt
import pandas as pd

dfV = pd.read_csv('./../output/marsLanding.txt', sep='\t')
fig, ax = plt.subplots()
ax.plot(dfV['v'], dfV['t'])
ax.set_yscale('log')
ax.xlabel("Speed (km/s)")
ax.ylabel("t (s)")
plt.show()
