# **UpdateProductService**

### **1. Responsabilidad Única**

El servicio se encarga de actualizar la información comercial de un producto existente (nombre, descripción y precio base) sin alterar su clasificación ni su estado en el catálogo.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- productRepositoryPort: Puerto de salida para consultar y persistir el producto.
- sellerRepositoryPort: Puerto de salida para validar la propiedad y habilitación del vendedor.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId | String | Requerido | Identificador único del producto a actualizar |
    | sellerId | String | Requerido | Identificador del vendedor propietario |
    | name | String | Requerido | Nuevo nombre del producto |
    | description | String | Opcional | Nueva descripción del producto |
    | basePrice | BigDecimal | Requerido, debe ser > 0 | Nuevo precio base de venta |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | product | Product | Producto con los datos actualizados |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Producto Existente:** Si el productId no existe, se dispara productNotFoundException.
2. **Validación de Propiedad:** Si el vendedor no es propietario del producto o se encuentra inactivo, se dispara unauthorizedSellerException.
3. **Validación de Datos Básicos:** Si el name está vacío o el basePrice es menor o igual a cero, se dispara invalidProductDataException.
4. **Validación de Estado:** Si el producto está DISCONTINUED, no admite modificaciones y se dispara productNotEditableException.

### **5. Camino Feliz**

1. Se recupera el producto y se valida la propiedad del vendedor.
2. Se actualizan los campos editables del producto.
3. Se mantiene inalterada la clasificación (ProductType) y el estado actual.
4. Se persiste el producto mediante productRepositoryPort.
5. Se retorna el objeto product actualizado.
