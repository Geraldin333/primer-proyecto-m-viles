object UIController {

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
            println("4. Modificar datos de cliente")
            println("5. Eliminar cliente")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            when (leerEntero()) {
                1 -> registrarCliente()
                2 -> consultarTodosClientes()
                3 -> buscarClientesPorNombre()
                4 -> modificarCliente()
                5 -> eliminarCliente()
                0 -> salir = true
                else -> println("Opción no válida.")
            }
        }
    }

    private fun registrarCliente() {
        println("\n--- REGISTRAR NUEVO CLIENTE ---")
        val nombre = leerTexto("Ingrese el nombre del cliente: ")

        var telefono: String
        while (true) {
            telefono = leerTexto("Ingrese el teléfono: ")
            if (CustomerRegistry.existsByPhone(telefono)) {
                println(" Error: Ya existe un cliente registrado con ese número de teléfono.")
            } else {
                break
            }
        }

        var correo: String
        while (true) {
            correo = leerTexto("Ingrese el correo: ")
            if (CustomerRegistry.existsByEmail(correo)) {
                println(" Error: Ya existe un cliente registrado con ese correo electrónico.")
            } else {
                break
            }
        }

        CustomerRegistry.registerCustomer(nombre, telefono, correo)
        println(" ¡Cliente registrado exitosamente!")
    }

    private fun consultarTodosClientes() {
        val clientes = CustomerRegistry.getCustomers()
        if (clientes.isEmpty()) {
            println("No hay clientes registrados.")
        } else {
            println("\n--- LISTA DE CLIENTES ---")
            clientes.forEach {
                println("ID: ${it.id} | Nombre: ${it.nombre} | Tel: ${it.telefono} | Correo: ${it.correo}")
            }
        }
    }

    private fun buscarClientesPorNombre() {
        println("\n--- BUSCAR CLIENTE POR NOMBRE ---")
        val nombre = leerTexto("Ingrese el nombre a buscar: ")
        val coincidencias = CustomerRegistry.findCustomersByName(nombre)

        if (coincidencias.isEmpty()) {
            println("No se encontraron clientes con el nombre '$nombre'.")
        } else {
            println("\nSe encontraron ${coincidencias.size} resultado(s):")
            coincidencias.forEach { cliente ->
                println("ID: ${cliente.id} | Nombre: ${cliente.nombre} | Tel: ${cliente.telefono} | Correo: ${cliente.correo}")
            }
        }
    }

    private fun modificarCliente() {
        println("\n--- MODIFICAR CLIENTE ---")
        val id = leerEntero("Ingrese el ID del cliente a modificar: ")
        val cliente = CustomerRegistry.findCustomerById(id)

        if (cliente == null) {
            println(" Error: No existe ningún cliente con el ID $id.")
            return
        }

        println("Cliente actual -> Nombre: ${cliente.nombre} | Tel: ${cliente.telefono} | Correo: ${cliente.correo}")
        val nuevoNombre = leerTexto("Ingrese el nuevo nombre: ")

        var nuevoTelefono: String
        while (true) {
            nuevoTelefono = leerTexto("Ingrese el nuevo teléfono: ")
            if (CustomerRegistry.existsByPhone(nuevoTelefono, ignoreId = id)) {
                println(" Error: Ya existe otro cliente con ese número de teléfono.")
            } else {
                break
            }
        }

        var nuevoCorreo: String
        while (true) {
            nuevoCorreo = leerTexto("Ingrese el nuevo correo: ")
            if (CustomerRegistry.existsByEmail(nuevoCorreo, ignoreId = id)) {
                println(" Error: Ya existe otro cliente con ese correo electrónico.")
            } else {
                break
            }
        }

        if (CustomerRegistry.updateCustomer(id, nuevoNombre, nuevoTelefono, nuevoCorreo)) {
            println(" ¡Datos del cliente actualizados correctamente!")
        } else {
            println(" Error al actualizar los datos.")
        }
    }

    private fun eliminarCliente() {
        println("\n--- ELIMINAR CLIENTE ---")
        val id = leerEntero("Ingrese el ID del cliente a eliminar: ")
        val cliente = CustomerRegistry.findCustomerById(id)

        if (cliente == null) {
            println(" Error: No existe ningún cliente con el ID $id.")
            return
        }

        println("¿Está seguro de eliminar al cliente '${cliente.nombre}' (ID: ${cliente.id})?")
        println("1. Sí, eliminar")
        println("2. Cancelar")

        if (leerEntero("Seleccione una opción: ") == 1) {
            if (CustomerRegistry.deleteCustomer(id)) {
                println(" ¡Cliente eliminado correctamente!")
            } else {
                println(" Error al intentar eliminar el cliente.")
            }
        } else {
            println(" Operación cancelada.")
        }
    }
}