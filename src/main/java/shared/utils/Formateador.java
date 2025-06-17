package shared.utils;

public class Formateador {
  public static String normalizarString(String texto) {
    if (texto == null) {
      return null;
    }

    // Eliminar espacios iniciales y finales
    String textoLimpio = texto.trim();

    // Si está vacío después del trim, retornar vacío
    if (textoLimpio.isEmpty()) {
      return "";
    }

    // Dividir por espacios (esto maneja múltiples espacios automáticamente)
    String[] palabras = textoLimpio.split("\\s+");

    // StringBuilder para construir el resultado
    StringBuilder resultado = new StringBuilder();

    for (int i = 0; i < palabras.length; i++) {
      String palabra = palabras[i];

      if (!palabra.isEmpty()) {
        // Capitalizar primera letra, resto en minúsculas
        String palabraFormateada = palabra.substring(0, 1).toUpperCase() +
            palabra.substring(1).toLowerCase();

        resultado.append(palabraFormateada);

        if (i < palabras.length - 1) {
          resultado.append(" ");
        }
      }
    }

    return resultado.toString();
  }
}