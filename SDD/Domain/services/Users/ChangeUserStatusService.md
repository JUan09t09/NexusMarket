# **ChangeUserStatusService**

### **1. Responsabilidad Única**

El servicio se encarga de modificar el estado operativo (UserStatus) de un usuario, permitiendo bloquearlo, suspenderlo o reactivarlo según lo determine un administrador.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- userRepositoryPort: Puerto de salida para consultar y persistir el usuario.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | executorAdminId | String | Requerido | Identificador del administrador que ejecuta el cambio |
    | userId | String | Requerido | Identificador del usuario a modificar |
    | newStatus | UserStatus | Valores permitidos: ACTIVO, BLOQUEADO, SUSPENDIDO | Nuevo estado operativo a asignar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | user | User | Usuario con el estado actualizado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Administrador:** Si el executorAdminId no corresponde a un usuario activo con rol ADMINISTRATOR, se dispara unauthorizedUserException.
2. **Validación de Usuario Objetivo:** Si el userId no existe, se dispara userNotFoundException.
3. **Validación de Transición de Estado:** Si el nuevo estado no es un valor permitido del catálogo UserStatus, se dispara invalidUserStatusException.

### **5. Camino Feliz**

1. Se verifica la autorización del Administrador ejecutante.
2. Se recupera el usuario objetivo.
3. Se actualiza su estado al valor newStatus indicado.
4. Se persiste el usuario mediante userRepositoryPort.
5. Se retorna el objeto user actualizado.
