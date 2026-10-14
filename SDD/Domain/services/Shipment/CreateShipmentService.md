# **CreateShipmentService**

### **1. Responsabilidad Única**

El servicio se encarga de crear el envío (Shipment) asociado a un pedido de productos físicos que ya ha sido despachado.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar el pedido y su comprador.
- shipmentRepositoryPort: Puerto de salida para persistir el envío generado.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido físico asociado |
    | identifier | long | Requerido | Identificador único del envío |
    | shippingDate | LocalDate | Requerido | Fecha de despacho del pedido |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | shipment | Shipment | Envío creado en estado PENDING |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Pedido Existente:** Si el orderId no existe, se dispara orderNotFoundException.
2. **Validación de Pedido Físico:** Si el pedido no contiene productos físicos, se dispara orderNotPhysicalException.
3. **Validación de Envío Único:** Si el pedido ya cuenta con un envío asociado, se dispara shipmentAlreadyExistsException.

### **5. Camino Feliz**

1. Se recupera el pedido y se valida que contenga productos físicos.
2. Se verifica que no exista un envío previo para el pedido.
3. Se instancia la entidad Shipment con su identificador, fechas y estado PENDING.
4. Se vincula el envío al comprador como destinatario.
5. Se persiste el envío mediante shipmentRepositoryPort y se retorna.
