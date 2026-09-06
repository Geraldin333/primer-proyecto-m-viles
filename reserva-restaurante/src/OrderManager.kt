data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
)

data class Pedido(
    val idPedido: Int,
    val numeroMesa: Int,
    val items: MutableList<Producto> = mutableListOf()
) {
    fun subtotal(): Double = items.sumOf { it.precio }
}

class OrderManager {

    private val pedidos = mutableListOf<Pedido>()
    private var contadorPedidos = 1

    fun obtenerPedidoPorMesa(numeroMesa: Int): List<Pedido> {
        return pedidos.filter { it.numeroMesa == numeroMesa }
    }

    fun tienePedidosPendientes(numeroMesa: Int): Boolean {
        return obtenerPedidoPorMesa(numeroMesa).isNotEmpty()
    }

    fun agregarPedido(numeroMesa: Int, productos: List<Producto>) {
        val nuevoPedido = Pedido(
            idPedido = contadorPedidos++,
            numeroMesa = numeroMesa,
            items = productos.toMutableList()
        )
        pedidos.add(nuevoPedido)
    }

    fun limpiarPedidosMesa(numeroMesa: Int) {
        pedidos.removeAll { it.numeroMesa == numeroMesa }
    }

    fun gestionarPedidos(tableManager: TableManager) {
        var opcion: Int

        do {
            println("\n---------------------------------------------")
            println(" MÓDULO DE GESTIÓN DE PEDIDOS")
            println("---------------------------------------------")
            println("1. Tomar / Agregar nuevo pedido")
            println("2. Ver consumos de una mesa")
            println("3. Modificar pedido de una mesa")
            println("4. Quitar / Eliminar producto de un pedido")
            println("0. Volver al menú principal")
            println("---------------------------------------------")

            opcion = UIController.leerEntero("Ingrese la opción deseada (0-4): ")

            when (opcion) {
                1 -> tomarPedido(tableManager)
                2 -> verPedidosMesa()
                3 -> modificarPedido(tableManager)
                4 -> quitarProductoPedido()
                0 -> println(" Regresando al menú principal...")
                else -> println(" Opción no válida. Ingrese un número entre 0 y 4.")
            }

        } while (opcion != 0)
    }

    private fun tomarPedido(tableManager: TableManager) {
        println("\n--- TOMAR NUEVO PEDIDO ---")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")

        val mesa = tableManager.buscarMesa(numeroMesa)
        if (mesa == null) {
            println(" Error: No existe la mesa $numeroMesa.")
            return
        }

        if (!mesa.estado.equals("Ocupada", ignoreCase = true)) {
            println(" Error: La mesa $numeroMesa debe estar en estado 'Ocupada' para tomar un pedido.")
            return
        }

        MenuCatalog.mostrarCatalogo()

        val productosSeleccionados = mutableListOf<Producto>()

        do {
            val idProducto = UIController.leerEntero("Ingrese el ID del producto a agregar (o 0 para terminar): ")

            if (idProducto != 0) {
                // Se busca usando buscarPorId de MenuCatalog
                val menuItem = MenuCatalog.buscarPorId(idProducto)
                if (menuItem != null) {
                    if (menuItem.disponible) {
                        // Mapeo de MenuItem a Producto para OrderManager
                        val producto = Producto(
                            id = menuItem.id,
                            nombre = menuItem.nombre,
                            precio = menuItem.precio
                        )
                        productosSeleccionados.add(producto)
                        println(" ¡${menuItem.nombre} registrado con éxito!")
                    } else {
                        println(" El producto '${menuItem.nombre}' no está disponible actualmente.")
                    }
                } else {
                    println(" Error: No existe un producto con el ID $idProducto en el catálogo.")
                }
            }
        } while (idProducto != 0)

        if (productosSeleccionados.isNotEmpty()) {
            agregarPedido(numeroMesa, productosSeleccionados)
            println("\n ¡Pedido registrado exitosamente para la Mesa $numeroMesa!")
        } else {
            println(" No se agregaron productos al pedido.")
        }
    }

    private fun verPedidosMesa() {
        println("\n--- CONSULTAR CONSUMOS DE MESA ---")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")

        val listaPedidos = obtenerPedidoPorMesa(numeroMesa)
        if (listaPedidos.isEmpty()) {
            println(" La mesa $numeroMesa no tiene pedidos registrados.")
            return
        }

        println("\n--- CONSUMOS DE LA MESA $numeroMesa ---")
        listaPedidos.forEach { pedido ->
            println("Pedido #${pedido.idPedido}:")
            pedido.items.forEachIndexed { index, item ->
                println("  [${index + 1}] ${item.nombre} : $${"%.2f".format(item.precio)}")
            }
        }
        val totalMesa = listaPedidos.sumOf { it.subtotal() }
        println("---------------------------------------------")
        println("Subtotal acumulado: $${"%.2f".format(totalMesa)}")
    }

    private fun modificarPedido(tableManager: TableManager) {
        println("\n--- MODIFICAR PEDIDO DE UNA MESA ---")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa a modificar: ")

        val listaPedidos = obtenerPedidoPorMesa(numeroMesa)
        if (listaPedidos.isEmpty()) {
            println(" La mesa $numeroMesa no tiene pedidos activos para modificar.")
            return
        }

        println("\nAgregando ítems adicionales a la Mesa $numeroMesa:")
        tomarPedido(tableManager)
    }

    private fun quitarProductoPedido() {
        println("\n--- QUITAR PRODUCTO DE UN PEDIDO ---")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa: ")

        val listaPedidos = obtenerPedidoPorMesa(numeroMesa)
        if (listaPedidos.isEmpty()) {
            println(" La mesa $numeroMesa no tiene pedidos registrados.")
            return
        }

        println("\nPedidos de la Mesa $numeroMesa:")
        listaPedidos.forEach { pedido ->
            println("Pedido ID: ${pedido.idPedido}")
            pedido.items.forEachIndexed { index, item ->
                println("  [${index + 1}] ${item.nombre} - $${"%.2f".format(item.precio)}")
            }
        }

        val idPedidoElegido = UIController.leerEntero("\nIngrese el ID del Pedido del cual desea eliminar un producto (0 para cancelar): ")
        if (idPedidoElegido == 0) return

        val pedidoTarget = listaPedidos.find { it.idPedido == idPedidoElegido }
        if (pedidoTarget == null || pedidoTarget.items.isEmpty()) {
            println(" Error: No se encontró un pedido con el ID $idPedidoElegido o no tiene productos.")
            return
        }

        val posicion = UIController.leerEntero("Ingrese el número de ítem a eliminar (1 - ${pedidoTarget.items.size}): ") - 1

        if (posicion in 0 until pedidoTarget.items.size) {
            val productoEliminado = pedidoTarget.items.removeAt(posicion)
            println(" ¡Se eliminó '${productoEliminado.nombre}' del pedido con éxito!")

            if (pedidoTarget.items.isEmpty()) {
                pedidos.remove(pedidoTarget)
                println(" El pedido #${pedidoTarget.idPedido} quedó vacío y fue removido.")
            }
        } else {
            println(" Error: Posición de producto inválida.")
        }
    }
}