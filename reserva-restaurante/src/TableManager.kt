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
                        println("2. Consultar una mesa específica")
                        println("3. Buscar mesa disponible para N comensales")
                        println("4. Sentar cliente sin reserva")
                        println("5. Cambiar estado de una mesa")
                        println("6. Agregar nueva mesa")
                        println("7. Eliminar mesa")
                        println("0. Volver al menú principal")
                        println("---------------------------------------------")

                        opcion = UIController.leerEntero("Ingrese la opción deseada (0-7): ")

                        when (opcion) {
                                1 -> mostrarMesas()
                                2 -> consultarMesa()
                                3 -> buscarMesaPorCapacidad()
                                4 -> sentarClienteSinReserva()
                                5 -> cambiarEstadoMesa()
                                6 -> agregarMesa()
                                7 -> eliminarMesa()
                                0 -> println(" Regresando al menú principal...")
                                else -> println(" Opción no válida. Ingrese un número entre 0 y 7.")
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

        private fun buscarMesaPorCapacidad() {
                println("\n--- BÚSQUEDA DE MESAS POR COMENSALES ---")
                var numPersonas = 0

                while (numPersonas <= 0) {
                        numPersonas = UIController.leerEntero("Ingrese la cantidad de comensales (N): ")

                        if (numPersonas <= 0) {
                                println(" Error: La cantidad de comensales debe ser mayor a 0. Intente nuevamente.")
                        }
                }

                val disponibles = mesas.filter { it.estado.equals("Disponible", ignoreCase = true) && it.capacidad >= numPersonas }

                if (disponibles.isEmpty()) {
                        println(" No hay mesas disponibles para $numPersonas personas.")
                        return
                }

                val capacidadExacta = disponibles.filter { it.capacidad == numPersonas }

                if (capacidadExacta.isNotEmpty()) {
                        println("\nMesas disponibles con capacidad exacta para $numPersonas personas:")
                        capacidadExacta.forEach { mesa ->
                                println("• Mesa #${mesa.numero} | Capacidad: ${mesa.capacidad} personas | Estado: ${mesa.estado}")
                        }
                } else {
                        val menorCapacidadPosible = disponibles.minOf { it.capacidad }
                        val mesasAlternativas = disponibles.filter { it.capacidad == menorCapacidadPosible }

                        println("\nNo hay mesas de capacidad exacta ($numPersonas). Opciones más cercanas disponibles:")
                        mesasAlternativas.forEach { mesa ->
                                println("• Mesa #${mesa.numero} | Capacidad: ${mesa.capacidad} personas | Estado: ${mesa.estado}")
                        }
                }
        }

        private fun sentarClienteSinReserva() {
                println("\n--- SENTAR CLIENTE SIN RESERVA (WALK-IN) ---")
                val numPersonas = UIController.leerEntero("Ingrese la cantidad de personas: ")

                if (numPersonas <= 0) {
                        println(" Error: Debe haber al menos 1 persona.")
                        return
                }

                val disponibles = mesas.filter { it.estado.equals("Disponible", ignoreCase = true) && it.capacidad >= numPersonas }

                if (disponibles.isEmpty()) {
                        println(" No se encontró ninguna mesa disponible para $numPersonas personas.")
                        return
                }

                val capacidadExacta = disponibles.filter { it.capacidad == numPersonas }
                val mejoresMesas = if (capacidadExacta.isNotEmpty()) {
                        capacidadExacta
                } else {
                        val menorCapacidad = disponibles.minOf { it.capacidad }
                        disponibles.filter { it.capacidad == menorCapacidad }
                }

                println("\nMesas recomendadas para la cantidad solicitada:")
                mejoresMesas.forEach { println("• Mesa #${it.numero} (Capacidad: ${it.capacidad} personas)") }

                val numMesaElegida = UIController.leerEntero("\nIngrese el número de mesa a asignar: ")
                val mesaElegida = mejoresMesas.find { it.numero == numMesaElegida }

                if (mesaElegida == null) {
                        println(" Error: La mesa elegida no está en la lista de recomendadas.")
                } else {
                        mesaElegida.estado = "Ocupada"
                        println(" ¡Mesa $numMesaElegida asignada y cambiada a estado 'Ocupada' con éxito!")
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

        private fun agregarMesa() {
                println("\n--- AGREGAR NUEVA MESA ---")
                val numeroMesa = UIController.leerEntero("Ingrese el número de la nueva mesa: ")

                if (buscarMesa(numeroMesa) != null) {
                        println(" Error: Ya existe una mesa registrada con el número $numeroMesa.")
                        return
                }

                val capacidad = UIController.leerEntero("Ingrese la capacidad de personas para la mesa: ")
                if (capacidad <= 0) {
                        println(" Error: La capacidad debe ser mayor a 0.")
                        return
                }

                val nuevaMesa = Mesa(numero = numeroMesa, capacidad = capacidad, estado = "Disponible")
                mesas.add(nuevaMesa)
                println(" ¡Mesa $numeroMesa para $capacidad personas agregada exitosamente!")
        }

        private fun eliminarMesa() {
                println("\n--- ELIMINAR MESA ---")
                val numeroMesa = UIController.leerEntero("Ingrese el número de mesa a eliminar: ")
                val mesa = buscarMesa(numeroMesa)

                if (mesa == null) {
                        println(" Error: No existe una mesa con el número $numeroMesa.")
                        return
                }

                if (mesa.estado == "Ocupada") {
                        println(" Error: No se puede eliminar la mesa $numeroMesa porque actualmente está 'Ocupada'.")
                        return
                }

                mesas.remove(mesa)
                println(" ¡La mesa $numeroMesa ha sido eliminada del sistema correctamente!")
        }
}