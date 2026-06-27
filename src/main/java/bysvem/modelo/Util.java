package bysvem.modelo;

public class Util {
    public static boolean entradaInt(String valor) {
        return valor != null && valor.matches("-?\\d+");
    }

    public static boolean entradaDouble(String valor) {
        return valor != null && valor.matches("-?\\d+(\\.\\d+)?");
    }
}