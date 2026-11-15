# 🏁 Buscaminas en Java

<!-- local-development:start -->
## Desarrollo local

Consulta [RUNNING.md](RUNNING.md) para ubicar cada componente, abrirlo en su IDE, compilarlo y conocer las comprobaciones realizadas y los requisitos pendientes.
<!-- local-development:end -->

Este proyecto es una implementación del clásico juego **Buscaminas** desarrollada en **Java** para el curso **Programación I** de la Universidad Técnica Nacional.  
Se ha diseñado con un enfoque modular, separando la **lógica del juego** de la **interfaz**, lo que permite su adaptación tanto a versiones gráficas como de consola.

---
 Características
- Lógica completa del juego Buscaminas.
- Colocación aleatoria de minas, evitando la casilla inicial seleccionada.
- Apertura recursiva de casillas vacías.
- Cálculo automático de pistas numéricas.
- Eventos para manejar:
  - Apertura de casillas.
  - Fin del juego por derrota.
  - Victoria.
- Código estructurado y reutilizable.

<!-- verified-builds-20261115:start -->
## Compilación comprobada

- Compilación satisfactoria registrada para `build.xml`.

Comandos:

```powershell
.\build-local.ps1
```

La compilación no acredita todos los flujos gráficos, conexiones a bases de datos ni servicios externos. No se ejecutaron operaciones de administración contra esos servicios.
<!-- verified-builds-20261115:end -->
