import matplotlib.pyplot as plt
import pandas as pd

def main():
    directory = "../output/"
    for v in range(6,10):
        df = pd.read_csv(directory+"spaceships-V" +str(v) +".0.txt", sep=';')
        plt.plot(df['departureTime']/(3600*24), df['minDistance'], label="Vo = "+str(v) +" km/s")
        minIndex = df['minDistance'].idxmin()
        bestDay = df['departureTime'][minIndex]
        minTimeStart = df['departureTime'][minIndex]-(3600*24)
    plt.xlabel('Día de Despegue')
    plt.ylabel('Distancia mínima a Marte (km)')
    plt.yscale('log')
    plt.ylim( (10**0, 10**10) )
    plt.legend()
    plt.show()

    for v in range(6,10):
        df = pd.read_csv(directory+"spaceships-precise-V" +str(v) +".0.txt", sep=';')
        plt.plot(df['departureTime']/(3600*24), df['minDistance'], '.', label="Vo = "+str(v) +" km/s")
    plt.xlabel('Día de Despegue')
    plt.ylabel('Distancia mínima a Marte (km)')
    plt.yscale('symlog')
    plt.ylim( top=10**10 )
    plt.legend(loc='upper left')
    plt.show()

if __name__ == "__main__":
    main()
