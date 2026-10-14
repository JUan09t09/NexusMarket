# **CreateStoreService**

### **1. Responsabilidad Única**

El servicio se encarga de registrar una nueva bodega (Store) perteneciente al Marketplace o a un vendedor, dentro del sistema.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- storeRepositoryPort: Puerto de salida para validar duplicados y persistir la bodega.
- userRepositoryPort: Puerto de salida para validar al vendedor propietario cuando aplique.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | executorAdminId | String | Requerido | Identificador del administrador que crea la bodega |
    | sellerId | String | Requerido si WarehouseType es Vendedor | Identificador del vendedor propietario |
    | warehouseName | String | Requerido | Nombre de la bodega física |
    | warehouseType | WarehouseType | Valores permitidos: MARKETPLACE, SELLER | Tipo de bodega a registrar |
    | warehouseAddress | String | Requerido | Dirección física de la bodega |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | store | Store | Bodega registrada en el sistema |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Administrador:** Si el executorAdminId no corresponde a un usuario activo con rol ADMINISTRATOR, se dispara unauthorizedUserException.
2. **Validación de Tipo de Bodega:** Si warehouseType es SELLER y no se proporciona un sellerId válido o el vendedor no existe, se dispara invalidWarehouseException.
3. **Validación de Datos Básicos:** Si el nombre o la dirección están vacíos, se dispara invalidWarehouseException.

### **5. Camino Feliz**

1. Se verifica la autorización del Administrador ejecutante.
2. Se valida la coherencia entre el tipo de bodega y su propietario.
3. Se instancia la entidad Store con su tipo, nombre y dirección.
4. Se vincula la bodega al vendedor cuando el tipo es SELLER.
5. Se persiste la bodega mediante storeRepositoryPort y se retorna.
