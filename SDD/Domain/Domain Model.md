# Domain Model

# Introducción

El modelo de dominio representa las entidades empresariales fundamentales para el sistema de NexusMarket. Estas entidades encapsulan las reglas del negocio, los datos y las relaciones descritas en las especificaciones del proyecto.

El modelo sigue los principios del diseño orientado a objetos y aplica la herencia para eliminar la información duplicada, al tiempo que fomenta la reutilización y la facilidad de mantenimiento.

## Domain Class Hieerarchy

```jsx
Usuario (Abstract)
├── Comprador
├── Vendedor
├── OperadorLogistico
├── Administrador
└── Supervisor

Bodega

Producto
└── Variante

Inventario
└── Movimiento

CarritoDeCompras

Pedido
├── Factura
├── Envío
└── Devolución
		└── Reembolso
```

## Entidades

### Usuario(abstracto)

Descripción:

Representa una persona que interactúa con el sistema NexusMarket. Centraliza la información común de los diferentes tipos de usuarios que participan en la plataforma, como compradores, vendedores, operadores logísticos, administradores y supervisores.

Atributos

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| identifier | String | Identificador único de la entidad. Representa un número de identificación nacional para las personas físicas o un número de identificación fiscal para las empresas. |
| name | String | Nombre completo de una persona física o razón social de una empresa. |
| email | String | Dirección de correo electrónico principal registrada. |
| rol | String | Define las responsabilidades y permisos. |
| State | UserRole | Condición operativa (Activo, bloqueado, etc.) |
| status | UserStatus | Condicion operativa del usuario. |

### Comprador

Descripción:

Represente al usuario que utiliza el sistema para buscar, seleccionar y adquirir productos ofrecidos por los vendedores.

El comprador puede gestionar sus direcciones, utilizar el carrito de compras, realizar pedidos y participar en el proceso de compra hasta la entrega del producto.

Atributos

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| Main address | String | Ubicación habitual para entregas. |
| Additional addresses | String | Ubicaciones secundarias de entrega.  |
| Commercial status | String | Condiciones del comprador para realizar compras. |

### Vendedor

Descripción:

Representa al usuario encargado de ofrecer y administrar sus productos dentro del sistema.

El vendedor puede registrar productos en la plataforma y administrar su catalogo, permitiendo que estos sean ofrecidos a los compradores.

### Operador Logístico

Descripción:

Representa al usuario encargado de participar en las actividades operativas relacionadas con las bodegas y los procesos logísticos del sistema.

Su función esta relacionada con la gestión física de productos, preparación de pedidos y procesos de despacho.

### Administrador

Descripción:

Representa al usuario responsable de administrar y gestionar la operación general del sistema.

Entre sus responsabilidades se encuentra la gestión de vendedores y bodegas, así como la administración general de la operación de la plataforma.

### Supervisor

Descripción:

Representa al usuario encargado de supervisar y consultar las actividades operativas del sistema.

Controla y realiza seguimiento de las actividades relacionadas con las bodegas y los operadores logísticos.

### Bodega

Descripción:

Represente un espacio físico destinado al almacenamiento y gestión de productos dentro del sistema.

el sistema contempla bodegas propias del Marketplace y bodegas de vendedores. las bodegas participan en los procesos de inventario, almacenamiento y despacho de productos.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| WarehouseType | WarehouseType | Define si la bodega es de Marketplace o una bodega de un vendedor.  |

### Producto

Descripción:

Representa un producto comercializado dentro del sistema.

Los productos pueden ser físicos o digitales y pueden contar con diferentes variantes. Además, poseen un estado que permite controlar su disponibilidad en el catalogo.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| producType | ProductType | Se encarga de si el producto físicos (requieren inventario y despacho) o digitales (entrega inmediata tras el pago) |
| variants | List<Variant> | Diferencias de color, talla, modelo. |
| status | ProductStatus | Publicado, Suspendido o Descontinuado.  |

### Variante

Descripción:

Representa una versión o característica especifica de un producto.

La variantes permiten diferenciar un mismo producto mediante características como color, talla o modelo, permitiendo que un producto pueda disponer de diferentes opciones para los compradores.

Atributos:

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| color | String | Define el color de la variante. |
| size | String | Define la talla de la variante. |
| model | String | Define el modelo de la variante. |

### Inventario

Descripcion:

Representa el control de las existencias de un producto dentro de una bodega determinada.

El inventario permite conocer y controlar las existencias disponibles y mantiene un registro de los movimientos que afectan las cantidades almacenadas. El sistema debe impedir que las existencias sean negativas.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| amount | Integer | Indica la cantidad de unidades disponibles. |

### Movimiento

Descripcion:

Representa un cambio realizado sobre las existencias de un inventario.

Los movimientos permiten mantener la trazabilidad de las modificaciones realizadas sobre el inventario y pueden corresponder a ingresos, reservas, salidas por venta, ajustes o devoluciones.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| movementType | MovementType | Define qué tipo de movimiento ocurrió en el inventario. |

### CarritoDeCompras

Descripcion:

Representa la selección provisional de productos realizada por un comprador antes de confirmar una compra.

El carrito permite al comprador seleccionar productos y preparar la información necesaria para posteriormente confirmar un pedido.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| items | List<Variant> | Contiene los productos seleccionados por el comprador. |

### Pedido

Representa la solicitud de compra realizada por un comprador dentro del sistema.

El pedido concentra la información relacionada con la compra y atraviesa diferentes estados durante su ciclo de vida, desde la creación y el pago hasta el despacho y la entrega o finalización.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| Status | OrderStatus | Indica en qué estado se encuentra el pedido. |

### Factura

Representa la información comercial asociada a una venta realizada dentro del sistema.

La factura forma parte del proceso de facturación y permite registrar la información comercial correspondiente a las operaciones de venta.(en el documento se menciona información comercial asociada a las ventas)

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| invoiceId | String | Identificador único de la factura. |
| invoiceDate | LocalDate | Fecha en la que se genera la factura. |
| totalAmount | BigDecimal | Valor total de la factura. |

### Envío

Representa el proceso logístico encargado de llevar los productos físicos desde el proceso de preparación y despacho hasta su entrega al comprador.

El envío forma parte del ciclo de gestión de pedidos y se relaciona con las actividades de logística y distribución.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| shippingStatus | ShippingStatus | Indica el estado actual del envío. |
| shippingDate | LocalDate | Fecha en la que se realiza el despacho. |
| deliveryDate | LocalDate | Fecha en la que se entrega el pedido. |

### Devolución

Representa el proceso mediante el cual un producto vendido es devuelto dentro del sistema.

Las devoluciones forman parte de los procesos de posventa y pueden generar movimientos sobre el inventario y procesos posteriores de reembolso.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| returnDate | LocalDate | Fecha en la que se realiza la devolución. |
| reason | String | Motivo por el que se devuelve el producto. |
| status | ReturnStatus | Indica el estado actual de la devolución. |

### Reembolso

Representa el proceso mediante el cual se devuelve al comprador el dinero correspondiente a una operación que requiere un reembolso.

El reembolso forma parte de los procesos de posventa y está relacionado con las devoluciones y las operaciones comerciales realizadas en la plataforma.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| refundDate | LocalDate | Fecha en la que se realiza el reembolso. |
| amount | BigDecimal | Cantidad de dinero que se devuelve al comprador. |
| status | RefundStatus | Indica el estado actual del reembolso. |