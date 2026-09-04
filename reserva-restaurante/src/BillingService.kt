object BillingService {

    private const val IMPUESTO = 0.08
    private const val PROPINA = 0.10

    fun calcularImpuesto(subtotal: Double): Double {
        return subtotal * IMPUESTO
    }

    fun calcularPropina(subtotal: Double): Double {
        return subtotal * PROPINA
    }

    fun calcularTotal(subtotal: Double): Double {
        val impuesto = calcularImpuesto(subtotal)
        val propina = calcularPropina(subtotal)

        return subtotal + impuesto + propina
    }

    fun generarFactura(subtotal: Double) {
        val impuesto = calcularImpuesto(subtotal)
        val propina = calcularPropina(subtotal)
        val total = calcularTotal(subtotal)

        println("---------------------------------------------")
        println(" FACTURA DEL RESTAURANTE")
        println("---------------------------------------------")
        println("Subtotal: $${"%.2f".format(subtotal)}")
        println("Impuesto: $${"%.2f".format(impuesto)}")
        println("Propina:  $${"%.2f".format(propina)}")
        println("---------------------------------------------")
        println("TOTAL:    $${"%.2f".format(total)}")
        println("---------------------------------------------")
    }
}