# 🛒 Order Management API con Stripe

API REST para gestionar productos, pedidos y pagos en línea. Construida con **Spring Boot**, **JWT**, **MySQL** y **Stripe**. Permite a los usuarios autenticarse, realizar pedidos y pagarlos con Stripe. También incluye soporte para webhooks que actualizan automáticamente el estado del pedido cuando el pago es exitoso.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- JPA (Hibernate)
- MySQL (XAMPP)
- Stripe API
- Swagger (OpenAPI 3)
- Ngrok (para pruebas locales de webhooks)
- Spring Mail (opcional)

---

## 🔐 Autenticación

La autenticación se realiza mediante JWT. Al iniciar sesión, el usuario recibe un token que debe incluir en el encabezado de las solicitudes protegidas.

### Endpoint de login

```http
POST /auth/login
```

### Cuerpo de la solicitud

```json
{
  "email": "usuario@mail.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "token": "Bearer eyJhbGciOiJIUzI1...",
  "username": "usuario@mail.com"
}
```

---

## 📦 Funcionalidades principales

- Registro y autenticación de usuarios
- CRUD de productos
- Crear y cancelar pedidos
- Ver historial de pedidos
- Integración con Stripe para pagos
- Webhook de Stripe para actualizar estado del pedido
- Envío de correos de confirmación (opcional)

---

## 💳 Flujo de pago con Stripe

1. El cliente crea un pedido (estado: `PENDING`)
2. Se genera un `PaymentIntent` con Stripe y se guarda en el pedido.
3. Stripe procesa el pago.
4. Stripe envía un evento `payment_intent.succeeded` al webhook.
5. La API actualiza el pedido a estado `PAID` y (opcional) envía un correo.

---

## 📬 Webhook de Stripe

```http
POST /api/v1/stripe/webhook
```

Este endpoint escucha el evento `payment_intent.succeeded` y marca el pedido como pagado.

---

## 🧪 Pruebas

Puedes probar los pagos y webhooks usando:

- [Ngrok](https://ngrok.com): para exponer tu API local
- [Stripe CLI](https://stripe.com/docs/stripe-cli): para simular eventos

---

## 📁 Estructura del proyecto

```
com.shopapi.order_api
├── auth                # Seguridad, JWT y filtros
├── config              # Configuraciones generales (CORS, Swagger, etc.)
├── controller          # Controladores REST
├── dto                 # Objetos de transferencia de datos
├── exceptions          # Manejo de errores personalizados
├── model               # Entidades JPA
├── repository          # Repositorios JPA
├── service             # Lógica de negocio
├── utils               # Clases utilitarias
```

---

## 🛠️ Configuración

Asegúrate de configurar:

- Claves de Stripe (`stripe.secret.key`, `stripe.webhook.secret`)
- Credenciales de base de datos
- Datos SMTP para envío de correos (opcional)

---

## 📚 Documentación Swagger

Disponible en:

```http
http://localhost:8080/api/v1/swagger-ui/index.html
```

---

## 📦 Requisitos

- JDK 17+
- Maven
- MySQL (XAMPP)
- Cuenta en Stripe (modo test)
