object BillingService {

    private const val IMPUESTO = 0.08
    private const val PROPINA = 0.10

    fun calcularImpuesto(subtotal: Double): Double = subtotal * IMPUESTO
    fun calcularPropina(subtotal: Double): Double = subtotal * PROPINA
    fun calcularTotal(subtotal: Double): Double = subtotal + calcularImpuesto(subtotal) + calcularPropina(subtotal)

    fun procesarFacturaMesa(orderManager: OrderManager, tableManager: TableManager) {
        println("\n--- MÓDULO DE FACTURACIÓN Y COBRO ---")
        val numeroMesa = UIController.leerEntero("Ingrese el número de mesa a facturar: ")

        val mesa = tableManager.buscarMesa(numeroMesa)
        if (mesa == null) {
            println(" Error: No existe la mesa $numeroMesa.")
            return
        }

        val pedidos = orderManager.obtenerPedidoPorMesa(numeroMesa)
        if (pedidos.isEmpty()) {
            println(" La mesa $numeroMesa no tiene consumos registrados para facturar.")
            return
        }

        val subtotal = pedidos.sumOf { it.subtotal() }
        generarFactura(subtotal)

        // Limpiar comanda/pedidos de la mesa y liberarla
        orderManager.limpiarPedidosMesa(numeroMesa)
        mesa.estado = "Disponible"

        println(" ¡Cobro registrado con éxito! La mesa $numeroMesa ha saldado su cuenta y ahora está 'Disponible'.")
    }

    fun generarFactura(subtotal: Double) {
        val impuesto = calcularImpuesto(subtotal)
        val propina = calcularPropina(subtotal)
        val total = calcularTotal(subtotal)

        println("---------------------------------------------")
        println(" FACTURA DEL RESTAURANTE")
        println("---------------------------------------------")
        println("Subtotal: $${"%.2f".format(subtotal)}")
        println("Impuesto (8%): $${"%.2f".format(impuesto)}")
        println("Propina (10%): $${"%.2f".format(propina)}")
        println("---------------------------------------------")
        println("TOTAL A PAGAR: $${"%.2f".format(total)}")
        println("---------------------------------------------")
    }
}