# Plan de Commits

Este archivo describe la secuencia de commits para integrar los cambios de manera lógica y atómica.

---

### Commit 1: feat(reservas): Refactorizar Reserva de record a clase y añadir fecha de vencimiento

Este commit transforma la entidad `Reserva` de un `record` inmutable a una `clase`, permitiendo una mayor flexibilidad. Se añade la lógica para calcular y almacenar una `fechaVencimiento` de 4 días, un requisito funcional clave. La persistencia en CSV se actualiza para guardar y cargar esta nueva información.

```bash
git add src/main/java/domain/entities/Reserva.java
git add src/main/java/application/usecases/DevolverLibroCasoUso.java
git add src/main/java/domain/services/ServicioPrestamos.java
git add src/main/java/infraestructure/repositories/RepositorioReservas.java
git add src/main/java/domain/services/ServicioPersistencia.java
git commit -m "feat(reservas): Refactorizar Reserva de record a clase y añadir fecha de vencimiento"
```

---

### Commit 2: chore(entidades): Implementar equals y hashCode en entidades

Para asegurar el correcto funcionamiento de las entidades (`Libro`, `Usuario`, `Reserva`) dentro de colecciones como `HashMap` y `HashSet`, se implementan los métodos `equals()` y `hashCode()`. Esto mejora la consistencia y previene comportamientos inesperados al comparar objetos.

```bash
git add src/main/java/domain/entities/Libro.java
git add src/main/java/domain/entities/Usuario.java
# La entidad Reserva ya fue modificada en el commit anterior, pero este cambio es lógicamente parte de esta tarea.
git add src/main/java/domain/entities/Reserva.java
git commit -m "chore(entidades): Implementar equals y hashCode en entidades"
```

---

### Commit 3: feat(catalogo): Añadir catálogo de libros únicos usando Set

Se introduce una nueva funcionalidad para mostrar un catálogo de libros donde cada libro (definido por título y autor) aparece una sola vez, sin importar cuántos ejemplares existan. Esto cumple con el requisito de usar `HashSet` (a través de `Collectors.toSet()`) para garantizar la unicidad de los elementos.

```bash
git add src/main/java/domain/valueObject/LibroCatalogoEntry.java
git add src/main/java/interfaces/infraestructure/IRepositorioLibros.java
git add src/main/java/infraestructure/repositories/RepositorioLibros.java
git add src/main/java/presentation/services/BibliotecaApplicationService.java
git add src/main/java/presentation/controller/LibrosTerminalController.java
git add src/main/java/presentation/view/TerminalApplication.java
git commit -m "feat(catalogo): Añadir catálogo de libros únicos usando Set"
```

---

### Commit 4: feat(ui): Implementar vistas ordenadas para entidades

Para mejorar la experiencia de usuario, se implementa el ordenamiento en las listas de libros, usuarios, reservas y en el nuevo catálogo. En lugar de `TreeSet`, se utiliza `list.sort()` con expresiones lambda, cumpliendo el objetivo de mostrar datos ordenados de una manera más fundamental y directa, como se solicitó para alinear el código con un enfoque de aprendizaje progresivo.

```bash
git add src/main/java/presentation/controller/LibrosTerminalController.java
git add src/main/java/presentation/controller/UsuarioTerminalController.java
git add src/main/java/presentation/services/UsuarioApplicationService.java
git add src/main/java/presentation/view/TerminalApplication.java
git commit -m "feat(ui): Implementar vistas ordenadas para entidades"
```

---

### Commit 5: chore: Actualizar .gitignore

Se actualiza el archivo `.gitignore` para excluir archivos generados por herramientas de desarrollo y variables de entorno, manteniendo el repositorio limpio.

```bash
git add .gitignore
git commit -m "chore: Actualizar .gitignore"
```
