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
}