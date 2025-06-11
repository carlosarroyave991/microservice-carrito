# Centralized Exception Handling System

Este documento describe el sistema centralizado de manejo de excepciones y códigos de error implementado en el proyecto para proporcionar una gestión de errores consistente y estructurada.

## Arquitectura del Sistema de Excepciones

El sistema de excepciones sigue los principios de arquitectura limpia:

1. **Capa de Dominio**: Contiene las definiciones de códigos de error y excepciones de negocio
2. **Capa de Infraestructura**: Contiene el manejador global de excepciones que convierte las excepciones en respuestas HTTP

### Componentes Principales

#### 1. Registro de Códigos de Error

- **ErrorCode**: Interfaz que define el contrato para todos los códigos de error
- **ErrorCategory**: Enumeración que agrupa los códigos de error por categorías
- **CommonErrorCode**: Enumeración que implementa `ErrorCode` y proporciona códigos de error comunes
- **ErrorCodeRegistry**: Registro centralizado de todos los códigos de error
- **ErrorCodeUtils**: Utilidades para trabajar con códigos de error

#### 2. Excepciones de Negocio

- **BusinessException**: Clase base para todas las excepciones de negocio
- Excepciones específicas que extienden de `BusinessException`:
    - **BadRequestException**: Para solicitudes con datos inválidos
    - **ValidationException**: Para errores de validación
    - **NotFoundException**: Para recursos no encontrados
    - **UnauthorizedException**: Para accesos no autorizados
    - **SecurityException**: Para violaciones de seguridad
    - **DuplicateResourceException**: Para recursos duplicados
    - **DataAccessException**: Para errores de acceso a datos

#### 3. Manejo Global de Excepciones

- **GlobalExceptionHandler**: Captura todas las excepciones y las convierte en respuestas HTTP estructuradas
- **ErrorResponse**: DTO para respuestas de error con formato consistente

## Uso del Sistema de Excepciones

### Lanzar Excepciones con Códigos de Error Predefinidos

```java
// Usando un código de error predefinido
throw new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND);

// Usando un código de error predefinido con mensaje personalizado
throw new ValidationException(CommonErrorCode.INVALID_EMAIL, "El formato del email es inválido");
```

### Lanzar Excepciones con Compatibilidad hacia Atrás

```java
// Usando el constructor antiguo (compatibilidad hacia atrás)
throw new ValidationException("ERR_EMAIL_INVALID", "El formato del email es inválido");
```

### Usar el Patrón Builder con Lombok

Todas las excepciones ahora soportan el patrón Builder gracias a Lombok:

```java
// Usando el patrón builder para crear una excepción
BusinessException exception = BusinessException.builder()
    .errorCode(CommonErrorCode.INTERNAL_ERROR)
    .code("CUSTOM_CODE")
    .message("Mensaje personalizado")
    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    .build();

// También funciona con las subclases
NotFoundException notFoundException = NotFoundException.builder()
    .errorCode(CommonErrorCode.RESOURCE_NOT_FOUND)
    .message("El recurso solicitado no existe")
    .build();
```

### Registrar Nuevos Códigos de Error

Para añadir nuevos códigos de error específicos de un dominio:

1. Crear una enumeración que implemente `ErrorCode`:

```java
public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_FAILED("ERR_PAYMENT_FAILED", "El pago ha fallado", HttpStatus.BAD_REQUEST.value(), ErrorCategory.BUSINESS),
    INSUFFICIENT_FUNDS("ERR_INSUFFICIENT_FUNDS", "Fondos insuficientes", HttpStatus.BAD_REQUEST.value(), ErrorCategory.BUSINESS);
    
    // Implementación de los métodos de la interfaz ErrorCode
}
```

2. Registrar los nuevos códigos de error:

```java
static {
    // Registrar todos los códigos de error de pagos
    for (PaymentErrorCode errorCode : PaymentErrorCode.values()) {
        ErrorCodeRegistry.register(errorCode);
    }
}
```

### Crear Códigos de Error Personalizados

```java
ErrorCode customErrorCode = ErrorCodeUtils.createCustomErrorCode(
    "ERR_CUSTOM_ERROR",
    "Mensaje de error personalizado",
    HttpStatus.BAD_REQUEST.value(),
    ErrorCategory.BUSINESS
);

throw new BusinessException(customErrorCode);
```

## Categorías de Códigos de Error

- **VALIDATION**: Errores de validación de datos
- **RESOURCE**: Errores relacionados con recursos
- **AUTHENTICATION**: Errores de autenticación
- **AUTHORIZATION**: Errores de autorización
- **BUSINESS**: Errores de lógica de negocio
- **DATA_ACCESS**: Errores de acceso a datos
- **SYSTEM**: Errores del sistema
- **EXTERNAL_SERVICE**: Errores de servicios externos

## Formato de Respuesta de Error

Las excepciones son capturadas por `GlobalExceptionHandler` y convertidas en respuestas JSON con el siguiente formato:

```json
{
  "code": "ERR_CODE",
  "message": "Mensaje descriptivo del error",
  "timestamp": "2023-10-15T14:30:45.123Z",
  "details": {
    // Detalles adicionales del error (opcional)
  }
}
```

## Implementación con Lombok

Las clases de excepción utilizan las siguientes anotaciones de Lombok:

- **@Getter**: Genera automáticamente los métodos getter para todos los campos
- **@SuperBuilder**: Habilita el patrón Builder con soporte para herencia
- **@NoArgsConstructor**: Genera un constructor sin argumentos (necesario para @SuperBuilder)

Beneficios de usar Lombok:
1. **Código más limpio**: Reduce el código boilerplate
2. **Mantenibilidad**: Facilita la adición de nuevos campos
3. **Flexibilidad**: El patrón Builder permite crear excepciones de forma más flexible

## Generación de Documentación

El sistema incluye una utilidad para generar documentación de todos los códigos de error registrados:

```java
String documentation = ErrorCodeUtils.generateMarkdownDocumentation();
```

## Beneficios del Sistema

1. **Centralización**: Todos los códigos de error están definidos en un solo lugar
2. **Consistencia**: Formato uniforme para todas las respuestas de error
3. **Mantenibilidad**: Fácil de mantener y extender
4. **Trazabilidad**: Códigos de error únicos para facilitar la depuración
5. **Separación de Responsabilidades**: Cumple con los principios de arquitectura limpia