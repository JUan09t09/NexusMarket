# **DispatchOrderService**

### **1. Responsabilidad Única**

El servicio se encarga de registrar el despacho de un pedido de productos físicos, transitando su estado de PAID a DISPATCHED y consolidando la salida de inventario.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar y actualizar el pedido.
- inventoryRepositoryPort: Puerto de salida para consolidar la salida de existencias.
- shipmentRepositoryPort: Puerto de salida para vincular el envío generado.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido a despachar |
    | operatorId | String | Requerido | Identificador del operador logístico |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido con estado DISPATCHED |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Autorizado:** Si el operatorId no corresponde a un usuario con rol LOGISTIC_OPERATOR, se dispara unauthorizedUserException.
2. **Validación de Estado:** Si el pedido no está en estado PAID, se dispara invalidOrderStatusException.
3. **Validación de Pedido Físico:** Si el pedido es exclusivamente digital, no requiere despacho y se dispara orderNotPhysicalException.

### **5. Camino Feliz**

1. Se verifica la autorización del operador logístico.
2. Se recupera el pedido y se valida su estado.
3. Se consolida la salida de inventario de las variantes físicas.
4. Se actualiza el estado del pedido a DISPATCHED y se vincula el envío.
5. Se persiste el pedido y se retorna con el estado actualizado.
