fun main() {
    val tableManager = TableManager()
    val orderManager = OrderManager()
    val reservationSystem = ReservationSystem(tableManager)

    var opcion: Int

    println(" BIENVENIDO AL SISTEMA DE RESERVAS DE RESTAURANTE ")

    do {
        UIController.mostrarMenu()
        opcion = UIController.leerEntero("Ingrese la opción deseada (0-6): ")

        when (opcion) {
            1 -> tableManager.gestionarMesas(reservationSystem, orderManager)
            2 -> UIController.gestionarClientes()
            3 -> reservationSystem.gestionarReservas()
            4 -> MenuCatalog.mostrarCatalogo()
            5 -> orderManager.gestionarPedidos(tableManager)
            6 -> BillingService.procesarFacturaMesa(orderManager, tableManager)
            0 -> println(" Saliendo del sistema... ¡Gracias por usar la aplicación!")
            else -> println(" Opción no válida. Por favor ingrese un número entre 0 y 6.")
        }

    } while (opcion != 0)
}