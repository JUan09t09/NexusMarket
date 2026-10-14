# **RegisterBuyerService**

### **1. Responsabilidad Única**

El servicio se encarga de registrar a un nuevo comprador (Buyer) dentro del sistema, creando su cuenta con el rol BUYER y su información comercial de direcciones.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- userRepositoryPort: Puerto de salida para validar duplicados y persistir la cuenta del comprador.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | name | String | Requerido (No puede estar vacío) | Nombre completo del comprador |
    | documentId | String | Requerido | Número de identificación del comprador |
    | mail | String | Formato de Email válido | Correo electrónico de registro |
    | password | String | Requerido | Contraseña de acceso al sistema |
    | mainAddress | String | Requerido | Dirección habitual de entrega |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | buyer | Buyer | Comprador recién registrado con rol BUYER |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Identificador Único:** Se verifica en userRepositoryPort si ya existen registros mediante existsByMail(mail) o existsByIdentifier(documentId). Si alguno ya existe, se dispara buyerExistsException.
2. **Validación de Datos Básicos:** Si el name está vacío o el mail no cumple el formato, se dispara invalidBuyerDataException.
3. **Validación de Contraseña:** Si la password no cumple las políticas mínimas de seguridad, se dispara invalidPasswordException.

### **5. Camino Feliz**

1. Se validan los datos de entrada y la unicidad del identificador y el correo.
2. Se crea la entidad Buyer con su información comercial y su dirección principal.
3. Se asigna el rol BUYER y el estado ACTIVO.
4. Se persiste la cuenta mediante userRepositoryPort.
5. Se retorna el objeto buyer creado.
