# **AuthenticateUserService**

### **1. Responsabilidad Única**

El servicio se encarga de validar las credenciales de un usuario y verificar que su estado operativo le permita iniciar sesión en el sistema.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- userRepositoryPort: Puerto de salida para consultar el usuario por correo.
- passwordEncoderPort: Puerto de salida para verificar la contraseña de forma segura.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | mail | String | Requerido | Correo electrónico de la cuenta |
    | password | String | Requerido | Contraseña en texto plano a validar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | user | User | Usuario autenticado con su rol y estado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Credenciales:** Si el mail no existe o la contraseña no coincide, se dispara invalidCredentialsException.
2. **Validación de Estado Operativo:** Si el usuario se encuentra en estado BLOCKED o SUSPENDED, se dispara blockedUserException.
3. **Validación de Rol Autorizado:** Si el rol del usuario no está habilitado para operar en el canal, se dispara unauthorizedUserException.

### **5. Camino Feliz**

1. Se recupera el usuario por su correo electrónico.
2. Se verifica la contraseña mediante passwordEncoderPort.
3. Se valida que el estado del usuario sea ACTIVO.
4. Se marca la sesión como autorizada.
5. Se retorna el objeto user autenticado.
