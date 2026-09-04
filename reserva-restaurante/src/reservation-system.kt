import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ReservationSystem {
    private val reservas = mutableListOf<Reserva>()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun gestionarReservas() {
        var opcion: Int
        do {
            println("\n---------------------------------------------")
            println(" MÓDULO DE GESTIÓN DE RESERVAS")
            println("---------------------------------------------")
            println("1. Agendar nueva reserva")
            println("2. Consultar reservas existentes")
            println("3. Cancelar una reserva")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            opcion = UIController.leerEntero("Ingrese la opción deseada (0-3): ")

            when (opcion) {
                1 -> agendarReserva()
                2 -> consultarReservas()
                3 -> cancelarReserva()
                0 -> println(" Regresando al menú principal...")
                else -> println(" Opción no válida. Ingrese un número entre 0 y 3.")
            }
        } while (opcion != 0)
    }

    private fun agendarReserva() {
        println("\n--- AGENDAR NUEVA RESERVA ---")
        val idCliente = UIController.leerTexto("Ingrese el documento o nombre del cliente: ")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa a reservar: ")

        val fechaStr = leerFechaValida("Ingrese la fecha (AAAA-MM-DD): ")
        val horaStr = leerHoraValida("Ingrese la hora (HH:mm): ")

        val nuevaReserva = Reserva(
            id = reservas.size + 1,
            idCliente = idCliente,
            numeroMesa = numeroMesa,
            fecha = fechaStr,
            hora = horaStr,
            estado = "Activa"
        )

        reservas.add(nuevaReserva)
        println(" ¡Reserva #${nuevaReserva.id} agendada con éxito para el cliente $idCliente en la mesa $numeroMesa el ${nuevaReserva.fecha} a las ${nuevaReserva.hora}!")
    }

    private fun consultarReservas() {
        println("\n--- CONSULTAR RESERVAS ---")
        if (reservas.isEmpty()) {
            println(" No hay reservas registradas en el sistema.")
            return
        }

        println("1. Ver todas las reservas")
        println("2. Filtrar por fecha")
        val tipoConsulta = UIController.leerEntero("Seleccione una opción de consulta (1-2): ")

        when (tipoConsulta) {
            1 -> {
                println("\nListado completo de reservas:")
                reservas.forEach { it.mostrarDetalle() }
            }
            2 -> {
                val fechaBuscada = leerFechaValida("Ingrese la fecha a consultar (AAAA-MM-DD): ")
                val encontradas = reservas.filter { it.fecha == fechaBuscada }
                if (encontradas.isEmpty()) {
                    println(" No se encontraron reservas para la fecha $fechaBuscada.")
                } else {
                    println("\nReservas para el $fechaBuscada:")
                    encontradas.forEach { it.mostrarDetalle() }
                }
            }
            else -> println(" Opción de consulta no válida.")
        }
    }

    private fun cancelarReserva() {
        println("\n--- CANCELAR RESERVA ---")
        if (reservas.isEmpty()) {
            println(" No hay reservas registradas para cancelar.")
            return
        }

        val idBuscado = UIController.leerEntero("Ingrese el ID de la reserva que desea cancelar: ")
        val reserva = reservas.find { it.id == idBuscado }

        if (reserva == null) {
            println(" Error: No se encontró ninguna reserva con el ID $idBuscado.")
        } else if (reserva.estado == "Cancelada") {
            println(" La reserva ya se encuentra previamente cancelada.")
        } else {
            reserva.estado = "Cancelada"
            println(" La reserva #${reserva.id} ha sido cancelada exitosamente.")
        }
    }

    private fun leerFechaValida(mensaje: String): String {
        while (true) {
            val entrada = UIController.leerTexto(mensaje)
            try {
                val fecha = LocalDate.parse(entrada, dateFormatter)
                return fecha.format(dateFormatter)
            } catch (e: DateTimeParseException) {
                println(" Error: Formato de fecha inválido. Utilice el formato AAAA-MM-DD (ej. 2026-12-31).")
            }
        }
    }

    private fun leerHoraValida(mensaje: String): String {
        while (true) {
            val entrada = UIController.leerTexto(mensaje)
            try {
                val hora = LocalTime.parse(entrada, timeFormatter)
                return hora.format(timeFormatter)
            } catch (e: DateTimeParseException) {
                println(" Error: Formato de hora inválido. Utilice el formato HH:mm en formato de 24 horas (ej. 19:30).")
            }
        }
    }
}

data class Reserva(
    val id: Int,
    val idCliente: String,
    val numeroMesa: Int,
    val fecha: String,
    val hora: String,
    var estado: String
) {
    fun mostrarDetalle() {
        println("---------------------------------------------")
        println("Reserva ID: $id")
        println("Cliente: $idCliente")
        println("Mesa asignada: $numeroMesa")
        println("Fecha: $fecha")
        println("Hora: $hora")
        println("Estado: $estado")
        println("---------------------------------------------")
    }
}