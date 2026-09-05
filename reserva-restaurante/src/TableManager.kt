data class Mesa(
        val numero: Int,
        val capacidad: Int,
        var estado: String
) {
        fun mostrarDetalle() {
                println("---------------------------------------------")
                println("Mesa: $numero")
                println("Capacidad: $capacidad personas")
                println("Estado: $estado")
                println("---------------------------------------------")
        }
}

class TableManager {

        private val mesas = mutableListOf(
                Mesa(1, 2, "Disponible"),
                Mesa(2, 2, "Disponible"),
                Mesa(3, 4, "Disponible"),
                Mesa(4, 6, "Disponible"),
                Mesa(5, 4, "Disponible"),
                Mesa(6, 6, "Disponible"),
                Mesa(7, 2, "Disponible"),
                Mesa(8, 4, "Disponible"),
                Mesa(9, 1, "Disponible"),
                Mesa(10, 6, "Disponible")
        )

        fun buscarMesa(numero: Int): Mesa? {
                return mesas.find { it.numero == numero }
        }

        fun gestionarMesas() {
                var opcion: Int

                do {
                        println("\n---------------------------------------------")
                        println(" MÓDULO DE GESTIÓN DE MESAS")
                        println("---------------------------------------------")
                        println("1. Ver todas las mesas")
                        println("2. Consultar una mesa")
                        println("3. Cambiar estado de una mesa")
                        println("0. Volver al menú principal")
                        println("---------------------------------------------")

                        opcion = UIController.leerEntero("Ingrese la opción deseada (0-3): ")

                        when (opcion) {
                                1 -> mostrarMesas()
                                2 -> consultarMesa()
                                3 -> cambiarEstadoMesa()
                                0 -> println(" Regresando al menú principal...")
                                else -> println(" Opción no válida. Ingrese un número entre 0 y 3.")
                        }

                } while (opcion != 0)
        }

        private fun mostrarMesas() {
                println("\n--- ESTADO DE LAS MESAS ---")
                mesas.forEach { it.mostrarDetalle() }
        }

        private fun consultarMesa() {
                println("\n--- CONSULTAR MESA ---")
                val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")
                val mesa = buscarMesa(numeroMesa)

                if (mesa == null) {
                        println(" Error: No existe una mesa con el número $numeroMesa.")
                } else {
                        mesa.mostrarDetalle()
                }
        }

        private fun cambiarEstadoMesa() {
                println("\n--- CAMBIAR ESTADO DE MESA ---")
                val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")
                val mesa = buscarMesa(numeroMesa)

                if (mesa == null) {
                        println(" Error: No existe una mesa con el número $numeroMesa.")
                        return
                }

                println("\nEstado actual: ${mesa.estado}")
                println("1. Disponible")
                println("2. Ocupada")
                println("3. Reservada")

                val opcionEstado = UIController.leerEntero("Seleccione el nuevo estado (1-3): ")

                when (opcionEstado) {
                        1 -> {
                                mesa.estado = "Disponible"
                                println(" La mesa $numeroMesa ahora está disponible.")
                        }
                        2 -> {
                                mesa.estado = "Ocupada"
                                println(" La mesa $numeroMesa ahora está ocupada.")
                        }
                        3 -> {
                                mesa.estado = "Reservada"
                                println(" La mesa $numeroMesa ahora está reservada.")
                        }
                        else -> println(" Opción de estado no válida.")
                }
        }
}