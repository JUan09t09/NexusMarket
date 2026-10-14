# **RegisterInventoryEntryService**

### **1. Responsabilidad Única**

El servicio se encarga de registrar el ingreso de unidades (movimiento INBOUND) a las existencias de un inventario en una bodega determinada.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- inventoryRepositoryPort: Puerto de salida para consultar y actualizar las existencias.
- inventoryMovementRepositoryPort: Puerto de salida para registrar el movimiento de ingreso.
- userRepositoryPort: Puerto de salida para validar los roles autorizados del operador.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | operatorId | String | Requerido | Identificador del usuario que registra el ingreso |
    | productId | String | Requerido | Identificador del producto a ingresar |
    | storeId | String | Requerido | Identificador de la bodega destino |
    | quantity | int | Requerido, debe ser > 0 | Cantidad de unidades a ingresar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | inventory | Inventory | Inventario con las existencias actualizadas |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Autorizado:** Si el operatorId no corresponde a un usuario activo con rol SELLER o LOGISTIC_OPERATOR, se dispara unauthorizedUserException.
2. **Validación de Inventario Existente:** Si no existe inventario para la combinación de producto y bodega, se dispara inventoryNotFoundException.
3. **Validación de Cantidad:** Si la quantity es menor o igual a cero, se dispara invalidMovementQuantityException.

### **5. Camino Feliz**

1. Se verifica la autorización del operador.
2. Se localiza el registro de inventario para el producto en la bodega indicada.
3. Se incrementan las existencias con la cantidad ingresada.
4. Se registra un movimiento de tipo INBOUND para la trazabilidad.
5. Se persisten los cambios y se retorna el objeto inventory actualizado.
