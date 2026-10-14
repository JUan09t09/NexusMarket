# **UpdateShipmentStatusService**

### **1. Responsabilidad Única**

El servicio se encarga de actualizar el estado logístico de un envío (ShippingStatus) a lo largo de su ciclo de vida, desde el despacho hasta la entrega o devolución.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- shipmentRepositoryPort: Puerto de salida para consultar y persistir el envío.
- userRepositoryPort: Puerto de salida para validar los roles autorizados del operador.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | shipmentId | long | Requerido | Identificador único del envío |
    | operatorId | String | Requerido | Identificador del operador logístico |
    | newStatus | ShippingStatus | Valores permitidos: PENDING, SHIPPED, DELIVERED, RETURNED | Nuevo estado del envío |
    | deliveryDate | LocalDate | Requerido si el estado es DELIVERED | Fecha de entrega del pedido |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | shipment | Shipment | Envío con el estado actualizado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Rol Autorizado:** Si el operatorId no corresponde a un usuario con rol LOGISTIC_OPERATOR, se dispara unauthorizedUserException.
2. **Validación de Envío Existente:** Si el shipmentId no existe, se dispara shipmentNotFoundException.
3. **Validación de Transición de Estado:** Si la transición solicitada no es válida según el ciclo logístico, se dispara invalidShippingStatusTransitionException.

### **5. Camino Feliz**

1. Se verifica la autorización del operador logístico.
2. Se recupera el envío y se valida la transición de estado.
3. Se actualiza el estado del envío y, si aplica, la fecha de entrega.
4. Se persiste el envío mediante shipmentRepositoryPort.
5. Se retorna el objeto shipment con el estado actualizado.
