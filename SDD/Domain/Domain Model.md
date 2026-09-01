# Domain Model

# Introducción

El modelo de dominio representa las entidades empresariales fundamentales para el sistema de NexusMarket. Estas entidades encapsulan las reglas del negocio, los datos y las relaciones descritas en las especificaciones del proyecto.

El modelo sigue los principios del diseño orientado a objetos y aplica la herencia para eliminar la información duplicada, al tiempo que fomenta la reutilización y la facilidad de mantenimiento.

El modelo distingue entre:

1. **Usuarios**, que representan a las personas autorizadas para interactuar con el sistema.
2. **Compradores**, que representan a los usuarios con información comercial adicional para realizar compras.
3. **Bodegas**, que representan los espacios físicos donde se administra el inventario.
4. **Productos y variantes**, que representan los bienes físicos o digitales ofrecidos en el catálogo.
5. **Inventarios y movimientos**, que representan las existencias distribuidas y su trazabilidad.
6. **Carritos y pedidos**, que representan la selección provisional y el compromiso comercial formal.
7. **Facturas, envíos, devoluciones y reembolsos**, que representan los procesos comerciales, logísticos y de posventa.

## Domain Class Hieerarchy

```jsx
User
└── BUYER

Bodega

Producto
└── contiene Variante

Inventario
└── registra MovimientoInventario

CarritoDeCompras

Pedido
├── genera Factura
├── puede generar Envio
└── puede relacionarse con Devolucion
                         └── puede generar Reembolso
```

## Relaciones del dominio

```java
User
└── puede especializarse como ────────> BUYER

User
└── tiene exactamente un ─────────────> UserRole

User con rol ADMINISTRATOR
├── registra ─────────────────────────> SELLER(rol de Usuario)
└── administra ───────────────────────> Store
User con rol SELLER
├── administra ───────────────────────> Product
└── participa en la gestión de ───────> Inventory

User con rol LOGISTIC_OPERATOR
├── participa en la gestión de ───────> Inventory
└── gestiona ─────────────────────────> shipment

Buyer
├── utiliza ──────────────────────────> Shopping Cart
├── confirma ─────────────────────────> Order
├── recibe ───────────────────────────> Shipment
└── solicita ─────────────────────────> return

Product
└── contiene cero o más ──────────────> Variant

Inventory
├── corresponde a ────────────────────> Product
├── se encuentra en ──────────────────> Store
└── registra cero o más ──────────────> InventoryMovement

ShoppingCart
└── contiene cero o más ──────────────> Variant

order
├── pertenece a ──────────────────────> Buyer
├── genera ───────────────────────────> Bill
├── puede generar ────────────────────> Shipment
└── puede relacionarse con ───────────> return

Return
├── puede generar ────────────────────> InventoryMovement
└── puede generar ────────────────────> Refund
```

## Entidades

### User

Descripción:

Representa una persona que interactúa con el sistema NexusMarket. Centraliza la información común de los diferentes tipos de usuarios que participan en la plataforma, como compradores, vendedores, operadores logísticos, administradores y supervisores.

Atributos

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| identifier | long | Identificador único de la entidad. Representa un número de identificación nacional para las personas físicas o un número de identificación fiscal para las empresas. |
| name | name | Nombre completo de una persona física o razón social de una empresa. |
| email | String | Dirección de correo electrónico principal registrada. |
| rol | UserRole | Define las responsabilidades y permisos. |
| status | UserStatus | Condición operativa (Activo, bloqueado, etc.) |
| Password | String | Contraseña única de cada usuario para ingresar al sistema |

Relaciones

- Un usuario se puede relacionar con comprador cuando participa en procesos de compra.
- Cada usuario tiene su UserRole.
- Todo operación incluida en el sistema debe ser realizada por un usuario autorizado.

### Buyer

Descripción:

Represente al usuario que utiliza el sistema para buscar, seleccionar y adquirir productos ofrecidos por los vendedores.

El comprador puede gestionar sus direcciones, utilizar el carrito de compras, realizar pedidos y participar en el proceso de compra hasta la entrega del producto.

Hereda de:

Usuario

Atributos

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| Main address | String | Ubicación habitual para entregas. |
| Additional addresses | String | Ubicaciones secundarias de entrega.  |
| Commercial status | String | Condiciones del comprador para realizar compras. |

Relaciones:

- Un comprador utiliza carrito de compras para seleccionar productos provisionalmente.
- Un comprar pude confirmar uno o mas pedidos.
- Un comprador pude recibir envíos asociados con sus pedidos de productos físicos.
- Un comprar puede solicitar devoluciones.
- Un comprador participa en procesos de reembolso que se asocien a el.

### Store

Descripción:

Represente un espacio físico destinado al almacenamiento y gestión de productos dentro del sistema.

el sistema contempla bodegas propias del Marketplace y bodegas de vendedores. las bodegas participan en los procesos de inventario, almacenamiento y despacho de productos.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| WarehouseType | WarehouseType | Define si la bodega es de Marketplace o una bodega de un vendedor.  |

Relaciones:

- Una bodega puede almacenar 0 o mas inventarios.
- Cada inventario debe estar asociado con una bodega especifica.
- Una pude pertenecer al Marketplace o estar asociada a un vendedor.

### Product

Descripción:

Representa un producto comercializado dentro del sistema.

Los productos pueden ser físicos o digitales y pueden contar con diferentes variantes. Además, poseen un estado que permite controlar su disponibilidad en el catalogo.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| producType | ProductType | Se encarga de si el producto físicos (requieren inventario y despacho) o digitales (entrega inmediata tras el pago) |
| variants | List<Variant> | Diferencias de color, talla, modelo. |
| status | ProductStatus | Publicado, Suspendido o Descontinuado.  |

Relaciones:

- Un producto pude contener cero o mas instancias de variante.
- Un producto físico puede asociarse con registros de inventarios distribuidos entre varias bodegas.
- Un usuario con rol SELLER puede registrar y administrar productos.
- Un producto o una de sus variantes puede seleccionarse en un carrito de compras

### Variant

Descripción:

Representa una versión o característica especifica de un producto.

La variantes permiten diferenciar un mismo producto mediante características como color, talla o modelo, permitiendo que un producto pueda disponer de diferentes opciones para los compradores.

Atributos:

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| color | String | Define el color de la variante. |
| size | String | Define la talla de la variante. |
| model | String | Define el modelo de la variante. |

Relaciones:

- Una variante pertenece a un producto.
- Una variante de un producto fisico pude participar en el control de un inventario.
- Una variante puede seleccionarse dentro de un carrito de compras.

### Inventario

Descripcion:

Representa el control de las existencias de un producto dentro de una bodega determinada.

El inventario permite conocer y controlar las existencias disponibles y mantiene un registro de los movimientos que afectan las cantidades almacenadas. El sistema debe impedir que las existencias sean negativas.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| amount | Integer | Indica la cantidad de unidades disponibles. |

Relaciones:

- Un inventario corresponde a un producto.
- Un inventario se encuentra en una bodega especifica.
- “Un inventario registra cero o mas acciones de Movimiento”
- Los usuarios con roles autorizados participan en la administracion de el inventario.

### Motion

Descripcion:

Representa un cambio realizado sobre las existencias de un inventario.

Los movimientos permiten mantener la trazabilidad de las modificaciones realizadas sobre el inventario y pueden corresponder a ingresos, reservas, salidas por venta, ajustes o devoluciones.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| movementType | MovementType | Define qué tipo de movimiento ocurrió en el inventario. |

Relaciones: 

- Cada Movimiento pertenece a un inventario.
- Un inventario puede registrar varios movimientos.
- Una devolución puede generar un inventario tipo devolución.

### ShoppingCart

Descripcion:

Representa la selección provisional de productos realizada por un comprador antes de confirmar una compra.

El carrito permite al comprador seleccionar productos y preparar la información necesaria para posteriormente confirmar un pedido.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| items | List<Variant> | Contiene los productos seleccionados por el comprador. |

Relaciones:

- Un carrito de compras es utilizado por un comprador.
- Un carrito de compras pude contener cero o mas mas variantes de un producto
- La confirmación de un carrito de compras crea un pedido.

### Order

Representa la solicitud de compra realizada por un comprador dentro del sistema.

El pedido concentra la información relacionada con la compra y atraviesa diferentes estados durante su ciclo de vida, desde la creación y el pago hasta el despacho y la entrega o finalización.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| Status | OrderStatus | Indica en qué estado se encuentra el pedido. |

Relaciones:

- Un pedido pertenece a un carrito de compras.
- Un pedido genera una factura.
- Un pedido de productos físicos genera un envió.
- Un pedido se relaciona a una devolución.

### Bill

Representa la información comercial asociada a una venta realizada dentro del sistema.

La factura forma parte del proceso de facturación y permite registrar la información comercial correspondiente a las operaciones de venta.(en el documento se menciona información comercial asociada a las ventas)

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| invoiceId | String | Identificador único de la factura. |
| invoiceDate | LocalDate | Fecha en la que se genera la factura. |
| totalAmount | BigDecimal | Valor total de la factura. |

Relaciones.

- Una Factura corresponde a un pedido.

### Shipment

Representa el proceso logístico encargado de llevar los productos físicos desde el proceso de preparación y despacho hasta su entrega al comprador.

El envío forma parte del ciclo de gestión de pedidos y se relaciona con las actividades de logística y distribución.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| shippingStatus | ShippingStatus | Indica el estado actual del envío. |
| shippingDate | LocalDate | Fecha en la que se realiza el despacho. |
| deliveryDate | LocalDate | Fecha en la que se entrega el pedido. |
| identifier | long | Identificador único de la entidad. Representa un número de identificación único del envió  |

Relaciones:

- Un envió corresponde a un pedido de productos fisicos
- Un envio tiene como destinatario al comprador asociado con el pedido.
- Un usuario con rol operador logistico participa en la gestion del envio.

### Return

Representa el proceso mediante el cual un producto vendido es devuelto dentro del sistema.

Las devoluciones forman parte de los procesos de posventa y pueden generar movimientos sobre el inventario y procesos posteriores de reembolso.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| returnDate | LocalDate | Fecha en la que se realiza la devolución. |
| reason | String | Motivo por el que se devuelve el producto. |
| status | ReturnStatus | Indica el estado actual de la devolución. |

Relaciones:

- Una devolucion la solicita un comprador.
- Una devolucion corresponde a un pedido.
- Una devolucion puede generar un reembolso.

### **Refund**

Representa el proceso mediante el cual se devuelve al comprador el dinero correspondiente a una operación que requiere un reembolso.

El reembolso forma parte de los procesos de posventa y está relacionado con las devoluciones y las operaciones comerciales realizadas en la plataforma.

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| refundDate | LocalDate | Fecha en la que se realiza el reembolso. |
| amount | BigDecimal | Cantidad de dinero que se devuelve al comprador. |
| status | RefundStatus | Indica el estado actual del reembolso. |

Relaciones:

- Un reembolso puede originarse en una devolución.
- Los usuarios autorizados participan en la gestión del reembolso de acuerdo con su rol.

# Ciclo de vida general del dominio

```java
User con rol ADMINISTRATOR
             │
             │ registra al seller y su primera store
             ▼
    User con rol SELLER ───────────────> Store
             │
             │ registra
             ▼
          Product
             │
             ├── contiene Variant
             ├── se asocia con Inventory
             └── se publica en el catalog
                         │
                         ▼
                   Shopping Cart
                         │
                         │ confirmación
                         ▼
                       Order
                         │
                         ├── PENDING_PAYMENT
                         ├── PAID
                         ├── genera Bill
                         ├── genera Shipment si es físico
                         ├── DISPATCHED
                         ├── DELIVERED
                         └── FINALISED
                                  │
                                  └── posible Return
                                             │
                                             ├── Inventory Movement
                                             └── Refund
```

Reglas generales de diseño del dominio

Usuario y comprador

- Comprador es la única especialización de usuario por que es el único participante con atributos adicionales definidos
- Vendedor, Operador Logistico, Administrador y supervisor se representan mediante UserRole y no como clases independientes
- Cada usuario tiene un rol
- El identificador y el correo electrónico de cada usuario son unicos
- Ningún usuario puede administrar información fuera de las permitidas por su rol

Productos e inventario 

- Un producto puede contener variantes.
- Variante no hereda de producto
- Un producto debe clasificarse como fisico o digital
- Los productos fisicos requieren inventario y despacho
- Los productos digitales se entregan después del validar el pago
- Todo inventario debe vincularse con un producto y una bodega
- El inventario nunca puede tener existencias negativas
- No se puede reservar inventario inexistente o dañado

Carrito y pedido

- El carrito representa una selección provisional
- Un pedido finalizado no pude modificarse
- Un pedido con productos digitales no requiere envió fisico

Devoluciones y reembolsos 

- Una devolucion se relaciona con un pedido
- Una devolución puede generar un movimiento de inventario
- Una devolución puede generar un reembolso