data class CustomerData(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String
)

object CustomerRegistry {
    private val listaClientes = mutableListOf<CustomerData>()
    private var contadorId = 1

    fun registerCustomer(nombre: String, telefono: String, correo: String) {
        listaClientes.add(CustomerData(contadorId++, nombre, telefono, correo))
    }

    fun getCustomers(): List<CustomerData> = listaClientes

    fun findCustomerByName(nombre: String): CustomerData? {
        return listaClientes.find { it.nombre.contains(nombre, ignoreCase = true) }
    }
}