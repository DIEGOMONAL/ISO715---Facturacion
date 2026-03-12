package util;

/**
 * Utilidades para validar documentos dominicanos (RNC y Cédula).
 *
 * Las reglas implementadas siguen los algoritmos utilizados en República Dominicana:
 * - RNC: 9 dígitos, con pesos 7 9 8 6 5 4 3 2 y módulo 11.
 * - Cédula: 11 dígitos, con pesos 1 2 alternados y módulo 10.
 */
public class DocumentosRD {

    private static final int[] PESOS_RNC = {7, 9, 8, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CEDULA = {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};

    private DocumentosRD() {
    }

    /**
     * Valida un RNC dominicano.
     *
     * Reglas:
     * - Debe tener 9 dígitos numéricos.
     * - El primer dígito debe ser 1, 4 o 5.
     * - Se multiplican los primeros 8 dígitos por los pesos 7,9,8,6,5,4,3,2.
     * - Se suma el resultado y se obtiene el residuo módulo 11.
     * - Si residuo = 0 -> dígito verificador = 2.
     * - Si residuo = 1 -> dígito verificador = 1.
     * - En otro caso -> dígito verificador = 11 - residuo.
     * - El dígito verificador calculado debe ser igual al noveno dígito.
     */
    public static boolean esRncValido(String rncRaw) {
        if (rncRaw == null) return false;
        String rnc = soloDigitos(rncRaw);
        if (rnc.length() != 9) return false;

        char primera = rnc.charAt(0);
        if (primera != '1' && primera != '4' && primera != '5') return false;

        int suma = 0;
        for (int i = 0; i < 8; i++) {
            int digito = Character.getNumericValue(rnc.charAt(i));
            suma += digito * PESOS_RNC[i];
        }
        int residuo = suma % 11;
        int digitoVerificador;
        if (residuo == 0) {
            digitoVerificador = 2;
        } else if (residuo == 1) {
            digitoVerificador = 1;
        } else {
            digitoVerificador = 11 - residuo;
        }

        int dvReal = Character.getNumericValue(rnc.charAt(8));
        return digitoVerificador == dvReal;
    }

    /**
     * Valida una cédula dominicana.
     *
     * Reglas resumidas:
     * - Debe tener 11 dígitos numéricos.
     * - Se multiplican los 11 dígitos por los pesos 1,2 alternados.
     * - Si un producto es >= 10, se suman sus dígitos (ej. 12 -> 1+2 = 3).
     * - Se suman todos los resultados.
     * - Es válida si la suma es múltiplo de 10 (suma % 10 == 0).
     */
    public static boolean esCedulaValida(String cedulaRaw) {
        if (cedulaRaw == null) return false;
        String cedula = soloDigitos(cedulaRaw);
        if (cedula.length() != 11) return false;

        int suma = 0;
        for (int i = 0; i < 11; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int producto = digito * PESOS_CEDULA[i];
            if (producto >= 10) {
                producto = (producto / 10) + (producto % 10);
            }
            suma += producto;
        }
        return suma % 10 == 0;
    }

    /**
     * Determina si una cadena es un RNC o una cédula válida.
     * Permite que el número venga con o sin guiones.
     */
    public static boolean esRncOCedulaValido(String valor) {
        String soloNums = soloDigitos(valor);
        if (soloNums.length() == 9) {
            return esRncValido(soloNums);
        }
        if (soloNums.length() == 11) {
            return esCedulaValida(soloNums);
        }
        return false;
    }

    private static String soloDigitos(String valor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valor.length(); i++) {
            char c = valor.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
        }
        return sb.toString();
    }
}

