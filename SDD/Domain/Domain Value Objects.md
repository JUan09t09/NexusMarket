# Objetos de valor del dominio

## Introducción

Los objetos de valor representas conceptos inmutables dentro del dominio de NexusMarket. A diferencia de las entidades, no tienen una identidad propia; en su ligar, se definen en su totalidad por sus atributos.

Estos objetos encapsulan valores de negocio controlados, mejoran la expresividad del dominio y evitan el uso de tipos primitivos o cadenas de texto dispersas a lo largo de la aplicación.

# DomainCatalog (Abstract)

### Descripción:

Representa un catalogo de negocio genérico utilizado a lo largo del dominio del sistema.

Todos los valores controlados del negocio heredan de esta clase, garantizado una estructura consistente en toda la aplicación.

### Atributos:

| Atributo | Tipo | Descripción |
| --- | --- | --- |
| code | String | Identificador único de negocio. |
| name | String | Nombre legible mostrado en la aplicación. |
| descripcion | String | Definición funcional del valor del catálogo. |

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

# WharehousType

## Descripción:

Representa la clasificación de una bodega dentro del Marketplace.

Hereda de: DomainCatalog

### Valores Permitidos:

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| MARKETPLACE | Marketplace | Bodega perteneciente al Marketplace. |
| SELLER | Vendedor | Bodega perteneciente a un vendedor. |

# ProductType

## Descripcion:

Representa el tipo de producto comercializado

Hereda de: DomainCatalog

### Valores permitidos

| Codigo | Nombre | Descripción |
| --- | --- | --- |
| PHYSICAL | Físico | Requiere inventario, almacenamiento y despacho. |
| DIGITAL | Digital | Se entrega electrónicamente tras el pago. |

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

# MovementType

(movimientos afectan las existencias del inve)

# OrderStatus

(ciclo de vida de un pedido)

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