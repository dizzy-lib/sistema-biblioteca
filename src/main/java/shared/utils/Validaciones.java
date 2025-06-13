package shared.utils;

import java.util.InputMismatchException;

public class Validaciones {
    /**
     * Método que valída si un string es alfanumérico, puede contener
     * puntos, comas y espacios
     */
    public static void esAlfanumericoFlexible(String input) {
        if (input == null) {
            throw new InputMismatchException("Input vacío, por favor reintentar");
        }

        // Regex que permite letras, números, puntos, comas y espacios
        String regex = "^[a-zA-Z0-9.,\\s]+$";

        if (!input.matches(regex)) {
            throw new InputMismatchException("El texto introducido no es alfanumérico");
        }
    }
}
