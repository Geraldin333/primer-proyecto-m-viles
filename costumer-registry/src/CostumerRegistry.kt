data class Customer(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String
)

object CustomerRegistry {
    private val customers = mutableListOf<Customer>()
    private var nextId = 1

    fun registerCustomer(nombre: String, telefono: String, correo: String) {
        customers.add(Customer(nextId++, nombre, telefono, correo))
    }

    fun getCustomers(): List<Customer> = customers

    fun findCustomerByName(nombre: String): Customer? {
        return customers.find { it.nombre.equals(nombre, ignoreCase = true) }
    }
}