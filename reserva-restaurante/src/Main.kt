fun main() {
    var opcion: Int
    val reservationSystem = ReservationSystem()

    println(" BIENVENIDO AL SISTEMA DE RESERVAS DE RESTAURANTE ")

    do {
        UIController.mostrarMenu()
        opcion = UIController.leerEntero("Ingrese la opción deseada (0-6): ")

        when (opcion) {
            1 -> println(" 1. --- Módulo de Mesas")
            2 -> println(" 2. --- Módulo de Clientes")
            3 -> reservationSystem.gestionarReservas() // <-- Conexión del módulo de reservas
            4 -> println(" 4. --- Catálogo de Menú")
            5 -> println(" 5. --- Módulo de Pedidos")
            6 -> println(" 6. --- Módulo de Facturación")
            0 -> println(" Saliendo del sistema... ¡Gracias por usar la aplicación!")
            else -> println(" Opción no válida. Por favor ingrese un número entre 0 y 6.")
        }

    } while (opcion != 0)
}