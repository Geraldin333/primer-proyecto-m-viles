data class CustomerData(
    val id: Int,
    var nombre: String,
    var telefono: String,
    var correo: String
)

object CustomerRegistry {
    private val listaClientes = mutableListOf<CustomerData>()
    private var contadorId = 1

    fun registerCustomer(nombre: String, telefono: String, correo: String): CustomerData {
        val nuevoCliente = CustomerData(contadorId++, nombre, telefono, correo)
        listaClientes.add(nuevoCliente)
        return nuevoCliente
    }

    fun getCustomers(): List<CustomerData> = listaClientes

    fun findCustomerById(id: Int): CustomerData? {
        return listaClientes.find { it.id == id }
    }

    fun findCustomerByName(nombre: String): CustomerData? {
        return listaClientes.find { it.nombre.contains(nombre, ignoreCase = true) }
    }

    fun findCustomersByName(nombre: String): List<CustomerData> {
        return listaClientes.filter { it.nombre.contains(nombre, ignoreCase = true) }
    }

    fun existsByPhone(telefono: String, ignoreId: Int? = null): Boolean {
        return listaClientes.any { it.telefono == telefono && it.id != ignoreId }
    }

    fun existsByEmail(correo: String, ignoreId: Int? = null): Boolean {
        return listaClientes.any { it.correo.equals(correo, ignoreCase = true) && it.id != ignoreId }
    }

    fun updateCustomer(id: Int, nuevoNombre: String, nuevoTelefono: String, nuevoCorreo: String): Boolean {
        val cliente = findCustomerById(id) ?: return false
        cliente.nombre = nuevoNombre
        cliente.telefono = nuevoTelefono
        cliente.correo = nuevoCorreo
        return true
    }

    fun deleteCustomer(id: Int): Boolean {
        return listaClientes.removeIf { it.id == id }
    }
}