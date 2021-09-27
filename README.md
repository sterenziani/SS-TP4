# SS-TP4

## Ejercicio 1
Ejecutar pendulumAuto.sh en post-processing para simular y graficar

### Clases
**Particle** tiene la posición y velocidad en cada coordenada

**Simulator** es una interfaz con el método updateParticle()

**SimulatorBeeman** implementa Simulator y recibe un boolean para saber si corre como Beeman normal o PRedictor-Corrector

**SimulatorVerlet** implementa Simulator y usa Euler para obtener x(-dt) y v(-dt)

**SimulatorGear** implementa Simulator y usa el coeficiente 3/16 porque *a* es función de *x* y *v*

**SimulatorOscillation** implementa Simulator para representar la solución analítica al oscilador

**App** crea a partir del input ("1, dt, MODE" línea por línea) el Simulator y ejecuta hasta el tf hardcodeado

## Ejercicio 2

### Clases
**SpaceSimulator** recibe las partículas, puede luego agregar una nave, y usa un SpaceGearAlgorithm para evolucionar el estado del sistema. Se le puede preguntar por la velocidad de la nave y su distancia a Marte.

**SpaceGearAlgorithm** es una versión extendida de SimulatorGear para manejar varias partículas al mismo tiempo. Usa el coeficiente 3/20 pues *a* ya no depende de *v*. Los *r* de orden mayor a 2 son inicializados en 0 ya que no funciona igual que hay muchas fuerzas en juego.

**SpaceReport** guarda la info de una misión. Tiene el instante de despegue, la velocidad final de la nave, la distancia mínima que estuvo a Marte durante el viaje, y el instante en donde tuvo esa distancia.

**App** lanza un cohete todos los días y guarda el reporte en una lista que, al haber realizado todas las misiones planificadas, vuelca al archivo de output.
