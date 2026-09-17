# **PublishProductService**

### **1. Responsabilidad Única**

El servicio se encarga de validar, clasificar (físico o digital) y registrar la publicación de un producto en el catálogo público del sistema.

### **2. Dependencias y Puertos**
La clase se anota con @Service para permitir la inyeccion de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):
- productRepositoryPort: Puerto de salida para la persistencia del producto en catálogo.
- sellerRepositoryPort: Puerto de salida para verificar que el vendedor esté habilitado.
- storeRepositoryPort: Puerto de salida para validar la bodega física asociada (obligatorio si el producto es físico).

### **3. Parámetros de Entrada y Salida**

- **Entrada:**
    
    
    | Campo | Tipo de dato  | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId  | String | Requerido | Identificador unico del producto en el sistema |
    | sellerId | String | Requerido | Identificador unico del vendedor que publica el producto |
    | productType | productType | Valores: “Fisico”, “Digital” | Define el tipo de producto que se va publicar |
    | name | String | Requerido | Nombre del producto que se va publicar |
    | description | String | opcional | Detalla las caracteristicas del producto |
    | basePrice | BigDecimal | Requerido debe ser >0 | Precio de venta inicial |
    | storeId | String | Requerido si producType es “Fisico” / si es “digital” es nulo  | Identificador de la bodega de despacho obligatorio para productos “Fisico” |
    | variants | List<Variant> | Opcional | Definicion de color, talla y modelo del producto publicado |
- **Salida:**
    
    
    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | Product | product | Producto registrado y publicado en el catalogo |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Vendedor Activo:** Si el vendedor no existe o su estado es inactivo o suspendido, se dispara unauthorizedSellerException.
2. **Validación de Bodega Física:** Si productType == FISICO y no se proporciona un storeId válido o existente, se dispara requiredWarehouseException.
3. **Validación de Datos Básicos:** Si el name está vacío o el basePrice es menor o igual a cero, se dispara invalidProductDataException

### **5. Camino Feliz**

1. Se valida el estado activo del vendedor.
2. Se instancia la entidad Product con sus datos, variantes y clasificación.
3. Se asigna la bodega física en caso de ser un producto físico.
4. Se establece el estado del producto como PUBLICADO.
5. Se guarda mediante productRepositoryPort y se retorna la entidad.