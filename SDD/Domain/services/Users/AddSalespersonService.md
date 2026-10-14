# **AddSalespersonService**

**Especificación del Servicio: AddSalespersonService**

### **1. Responsabilidad Única**

El servicio tiene como única responsabilidad procesar el registro formal e incorporación de un vendedor y su primera bodega física dentro del sistema.

### **2. Dependencias y Puertos**

La clase se anota con @Service. para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables :

- userRepositoryPort: Puerto de salida para validar permisos, consultar duplicados y guardar datos del usuario.
- storeRepositoryPort: Puerto de salida para registrar la primera bodega vinculada al vendedor.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**
    
    
    | campo | Tipo de dato | Restricciones  | Descripcion |
    | --- | --- | --- | --- |
    | executorAdminId | String  |  | Identificador unico del administrador que ejecuta el registro del vendedor|
    | tradeName | String | Requerido(No puede estar vacio) | Nombre comercial |
    | documentId | String | Requerido(No puede estar vacio) | Numero de identificacion del vendedor |
    | mail | String | Formato de Email valido | Correo electrónico de contacto |
    | wineryName | String | Requerido | Nombre de la primera bodega fisica |
    | typeWinery | String | Requerido (Valores permitidos: "Marketplace" o "Vendedor") | Tipo inicial de la bodega |
    | wineryAddress | String | Opcional (o Requerido si aplica) | Dirección física de la bodega |
- **Salida:**
    
    
    | Campo | Tipo de objeto | Descripcion  |
    | --- | --- | --- |
    | user | User | Objeto de dominio del vendedor recién registrado e incorporado en el sistema. |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Administrador:** Se consulta userRepositoryPort. Los vendedores no pueden auto-registrarse; deben ser incorporados por un Administrador. Si el executorAdminId no corresponde a un usuario activo con rol ADMINISTRATOR, se dispara unauthorizedUserException.
2. **Validación de Identificador Único:** Se verifica en userRepositoryPort si ya existen registros mediante existsByMail(mail) o existsByIdentifier(documentId). Si alguno ya existe, se dispara sellerExistsException.
3. **Validación de Bodega Inicial:** Todo vendedor debe quedar vinculado obligatoriamente a una bodega desde su incorporación. Si los datos de la bodega inicial son incompletos, se dispara invalidWarehouseException.

### **5. Camino Feliz**

1. Se verifica la autorización del Administrador ejecutante.
2. Se crea la entidad User asignandole sus datos, el rol SELLER y en estado ACTIVO.
3. Se instancia y vincula la primera store asociada al identificador del seller.
4. Se persisten las entidades a través de userRepositoryPort y storeRepositoryPort.
5. Se retorna el objeto seller creado.