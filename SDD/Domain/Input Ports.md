# Input Ports

## Introducción

Los **Input Ports** (puertos de entrada) definen los casos de uso que el Dominio ofrece al exterior.

Cada Input Port es una interfaz que declara una única operación de negocio mediante un método `execute(...)`. Los **adaptadores de entrada** (controladores REST construidos con **Spring Boot**) invocan estos puertos para disparar la lógica de dominio.

El Dominio es el propietario de estas interfaces. Los adaptadores de entrada nunca contienen reglas de negocio: solo reciben la petición, validan el formato de transporte y delegan en el caso de uso correspondiente.

El proyecto se ejecuta sobre **Spring Boot**, con persistencia en **MySQL** y **MongoDB**. Los Input Ports permanecen totalmente ajenos a la infraestructura: no conocen HTTP, JSON, SQL ni documentos de Mongo.

## Regla Arquitectónica

El flujo de dependencias debe ser siempre:

```text
Input Adapter  (@RestController)
      |
      v
Input Port  (interfaz en application.domain.ports.in)
      |
      v
Domain Service  (implementa el Input Port)
      |
      v
Output Port  (application.domain.ports.out)
```

Por ejemplo, para autenticación:

```text
AuthController  (@RestController)
      |
      v
AuthenticateUserUseCase
      |
      v
AuthenticateUserService
      |
      v
UserRepositoryPort  +  PasswordEncoderPort
```

La flecha de dependencia apunta hacia el Dominio: el adaptador depende del Input Port, y el servicio de dominio implementa el Input Port. El Dominio no depende del adaptador.

## Regla General de Parámetros

Los métodos de los Input Ports representan **entradas de casos de uso**. Reciben los datos mínimos necesarios para ejecutar la operación, tal como están especificados en el SDD (identificadores, datos de formulario y valores de dominio como `ProductStatus`, `WarehouseType` o `ShippingStatus`).

Reglas:

- El método de un caso de uso siempre se llama `execute`.
- El retorno es un **Modelo de Dominio** (por ejemplo `Order`, `Product`, `Inventario`), nunca una entidad de persistencia ni un DTO de infraestructura.
- Las fechas de negocio se modelan con tipos de la plataforma (`LocalDate`) y los montos con `BigDecimal`.
- Los campos opcionales se representan como parámetros que el adaptador puede enviar nulos (por ejemplo `deliveryDate` o `refundDate`); el servicio aplica el valor por defecto.
- Un Input Port no debe filtrar detalles de persistencia (SQL, Mongo, JPA): solo define el contrato del caso de uso.

Ejemplo correcto:

```java
public interface ConfirmOrderPaymentUseCase {
    Order execute(String orderId, String paymentReference);
}
```

Los identificadores primitivos son aceptables **en los Input Ports**, porque representan datos que llegan desde el exterior (la petición HTTP). Esto es distinto de los Output Ports, donde el Dominio ya posee el Modelo y debe usarlo.

## Input Ports

### Usuarios

#### RegisterBuyerUseCase

**Responsabilidad**

Registrar un nuevo comprador.

```java
Buyer execute(String name, String documentId, String mail, String password, String mainAddress);
```

**Consumidores:** `BuyerController`.

#### AuthenticateUserUseCase

**Responsabilidad**

Autenticar a un usuario con correo y contraseña.

```java
User execute(String mail, String password);
```

**Consumidores:** `AuthController`.

#### ChangeUserStatusUseCase

**Responsabilidad**

Cambiar el estado de un usuario (activo, bloqueado o suspendido).

```java
User execute(String executorAdminId, String userId, UserStatus newStatus);
```

**Consumidores:** `AdminUserController`.

#### AddSalespersonUseCase

**Responsabilidad**

Registrar un vendedor y su bodega inicial.

```java
User execute(String executorAdminId, String tradeName, String documentId, String mail,
             String wineryName, String typeWinery, String wineryAddress);
```

**Consumidores:** `AdminUserController`.

### Bodegas

#### CreateStoreUseCase

**Responsabilidad**

Crear una bodega (Marketplace o de vendedor).

```java
Store execute(String executorAdminId, String sellerId, String warehouseName,
              WarehouseType warehouseType, String warehouseAddress);
```

**Consumidores:** `StoreController`.

### Catálogo

#### PublishProductUseCase

**Responsabilidad**

Publicar un producto con sus variantes.

```java
Product execute(String productId, String sellerId, ProductType productType, String name,
                String description, BigDecimal basePrice, String storeId, List<Variant> variants);
```

**Consumidores:** `SellerProductController`.

#### UpdateProductUseCase

**Responsabilidad**

Actualizar los datos editables de un producto.

```java
Product execute(String productId, String sellerId, String name, String description, BigDecimal basePrice);
```

**Consumidores:** `SellerProductController`.

#### AddProductVariantUseCase

**Responsabilidad**

Agregar una variante a un producto existente.

```java
Product execute(String productId, String sellerId, String color, String size, String model);
```

**Consumidores:** `SellerProductController`.

#### ChangeProductStatusUseCase

**Responsabilidad**

Cambiar el estado de un producto (publicado, suspendido o descontinuado).

```java
Product execute(String productId, String sellerId, ProductStatus newStatus);
```

**Consumidores:** `SellerProductController`.

### Carrito

#### AddItemToCartUseCase

**Responsabilidad**

Agregar un artículo al carrito del comprador.

```java
ShoppingCart execute(String buyerId, String productId, String variantId, int quantity);
```

**Consumidores:** `CartController`.

#### RemoveItemFromCartUseCase

**Responsabilidad**

Eliminar un artículo del carrito.

```java
ShoppingCart execute(String buyerId, String variantId);
```

**Consumidores:** `CartController`.

#### ConfirmCartUseCase

**Responsabilidad**

Confirmar el carrito y generar el pedido.

```java
Order execute(String buyerId, String cartId);
```

**Consumidores:** `CartController`.

### Pedidos

#### CreateOrderUseCase

**Responsabilidad**

Crear un pedido a partir de un carrito.

```java
Order execute(String buyerId, String cartId);
```

**Consumidores:** `OrderController`.

#### CancelOrderUseCase

**Responsabilidad**

Cancelar un pedido.

```java
Order execute(String orderId, String buyerId, String reason);
```

**Consumidores:** `OrderController`.

#### ConfirmOrderPaymentUseCase

**Responsabilidad**

Confirmar el pago de un pedido.

```java
Order execute(String orderId, String paymentReference);
```

**Consumidores:** `OrderController`.

#### DispatchOrderUseCase

**Responsabilidad**

Despachar un pedido pagado.

```java
Order execute(String orderId, String operatorId);
```

**Consumidores:** `LogisticsOrderController`.

#### FinalizeOrderUseCase

**Responsabilidad**

Finalizar un pedido entregado.

```java
Order execute(String orderId);
```

**Consumidores:** `LogisticsOrderController`.

### Facturación

#### GenerateBillUseCase

**Responsabilidad**

Generar la factura de un pedido pagado.

```java
Bill execute(String orderId);
```

**Consumidores:** `BillingController`.

### Inventario

#### ReserveInventoryUseCase

**Responsabilidad**

Reservar inventario para un producto y bodega.

```java
ReserveResult execute(String productId, String storeId, int quantityReserve);
```

**Consumidores:** `InventoryController`.

#### ReleaseInventoryReservationUseCase

**Responsabilidad**

Liberar una reserva de inventario.

```java
Inventario execute(String productId, String storeId, int quantity);
```

**Consumidores:** `InventoryController`.

#### ConfirmSaleOutputUseCase

**Responsabilidad**

Confirmar la salida de inventario por una venta.

```java
Inventario execute(String productId, String storeId, int quantity, String orderId);
```

**Consumidores:** `InventoryController`.

#### RegisterInventoryEntryUseCase

**Responsabilidad**

Registrar el ingreso de inventario.

```java
Inventario execute(String operatorId, String productId, String storeId, int quantity);
```

**Consumidores:** `InventoryController`.

#### AdjustInventoryUseCase

**Responsabilidad**

Ajustar manualmente el inventario.

```java
Inventario execute(String operatorId, String productId, String storeId, int newAmount, String reason);
```

**Consumidores:** `InventoryController`.

### Logística

#### CreateShipmentUseCase

**Responsabilidad**

Crear el envío asociado a un pedido físico.

```java
Shipment execute(String orderId, long identifier, LocalDate shippingDate);
```

**Consumidores:** `ShipmentController`.

#### UpdateShipmentStatusUseCase

**Responsabilidad**

Actualizar el estado de un envío.

```java
Shipment execute(long shipmentId, String operatorId, ShippingStatus newStatus, LocalDate deliveryDate);
```

**Consumidores:** `ShipmentController`.

### Posventa

#### RequestReturnUseCase

**Responsabilidad**

Solicitar la devolución de un pedido entregado.

```java
Return execute(String orderId, String buyerId, String reason, LocalDate returnDate);
```

**Consumidores:** `ReturnController`.

#### ProcessReturnUseCase

**Responsabilidad**

Procesar y aprobar una devolución.

```java
Return execute(String returnId, String operatorId, String storeId);
```

**Consumidores:** `ReturnController`.

#### ProcessRefundUseCase

**Responsabilidad**

Procesar el reembolso de una devolución aprobada.

```java
Refund execute(String returnId, BigDecimal amount, LocalDate refundDate);
```

**Consumidores:** `RefundController`.

## Organización de Paquetes

```text
domain/
└── ports/
    ├── in/
    │   ├── RegisterBuyerUseCase
    │   ├── AuthenticateUserUseCase
    │   ├── ChangeUserStatusUseCase
    │   ├── AddSalespersonUseCase
    │   ├── CreateStoreUseCase
    │   ├── PublishProductUseCase
    │   ├── UpdateProductUseCase
    │   ├── AddProductVariantUseCase
    │   ├── ChangeProductStatusUseCase
    │   ├── AddItemToCartUseCase
    │   ├── RemoveItemFromCartUseCase
    │   ├── ConfirmCartUseCase
    │   ├── CreateOrderUseCase
    │   ├── CancelOrderUseCase
    │   ├── ConfirmOrderPaymentUseCase
    │   ├── DispatchOrderUseCase
    │   ├── FinalizeOrderUseCase
    │   ├── GenerateBillUseCase
    │   ├── ReserveInventoryUseCase
    │   ├── ReleaseInventoryReservationUseCase
    │   ├── ConfirmSaleOutputUseCase
    │   ├── RegisterInventoryEntryUseCase
    │   ├── AdjustInventoryUseCase
    │   ├── CreateShipmentUseCase
    │   ├── UpdateShipmentStatusUseCase
    │   ├── RequestReturnUseCase
    │   ├── ProcessReturnUseCase
    │   └── ProcessRefundUseCase
    │
    └── out/
        └── ...          (Output Ports)
```

## Mapeo a Adaptadores

Los adaptadores de entrada implementan el transporte (REST) y delegan en el Input Port. El Input Port es implementado por un servicio de dominio.

```text
AuthController  (@RestController)
      |
      v
AuthenticateUserUseCase   (interfaz en domain/ports/in)
      ^
      |
AuthenticateUserService   (@Service, implementa el Input Port)
```

Con **Spring Boot**, los controladores reciben el Input Port por inyección de dependencias (`@RequiredArgsConstructor`) y ejecutan el caso de uso:

```java
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/auth/login")
    public User login(@RequestBody LoginRequest request) {
        return authenticateUserUseCase.execute(request.mail(), request.password());
    }
}
```

Durante la ejecución, a diferencia de los **Input Ports** (que se traducen a un **Adaptador** mediante el patrón **Adapter**), aquí el Input Port es implementado directamente por el servicio de dominio: el adaptador es el controlador y el puerto es la interfaz. La lógica de negocio permanece en el Dominio.

## Reglas Arquitectónicas Finales

Las siguientes reglas son obligatorias:

1. Todos los Input Ports pertenecen al Dominio.
2. Los Input Ports son interfaces.
3. Cada Input Port declara un único método `execute(...)`.
4. Los servicios de dominio implementan los Input Ports.
5. Los adaptadores de entrada (controladores REST) dependen de los Input Ports, nunca de los servicios concretos.
6. Los controladores no contienen lógica de negocio: solo traducen transporte y delegan.
7. Los Input Ports no dependen de HTTP, DTOs de infraestructura, JPA, MySQL ni MongoDB.
8. Un Input Port retorna Modelos de Dominio, nunca entidades de persistencia.
9. Los identificadores primitivos son admisibles en los Input Ports porque representan entradas externas.
10. Los servicios de dominio no dependen entre sí: la coordinación entre casos de uso ocurre en los adaptadores de entrada o mediante eventos, nunca ocultando reglas de negocio en el adaptador.

> **Nota sobre Spring Boot:** los controladores se anotan con `@RestController` y usan `@RequiredArgsConstructor` para inyectar los Input Ports. Los servicios de dominio se anotan con `@Service` únicamente como marcador de inyección de dependencias; la lógica de negocio no importa ni utiliza APIs de Spring, JPA, MySQL o MongoDB.
