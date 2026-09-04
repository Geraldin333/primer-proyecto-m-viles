object UIController {

    // Muestra el menú principal de la aplicación
    fun mostrarMenu() {
        println("---------------------------------------------")
        println(" MENU- SISTEMA DE RESERVAS DE RESTAURANTE")
        println("---------------------------------------------")
        println("1. Gestionar Mesas ")
        println("2. Registrar / Consultar Clientes ")
        println("3. Agendar / Consultar Reservas ")
        println("4. Catálogo de Menú y Platillos ")
        println("5. Gestionar Pedidos de Mesa ")
        println("6. Facturación y Cobro ")
        println("0. Salir")
        println("---------------------------------------------")
    }


    fun leerEntero(mensaje: String = "Seleccione una opción: "): Int {
        while (true) {
            print(mensaje)
            val entrada = readlnOrNull()?.trim()
            val numero = entrada?.toIntOrNull()

            if (numero != null) {
                return numero
            }
            println(" Error: Debe ingresar un número entero válido.")
        }
    }


    fun leerTexto(mensaje: String): String {
        while (true) {
            print(mensaje)
            val entrada = readlnOrNull()?.trim()

            if (!entrada.isNullOrEmpty()) {
                return entrada
            }
            println(" Error: El campo no puede estar vacío.")
        }
    }


    fun leerDecimal(mensaje: String): Double {
        while (true) {
            print(mensaje)
            val entrada = readlnOrNull()?.trim()
            val numero = entrada?.toDoubleOrNull()

            if (numero != null) {
                return numero
            }
            println(" Error: Debe ingresar un número decimal válido (ej. 15.5).")
        }
    }
}