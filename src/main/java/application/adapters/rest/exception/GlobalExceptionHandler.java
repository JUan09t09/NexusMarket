package application.adapters.rest.exception;

import application.domain.exceptions.BlockedUserException;
import application.domain.exceptions.CartItemNotFoundException;
import application.domain.exceptions.CartNotFoundException;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.InvalidBuyerDataException;
import application.domain.exceptions.InvalidCartQuantityException;
import application.domain.exceptions.InvalidCredentialsException;
import application.domain.exceptions.InvalidMovementQuantityException;
import application.domain.exceptions.InvalidPasswordException;
import application.domain.exceptions.InvalidProductDataException;
import application.domain.exceptions.InvalidRefundAmountException;
import application.domain.exceptions.InvalidReservationQuantityException;
import application.domain.exceptions.InvalidVariantException;
import application.domain.exceptions.InvalidWarehouseException;
import application.domain.exceptions.InventoryNotFoundException;
import application.domain.exceptions.OrderNotFoundException;
import application.domain.exceptions.ProductNotFoundException;
import application.domain.exceptions.ReservationNotFoundException;
import application.domain.exceptions.ShipmentNotFoundException;
import application.domain.exceptions.UnauthorizedBuyerException;
import application.domain.exceptions.UnauthorizedSellerException;
import application.domain.exceptions.UnauthorizedUserException;
import application.domain.exceptions.UserNotFoundException;
import application.domain.exceptions.VariantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Autenticacion fallida [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, requestId);
    }

    @ExceptionHandler({BlockedUserException.class, UnauthorizedBuyerException.class,
            UnauthorizedSellerException.class, UnauthorizedUserException.class})
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            DomainException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Operacion no autorizada [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.FORBIDDEN, ex.getMessage(), request, requestId);
    }

    @ExceptionHandler({CartItemNotFoundException.class, CartNotFoundException.class,
            InventoryNotFoundException.class, OrderNotFoundException.class,
            ProductNotFoundException.class, ReservationNotFoundException.class,
            ShipmentNotFoundException.class, UserNotFoundException.class,
            VariantNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(
            DomainException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Recurso no encontrado [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.NOT_FOUND, ex.getMessage(), request, requestId);
    }

    @ExceptionHandler({InvalidBuyerDataException.class, InvalidPasswordException.class,
            InvalidProductDataException.class, InvalidMovementQuantityException.class,
            InvalidCartQuantityException.class, InvalidReservationQuantityException.class,
            InvalidRefundAmountException.class, InvalidWarehouseException.class,
            InvalidVariantException.class})
    public ResponseEntity<Map<String, Object>> handleInvalidData(
            DomainException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Datos invalidos [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.BAD_REQUEST, ex.getMessage(), request, requestId);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomain(DomainException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Regla de negocio violada [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.CONFLICT, ex.getMessage(), request, requestId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        String requestId = resolveRequestId(request);
        log.warn("Validacion de cuerpo fallida [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), message);
        return body(HttpStatus.BAD_REQUEST, message, request, requestId);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedBody(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Cuerpo JSON malformado [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.BAD_REQUEST, "Malformed JSON request body.", request, requestId);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRouteNotFound(
            NoResourceFoundException ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.warn("Ruta no encontrada [requestId={}] {} {} -> {}",
                requestId, request.getMethod(), request.getRequestURI(), ex.getMessage());
        return body(HttpStatus.NOT_FOUND, "Resource not found: " + request.getRequestURI(), request, requestId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex, HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        log.error("Error inesperado [requestId={}] {} {} -> {}: {}",
                requestId, request.getMethod(), request.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error.", request, requestId);
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private ResponseEntity<Map<String, Object>> body(
            HttpStatus status, String message, HttpServletRequest request, String requestId) {
        Map<String, Object> payload = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "message", message == null ? "" : message,
                "path", request.getRequestURI() == null ? "" : request.getRequestURI(),
                "requestId", requestId);
        return ResponseEntity.status(status).header("X-Request-Id", requestId).body(payload);
    }
}
