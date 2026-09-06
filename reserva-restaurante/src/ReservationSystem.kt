import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class Reserva(
    val idCliente: Int,
    val numeroMesa: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val numPersonas: Int
) {
    fun mostrarDetalle() {
        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm")
        println("---------------------------------------------")
        println("ID Cliente: $idCliente")
        println("Mesa #: $numeroMesa")
        println("Fecha: ${fecha.format(formatoFecha)}")
        println("Hora: ${hora.format(formatoHora)}")
        println("Personas: $numPersonas")
        println("---------------------------------------------")
    }
}

class ReservationSystem(
    private val tableManager: TableManager
) {

    private val reservas = mutableListOf<Reserva>()
    private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val formatoHora = DateTimeFormatter.ofPattern("HH:mm")

    fun gestionarReservas() {
        var opcion: Int

        do {
            println("\n---------------------------------------------")
            println(" MÓDULO DE GESTIÓN DE RESERVAS")
            println("---------------------------------------------")
            println("1. Crear nueva reserva")
            println("2. Consultar TODAS las reservas")
            println("3. Consultar reservas por fecha")
            println("4. Cancelar una reserva")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            opcion = UIController.leerEntero("Ingrese la opción deseada (0-4): ")

            when (opcion) {
                1 -> crearReserva()
                2 -> consultarTodasLasReservas()
                3 -> consultarReservasPorFecha()
                4 -> cancelarReserva()
                0 -> println(" Regresando al menú principal...")
                else -> println(" Opción no válida. Ingrese un número entre 0 y 4.")
            }

        } while (opcion != 0)
    }

    private fun crearReserva() {
        println("\n--- REGISTRAR NUEVA RESERVA ---")

        // 1. Pedir ID del cliente con opción de salida (0)
        var clienteExistente: CustomerData? = null

        while (clienteExistente == null) {
            val idCliente = UIController.leerEntero("Ingrese el ID del cliente registrado (o 0 para cancelar y volver): ")

            if (idCliente == 0) {
                println(" Operación cancelada. Regresando al menú de reservas...")
                return
            }

            clienteExistente = CustomerRegistry.getCustomers().find { it.id == idCliente }

            if (clienteExistente == null) {
                println(" Error: No existe ningún cliente registrado con el ID $idCliente. Intente nuevamente.")
            }
        }

        // 2. Pedir Fecha (Hoy o Futura)
        var fecha: LocalDate? = null
        val hoy = LocalDate.now()

        while (fecha == null) {
            print("Ingrese la fecha de la reserva (dd/MM/yyyy) [o '0' para cancelar]: ")
            val textoFecha = readlnOrNull()?.trim() ?: ""

            if (textoFecha == "0") {
                println(" Operación cancelada. Regresando al menú de reservas...")
                return
            }

            try {
                val fechaIngresada = LocalDate.parse(textoFecha, formatoFecha)
                if (fechaIngresada.isBefore(hoy)) {
                    println(" Error: No se pueden realizar reservas para fechas pasadas.")
                } else {
                    fecha = fechaIngresada
                }
            } catch (e: DateTimeParseException) {
                println(" Error: Formato de fecha inválido. Use dd/MM/yyyy (Ejemplo: 25/12/2026).")
            }
        }

        // 3. Pedir Hora
        var hora: LocalTime? = null

        while (hora == null) {
            print("Ingrese la hora de la reserva (HH:mm - Formato 24h) [o '0' para cancelar]: ")
            val textoHora = readlnOrNull()?.trim() ?: ""

            if (textoHora == "0") {
                println(" Operación cancelada. Regresando al menú de reservas...")
                return
            }

            try {
                hora = LocalTime.parse(textoHora, formatoHora)
            } catch (e: DateTimeParseException) {
                println(" Error: Formato de hora inválido. Use HH:mm (Ejemplo: 14:30).")
            }
        }

        // 4. Pedir comensales y mostrar mesas disponibles
        var numPersonas = 0
        while (numPersonas <= 0) {
            numPersonas = UIController.leerEntero("Ingrese la cantidad de personas (o 0 para cancelar): ")

            if (numPersonas == 0) {
                println(" Operación cancelada. Regresando al menú de reservas...")
                return
            }
            if (numPersonas < 0) {
                println(" Error: La cantidad de personas debe ser mayor a 0.")
            }
        }

        var mesaValida = false

        while (!mesaValida) {
            val mesasCandidatas = (1..10).mapNotNull { tableManager.buscarMesa(it) }
                .filter { mesa ->
                    mesa.capacidad >= numPersonas &&
                            reservas.none { it.numeroMesa == mesa.numero && it.fecha == fecha && it.hora == hora }
                }

            if (mesasCandidatas.isEmpty()) {
                println(" No hay mesas disponibles con capacidad suficiente para esa fecha y hora.")
                return
            }

            println("\nMesas disponibles para $numPersonas o más personas:")
            mesasCandidatas.forEach { mesa ->
                println("• Mesa #${mesa.numero} | Capacidad: ${mesa.capacidad} personas")
            }

            val numeroMesa = UIController.leerEntero("\nIngrese el número de la mesa a elegir (o 0 para cancelar): ")

            if (numeroMesa == 0) {
                println(" Operación cancelada. Regresando al menú de reservas...")
                return
            }

            val mesaElegida = mesasCandidatas.find { it.numero == numeroMesa }

            if (mesaElegida == null) {
                println(" Error: La mesa elegida no está en la lista de disponibles o no tiene capacidad suficiente.")
                continue
            }

            mesaValida = true

            // Registrar reserva
            val nuevaReserva = Reserva(
                idCliente = clienteExistente.id,
                numeroMesa = numeroMesa,
                fecha = fecha,
                hora = hora,
                numPersonas = numPersonas
            )

            reservas.add(nuevaReserva)
            println("\n ¡Reserva confirmada exitosamente para ${clienteExistente.nombre} en la Mesa #$numeroMesa!")
        }
    }

    private fun consultarTodasLasReservas() {
        println("\n--- LISTADO GENERAL DE RESERVAS ---")
        if (reservas.isEmpty()) {
            println(" No hay reservas registradas en el sistema.")
        } else {
            reservas.forEach { it.mostrarDetalle() }
        }
    }

    private fun consultarReservasPorFecha() {
        println("\n--- CONSULTAR RESERVAS POR FECHA ---")
        var fecha: LocalDate? = null

        while (fecha == null) {
            print("Ingrese la fecha a consultar (dd/MM/yyyy) [o '0' para salir]: ")
            val textoFecha = readlnOrNull()?.trim() ?: ""

            if (textoFecha == "0") return

            try {
                fecha = LocalDate.parse(textoFecha, formatoFecha)
            } catch (e: DateTimeParseException) {
                println(" Error: Formato de fecha inválido. Debe ser dd/MM/yyyy.")
            }
        }

        val encontradas = reservas.filter { it.fecha == fecha }

        if (encontradas.isEmpty()) {
            println(" No existen reservas registradas para la fecha ${fecha.format(formatoFecha)}.")
        } else {
            println("\nReservas para el día ${fecha.format(formatoFecha)}:")
            encontradas.forEach { it.mostrarDetalle() }
        }
    }

    private fun cancelarReserva() {
        println("\n--- CANCELAR RESERVA ---")
        val idCliente = UIController.leerEntero("Ingrese el ID del cliente (o 0 para salir): ")

        if (idCliente == 0) return

        val reservasCliente = reservas.filter { it.idCliente == idCliente }

        if (reservasCliente.isEmpty()) {
            println(" Error: No se encontraron reservas activas para el ID $idCliente.")
            return
        }

        println("\nReservas encontradas para el cliente ID $idCliente:")
        reservasCliente.forEachIndexed { index, reserva ->
            println("${index + 1}. Mesa #${reserva.numeroMesa} - Fecha: ${reserva.fecha.format(formatoFecha)} Hora: ${reserva.hora.format(formatoHora)}")
        }

        val seleccion = UIController.leerEntero("Seleccione el número de la reserva a cancelar (1-${reservasCliente.size}) [o 0 para salir]: ")

        if (seleccion == 0) return

        if (seleccion in 1..reservasCliente.size) {
            val reservaAEliminar = reservasCliente[seleccion - 1]
            reservas.remove(reservaAEliminar)
            println(" ¡La reserva ha sido cancelada correctamente!")
        } else {
            println(" Opción fuera de rango.")
        }
    }
}