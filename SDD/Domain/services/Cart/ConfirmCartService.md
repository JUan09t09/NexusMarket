# **ConfirmCartService**

### **1. Responsabilidad Única**

El servicio se encarga de confirmar la selección provisional del carrito y crear el pedido formal, reservando el inventario de los productos físicos incluidos.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- cartRepositoryPort: Puerto de salida para consultar y vaciar el carrito.
- orderRepositoryPort: Puerto de salida para persistir el pedido generado.
- inventoryRepositoryPort: Puerto de salida para reservar las existencias de productos físicos.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | buyerId | String | Requerido | Identificador del comprador que confirma |
    | cartId | String | Requerido | Identificador del carrito a confirmar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido creado en estado PENDING_PAYMENT |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Carrito No Vacío:** Si el carrito no contiene al menos una variante, se dispara emptyCartException.
2. **Validación de Disponibilidad:** Si algún producto físico no tiene existencias suficientes, se dispara insufficientStockException.
3. **Validación de Propiedad:** Si el carrito no pertenece al comprador, se dispara unauthorizedBuyerException.

### **5. Camino Feliz**

1. Se recupera el carrito de compras del comprador.
2. Se reserva el inventario de los productos físicos incluidos.
3. Se instancia el pedido Order en estado PENDING_PAYMENT.
4. Se vacía el carrito y se persiste el pedido.
5. Se retorna el objeto order creado.
