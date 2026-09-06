object UIController {

    fun mostrarMenu() {
        println("---------------------------------------------")
        println(" MENU - SISTEMA DE RESERVAS DE RESTAURANTE")
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
            println("\n---------------------------------------------")
            println(" GESTIÓN DE CLIENTES")
            println("---------------------------------------------")
            println("1. Registrar nuevo cliente")
            println("2. Consultar todos los clientes")
            println("3. Buscar clientes por nombre")
            println("4. Actualizar datos de cliente")
            println("5. Eliminar cliente")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            when (leerEntero("Ingrese la opción deseada (0-5): ")) {
                1 -> {
                    println("\n--- REGISTRAR CLIENTE ---")
                    val nombre = leerTexto("Ingrese el nombre del cliente: ")

                    // Validar Teléfono Único
                    var telefono = ""
                    while (telefono.isEmpty()) {
                        val inputTel = leerTexto("Ingrese el teléfono (o '0' para cancelar): ")
                        if (inputTel == "0") return

                        if (CustomerRegistry.existsByPhone(inputTel)) {
                            println(" Error: Ya existe un cliente registrado con el teléfono $inputTel. Intente con otro.")
                        } else {
                            telefono = inputTel
                        }
                    }

                    // Validar Correo Único
                    var correo = ""
                    while (correo.isEmpty()) {
                        val inputCorreo = leerTexto("Ingrese el correo (o '0' para cancelar): ")
                        if (inputCorreo == "0") return

                        if (CustomerRegistry.existsByEmail(inputCorreo)) {
                            println(" Error: Ya existe un cliente registrado con el correo $inputCorreo. Intente con otro.")
                        } else {
                            correo = inputCorreo
                        }
                    }

                    val cliente = CustomerRegistry.registerCustomer(nombre, telefono, correo)
                    println(" ¡Cliente registrado exitosamente con el ID: ${cliente.id}!")
                }
                2 -> {
                    println("\n--- LISTA GENERAL DE CLIENTES ---")
                    val clientes = CustomerRegistry.getCustomers()
                    if (clientes.isEmpty()) {
                        println(" No hay clientes registrados en el sistema.")
                    } else {
                        clientes.forEach {
                            println("ID: ${it.id} | Nombre: ${it.nombre} | Tel: ${it.telefono} | Correo: ${it.correo}")
                        }
                    }
                }
                3 -> {
                    println("\n--- BUSCAR CLIENTES POR NOMBRE ---")
                    val nombre = leerTexto("Ingrese el nombre a buscar: ")
                    val coincidencias = CustomerRegistry.findCustomersByName(nombre)

                    if (coincidencias.isNotEmpty()) {
                        println("\nClientes encontrados (${coincidencias.size}):")
                        coincidencias.forEach { cliente ->
                            println("• ID: ${cliente.id} | Nombre: ${cliente.nombre} | Tel: ${cliente.telefono} | Correo: ${cliente.correo}")
                        }
                    } else {
                        println(" Error: No se encontró ningún cliente con la palabra '$nombre'.")
                    }
                }
                4 -> {
                    println("\n--- ACTUALIZAR DATOS DE CLIENTE ---")
                    val id = leerEntero("Ingrese el ID del cliente a actualizar (o 0 para salir): ")
                    if (id == 0) return

                    val cliente = CustomerRegistry.findCustomerById(id)

                    if (cliente == null) {
                        println(" Error: No existe un cliente con el ID $id.")
                    } else {
                        println("Datos actuales -> Nombre: ${cliente.nombre} | Tel: ${cliente.telefono} | Correo: ${cliente.correo}")
                        val nuevoNombre = leerTexto("Ingrese el nuevo nombre: ")

                        // Validar Teléfono Único (ignorando el ID actual)
                        var nuevoTelefono = ""
                        while (nuevoTelefono.isEmpty()) {
                            val inputTel = leerTexto("Ingrese el nuevo teléfono (o '0' para cancelar): ")
                            if (inputTel == "0") return

                            if (CustomerRegistry.existsByPhone(inputTel, ignoreId = id)) {
                                println(" Error: El teléfono $inputTel ya está registrado por otro cliente.")
                            } else {
                                nuevoTelefono = inputTel
                            }
                        }

                        // Validar Correo Único (ignorando el ID actual)
                        var nuevoCorreo = ""
                        while (nuevoCorreo.isEmpty()) {
                            val inputCorreo = leerTexto("Ingrese el nuevo correo (o '0' para cancelar): ")
                            if (inputCorreo == "0") return

                            if (CustomerRegistry.existsByEmail(inputCorreo, ignoreId = id)) {
                                println(" Error: El correo $inputCorreo ya está registrado por otro cliente.")
                            } else {
                                nuevoCorreo = inputCorreo
                            }
                        }

                        CustomerRegistry.updateCustomer(id, nuevoNombre, nuevoTelefono, nuevoCorreo)
                        println(" ¡Cliente ID $id actualizado exitosamente!")
                    }
                }
                5 -> {
                    println("\n--- ELIMINAR CLIENTE ---")
                    val id = leerEntero("Ingrese el ID del cliente a eliminar (o 0 para salir): ")
                    if (id == 0) return

                    val eliminado = CustomerRegistry.deleteCustomer(id)

                    if (eliminado) {
                        println(" ¡Cliente ID $id eliminado correctamente!")
                    } else {
                        println(" Error: No existe un cliente registrado con el ID $id.")
                    }
                }
                0 -> salir = true
                else -> println(" Opción no válida. Ingrese un número entre 0 y 5.")
            }
        }
    }
}