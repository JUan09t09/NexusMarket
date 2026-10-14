# **AddProductVariantService**

### **1. Responsabilidad Única**

El servicio se encarga de agregar una nueva variante (color, talla o modelo) a un producto existente del catálogo.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- productRepositoryPort: Puerto de salida para consultar y persistir el producto con sus variantes.
- sellerRepositoryPort: Puerto de salida para validar la propiedad y habilitación del vendedor.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | productId | String | Requerido | Identificador único del producto |
    | sellerId | String | Requerido | Identificador del vendedor propietario |
    | color | String | Opcional | Color de la variante |
    | size | String | Opcional (valores Size) | Talla de la variante |
    | model | String | Opcional | Modelo de la variante |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | product | Product | Producto con la nueva variante agregada |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Producto Existente:** Si el productId no existe, se dispara productNotFoundException.
2. **Validación de Propiedad:** Si el vendedor no es propietario del producto, se dispara unauthorizedSellerException.
3. **Validación de Variante Duplicada:** Si ya existe una variante con la misma combinación de color, talla y modelo, se dispara duplicatedVariantException.
4. **Validación de Datos de Variante:** Si los tres atributos están vacíos, se dispara invalidVariantException.

### **5. Camino Feliz**

1. Se recupera el producto y se valida la propiedad del vendedor.
2. Se valida que la combinación de atributos no exista previamente.
3. Se instancia la entidad Variant con sus atributos.
4. Se agrega la variante a la lista del producto.
5. Se persiste el producto mediante productRepositoryPort y se retorna.
