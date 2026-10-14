# **CancelOrderService**

### **1. Responsabilidad Única**

El servicio se encarga de cancelar un pedido que aún no ha sido despachado, liberando el inventario reservado y dejando constancia de la cancelación.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar y actualizar el pedido.
- inventoryRepositoryPort: Puerto de salida para liberar las reservas de inventario.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido a cancelar |
    | buyerId | String | Requerido | Identificador del comprador que solicita la cancelación |
    | reason | String | Opcional | Motivo de la cancelación |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | order | Order | Pedido cancelado |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Propiedad:** Si el pedido no pertenece al comprador, se dispara unauthorizedBuyerException.
2. **Validación de Estado:** Si el pedido ya fue DISPATCHED, DELIVERED o FINALIZED, no puede cancelarse y se dispara orderNotCancellableException.
3. **Validación de Pedido Existente:** Si el orderId no existe, se dispara orderNotFoundException.

### **5. Camino Feliz**

1. Se recupera el pedido y se valida su propiedad y estado.
2. Se liberan las reservas de inventario de las variantes físicas.
3. Se marca el pedido como cancelado.
4. Se persiste el pedido mediante orderRepositoryPort.
5. Se retorna el objeto order cancelado.
