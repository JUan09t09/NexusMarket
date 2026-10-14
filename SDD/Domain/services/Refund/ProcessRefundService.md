# **ProcessRefundService**

### **1. Responsabilidad Única**

El servicio se encarga de gestionar el reembolso (Refund) originado en una devolución aprobada, registrando y ejecutando la devolución del dinero al comprador.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- returnRepositoryPort: Puerto de salida para validar la devolución aprobada.
- refundRepositoryPort: Puerto de salida para consultar y persistir el reembolso.
- paymentGatewayPort: Puerto de salida para ejecutar la devolución del dinero.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | returnId | String | Requerido | Identificador de la devolución aprobada |
    | amount | BigDecimal | Requerido, debe ser > 0 | Cantidad de dinero a reembolsar |
    | refundDate | LocalDate | Requerido | Fecha en la que se procesa el reembolso |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | refund | Refund | Reembolso con su estado actualizado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Devolución Aprobada:** Si la devolución no existe o no está en estado Approved, se dispara invalidReturnStatusException.
2. **Validación de Monto:** Si el amount es menor o igual a cero o supera el valor del pedido, se dispara invalidRefundAmountException.
3. **Validación de Pago:** Si paymentGatewayPort no confirma la transacción, se dispara paymentRejectedException.

### **5. Camino Feliz**

1. Se recupera la devolución y se valida que esté aprobada.
2. Se crea el refund con su monto y estado Processing.
3. Se ejecuta la devolución del dinero mediante paymentGatewayPort.
4. Se actualiza el estado del refund a Succeeded.
5. Se persiste el reembolso mediante refundRepositoryPort y se retorna.
