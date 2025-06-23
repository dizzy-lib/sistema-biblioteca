package domain.valueObject;

import java.util.Objects;

/**
 * Value Object que representa un RUT chileno válido.
 * Garantiza que solo existan instancias con RUTs válidos en el dominio.
 * Es inmutable y encapsula las reglas de validación y formateo del RUT.
 */
public class DocumentoRut {

    private final String numero;
    private final char digitoVerificador;

    /**
     * Constructor privado para garantizar que solo se creen instancias válidas
     * a través del método factory 'definir'
     */
    private DocumentoRut(String numero, char digitoVerificador) {
        this.numero = numero;
        this.digitoVerificador = digitoVerificador;
    }

    /**
     * Método factory público para crear una instancia de DocumentoRut
     *
     * @param rut RUT en cualquier formato (con o sin puntos y guión)
     * @return DocumentoRut válido
     * @throws IllegalArgumentException si el RUT no es válido
     */
    public static DocumentoRut definir(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            throw new IllegalArgumentException("El RUT no puede ser nulo o vacío");
        }

        return of(rut.trim());
    }

    /**
     * Método privado que ejecuta las validaciones y crea la instancia
     *
     * @param rut RUT a validar
     * @return DocumentoRut válido
     * @throws IllegalArgumentException si el RUT no es válido
     */
    private static DocumentoRut of(String rut) {
        // Limpiar el RUT (remover puntos, guiones y espacios)
        String rutLimpio = limpiarRut(rut);

        // Validar formato básico
        if (!esFormatoValido(rutLimpio)) {
            throw new IllegalArgumentException("Formato de RUT inválido: " + rut + ". Formato esperado: 12.345.678-9");
        }

        // Separar número y dígito verificador
        String numero = rutLimpio.substring(0, rutLimpio.length() - 1);
        char digitoVerificador = rutLimpio.charAt(rutLimpio.length() - 1);

        // Validar que el número sea numérico
        if (!esNumerico(numero)) {
            throw new IllegalArgumentException("El número del RUT debe ser numérico: " + rut);
        }

        // Validar rango del número
        int numeroInt = Integer.parseInt(numero);
        if (numeroInt < 1000000 || numeroInt > 99999999) {
            throw new IllegalArgumentException("El número del RUT debe estar entre 1.000.000 y 99.999.999: " + rut);
        }

        // Validar dígito verificador
        if (!esDigitoVerificadorValido(numeroInt, digitoVerificador)) {
            throw new IllegalArgumentException("Dígito verificador inválido para el RUT: " + rut);
        }

        return new DocumentoRut(numero, digitoVerificador);
    }

    /**
     * Limpia el RUT removiendo puntos, guiones y espacios
     */
    private static String limpiarRut(String rut) {
        return rut.replaceAll("[.\\-\\s]", "").toUpperCase();
    }

    /**
     * Valida que el RUT tenga un formato básico correcto
     */
    private static boolean esFormatoValido(String rutLimpio) {
        return rutLimpio.length() >= 8 && rutLimpio.length() <= 9;
    }

    /**
     * Valida que una cadena sea completamente numérica
     */
    private static boolean esNumerico(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida el dígito verificador usando el algoritmo estándar chileno
     */
    private static boolean esDigitoVerificadorValido(int numero, char digitoVerificador) {
        char digitoCalculado = calcularDigitoVerificador(numero);
        return digitoCalculado == digitoVerificador;
    }

    /**
     * Calcula el dígito verificador usando el algoritmo estándar chileno
     */
    private static char calcularDigitoVerificador(int numero) {
        int suma = 0;
        int multiplicador = 2;

        while (numero > 0) {
            suma += (numero % 10) * multiplicador;
            numero /= 10;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        int digito = 11 - resto;

        if (digito == 11) return '0';
        if (digito == 10) return 'K';
        return (char) ('0' + digito);
    }

    /**
     * Obtiene el número del RUT sin el dígito verificador
     *
     * @return número del RUT
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Obtiene el dígito verificador del RUT
     *
     * @return dígito verificador
     */
    public char getDigitoVerificador() {
        return digitoVerificador;
    }

    /**
     * Obtiene el RUT sin formato (solo números y dígito verificador)
     *
     * @return RUT sin formato
     */
    public String getSinFormato() {
        return numero + digitoVerificador;
    }

    /**
     * Obtiene el RUT formateado con puntos y guión (ej: 12.345.678-9)
     *
     * @return RUT formateado
     */
    public String getFormateado() {
        StringBuilder sb = new StringBuilder();
        String numeroStr = numero;

        // Agregar puntos desde la derecha
        for (int i = numeroStr.length() - 1, contador = 0; i >= 0; i--, contador++) {
            if (contador > 0 && contador % 3 == 0) {
                sb.insert(0, ".");
            }
            sb.insert(0, numeroStr.charAt(i));
        }

        // Agregar guión y dígito verificador
        sb.append("-").append(digitoVerificador);

        return sb.toString();
    }

    /**
     * Representación string por defecto (formateado)
     */
    @Override
    public String toString() {
        return getFormateado();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DocumentoRut that = (DocumentoRut) obj;
        return digitoVerificador == that.digitoVerificador &&
                Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, digitoVerificador);
    }
}
