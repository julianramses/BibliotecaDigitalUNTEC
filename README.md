# Biblioteca Digital UNTEC

**Evaluación Módulo 5 – Desarrollo de Aplicaciones Web Dinámicas Java**  
Alkemy · Java EE · MVC · JSP · Servlets · JDBC · H2

---

##  Descripción

Sistema de gestión de biblioteca digital para la Universidad UNTEC.
Permite administrar libros, usuarios y préstamos a través de una aplicación
web dinámica desarrollada con Java EE siguiendo el patrón de diseño **MVC**.

---

##  Arquitectura MVC

```
com.untec.biblioteca
├── model/          ← CAPA MODEL
│   ├── Libro.java
│   ├── Usuario.java
│   └── Prestamo.java
│
├── dao/            ← CAPA DE ACCESO A DATOS (DAO)
│   ├── ConexionDB.java     (Singleton JDBC)
│   ├── LibroDAO.java       (CRUD de libros)
│   ├── UsuarioDAO.java     (CRUD + autenticación)
│   └── PrestamoDAO.java    (préstamos y devoluciones)
│
└── controller/     ← CAPA CONTROLLER
    ├── LoginServlet.java
    ├── LogoutServlet.java
    ├── DashboardServlet.java
    ├── LibroServlet.java
    ├── PrestamoServlet.java
    └── EncodingFilter.java

src/main/webapp/
├── index.jsp
├── css/style.css
└── WEB-INF/
    ├── web.xml
    └── views/              ← CAPA VIEW (JSP + JSTL)
        ├── login.jsp
        ├── nav-include.jsp
        ├── dashboard.jsp
        ├── libros.jsp
        ├── agregar-libro.jsp
        ├── editar-libro.jsp
        ├── prestamos.jsp
        └── error.jsp
```

---

##  Tecnologías utilizadas

| Tecnología        | Versión  | Uso                              |
|-------------------|----------|----------------------------------|
| Java              | 11+      | Lenguaje principal               |
| Java EE / Servlet | 4.0      | Controladores HTTP               |
| JSP               | 2.3      | Capa de vista                    |
| JSTL              | 1.2      | Tags en vistas (c:forEach, etc.) |
| JDBC              | —        | Acceso a base de datos           |
| H2 Database       | 2.2.224  | BD embebida (uso educativo)      |
| Apache Tomcat     | 9/10     | Servidor de aplicaciones         |
| Maven             | 3.x      | Gestión de dependencias y build  |

---

##  Instrucciones de ejecución en IntelliJ IDEA

### Requisitos previos
- **IntelliJ IDEA Ultimate** (necesario para soporte de Java EE y Tomcat)
- **JDK 11 o superior** instalado
- **Apache Tomcat 9** descargado desde https://tomcat.apache.org/download-90.cgi

---

### Paso 1 – Abrir el proyecto

1. Abre IntelliJ IDEA
2. `File → Open` → selecciona la carpeta `BibliotecaDigitalUNTEC`
3. IntelliJ (No se como es en Eclipse) detectará automáticamente el `pom.xml` (Maven)
4. Clic en **"Load Maven Project"** si aparece el aviso
5. Espera a que descargue las dependencias (barra de progreso inferior)

---

### Paso 2 – Configurar Tomcat (En IntelliJ (Community Edition))

1. Ve a `Run → Edit Configurations`
2. Clic en **`+`** → selecciona **`Tomcat Server → Local`**
3. En la pestaña **Server**:
   - Name: `Tomcat 9 - Biblioteca`
   - Application server: clic en `Configure...` → apunta a tu carpeta de Tomcat
4. En la pestaña **Deployment**:
   - Clic en **`+`** → `Artifact`
   - Selecciona **`biblioteca-digital:war exploded`**
   - Application context: `/biblioteca`
5. Clic en **OK**

---

### Paso 3 – Ejecutar la aplicación

1. En la barra superior selecciona la configuración `Tomcat 9 - Biblioteca`
2. Clic en el botón ▶ **Run** (o `Shift+F10`)
3. IntelliJ compilará el proyecto y desplegará en Tomcat
4. El navegador se abrirá automáticamente en:

```
http://localhost:8080/biblioteca-digital/
```

---

### Paso 4 – Ingresar al sistema

| Rol           | Email                | Contraseña |
|---------------|----------------------|------------|
| Administrador | admin@untec.edu      | admin123   |
| Estudiante    | juan@untec.edu       | juan123    |
| Estudiante    | maria@untec.edu      | maria123   |

---

##  Generar el archivo WAR

### Desde IntelliJ:
```
Build → Build Artifacts → biblioteca-digital:war → Build
```
El archivo `.war` se genera en: `target/biblioteca-digital.war`

### Desde línea de comandos (Maven):
```bash
mvn clean package
```

---

##  Desplegar en Tomcat Manager

1. Accede a: `http://localhost:8080/manager/html`
2. Usuario: `admin` / Contraseña: `admin` (según tu `tomcat-users.xml`)
3. En la sección **"WAR file to deploy"** → selecciona `biblioteca-digital.war`
4. Clic en **Deploy**
5. Accede a: `http://localhost:8080/biblioteca`

---

## ️ Base de datos

El proyecto usa **H2 Database** embebida. No requiere instalación.

- El archivo de datos se crea automáticamente en `./biblioteca_untec_db.mv.db`
- Las tablas y datos de ejemplo se crean al iniciar la aplicación por primera vez
- Para resetear los datos: elimina el archivo `.mv.db` y reinicia

**Tablas creadas automáticamente:**
- `usuarios` – Usuarios del sistema con roles
- `libros` – Catálogo de libros
- `prestamos` – Registro de préstamos y devoluciones

---

## ✅ Funcionalidades implementadas

- [x] Login y logout con gestión de sesiones HTTP
- [x] Dashboard con estadísticas en tiempo real
- [x] Listado completo de libros con disponibilidad
- [x] CRUD de libros (solo administradores)
- [x] Registro de préstamos y devoluciones
- [x] Patrón MVC correctamente separado
- [x] Capa DAO con JDBC y patrón Singleton
- [x] Transacciones JDBC para préstamos/devoluciones
- [x] JSTL: `c:forEach`, `c:if`, `c:choose`, `c:when`, `c:out`
- [x] Control de acceso por rol (ADMIN / ESTUDIANTE)
- [x] Despliegue como `.WAR` en Apache Tomcat
- [x] Filtro de codificación UTF-8
- [x] Página de error personalizada (404/500)

---

##  Entregables

```
BibliotecaDigitalUNTEC/
├── pom.xml
├── README.md                          ← este archivo
├── src/main/java/...                  ← código fuente Java
└── src/main/webapp/...                ← vistas JSP + CSS

target/
└── biblioteca-digital.war             ← WAR generado con mvn package
```

---

*Proyecto desarrollado para la Evaluación del Módulo 5 – Alkemy*
