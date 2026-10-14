# Output Ports

## Introducción

Los **Output Ports** (puertos de salida) definen los contratos mediante los cuales el Dominio se comunica con recursos externos.

El Dominio es el propietario de estas interfaces. Los servicios de dominio nunca deben depender directamente de:

- MySQL
- MongoDB
- JPA / Hibernate
- Spring Data
- HTTP / REST
- APIs externas
- repositorios de persistencia

Cuando un servicio de dominio necesita información o funcionalidad que está fuera del Dominio, debe hacerlo a través del Output Port correspondiente.

Las implementaciones de estos puertos pertenecen a la **capa de adaptadores** (infraestructura).

El proyecto se ejecuta sobre **Spring Boot** y persiste la información en **MySQL** (datos transaccionales) y **MongoDB** (trazabilidad de movimientos). Aunque Spring Boot es el framework de composición, los servicios de dominio solo conocen las interfaces `application.domain.ports.out.*` y los modelos `application.domain.models.*`.

## Regla Arquitectónica

El flujo de dependencias debe ser siempre:

```text
Domain Service
      |
      v
Output Port  (interfaz en application.domain.ports.out)
      |
      v
Output Adapter  (infraestructura)
      |
      v
Recurso externo  (MySQL / MongoDB / API de pago)
```

Por ejemplo, para el catálogo:

```text
ChangeProductStatusService
      |
      v
ProductRepositoryPort
      |
      v
ProductMySqlAdapter  (@Repository)
      |
      v
MySQL
```

Para la trazabilidad de inventario:

```text
RegisterInventoryEntryService
      |
      v
InventoryMovementRepositoryPort
      |
      v
InventoryMovementMongoAdapter  (@Repository)
      |
      v
MongoDB
```

Las flechas representan dependencias en tiempo de compilación: el servicio depende de la interfaz, y el adaptador depende de la interfaz (la implementa). El servicio nunca importa clases de `org.springframework.data.*`, de `jakarta.persistence.*`, del driver de MySQL ni del driver de MongoDB.

## Regla General de Parámetros

Los métodos de los Output Ports deben trabajar con **Modelos de Dominio**.

No deben recibir DTOs, entidades de persistencia ni **identificadores primitivos** cuando el Modelo de Dominio correspondiente ya existe.

Incorrecto:

```java
Optional<Product> findById(String productId);
```

Correcto:

```java
Optional<Product> findById(Product product);
```

Incorrecto:

```java
boolean existsByIdentification(String documentId);
```

Correcto:

```java
boolean existsByIdentification(User user);
```

Incorrecto:

```java
void updateInventory(String storeId, Integer amount);
```

Correcto:

```java
Inventario save(Inventario inventory);
```

Esto mantiene al Dominio independiente de la representación de persistencia y de transporte.

**Patrón de referencia (probe).** Como los puertos de entrada (casos de uso) reciben identificadores primitivos definidos en el SDD (por ejemplo `productId`, `executorAdminId`), los servicios construyen un Modelo de Dominio "de referencia" con únicamente el campo usado para la búsqueda y lo pasan al puerto. Ejemplo:

```java
Product reference = new Product();
reference.setId(productId);
Product product = productRepositoryPort.findById(reference).orElseThrow(...);
```

El adaptador traduce ese Modelo de Dominio a la consulta SQL o al documento de Mongo correspondiente.

## Output Ports

### 1. UserRepositoryPort

**Responsabilidad**

Brinda al Dominio capacidades de persistencia y consulta para `User`.

**Métodos**

```java
public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByIdentification(User user);

    Optional<User> findByEmail(User user);

    boolean existsByIdentification(User user);

    boolean existsByEmail(User user);

    boolean hasRole(User user, UserRole role);

    boolean isActive(User user);
}
```

**Consumidores principales**

- `AddSalespersonService`
- `AuthenticateUserService`
- `ChangeUserStatusService`
- `RegisterBuyerService`
- `CreateStoreService`
- `AdjustInventoryService`
- `RegisterInventoryEntryService`
- `DispatchOrderService`
- `UpdateShipmentStatusService`
- `ProcessReturnService`

**Adaptador sugerido:** `UserMySqlAdapter` (@Repository) → MySQL.

### 2. StoreRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de bodegas (`Store`), tanto del Marketplace como de vendedores.

**Métodos**

```java
public interface StoreRepositoryPort {

    Store save(Store store);

    Optional<Store> findById(Store store);

    List<Store> findByOwner(Store store);

    boolean existsById(Store store);

    boolean isActive(Store store);
}
```

**Consumidores principales**

- `AddSalespersonService`
- `CreateStoreService`
- `PublishProductService`
- `ReserveInventoryService`

**Adaptador sugerido:** `StoreMySqlAdapter` (@Repository) → MySQL.

### 3. ProductRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de productos del catálogo (`Product`), incluidas sus variantes.

**Métodos**

```java
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Product product);

    List<Product> findBySeller(Product product);

    boolean existsById(Product product);
}
```

**Consumidores principales**

- `AddProductVariantService`
- `ChangeProductStatusService`
- `PublishProductService`
- `UpdateProductService`
- `AddItemToCartService`
- `CreateOrderService`
- `DispatchOrderService`
- `CreateShipmentService`

**Adaptador sugerido:** `ProductMySqlAdapter` (@Repository) → MySQL.

### 4. SellerRepositoryPort

**Responsabilidad**

Permite validar la existencia y habilitación operativa de un vendedor.

**Métodos**

```java
public interface SellerRepositoryPort {

    boolean exists(User seller);

    boolean isActive(User seller);
}
```

**Consumidores principales**

- `AddProductVariantService`
- `ChangeProductStatusService`
- `PublishProductService`
- `UpdateProductService`

**Adaptador sugerido:** `SellerMySqlAdapter` (@Repository) → MySQL.

### 5. CartRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta del carrito de compras (`ShoppingCart`).

**Métodos**

```java
public interface CartRepositoryPort {

    ShoppingCart save(ShoppingCart cart);

    Optional<ShoppingCart> findById(ShoppingCart cart);

    Optional<ShoppingCart> findByBuyer(ShoppingCart cart);
}
```

**Consumidores principales**

- `AddItemToCartService`
- `RemoveItemFromCartService`
- `ConfirmCartService`
- `CreateOrderService`

**Adaptador sugerido:** `CartMySqlAdapter` (@Repository) → MySQL.

### 6. OrderRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de pedidos (`Order`) y de su estado comercial.

**Métodos**

```java
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Order order);

    Optional<Order> findByIdAndBuyer(Order order);
}
```

**Consumidores principales**

- `CancelOrderService`
- `ConfirmOrderPaymentService`
- `CreateOrderService`
- `DispatchOrderService`
- `FinalizeOrderService`
- `GenerateBillService`
- `CreateShipmentService`
- `RequestReturnService`

**Adaptador sugerido:** `OrderMySqlAdapter` (@Repository) → MySQL.

### 7. InventoryRepositoryPort

**Responsabilidad**

Brinda consulta y actualización de las existencias de inventario (`Inventario`) por producto y bodega.

**Métodos**

```java
public interface InventoryRepositoryPort {

    Inventario save(Inventario inventory);

    Optional<Inventario> findByProductAndStore(Inventario inventory);

    List<Inventario> findByProduct(Inventario inventory);

    Optional<Inventario> findById(Inventario inventory);
}
```

**Consumidores principales**

- `AdjustInventoryService`
- `ConfirmSaleOutputService`
- `RegisterInventoryEntryService`
- `ReleaseInventoryReservationService`
- `ReserveInventoryService`
- `ConfirmCartService`
- `CreateOrderService`
- `CancelOrderService`
- `DispatchOrderService`
- `ProcessReturnService`

**Adaptador sugerido:** `InventoryMySqlAdapter` (@Repository) → MySQL.

### 8. InventoryMovementRepositoryPort

**Responsabilidad**

Brinda persistencia para la trazabilidad de los movimientos de inventario (`Motion`): ingresos, reservas, salidas por venta, ajustes y devoluciones.

Es un registro inmutable tipo *append-only*, por lo que su implementación se realiza en **MongoDB**, mientras el Dominio permanece completamente ajeno a Mongo.

**Métodos**

```java
public interface InventoryMovementRepositoryPort {

    Motion save(Motion movement);
}
```

**Consumidores principales**

- `AdjustInventoryService`
- `ConfirmSaleOutputService`
- `RegisterInventoryEntryService`
- `ProcessReturnService`

**Adaptador sugerido:** `InventoryMovementMongoAdapter` (@Repository) → MongoDB.

### 9. BillRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de facturas (`Bill`) asociadas a un pedido pagado.

**Métodos**

```java
public interface BillRepositoryPort {

    Bill save(Bill bill);

    Optional<Bill> findByOrder(Bill bill);

    boolean existsByOrder(Bill bill);
}
```

**Consumidores principales**

- `GenerateBillService`

**Adaptador sugerido:** `BillMySqlAdapter` (@Repository) → MySQL.

### 10. ShipmentRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de envíos (`Shipment`) asociados a pedidos de productos físicos.

**Métodos**

```java
public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(Shipment shipment);

    Optional<Shipment> findByOrder(Shipment shipment);

    boolean existsByOrder(Shipment shipment);
}
```

**Consumidores principales**

- `CreateShipmentService`
- `UpdateShipmentStatusService`
- `DispatchOrderService`

**Adaptador sugerido:** `ShipmentMySqlAdapter` (@Repository) → MySQL.

### 11. ReturnRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de solicitudes de devolución (`Return`).

**Métodos**

```java
public interface ReturnRepositoryPort {

    Return save(Return returnRequest);

    Optional<Return> findById(Return returnRequest);

    Optional<Return> findByOrder(Return returnRequest);

    boolean existsByOrder(Return returnRequest);
}
```

**Consumidores principales**

- `RequestReturnService`
- `ProcessReturnService`
- `FinalizeOrderService`

**Adaptador sugerido:** `ReturnMySqlAdapter` (@Repository) → MySQL.

### 12. RefundRepositoryPort

**Responsabilidad**

Brinda persistencia y consulta de reembolsos (`Refund`) originados en una devolución aprobada.

**Métodos**

```java
public interface RefundRepositoryPort {

    Refund save(Refund refund);

    Optional<Refund> findByReturn(Refund refund);
}
```

**Consumidores principales**

- `ProcessRefundService`

**Adaptador sugerido:** `RefundMySqlAdapter` (@Repository) → MySQL.

### 13. PaymentGatewayPort

**Responsabilidad**

Abstrae la validación de pagos y la ejecución de reembolsos contra una pasarela externa.

El Dominio no debe depender de ninguna librería o SDK de pago.

**Métodos**

```java
public interface PaymentGatewayPort {

    boolean confirmPayment(Order order);

    boolean refundPayment(Refund refund);
}
```

`Order` lleva la `paymentReference` de la transacción y `Refund` lleva el monto a reembolsar, de modo que el puerto trabaja con Modelos de Dominio.

**Consumidores principales**

- `ConfirmOrderPaymentService`
- `ProcessRefundService`

**Adaptador sugerido:** `PaymentGatewayAdapter` (@Component) → API externa de pagos.

### 14. PasswordEncoderPort

**Responsabilidad**

Abstrae el cifrado de contraseñas y su verificación.

El Dominio no debe depender directamente de BCrypt, Argon2 ni de Spring Security.

**Métodos**

```java
public interface PasswordEncoderPort {

    String encode(User user, String rawPassword);

    boolean matches(User user, String rawPassword);
}
```

**Consumidores principales**

- `AuthenticateUserService`

**Adaptador sugerido:** `PasswordSecurityAdapter` (@Component) → BCrypt de Spring Security.

## Organización de Paquetes

El paquete del Dominio contiene:

```text
domain/
└── ports/
    ├── in/
    │   └── ...            (Input Ports - casos de uso)
    │
    └── out/
        ├── UserRepositoryPort
        ├── StoreRepositoryPort
        ├── ProductRepositoryPort
        ├── SellerRepositoryPort
        ├── CartRepositoryPort
        ├── OrderRepositoryPort
        ├── InventoryRepositoryPort
        ├── InventoryMovementRepositoryPort
        ├── BillRepositoryPort
        ├── ShipmentRepositoryPort
        ├── ReturnRepositoryPort
        ├── RefundRepositoryPort
        ├── PaymentGatewayPort
        └── PasswordEncoderPort
```

## Mapeo a Adaptadores

Los adaptadores de salida implementan los puertos del Dominio.

```text
domain/ports/out/
        |
        +-- UserRepositoryPort
        |       ^
        |       |
        |   UserMySqlAdapter                 -> MySQL
        |
        +-- StoreRepositoryPort
        |       ^
        |   StoreMySqlAdapter                -> MySQL
        |
        +-- ProductRepositoryPort
        |       ^
        |   ProductMySqlAdapter              -> MySQL
        |
        +-- SellerRepositoryPort
        |       ^
        |   SellerMySqlAdapter               -> MySQL
        |
        +-- CartRepositoryPort
        |       ^
        |   CartMySqlAdapter                 -> MySQL
        |
        +-- OrderRepositoryPort
        |       ^
        |   OrderMySqlAdapter                -> MySQL
        |
        +-- InventoryRepositoryPort
        |       ^
        |   InventoryMySqlAdapter            -> MySQL
        |
        +-- InventoryMovementRepositoryPort
        |       ^
        |   InventoryMovementMongoAdapter    -> MongoDB
        |
        +-- BillRepositoryPort
        |       ^
        |   BillMySqlAdapter                 -> MySQL
        |
        +-- ShipmentRepositoryPort
        |       ^
        |   ShipmentMySqlAdapter             -> MySQL
        |
        +-- ReturnRepositoryPort
        |       ^
        |   ReturnMySqlAdapter               -> MySQL
        |
        +-- RefundRepositoryPort
        |       ^
        |   RefundMySqlAdapter               -> MySQL
        |
        +-- PaymentGatewayPort
        |       ^
        |   PaymentGatewayAdapter            -> API externa de pagos
        |
        +-- PasswordEncoderPort
                ^
            PasswordSecurityAdapter          -> BCrypt / Spring Security
```

Con Spring Boot, los adaptadores se registran como beans (`@Repository`, `@Component`) y se inyectan en los servicios mediante `@RequiredArgsConstructor`. Los servicios de dominio solo declaran dependencias de tipos que pertenecen a `application.domain.ports.out`.

## Responsabilidad de Base de Datos

Un Puerto no corresponde necesariamente uno a uno con una tabla física. Por ejemplo:

```text
UserRepositoryPort
        |
        v
UserMySqlAdapter
        |
        +-- tabla user
        +-- tabla buyer
        (Buyer es una especialización de User)
```

El Dominio solo conoce `UserRepositoryPort`. No conoce cuántas tablas se usan para persistir un `User` ni que `Buyer` comparte la tabla base. Por lo tanto, el diseño físico de la base de datos puede evolucionar sin modificar el Dominio.

Igualmente, `InventoryMovementRepositoryPort` puede respaldarse en una colección de MongoDB cuyo esquema de documento difiere por completo del modelo de tablas de MySQL, sin que el Dominio lo perciba.

## Relación Servicio ↔ Puerto

**Servicios de Usuarios** (`RegisterBuyerService`, `AuthenticateUserService`, `ChangeUserStatusService`, `AddSalespersonService`) dependen típicamente de:

- `UserRepositoryPort`
- `PasswordEncoderPort`
- `StoreRepositoryPort`

**Servicios de Catálogo** (`PublishProductService`, `UpdateProductService`, `AddProductVariantService`, `ChangeProductStatusService`) dependen típicamente de:

- `ProductRepositoryPort`
- `SellerRepositoryPort`
- `StoreRepositoryPort`

**Servicios de Carrito y Pedidos** (`AddItemToCartService`, `RemoveItemFromCartService`, `ConfirmCartService`, `CreateOrderService`, `CancelOrderService`, `ConfirmOrderPaymentService`, `DispatchOrderService`, `FinalizeOrderService`, `GenerateBillService`) dependen típicamente de:

- `CartRepositoryPort`
- `OrderRepositoryPort`
- `ProductRepositoryPort`
- `InventoryRepositoryPort`
- `PaymentGatewayPort`
- `BillRepositoryPort`

**Servicios de Inventario** (`ReserveInventoryService`, `ReleaseInventoryReservationService`, `ConfirmSaleOutputService`, `RegisterInventoryEntryService`, `AdjustInventoryService`) dependen típicamente de:

- `InventoryRepositoryPort`
- `InventoryMovementRepositoryPort`
- `StoreRepositoryPort`
- `UserRepositoryPort`

**Servicios Logísticos** (`CreateShipmentService`, `UpdateShipmentStatusService`) dependen típicamente de:

- `ShipmentRepositoryPort`
- `OrderRepositoryPort`
- `ProductRepositoryPort`
- `UserRepositoryPort`

**Servicios de Posventa** (`RequestReturnService`, `ProcessReturnService`, `ProcessRefundService`) dependen típicamente de:

- `ReturnRepositoryPort`
- `RefundRepositoryPort`
- `InventoryRepositoryPort`
- `InventoryMovementRepositoryPort`
- `PaymentGatewayPort`
- `OrderRepositoryPort`

## Reglas Arquitectónicas Finales

Las siguientes reglas son obligatorias:

1. Todos los Output Ports pertenecen al Dominio.
2. Los Output Ports son interfaces.
3. Los adaptadores implementan los Output Ports.
4. Los servicios de dominio nunca acceden directamente a repositorios.
5. Los servicios de dominio nunca acceden directamente a la base de datos.
6. Los servicios de dominio nunca acceden directamente a HTTP/REST ni a APIs externas.
7. Los servicios de dominio no dependen de JPA, Hibernate, MySQL, MongoDB ni de Spring Data.
8. Los Puertos deben usar Modelos de Dominio en lugar de DTOs o identificadores primitivos.
9. No se representan relaciones del Dominio con identificadores primitivos cuando un Modelo de Dominio puede representar la relación.
10. Un Puerto se usa solo cuando la información o capacidad requerida es externa al Dominio.
11. No se crea un Puerto solo porque exista otra tabla física. Por ejemplo, `StoreRepositoryPort` existe porque el Dominio necesita consultar bodegas, no porque exista una tabla `store`.
12. Las reglas de negocio evaluables con los Modelos de Dominio permanecen dentro del Dominio.
13. La información externa/configurable se accede a través del Output Port adecuado.
14. Los adaptadores de persistencia traducen entre Modelos de Dominio y representaciones de persistencia (filas de MySQL o documentos de MongoDB).
15. El Dominio debe permanecer totalmente testeable sin base de datos ni infraestructura externa.

> **Nota sobre Spring Boot:** los servicios de dominio se anotan con `@Service` y usan `@RequiredArgsConstructor` (Lombok) únicamente como mecanismo de inyección de dependencias. La lógica de negocio no importa ni utiliza APIs de Spring, JPA, MySQL o MongoDB; solo depende de interfaces de puertos y de Modelos de Dominio.
