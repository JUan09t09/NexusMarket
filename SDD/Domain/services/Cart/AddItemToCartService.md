# **AddItemToCartService**

### **1. Responsabilidad Única**

El servicio se encarga de agregar una variante de producto a la selección provisional del carrito de compras de un comprador.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- cartRepositoryPort: Puerto de salida para consultar y persistir el carrito de compras.
- productRepositoryPort: Puerto de salida para validar la existencia y disponibilidad del producto.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | buyerId | String | Requerido | Identificador del comprador propietario del carrito |
    | productId | String | Requerido | Identificador del producto a agregar |
    | variantId | String | Requerido | Identificador de la variante seleccionada |
    | quantity | int | Requerido, debe ser > 0 | Cantidad de unidades a agregar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | cart | ShoppingCart | Carrito con el ítem agregado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Producto Publicado:** Si el producto no existe o su estado no es PUBLISHED, se dispara productNotAvailableException.
2. **Validación de Variante:** Si la variante no pertenece al producto, se dispara variantNotFoundException.
3. **Validación de Cantidad:** Si la quantity es menor o igual a cero, se dispara invalidCartQuantityException.

### **5. Camino Feliz**

1. Se recupera o crea el carrito de compras del comprador.
2. Se valida que el producto esté publicado y la variante sea válida.
3. Se agrega la variante con la cantidad indicada al carrito.
4. Se persiste el carrito mediante cartRepositoryPort.
5. Se retorna el objeto cart actualizado.
