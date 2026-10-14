# **AdjustInventoryService**

### **1. Responsabilidad Única**

El servicio se encarga de corregir las existencias de un inventario mediante un movimiento de ajuste (ADJUSTMENT), reflejando diferencias detectadas en una revisión operativa.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- inventoryRepositoryPort: Puerto de salida para consultar y actualizar las existencias.
- inventoryMovementRepositoryPort: Puerto de salida para registrar el movimiento de ajuste.
- userRepositoryPort: Puerto de salida para validar los roles autorizados del operador.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | operatorId | String | Requerido | Identificador del usuario que realiza el ajuste |
    | productId | String | Requerido | Identificador del producto a ajustar |
    | storeId | String | Requerido | Identificador de la bodega |
    | newAmount | int | Requerido, debe ser >= 0 | Nueva cantidad de unidades disponibles |
    | reason | String | Requerido | Motivo del ajuste operativo |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | inventory | Inventory | Inventario con las existencias corregidas |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Autorizado:** Si el operatorId no corresponde a un usuario activo autorizado, se dispara unauthorizedUserException.
2. **Validación de Inventario Existente:** Si no existe inventario para la combinación de producto y bodega, se dispara inventoryNotFoundException.
3. **Validación de Existencias No Negativas:** Si newAmount es menor que cero, se dispara negativeStockException.

### **5. Camino Feliz**

1. Se verifica la autorización del operador.
2. Se localiza el registro de inventario correspondiente.
3. Se calcula la diferencia entre las existencias actuales y newAmount.
4. Se actualizan las existencias y se registra un movimiento de tipo ADJUSTMENT.
5. Se persisten los cambios y se retorna el objeto inventory actualizado.
