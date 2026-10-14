# **ProcessReturnService**

### **1. Responsabilidad Única**

El servicio se encarga de aprobar una solicitud de devolución y generar el movimiento de inventario de tipo RETURN, reincorporando las unidades devueltas a la bodega correspondiente.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- returnRepositoryPort: Puerto de salida para consultar y actualizar la devolución.
- inventoryRepositoryPort: Puerto de salida para reincorporar las unidades devueltas.
- inventoryMovementRepositoryPort: Puerto de salida para registrar el movimiento de devolución.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | returnId | String | Requerido | Identificador de la solicitud de devolución |
    | operatorId | String | Requerido | Identificador del usuario que aprueba la devolución |
    | storeId | String | Requerido | Identificador de la bodega que recibe el producto |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | return | Return | Devolución en estado Approved |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Autorizado:** Si el operatorId no corresponde a un usuario autorizado, se dispara unauthorizedUserException.
2. **Validación de Estado:** Si la devolución no está en estado Pending, se dispara invalidReturnStatusException.
3. **Validación de Inventario Existente:** Si no existe inventario para la combinación de producto y bodega, se dispara inventoryNotFoundException.

### **5. Camino Feliz**

1. Se verifica la autorización del operador.
2. Se recupera la devolución y se valida su estado.
3. Se reincorporan las unidades devueltas al inventario de la bodega indicada.
4. Se registra un movimiento de tipo RETURN y se marca la devolución como Approved.
5. Se persisten los cambios y se retorna el objeto return actualizado.
