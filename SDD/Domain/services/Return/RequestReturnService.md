# **RequestReturnService**

### **1. Responsabilidad Única**

El servicio se encarga de registrar la solicitud de devolución (Return) de un producto asociado a un pedido, por parte de un comprador.

### **2. Dependencias y Puertos**

La clase se anota con @Service para permitir la inyección de dependencias en Spring Boot. Requiere los siguientes atributos privados e inmutables (private final):

- returnRepositoryPort: Puerto de salida para persistir la solicitud de devolución.
- orderRepositoryPort: Puerto de salida para validar el pedido asociado y su propiedad.

### **3. Parámetros de Entrada y Salida**

- **Entrada:**

    | Campo | Tipo de dato | Restricciones | Descripcion |
    | --- | --- | --- | --- |
    | orderId | String | Requerido | Identificador del pedido a devolver |
    | buyerId | String | Requerido | Identificador del comprador que solicita la devolución |
    | reason | String | Requerido | Motivo por el que se devuelve el producto |
    | returnDate | LocalDate | Requerido | Fecha en la que se solicita la devolución |
- **Salida:**

    | Campo | Tipo de dato | Descripcion |
    | --- | --- | --- |
    | return | Return | Solicitud de devolución en estado Pending |

### **4. Reglas de Negocio (Validaciones Inversas)**

1. **Validación de Pedido Existente:** Si el orderId no existe, se dispara orderNotFoundException.
2. **Validación de Propiedad:** Si el pedido no pertenece al comprador, se dispara unauthorizedBuyerException.
3. **Validación de Estado del Pedido:** Si el pedido no ha sido DELIVERED o FINALIZED, no admite devolución y se dispara orderNotReturnableException.

### **5. Camino Feliz**

1. Se recupera el pedido y se valida su propiedad y estado.
2. Se valida que no exista una devolución previa activa para el pedido.
3. Se instancia la entidad Return con su fecha, motivo y estado Pending.
4. Se vincula la devolución al pedido y al comprador.
5. Se persiste la devolución mediante returnRepositoryPort y se retorna.
