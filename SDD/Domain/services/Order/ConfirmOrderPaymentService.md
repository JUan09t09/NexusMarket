# **ConfirmOrderPaymentService**

### **1. Responsabilidad Única**

El servicio se encarga de validar el pago de un pedido y transitar su estado de PENDING_PAYMENT a PAID, habilitando el inicio del alistamiento.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar y actualizar el pedido.
- paymentGatewayPort: Puerto de salida para validar la transacción financiera.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido a pagar |
    | paymentReference | String | Requerido | Referencia de la transacción de pago |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido con estado PAID |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Pedido Existente:** Si el orderId no existe, se dispara orderNotFoundException.
2. **Validación de Estado:** Si el pedido no está en estado PENDING_PAYMENT, se dispara invalidOrderStatusException.
3. **Validación de Pago:** Si la transacción no es confirmada por paymentGatewayPort, se dispara paymentRejectedException.

### **5. Camino Feliz**

1. Se recupera el pedido y se valida su estado actual.
2. Se valida la transacción mediante paymentGatewayPort.
3. Se actualiza el estado del pedido a PAID.
4. Se persiste el pedido mediante orderRepositoryPort.
5. Se retorna el objeto order con el estado actualizado.
