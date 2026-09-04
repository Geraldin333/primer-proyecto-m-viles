fun main() {
    var opcion: Int

    println(" BIENVENIDO AL SISTEMA DE RESERVAS DE RESTAURANTE ")

    do {
        UIController.mostrarMenu()
        opcion = UIController.leerEntero("Ingrese la opción deseada (0-6): ")

        when (opcion) {
            1 -> println(" 1. --- Módulo de Mesas")
            2 -> {
                println(" 2. --- Módulo de Clientes") UIController.gestionarClientes()
            }

            3 -> println(" 3. --- Módulo de Reservas")
            4 -> {
                println(" 4. --- Catálogo de Menú")
            MenuCatalog.mostrarCatalogo()
        }
            5 -> println(" 5. --- Módulo de Pedidos")
            6 -> println(" 6. --- Módulo de Facturación")
            0 -> println(" Saliendo del sistema... ¡Gracias por usar la aplicación!")
            else -> println(" Opción no válida. Por favor ingrese un número entre 0 y 6.")
        }

    } while (opcion != 0)
}
