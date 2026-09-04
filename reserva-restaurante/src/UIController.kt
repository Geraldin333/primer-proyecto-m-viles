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

    fun gestionarClientes() {
        var salir = false
        while (!salir) {
            println("---------------------------------------------")
            println(" GESTIÓN DE CLIENTES ")
            println("---------------------------------------------")
            println("1. Registrar nuevo cliente")
            println("2. Consultar todos los clientes")
            println("3. Buscar cliente por nombre")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            when (leerEntero()) {
                1 -> {
                    val nombre = leerTexto("Ingrese el nombre del cliente: ")
                    val telefono = leerTexto("Ingrese el teléfono: ")
                    val correo = leerTexto("Ingrese el correo: ")
                    CustomerRegistry.registerCustomer(nombre, telefono, correo)
                    println("Cliente registrado exitosamente.")
                }
                2 -> {
                    val clientes = CustomerRegistry.getCustomers()
                    if (clientes.isEmpty()) {
                        println("No hay clientes registrados.")
                    } else {
                        clientes.forEach {
                            println("ID: ${it.id} | Nombre: ${it.nombre} | Tel: ${it.telefono} | Correo: ${it.correo}")
                        }
                    }
                }
                3 -> {
                    val nombre = leerTexto("Ingrese el nombre a buscar: ")
                    val cliente = CustomerRegistry.findCustomerByName(nombre)
                    if (cliente != null) {
                        println("ID: ${cliente.id} | Nombre: ${cliente.nombre} | Tel: ${cliente.telefono} | Correo: ${cliente.correo}")
                    } else {
                        println("Cliente no encontrado.")
                    }
                }
                0 -> salir = true
                else -> println("Opción no válida.")
            }
        }
    }
}