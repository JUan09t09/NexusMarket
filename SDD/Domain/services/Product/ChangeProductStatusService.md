# **ChangeProductStatusService**

### **1. Responsabilidad Única**

El servicio se encarga de gestionar el ciclo de vida del producto, transitando su estado (ProductStatus) entre PUBLISHED, SUSPENDED y DISCONTINUED conforme a las reglas del catálogo.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- productRepositoryPort: Puerto de salida para consultar y persistir el producto.
- sellerRepositoryPort: Puerto de salida para validar la propiedad y habilitación del vendedor.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId | String | Requerido | Identificador único del producto |
    | sellerId | String | Requerido | Identificador del vendedor propietario |
    | newStatus | ProductStatus | Valores permitidos: PUBLISHED, SUSPENDED, DISCONTINUED | Nuevo estado del producto |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | product | Product | Producto con el estado actualizado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Producto Existente:** Si el productId no existe, se dispara productNotFoundException.
2. **Validación de Propiedad:** Si el vendedor no es propietario del producto, se dispara unauthorizedSellerException.
3. **Validación de Transición de Estado:** Si la transición solicitada no es válida según el ciclo de vida (por ejemplo, de DISCONTINUED a PUBLISHED), se dispara invalidProductStatusTransitionException.

### **5. Camino Feliz**

1. Se recupera el producto y se valida la propiedad del vendedor.
2. Se valida que la transición entre el estado actual y el nuevo sea permitida.
3. Se actualiza el estado del producto.
4. Se persiste el producto mediante productRepositoryPort.
5. Se retorna el objeto product con el estado actualizado.
