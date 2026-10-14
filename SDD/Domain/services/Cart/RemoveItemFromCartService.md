# **RemoveItemFromCartService**

### **1. Responsabilidad Única**

El servicio se encarga de eliminar una variante de la selección provisional del carrito de compras de un comprador.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- cartRepositoryPort: Puerto de salida para consultar y persistir el carrito de compras.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | buyerId | String | Requerido | Identificador del comprador propietario del carrito |
    | variantId | String | Requerido | Identificador de la variante a eliminar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | cart | ShoppingCart | Carrito con el ítem eliminado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Carrito Existente:** Si el carrito del comprador no existe, se dispara cartNotFoundException.
2. **Validación de Ítem Existente:** Si la variante no se encuentra dentro del carrito, se dispara cartItemNotFoundException.
3. **Validación de Propiedad:** Si el carrito no pertenece al comprador, se dispara unauthorizedBuyerException.

### **5. Camino Feliz**

1. Se recupera el carrito de compras del comprador.
2. Se localiza la variante dentro de los ítems del carrito.
3. Se elimina la variante de la selección.
4. Se persiste el carrito mediante cartRepositoryPort.
5. Se retorna el objeto cart actualizado.
