
enum class Categoria {
    PLATILLO,
    BEBIDA
}


data class MenuItem(
    val id: Int,
    val nombre: String,
    val categoria: Categoria,
    var precio: Double,
    var disponible: Boolean = true
)


object MenuCatalog {

    // Lista interna de ítems del menú. Es mutable porque el catálogo
    // puede actualizarse en tiempo de ejecución (agregar ítems, cambiar
    // precios, marcar algo como agotado, etc.).
    private val items: MutableList<MenuItem> = cargarCatalogoInicial()


    private fun cargarCatalogoInicial(): MutableList<MenuItem> {
        return mutableListOf(
            MenuItem(1, "Bandeja Paisa", Categoria.PLATILLO, 28000.0),
            MenuItem(2, "Sancocho de Gallina", Categoria.PLATILLO, 22000.0),
            MenuItem(3, "Arepa con Queso", Categoria.PLATILLO, 8000.0),
            MenuItem(4, "Ensalada César", Categoria.PLATILLO, 15000.0),
            MenuItem(5, "Limonada Natural", Categoria.BEBIDA, 6000.0),
            MenuItem(6, "Jugo de Mora", Categoria.BEBIDA, 7000.0),
            MenuItem(7, "Gaseosa", Categoria.BEBIDA, 4000.0),
            MenuItem(8, "Cerveza Nacional", Categoria.BEBIDA, 9000.0)
        )
    }


    fun obtenerTodos(): List<MenuItem> = items.toList()


    fun obtenerDisponibles(): List<MenuItem> = items.filter { it.disponible }


    fun buscarPorId(id: Int): MenuItem? = items.find { it.id == id }


    fun buscarPorNombre(texto: String): List<MenuItem> =
        items.filter { it.nombre.contains(texto, ignoreCase = true) }


    fun filtrarPorCategoria(categoria: Categoria): List<MenuItem> =
        items.filter { it.categoria == categoria }


    fun agregarItem(item: MenuItem): Boolean {
        if (buscarPorId(item.id) != null) return false
        items.add(item)
        return true
    }


    fun eliminarItem(id: Int): Boolean {
        val item = buscarPorId(id) ?: return false
        return items.remove(item)
    }


    fun actualizarPrecio(id: Int, nuevoPrecio: Double): Boolean {
        val item = buscarPorId(id) ?: return false
        item.precio = nuevoPrecio
        return true
    }


    fun cambiarDisponibilidad(id: Int, disponible: Boolean): Boolean {
        val item = buscarPorId(id) ?: return false
        item.disponible = disponible
        return true
    }


    fun mostrarCatalogo() {
        println("===== CATÁLOGO DE MENÚ =====")
        Categoria.values().forEach { categoria ->
            println("\n-- ${categoria.name} --")
            filtrarPorCategoria(categoria).forEach { item ->
                val estado = if (item.disponible) "Disponible" else "Agotado"
                println("[${item.id}] ${item.nombre} - $${"%,.0f".format(item.precio)} ($estado)")
            }
        }
    }
}