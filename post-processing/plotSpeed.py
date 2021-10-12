import matplotlib.pyplot as plt
import pandas as pd

dfV = pd.read_csv('./../output/marsLanding.txt', sep='\t')
fig, ax = plt.subplots()
ax.plot(dfV['v'], dfV['t'])
ax.set_yscale('log')
plt.xlabel("Speed (km/s)")
plt.ylabel("t (s)")
plt.show()
