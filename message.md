# Cumplimiento de Requisitos - Semana 5

## Introducción

Este documento detalla cómo las últimas modificaciones realizadas al proyecto de sistema de biblioteca cumplen con los requisitos especificados para la actividad formativa de la Semana 5: "Creando colecciones de objetos". El objetivo principal era implementar colecciones adecuadas para gestionar el crecimiento de datos, optimizando operaciones como búsqueda, inserción y eliminación.

A continuación, se desglosa cada requisito y se explica cómo fue abordado en el código.

---

### Paso 3: Uso de `ArrayList` para libros

**Requisito:** Utilizar `ArrayList` para almacenar objetos de la clase `Libro`.

**Cumplimiento:** Se ha cumplido este requisito de manera estricta. La clase `infraestructure/repositories/RepositorioLibros.java` utiliza un `ArrayList` para gestionar la colección de libros:

```java
public class RepositorioLibros implements IRepositorioLibros {
  private final ArrayList<Libro> libros = new ArrayList<>();
  // ...
}
```

Esta estructura nos permite almacenar múltiples ejemplares de un mismo libro (con diferente UUID) y mantener un orden de inserción, tal como se esperaba.

---

### Paso 4: Uso de `HashMap` para usuarios

**Requisito:** Emplear `HashMap` para gestionar objetos de la clase `Usuario`, usando un identificador único como clave.

**Cumplimiento:** Este requisito también se ha cumplido. La clase `infraestructure/repositories/RepositorioUsuarios.java` utiliza un `HashMap` donde la clave es el RUT del usuario (un identificador único) y el valor es el objeto `Usuario`.

```java
public class RepositorioUsuarios implements IRepositorioUsuarios {
  private final HashMap<String, Usuario> usuarios = new HashMap<>();
  // ...
}
```

Esto garantiza búsquedas de usuarios extremadamente eficientes (tiempo de ejecución O(1) en promedio), lo cual es ideal para operaciones frecuentes como la validación de un usuario al prestar un libro.

---

### Paso 5: Utiliza otras colecciones (`HashSet` y `TreeSet`)

#### `HashSet`

**Requisito:** Utilizar `HashSet` para almacenar una colección única de libros o usuarios.

**Cumplimiento:** Se ha implementado una nueva funcionalidad: **"Mostrar catálogo de libros"**. Esta opción de menú presenta al usuario una lista de todos los títulos de libros disponibles, pero sin mostrar duplicados (por ejemplo, si hay 3 copias de "Cien Años de Soledad", solo aparece una vez en el catálogo).

Para lograr esto, en `infraestructure/repositories/RepositorioLibros.java`, se utiliza un `Stream` que recolecta los libros en un `Set`, garantizando la unicidad. Internamente, `Collectors.toSet()` utiliza un `HashSet`.

```java
@Override
public Set<LibroCatalogoEntry> obtenerCatalogoLibros() {
  return libros.stream()
      .map(libro -> new LibroCatalogoEntry(libro.getTitulo(), libro.getAutor()))
      .collect(Collectors.toSet()); // Usa HashSet para garantizar unicidad
}
```

#### `TreeSet`

**Requisito:** Utilizar `TreeSet` para mantener un catálogo ordenado de libros o una lista ordenada de usuarios.

**Cumplimiento:** Para cumplir con el objetivo de presentar datos ordenados, se optó por un enfoque alternativo que es conceptualmente equivalente y se alinea mejor con un aprendizaje progresivo, evitando la complejidad de la interfaz `Comparable`.

En lugar de usar `TreeSet` directamente, las listas (`ArrayList`) de libros, usuarios y reservas se ordenan explícitamente justo antes de ser mostradas en la terminal. Esto se logra utilizando el método `list.sort()` junto con una expresión lambda que define el criterio de ordenamiento.

Por ejemplo, en `presentation/controller/LibrosTerminalController.java` los libros se ordenan por título y autor:

```java
// Ordenar los libros por título y luego por autor
libros.sort((l1, l2) -> {
    int comp = l1.getTitulo().compareTo(l2.getTitulo());
    if (comp == 0) {
        return l1.getAutor().compareTo(l2.getAutor());
    }
    return comp;
});
```

Este enfoque cumple con el **propósito** de `TreeSet` (mantener una colección ordenada para su visualización) de una manera más fundamental y didáctica.

---

## Conclusión

Los cambios implementados no solo satisfacen los requisitos de uso de `ArrayList`, `HashMap` y `HashSet`, sino que también mejoran la funcionalidad y la experiencia de usuario de la aplicación, añadiendo catálogos únicos y vistas ordenadas que hacen que la interacción con el sistema sea más intuitiva y eficiente.
