# **CreateOrderService**

### **1. Responsabilidad Única**

El servicio se encarga de confirmar el carrito de compras de un comprador y transformar la selección provisional en un pedido formal, dejándolo en estado PENDING_PAYMENT.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- cartRepositoryPort: Puerto de salida para consultar y limpiar el carrito de compras.
- orderRepositoryPort: Puerto de salida para persistir el pedido creado.
- productRepositoryPort: Puerto de salida para clasificar los productos como físicos o digitales.
- inventoryRepositoryPort: Puerto de salida para validar la disponibilidad de los productos físicos.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | buyerId | String | Requerido | Identificador único del comprador que confirma el pedido |
    | cartId | String | Requerido | Identificador único del carrito de compras a confirmar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido creado con estado PENDING_PAYMENT |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Carrito Existente:** Se consulta cartRepositoryPort. Si el carrito no existe o no pertenece al comprador, se dispara cartNotFoundException.
2. **Validación de Carrito No Vacío:** Si el carrito no contiene al menos una variante, se dispara emptyCartException.
3. **Validación de Productos Físicos:** Si el pedido contiene productos físicos sin inventario disponible en su bodega, se dispara insufficientStockException.

### **5. Camino Feliz**

1. Se recupera el carrito de compras del comprador.
2. Se valida que el carrito contenga al menos una variante.
3. Se clasifican los productos incluidos como físicos o digitales.
4. Se instancia la entidad Order con el estado PENDING_PAYMENT y las variantes del carrito.
5. Se persiste el pedido mediante orderRepositoryPort y se retorna.
