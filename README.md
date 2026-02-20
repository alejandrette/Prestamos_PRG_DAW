# 📚 Sistema de Gestión de Biblioteca (Java)

Proyecto desarrollado en Java orientado a la gestión de una biblioteca, enfocado principalmente en:

- ✔ Programación Orientada a Objetos (POO)
- ✔ Uso de excepciones personalizadas
- ✔ Validación de datos
- ✔ Gestión de préstamos y sanciones
- ✔ Manejo seguro de entrada por consola

# 🎯 Objetivo del proyecto

Implementar un sistema capaz de:

- Registrar usuarios
- Gestionar préstamos de libros
- Registrar devoluciones
- Aplicar sanciones automáticas por retraso
- Consultar usuarios y préstamos
- Actualizar sanciones vencidas

Todo ello controlando correctamente los errores mediante excepciones.

# 🏗️ Estructura del proyecto
```bash
prestamos/
│
├── Main.java
├── GestorBiblioteca.java
├── Usuario.java
├── Prestamo.java
├── Utils.java
│
└── usuarioException/
    ├── UsuarioInvalidoException.java
    ├── UsuarioRepetidoException.java
    ├── UsuarioSancionadoException.java
    ├── PrestamoInvalidoException.java
    ├── LibroNoDisponibleException.java
└── prestamoException/
  ├── PrestamoInvalidoException.java
```

📌 Clases principales
👤 Usuario

Representa a un socio de la biblioteca.

Validaciones implementadas:

Nombre no vacío

Email con formato válido

Número de socio con patrón: SOC12345

Fecha de registro no nula

Funcionalidades:

Aplicar sanción por días

Levantar sanción

Comprobar si está sancionado (con auto-actualización)

📖 Prestamo

Representa el préstamo de un libro a un usuario.

Validaciones:

Código libro formato: LIB0001

Título no vacío

Fecha de préstamo válida

No permitir devolución anterior al préstamo

Funcionalidades:

Registrar devolución

Calcular días de retraso

Determinar si está devuelto

Determinar si está retrasado

🏢 GestorBiblioteca

Clase central que gestiona:

Usuarios (máximo 50)

Préstamos (máximo 200)

Funcionalidades:

Registrar usuario (evita duplicados)

Realizar préstamo (verifica sanciones y disponibilidad)

Devolver libro (aplica sanción automática)

Buscar usuario

Mostrar préstamos activos

Mostrar usuarios sancionados

Levantar sanciones vencidas

🛠️ Utils

Clase auxiliar con métodos estáticos:

Formateo de fecha (dd/MM/yyyy)

Conversión segura de String a LocalDate