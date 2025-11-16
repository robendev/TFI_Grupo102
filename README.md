# 🐶 TFI Programación II - UTN - Sistema de gestión de Mascotas y Microchips

Este es el Trabajo Final Integrador para la materia Programación II de la Tecnicatura Universitaria en Programación (TUP) de la UTN.

## 👥 INTEGRANTES Y ROLES GRUPO N°102: 

* **Barroeta Victor** – Comisión: 2 – DNI: 95805903: Tester QA y Entities
* **Barrutia Milagro** – Comisión:10 – DNI: 38603968: Base de datos y App Menú
* **Batista Federico** – Comisión:10 – DNI: 38019234: DAO y Entities
* **Benitez Rodrigo** – Comisión:10 – DNI: 38778097: Services y Configuración

---

## 📋 Descripción del Dominio

El proyecto modela la relación entre una **Mascota (Clase A)** y su **Microchip (Clase B)**.

La arquitectura sigue una asociación **unidireccional 1-a-1** (A → B), donde la entidad `Mascota` (A) contiene una referencia al objeto `Microchip` (B). La persistencia en la base de datos se implementa de forma inversa (A ← B), donde la tabla `microchip` (B) contiene la clave foránea (`mascota_id`) que referencia a la tabla `mascota` (A), asegurando la unicidad con una restricción `UNIQUE`.

---

## 🛠️ Tecnologías y Requisitos

Para poder compilar y ejecutar este proyecto, necesitas:

* **Java:** JDK 17 o superior (el TFI recomienda Java 21).
* **Base de Datos:** MySQL Server 8.0 o superior.
* **Driver JDBC:** `mysql-connector-j.jar` (provisto en el repo).
* **IDE (Recomendado):** Apache NetBeans.

---

## 🚀 Instrucciones de Instalación y Ejecución

Estos pasos son necesarios para que el proyecto "corra desde cero".

### 1. Clonar el Repositorio

```bash
git clone [https://github.com/robendev/TFI_Grupo102](https://github.com/robendev/TFI_Grupo102)
cd TFI_Grupo102
```

### 2. Configurar la Base de Datos

La base de datos debe estar creada y con las tablas antes de ejecutar la aplicación Java.

 1. Asegurarse de que el servicio MySQL esté corriendo (ej. desde XAMPP, en el puerto 3306).

 2. Abrir un gestor de MySQL (Workbench, DBeaver, etc.).

 3. Ejecutar el script `sql/creacion_DB.sql` para crear la base de datos tfi_mascotas y las tablas mascota y microchip.

 4. (Opcional) Ejecutar el script `sql/Inserts_TFI.sql` para cargar 10 mascotas y 10 microchips de prueba.

### 3. Configurar el Driver JDBC   

El driver de MySQL (mysql-connector-j-9.5.0.jar) ya está incluido en la carpeta lib/ de este repositorio.

Al abrir el proyecto con NetBeans, la librería debería cargarse automáticamente.

#### Cómo verificar (o arreglarlo si falla):

  1. Abre el proyecto en NetBeans.

  2. Ve a la pestaña `Projects` (Proyectos).

  3. Expande la carpeta `Libraries`.

  4. Deberías ver `mysql-connector-j-9.5.0.jar` listado allí.

  5. **Si no aparece** (o tiene un ícono de error):

   * Haz clic derecho en `Libraries`.

   * Selecciona `Add JAR/Folder....`

   * Navega dentro de la carpeta del proyecto a lib/ y selecciona el archivo `mysql-connector-j-9.5.0.jar`.

### 4. Credenciales de Prueba  
     
El proyecto está configurado por defecto para conectarse con las siguientes credenciales:

 * URL: `jdbc:mysql://localhost:3306/tfi_mascotas`

 * Usuario: `root`

 * Contraseña: `""`(vacía)

Si tus credenciales de MySQL son diferentes, puedes modificarlas directamente en el archivo `config/DatabaseConnection.java`

### 5. Compilar y Ejecutar
 
  1. En NetBeans, haz clic derecho en el archivo `main/Main.java`.

  2. Selecciona `Run File` (Ejecutar Archivo).

  3. ¡La aplicación se ejecutará en la consola!

## 🏛️ Arquitectura del Proyecto

El proyecto sigue una arquitectura de 3 capas bien definida para separar responsabilidades:

  * **config** : Contiene DatabaseConnection.java (conexión) y TransactionManager.java (manejo de commit/rollback).

  * **entities** : Clases de dominio (POJOs) Base, Mascota y Microchip.

  * **dao** : Patrón Data Access Object (MascotaDao, MicrochipDao) que implementan GenericDao y manejan todo el SQL.

  * **service** : Lógica de negocio y orquestación de transacciones (MascotaServiceImpl, MicrochipServiceImpl).

  * **main** : Capa de Vista (consola) que incluye Main (arranque), AppMenu (inyección de dependencias) y MenuHandler (controlador).
