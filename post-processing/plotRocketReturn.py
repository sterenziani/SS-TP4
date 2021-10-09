import matplotlib.pyplot as plt
import pandas as pd

def main():
    directory = "../output/"
    df = pd.read_csv(directory+"spaceships-return.txt", sep=';')
    plt.plot(df['departureTime']/(3600*24), df['minDistance'])
    plt.xlabel('Día de Despegue')
    plt.ylabel('Distancia mínima a la Tierra (km)')
    plt.yscale('log')
    plt.show()
    minIndex = df['minDistance'].idxmin()
    bestDay = df['departureTime'][minIndex]
    minTimeStart = df['departureTime'][minIndex]-(3600*24)

    # Desde el día anterior al mínimo, a qué hora nos conviene lanzarlo?
    df = pd.read_csv(directory+"spaceships-return-precise.txt", sep=';')
    plt.plot((df['departureTime'] - minTimeStart)/3600, df['minDistance'], '.')
    plt.xlabel('Hora de despegue (hrs)')
    plt.ylabel('Distancia mínima a la Tierra (km)')
    plt.axhline(y=0, color='grey', linestyle='-')
    plt.yscale('symlog')
    plt.show()
    minIndex = df['minDistance'].idxmin()
    bestLaunch = df['departureTime'][minIndex]

    # Usando el vuelo que más se acerque de esos, graficar velocidad
    df = pd.read_csv(directory+"spaceship-return-velocity.txt", sep=';')
    plt.plot(df['t']-min(df['t']), df['v'])
    plt.xlabel('Tiempo desde despegue (s)')
    plt.ylabel('Velocidad de la nave (km/s)')
    plt.show()

if __name__ == "__main__":
    main()
