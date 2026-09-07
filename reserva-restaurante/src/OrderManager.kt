data class PedidoItem(
    val item: MenuItem,
    var cantidad: Int
) {
    fun subtotal(): Double = item.precio * cantidad
}

class OrderManager(private val tableManager: TableManager) {

    private val pedidosPorMesa = mutableMapOf<Int, MutableList<PedidoItem>>()

    fun gestionarPedidos() {
        var opcion: Int

        do {
            println("\n---------------------------------------------")
            println(" MÓDULO DE GESTIÓN DE PEDIDOS ")
            println("---------------------------------------------")
            println("1. Agregar producto a una mesa")
            println("2. Quitar producto de una mesa")
            println("3. Modificar cantidad de un producto")
            println("4. Ver pedido actual de una mesa")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            opcion = UIController.leerEntero("Ingrese la opción deseada (0-4): ")

            when (opcion) {
                1 -> agregarProducto()
                2 -> quitarProducto()
                3 -> modificarCantidad()
                4 -> verPedidoMesa()
                0 -> println(" Regresando al menú principal...")
                else -> println(" Opción no válida. Ingrese un número entre 0 y 4.")
            }

        } while (opcion != 0)
    }

    private fun obtenerMesaActiva(): Int? {
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")
        val mesa = tableManager.buscarMesa(numeroMesa)

        if (mesa == null) {
            println(" Error: La mesa $numeroMesa no existe.")
            return null
        }

        if (!mesa.estado.equals("Ocupada", ignoreCase = true)) {
            println(" Error: La mesa $numeroMesa está en estado '${mesa.estado}'. Solo se pueden gestionar pedidos en mesas activas ('Ocupada').")
            return null
        }

        return numeroMesa
    }

    private fun agregarProducto() {
        println("\n--- AGREGAR PRODUCTO A MESA ---")
        val numMesa = obtenerMesaActiva() ?: return

        val disponibles = MenuCatalog.obtenerDisponibles()
        if (disponibles.isEmpty()) {
            println(" No hay productos disponibles en el menú actualmente.")
            return
        }

        println("\nProductos disponibles:")
        disponibles.forEach {
            println("[ID: ${it.id}] ${it.nombre} - $${"%,.0f".format(it.precio)}")
        }

        val idProducto = UIController.leerEntero("\nIngrese el ID del producto a agregar: ")
        val producto = MenuCatalog.buscarPorId(idProducto)

        if (producto == null || !producto.disponible) {
            println(" Error: Producto no encontrado o agotado.")
            return
        }

        val cantidad = UIController.leerEntero("Ingrese la cantidad: ")
        if (cantidad <= 0) {
            println(" Error: La cantidad debe ser mayor a 0.")
            return
        }

        val listaPedidos = pedidosPorMesa.getOrPut(numMesa) { mutableListOf() }
        val itemExistente = listaPedidos.find { it.item.id == idProducto }

        if (itemExistente != null) {
            itemExistente.cantidad += cantidad
        } else {
            listaPedidos.add(PedidoItem(producto, cantidad))
        }

        println(" ¡Producto '${producto.nombre}' (x$cantidad) agregado a la mesa $numMesa exitosamente!")
    }

    private fun quitarProducto() {
        println("\n--- QUITAR PRODUCTO DE MESA ---")
        val numMesa = obtenerMesaActiva() ?: return

        val listaPedidos = pedidosPorMesa[numMesa]
        if (listaPedidos.isNullOrEmpty()) {
            println(" La mesa $numMesa no tiene productos en su pedido.")
            return
        }

        mostrarDetallePedido(numMesa, listaPedidos)
        val idProducto = UIController.leerEntero("Ingrese el ID del producto que desea quitar: ")

        val eliminado = listaPedidos.removeIf { it.item.id == idProducto }
        if (eliminado) {
            println(" Producto eliminado del pedido de la mesa $numMesa.")
            if (listaPedidos.isEmpty()) {
                pedidosPorMesa.remove(numMesa)
            }
        } else {
            println(" Error: El producto con ID $idProducto no se encuentra en el pedido de esta mesa.")
        }
    }

    private fun modificarCantidad() {
        println("\n--- MODIFICAR CANTIDAD DE PRODUCTO ---")
        val numMesa = obtenerMesaActiva() ?: return

        val listaPedidos = pedidosPorMesa[numMesa]
        if (listaPedidos.isNullOrEmpty()) {
            println(" La mesa $numMesa no tiene productos en su pedido.")
            return
        }

        mostrarDetallePedido(numMesa, listaPedidos)
        val idProducto = UIController.leerEntero("Ingrese el ID del producto a modificar: ")
        val item = listaPedidos.find { it.item.id == idProducto }

        if (item == null) {
            println(" Error: Producto no encontrado en el pedido de esta mesa.")
            return
        }

        val nuevaCantidad = UIController.leerEntero("Ingrese la nueva cantidad (0 para eliminar): ")
        if (nuevaCantidad <= 0) {
            listaPedidos.remove(item)
            println(" Producto eliminado del pedido.")
            if (listaPedidos.isEmpty()) {
                pedidosPorMesa.remove(numMesa)
            }
        } else {
            item.cantidad = nuevaCantidad
            println(" Cantidad actualizada a $nuevaCantidad para '${item.item.nombre}'.")
        }
    }

    private fun verPedidoMesa() {
        println("\n--- DETALLE DE PEDIDO MESA ---")
        val numMesa = UIController.leerEntero("Ingrese el número de mesa: ")
        val listaPedidos = pedidosPorMesa[numMesa]

        if (listaPedidos.isNullOrEmpty()) {
            println(" La mesa $numMesa no tiene consumos registrados.")
        } else {
            mostrarDetallePedido(numMesa, listaPedidos)
        }
    }

    private fun mostrarDetallePedido(numMesa: Int, lista: List<PedidoItem>) {
        println("\n=============================================")
        println(" DETALLE DE CONSUMO - MESA $numMesa")
        println("=============================================")
        var total = 0.0
        lista.forEach {
            val sub = it.subtotal()
            total += sub
            println("• [ID: ${it.item.id}] ${it.item.nombre} x${it.cantidad} - Subtotal: $${"%,.0f".format(sub)}")
        }
        println("---------------------------------------------")
        println(" TOTAL ACUMULADO: $${"%,.0f".format(total)}")
        println("=============================================\n")
    }

    // Métodos de integración con TableManager y BillingService
    fun obtenerPedidoPorMesa(numeroMesa: Int): List<PedidoItem> {
        return pedidosPorMesa[numeroMesa] ?: emptyList()
    }

    fun tienePedidosPendientes(numeroMesa: Int): Boolean {
        val lista = pedidosPorMesa[numeroMesa]
        return !lista.isNullOrEmpty()
    }

    fun limpiarPedidosMesa(numeroMesa: Int) {
        pedidosPorMesa.remove(numeroMesa)
    }
}