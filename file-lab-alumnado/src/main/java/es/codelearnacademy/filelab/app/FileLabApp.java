package es.codelearnacademy.filelab.app;

/**
 * Punto de entrada de la aplicación. Solo muestra un mensaje; la funcionalidad se
 * comprueba con los tests. Constructor privado: clase de utilidad que no se instancia.
 */
public final class FileLabApp {

    private FileLabApp() {
    }

    /**
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        System.out.println("FileLab: completa los bloques indicados en README.md");
    }
}
