# **FinalizeOrderService**

### **1. Responsabilidad Única**

El servicio se encarga de cerrar el ciclo comercial de un pedido entregado, transitando su estado de DELIVERED a FINALIZED.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar y actualizar el pedido.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido a finalizar |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido con estado FINALIZED |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Pedido Existente:** Si el orderId no existe, se dispara orderNotFoundException.
2. **Validación de Estado:** Si el pedido no está en estado DELIVERED, se dispara invalidOrderStatusException.
3. **Validación de Devolución Pendiente:** Si existe una devolución en curso asociada al pedido, no puede finalizarse y se dispara pendingReturnException.

### **5. Camino Feliz**

1. Se recupera el pedido y se valida su estado.
2. Se verifica que no existan devoluciones pendientes.
3. Se actualiza el estado del pedido a FINALIZED.
4. Se persiste el pedido mediante orderRepositoryPort.
5. Se retorna el objeto order finalizado.
