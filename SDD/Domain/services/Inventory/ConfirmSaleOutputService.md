# **ConfirmSaleOutputService**

### **1. Responsabilidad Única**

El servicio se encarga de confirmar la salida definitiva de unidades del inventario (movimiento SALE_OUTPUT) una vez que una venta reservada se consolida.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- inventoryRepositoryPort: Puerto de salida para consultar y actualizar las existencias.
- inventoryMovementRepositoryPort: Puerto de salida para registrar el movimiento de salida.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId | String | Requerido | Identificador del producto vendido |
    | storeId | String | Requerido | Identificador de la bodega de despacho |
    | quantity | int | Requerido, debe ser > 0 | Cantidad de unidades a dar de salida |
    | orderId | String | Requerido | Identificador del pedido que consolida la venta |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | inventory | Inventory | Inventario con las existencias actualizadas |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Reserva Previa:** Si las unidades de la orden no cuentan con una reserva previa, se dispara reservationNotFoundException.
2. **Validación de Inventario Existente:** Si no existe inventario para la combinación de producto y bodega, se dispara inventoryNotFoundException.
3. **Validación de Existencias No Negativas:** Si la salida dejaría las existencias por debajo de cero, se dispara negativeStockException.

### **5. Camino Feliz**

1. Se localiza la reserva asociada al pedido y sus unidades.
2. Se localiza el registro de inventario correspondiente.
3. Se descuentan las unidades reservadas del total de existencias.
4. Se registra un movimiento de tipo SALE_OUTPUT para la trazabilidad.
5. Se persisten los cambios y se retorna el objeto inventory actualizado.
