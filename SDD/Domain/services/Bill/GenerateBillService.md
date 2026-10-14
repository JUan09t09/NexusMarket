# **GenerateBillService**

### **1. Responsabilidad Única**

El servicio se encarga de generar la factura (Bill) asociada a un pedido que ya fue pagado, registrando la información comercial de la venta.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- orderRepositoryPort: Puerto de salida para consultar el estado y el detalle del pedido.
- billRepositoryPort: Puerto de salida para persistir la factura generada.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador único del pedido pagado |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | bill | Bill | Factura generada con los datos comerciales de la venta |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Pedido Existente:** Se consulta orderRepositoryPort. Si el pedido no existe, se dispara orderNotFoundException.
2. **Validación de Estado Pagado:** Si el pedido no se encuentra en estado PAID, se dispara orderNotPaidException.
3. **Validación de Factura Única:** Si el pedido ya tiene una factura generada, se dispara billAlreadyExistsException.

### **5. Camino Feliz**

1. Se recupera el pedido mediante su identificador.
2. Se valida que el pedido esté en estado PAID y sin factura previa.
3. Se calcula el totalAmount a partir de las variantes del pedido.
4. Se instancia la entidad Bill con invoiceId, invoiceDate y totalAmount.
5. Se persiste la factura mediante billRepositoryPort y se retorna.
