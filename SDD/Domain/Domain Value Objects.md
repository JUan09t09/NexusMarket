# Objetos de valor del dominio

## Introducción

Los objetos de valor representas conceptos inmutables dentro del dominio de NexusMarket. A diferencia de las entidades, no tienen una identidad propia; en su ligar, se definen en su totalidad por sus atributos.

Estos objetos encapsulan valores de negocio controlados, mejoran la expresividad del dominio y evitan el uso de tipos primitivos o cadenas de texto dispersas a lo largo de la aplicación.

## Jerarquía de objetos de valor

```java
DomainCatalog (Abstracto)
├── UserRole
├── UserStatus
├── CommercialStatus
├── ProductType
├── WarehouseType
├── ProductStatus
├── MovementType
├── OrderStatus
├── ShippingStatus
├── ReturnStatus
└── RefundStatus

Name
```

# DomainCatalog (Abstract)

### Descripción:

Representa un catalogo de negocio genérico utilizado a lo largo del dominio del sistema.

Todos los valores controlados del negocio heredan de esta clase, garantizado una estructura consistente en toda la aplicación.

### Atributos:

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| code | String | Identificador único de negocio. |
| name | String | Nombre legible mostrado en la aplicación. |
| description | String | Definición funcional del valor del catálogo. |

Características 

- Es inmutable
- La igualdad se determina por sus valores y no por la identidad del objeto
- Los valores del catalogo estan controlados con por el dominio
- Cada valor del catalogo code unico dentro de su tipo
- Las entidades deben hacer referencia al objeto de valor correspondiente

# UserRole

## Descripción:

Representa los diferentes roles de usuario dentro del sistema.

Hereda de : DomainCatalog

### Valores Permitidos:

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| BUYER | Comprador | Realiza compras dentro de la plataforma. |
| SELLER | Vendedor | Administra y comercializa productos. |
| LOGISTIC_OPERATOR | Operador Logístico | Gestiona inventarios, bodegas y despachos. |
| ADMINISTRATOR | Administrador | Administra la operación general del sistema. |
| SUPERVISOR | Supervisor | Supervisa y consulta la operación logística. |

# UserStatus

## Descripción:

Representa la condición operativa de un usuario.

Hereda de: DomainCatalog

### Valores Permitidos:

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| ACTIVE | Activo | El usuario puede operar normalmente. |
| BLOCKED | Bloqueado | El usuario no puede acceder al sistema. |
| SUSPENDED | Suspendido | El usuario se encuentra suspendido temporalmente. |

# **ProductType**

## **Descripcion:**

Representa el tipo de producto comercializado

Hereda de: DomainCatalog

### **Valores permitidos**

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| PHYSICAL | Físico | Requiere inventario, almacenamiento y despacho. |
| DIGITAL | Digital | Se entrega electrónicamente tras el pago. |

# WharehousType

## Descripción:

Representa la clasificación de una bodega dentro del Marketplace.

El tipo permite diferenciar los espacios físicos pertenecientes directamente al Marketplace de las bodegas pertenecientes o asociadas con vendedores.

Hereda de: DomainCatalog

### Valores Permitidos:

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| MARKETPLACE | Marketplace | Bodega perteneciente al Marketplace. |
| SELLER | Vendedor | Bodega perteneciente a un vendedor. |

# ProductStatus

## Descripcion:

Representa la disponibilidad de un producto del catalogo

Hereda de: DomainCatalog 

### Valores permitidos:

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| PUBLISHED | Publicado | Disponible para los compradores. |
| SUSPENDED | Suspendido | Temporalmente fuera del catálogo. |
| DISCONTINUED | Descontinuado | Producto retirado definitivamente. |

Ciclo de vida

```java
PUBLISHED
    │
    ├──────────────────> SUSPENDED
    │                         │
    │                         └──────────────> PUBLISHED
    │
    └──────────────────> DISCONTINUED

SUSPENDED
    └──────────────────> DISCONTINUED
```

# MovementType

Representa el tipo de modificación realizada sobre las existencias de un inventario.

Hereda de DomainCatalog

Valores permitidos 

| Codidigo | Nombre | Descripción |
| --- | --- | --- |
| INBOUND | Ingreso | Registra la entrada de unidades al inventario. |
| RESERVATION | Reserva | Separa temporalmente unidades para un proceso de compra. |
| SALE_OUTPUT | Salida por venta | Registra la salida de unidades como resultado de una venta. |
| ADJUSTMENT | Ajuste | Modifica las existencias debido a una corrección operativa. |
| RETURN | Devolución | Registra el retorno de unidades provenientes de una devolución. |

# OrderStatus

Representa el estado actual del compromiso comercial formal realizado por un comprador dentro del sistema.

Hereda de 

DomainCatalog

Valores permitidos

| Codidigo | Nombre | Descripción |
| --- | --- | --- |
| PENDING_PAYMENT | Pendiente de pago | El pedido espera la confirmación financiera. |
| PAID | Pagado | El pago fue validado y puede comenzar el proceso de alistamiento. |
| DISPATCHED | Despachado | El pedido físico salió de la bodega. |
| DELIVERED | Entregado | El pedido fue entregado al comprador. |
| FINALIZED | Finalizado | El ciclo comercial del pedido concluyó satisfactoriamente. |

## Ciclo de vida

```java
CarritoDeCompras
       │
       │ confirmación de compra
       ▼
PENDING_PAYMENT
       │
       │ validación del pago
       ▼
     PAID
       │
       │ preparación y salida física
       ▼
  DISPATCHED
       │
       │ entrega al comprador
       ▼
   DELIVERED
       │
       │ cierre del proceso
       ▼
   FINALISED
```

## ShippingStatus

### Descripcion

Representa el estado actual del proceso logistico de un envio 

Hereda de 

DomainCatalog

Valores permitidos

| codigo | nombre | Descripción |
| --- | --- | --- |
| PENDING | Pendiente | Pendiente por envio |
| SHIPPED | Enviado | El envio se a realizado |
| DELIVERED | Entregado | El envio fue entregado a el usuario |
| RETURNED | Devuelto | El envio fue devuelto por el usuario |

## ReturnStatus

### Descripcion

Representa el estado actual de una devolucion

Hereda de 

DomainCatalog

Valores permitidos

| codigo | nombre | Descripción |
| --- | --- | --- |
| Pending | Pendiente | Pendiente por revisar la solicitud de devolución |
| Approved | Aprobado  | Aprobada la solicitud de devolución  |
| Rejected | Rechazado | Rechazada la solicitud de devolución  |

## RefundStatus

### Descripcion

Representa el estado actual de un reembolso.

Hereda de 

DomainCatalog

Valores permitidos

| codigo | nombre | Descripción |
| --- | --- | --- |
| Pending | Reembolso pendiente | La devolucion ya fue aprovada esta pediente por el reembolso |
| Processing | Procedimiento | La orden de reembolso ya se envio. El dinero esta en transito |
| Succeeded | Tuvo exito | Confirmacion que el dinero fue reembolsado al usuario |

# Name

Descripcion

Representa el nombre completo de una persona física o la razón social de una tienda.

Atributos

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| value | String | Nombre legible utilizado en el negocio. |

Enumeraciones Primitivas 

Los siguientes conceptos son enumeraciones simples debido a que representa valores técnicos fijos sin comportamiento de negocio.

Size 

Descripcion: Representa la talla de una variante de producto

Valores

```java
1 XS
2 S
3 M
4 L
5 XL
6 XXL
```

# Notas de Diseño

1. Todos los catálogos de negocio heredan de DomainCatalog.
2. Los Objetos de Valor son inmutables.
3. La igualdad se determina por sus valores y no por la identidad de un objeto.
4. Las Entidades hacen referencia a Objetos de Valor en lugar de utilizar cadenas de texto dispersas.
5. Los valores permitidos provienen de las reglas y estados descritos en la especificación funcional.
6. Este enfoque mejora la mantenibilidad, la consistencia y la alineación con los principios de Domain-Driven Design (DDD).