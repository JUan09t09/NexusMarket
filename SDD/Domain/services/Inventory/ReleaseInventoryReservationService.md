# **ReleaseInventoryReservationService**

### **1. Responsabilidad Única**

El servicio se encarga de liberar las unidades previamente reservadas de un inventario cuando una compra no se consolida (por ejemplo, al cancelar el pedido o expirar la reserva).

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- inventoryRepositoryPort: Puerto de salida para consultar y actualizar las existencias reservadas.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId | String | Requerido | Identificador del producto reservado |
    | storeId | String | Requerido | Identificador de la bodega |
    | quantity | int | Requerido, debe ser > 0 | Cantidad de unidades a liberar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | inventory | Inventory | Inventario con las reservas liberadas |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Inventario Existente:** Si no existe inventario para la combinación de producto y bodega, se dispara inventoryNotFoundException.
2. **Validación de Reserva Suficiente:** Si la cantidad a liberar supera las unidades actualmente reservadas, se dispara invalidReservationQuantityException.
3. **Validación de Estado Operativo:** Si el inventario está marcado como no disponible, se dispara inventoryNotAvailableException.

### **5. Camino Feliz**

1. Se localiza el registro de inventario del producto en la bodega.
2. Se valida que las unidades reservadas cubran la cantidad a liberar.
3. Se descuentan las unidades del conteo reservado.
4. Se persisten los cambios mediante inventoryRepositoryPort.
5. Se retorna el objeto inventory con las reservas liberadas.
