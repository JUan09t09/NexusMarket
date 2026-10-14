# **ReserveInventoryService**

### **1. Responsabilidad Única**

El servicio verifica la disponibilidad de existencias físicas para un producto en una bodega específica y aplica la reserva temporal durante una transacción de compra.

### **2. Dependencias y Puertos**

- inventoryRepositoryPort: Puerto de salida para consultar y actualizar las existencias en bodega.
- soreRepositoryPort: Puerto de salida para validar que la bodega esté operativa.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

| Campo | Tipo de dato  | Restricciones | Descripcion |
| --- | --- | --- | --- |
| productId | String | Requerido | Identificador unico del producto en el sistema |
| storeId | String | Requerido | Identificador unico de la bodega donde esta ubicada el producto a reservar |
| quantityReserve | int | Requerido debe ser >0 | Es la cantidad de productos que se van a reservar |
- **Salida:**
    
    
    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | reserveResult | ReserveResult | Objeto con el estado de la reserva, bodega donde se encuentra ubicada y la cantidad |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Existencia de Registro:** Se consulta invotryRepositoryPort. Si no se encuentra inventario para la combinación de producto (productId) y bodega(sotoreId), se dispara `InventoryNotFoundException`.
2. **Validación de Estado Inoperativo:** Se consulta storeRepositoryPort si el inventario está marcado como "Dañado" o “No disponible” , se dispara `InventoryNotAvaibleException`.
3. **Validación de Stock Suficiente:** Si la cantidad quantityReserve ≤ 0 o la cantidad disponible en stock es menor a quantityReserve, se dispara insufficientStockException.

### **5. Camino Feliz**

1. Se localiza el registro de inventario activo para el producto (productId) en la bodega especificada (storeId).
2. Se deduce la cantidad quantityReserve del total de existencias disponibles.
3. Se incrementa el número de unidades en estado reservado.
4. Se guardan los cambios de inventario a través de inventoryRepositoryPort.
5. Se retorna el objeto reserveResult con la confirmación de la reserva.