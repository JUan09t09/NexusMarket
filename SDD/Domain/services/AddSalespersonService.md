# **AddSalespersonService**

**Especificación del Servicio: AddSalespersonService**

### **1. Responsabilidad Única**

El servicio tiene como única responsabilidad procesar el registro formal e incorporación de un vendedor y su primera bodega física dentro del sistema.

### **2. Dependencias y Puertos**

La clase se anota con @Service. para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables :

- `sellerRepositoryPort`: Puerto de salida para verificar existencia y guardar datos del vendedor.
- `userRepositoryPort`: Puerto de salida para validar la identidad y el rol del usuario ejecutante.
- `storeRepositoryPort`: Puerto de salida para registrar la primera bodega vinculada al vendedor.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**
    
    
    | campo | Tipo de dato | Restricciones  | Descripcion |
    | --- | --- | --- | --- |
    | executorAdminId | String  |  | Identificador unico del administrador que ejecuta el registro del vendedor|
    | TradeName | String | Requerido(No puede estar vacio) | Nombre comercial |
    | documentId | String | Requerido(No puede estar vacio) | Numero de identificacion del vendedor |
    | mail | String | Formato de Email valido | Correo electrónico de contacto |
    | TypeWinery | String | Requerido (Valores permitidos: "Marketplace" o "Vendedor") | Tipo inicial de la bodega |
    | wineryAddress | String | Opcional (o Requerido si aplica) | Dirección física de la bodega |
- **Salida:**
    
    
    | Campo | Tipo de objeto | Descripcion  |
    | --- | --- | --- |
    | seller | seller | Objeto de dominio del vendedor recién registrado e incorporado en el sistema. |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Administrador:** Se consulta userRepositoryPort. Los vendedores no pueden auto-registrarse; deben ser incorporados por un Administrador. Si el executorAdminId no corresponde a un usuario activo con rol ADMINISTRATOR, se dispara unauthorizedUserException.
2. **Validación de Identificador Único:** Se verifica en sellerRepositoryPort que ni el documento ni el correo existan previamente en el sistema. Si ya existen, se dispara sellerExistsException.
3. **Validación de Bodega Inicial:** Todo vendedor debe quedar vinculado obligatoriamente a una bodega desde su incorporación[1]. Si los datos de la bodega inicial son incompletos, se dispara invalidWarehouseException.

### **5. Camino Feliz**

1. Se verifica la autorización del Administrador ejecutante.
2. Se instancia la entidad seller en estado ACTIVO.
3. Se instancia y vincula la primera store asociada al identificador del seller.
4. Se persisten las entidades a través de sellerRepositoryPort y storeRepositoryPort.
5. Se retorna el objeto seller creado.