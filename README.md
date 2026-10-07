# Chuleta examen AED — FileLab

Oct 7, 2026 · @Francisco Rodríguez alfonso

## Path, File y Files

`Path` es solo una ruta y no toca el disco. `Files` sí toca el disco, y por eso lanza `IOException`. `File` es la API antigua (java.io).

| Qué | Path (ruta) | File (antigua) | Files (disco) |
| --- | --- | --- | --- |
| Crear | `Path.of("data", "p.csv")` | `new File("p.csv")` | — |
| Nombre | `getFileName()` (null en la raíz) | `getName()` | — |
| Padre | `getParent()` (null si no hay) | `getParentFile()` | — |
| Existe | — | `exists()`, `isFile()`, `isDirectory()` | `Files.exists(p)` |
| Convertir | `path.toFile()` | `file.toPath()` | — |
| Otros | `toAbsolutePath()`, `normalize()`, `resolve()`, `relativize()`, `isAbsolute()` | — | `createDirectory`, `createDirectories`, `createFile`, `copy`, `move`, `delete`, `size` |

- `createDirectory` falla si faltan carpetas padre o si ya existe. `createDirectories` crea los padres y no falla si ya existe.
- `copy`/`move` fallan si el destino existe, salvo con `StandardCopyOption.REPLACE_EXISTING`.
- `delete` lanza `NoSuchFileException` si no existe y `DirectoryNotEmptyException` si la carpeta tiene contenido. `deleteIfExists` no falla.
- `relativize` mezclando una ruta absoluta y una relativa lanza `IllegalArgumentException`.
- Extensión: `nombre.lastIndexOf('.')`; si es -1 devuelve `""`, si no `substring(punto + 1)`.

## Excepciones, Optional y try-with-resources

La API pública nunca lanza `IOException`: la captura y devuelve `Optional.empty()`, `false`, `""` o `List.of()`.

```java
try {
    return Optional.of(Files.createFile(path));
} catch (IOException e) {          // la concreta, NUNCA Exception
    return Optional.empty();
}
```

- `IOException` es *checked*: obliga a ponerla en `try/catch` o en `throws`. `NullPointerException` e `IllegalArgumentException` son *unchecked*.
- Por qué no `catch (Exception)`: convertiría un bug (NPE, NumberFormatException) en `false` y lo escondería.
- Comprobación de null: si es nulo, NPE; si es un dato inválido, IllegalArgumentException. El null va a la izquierda del `||`, porque el cortocircuito evita la NPE.
- `Optional.of(x)` lanza NPE si x es null. `Optional.ofNullable(x)` devuelve vacío si es null. Para leer el valor: `orElse(valorPorDefecto)`, `isEmpty()`, `get()`.
- `OptionalLong` existe para el primitivo `long` y evita el autoboxing.
- try-with-resources: `try (Reader r = ...) { }` cierra el recurso automáticamente (`AutoCloseable`), aunque salte una excepción. Con varios recursos, se cierran en orden inverso.
- En `readAll`/`writeAll` hay try-with-resources pero no catch, porque la excepción se propaga a `AbstractFileRepository`. Un catch vacío en `readAll` haría que `create` sobrescribiera el fichero solo con el producto nuevo.

## Texto UTF-8 y .properties

Indica siempre `StandardCharsets.UTF_8`. Si la lectura y la escritura no usan la misma codificación, la ñ sale como Ã±.

| Método | Hace |
| --- | --- |
| `Files.writeString(p, txt, UTF_8)` | Escribe un String y sustituye el contenido |
| `Files.readString(p, UTF_8)` | Lee todo el fichero como un String |
| `Files.write(p, lista, UTF_8)` | Escribe una línea por elemento |
| `Files.readAllLines(p, UTF_8)` | Devuelve `List<String>` |
| `writeString(..., StandardOpenOption.APPEND)` | Añade al final; si el fichero no existe falla, salvo que añadas `CREATE` |

Opciones por defecto al escribir: `CREATE`, `TRUNCATE_EXISTING` y `WRITE`. Es decir, crea el fichero o lo vacía. Si pasas opciones propias, las de por defecto dejan de aplicarse.

`.properties` (clase `Properties`). Patrón leer → modificar → guardar:

```java
private Properties cargar() throws IOException {
    Properties props = new Properties();
    if (Files.exists(path)) {                 // sin fichero = vacío (put debe funcionar)
        try (Reader r = Files.newBufferedReader(path, UTF_8)) { props.load(r); }
    }
    return props;
}
private void guardar(Properties props) throws IOException {
    try (Writer w = Files.newBufferedWriter(path, UTF_8)) { props.store(w, null); }
}
```

- `getProperty(k)` devuelve null si no existe, por eso se usa `Optional.ofNullable`.
- `setProperty(k, v)`, `remove(k)` y `stringPropertyNames()` (para `findAll`).
- `put` y `remove` llaman a `cargar()`, cambian la propiedad, llaman a `guardar()` y devuelven `true`. Si salta una IOException, devuelven `false`.

## Repositorio genérico (AbstractFileRepository)

El CRUD se escribe una sola vez en la clase abstracta (patrón Template Method). Cada formato implementa solo `getId`, `readAll` y `writeAll`.

```java
public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {
    protected abstract ID getId(T entity);
    protected abstract List<T> readAll() throws IOException;
    protected abstract void writeAll(List<T> entities) throws IOException;

    public boolean create(T entity) {
        if (entity == null) return false;
        try {
            List<T> lista = new ArrayList<>(readAll());   // copia modificable
            for (T t : lista)
                if (getId(t).equals(getId(entity))) return false;   // duplicado
            lista.add(entity);                             // FUERA del bucle
            writeAll(lista);
            return true;
        } catch (IOException e) { return false; }
    }
}
```

- Patrón de búsqueda: dentro del bucle, `if` con `return`; después del bucle, el `return` de fallo.
- `update` y `delete` usan un for con índice (`set(i, e)` y `remove(i)`) y hacen `return` nada más modificar. Así se evita la ConcurrentModificationException.
- `.equals` y no `==`: los ids son `Long`/`String`, que son objetos. `==` compara referencias, y la caché de `Long` solo cubre de -128 a 127.
- `new ArrayList<>(readAll())`: si `readAll` devuelve una lista inmutable, `add` lanza UnsupportedOperationException, que no se captura.
- `<T, ID>` son genéricos: con `T` sin restricción no se puede llamar a `t.id()`, por eso existe `getId`. Los genéricos no aceptan primitivos: `long` se convierte en `Long` (autoboxing).
- No hay constructor con `super(path)`: cada subclase guarda su propio `path`.
- Fichero inexistente = repositorio vacío (`if (!Files.exists(path)) return new ArrayList<>();`). Sin esto, el primer `create` sobre un fichero nuevo falla, y fue lo que rompía DataBridge.
- Javadoc de `IRepository` (2 puntos de la nota): descripción, `@param` y `@return` explicando qué significa vacío o false. Sin `@throws IOException`.

## CSV, JSON y XML: plantillas readAll/writeAll

|  | CSV | JSON | XML |
| --- | --- | --- | --- |
| Librería | Apache Commons CSV | Jackson `ObjectMapper` | Jackson `XmlMapper` |
| Leer | Bucle con `CSVRecord` y `parseLong`/`parseDouble`/`parseInt` | `readValue(file, new TypeReference<List<P>>(){})` | `readValue(file, DocumentoProductos.class).getProductos()` |
| Escribir | `printRecord(id, nombre, precio, stock)`, en el orden de la cabecera | `writeValue(file, lista)` | `writeValue(file, new DocumentoProductos(lista))` |
| ¿try? | try-with-resources con Reader/Parser y Writer/Printer | No (Jackson abre y cierra) | No |
| Clave | `setHeader().setSkipHeaderRecord(true)` al leer | `TypeReference` por el borrado de tipos | Clase envoltorio, porque el XML necesita una raíz |

```java
// CSV readAll
try (Reader reader = Files.newBufferedReader(path, UTF_8);
     CSVParser parser = inputFormat.parse(reader)) {
    for (CSVRecord fila : parser)
        productos.add(new Producto(Long.parseLong(fila.get("id")), fila.get("nombre"),
            Double.parseDouble(fila.get("precio")), Integer.parseInt(fila.get("stock"))));
}
```

- Borrado de tipos: en tiempo de ejecución no existe `List<Producto>.class`. Las llaves `{}` crean una subclase anónima que conserva el tipo.
- `path.toFile()`, porque `readValue`/`writeValue` aceptan `File` y no `Path`.
- `readValue` lee (fichero → objeto, devuelve algo). `writeValue` escribe (objeto → fichero, es `void`).
- XML: `@JacksonXmlRootElement(localName="productos")` da nombre a la raíz. `@JacksonXmlElementWrapper(useWrapping=false)` y `@JacksonXmlProperty(localName="producto")` dejan una etiqueta `<producto>` por elemento, sin otra envoltura. Hace falta un constructor vacío, getter y setter.
- `getProductos()` se llama sobre el objeto que devuelve `readValue`, no sobre la clase.
- La lista es una variable local, no un atributo: con un atributo se acumularían los productos en cada lectura.
- Escritura segura (opcional): escribir en `Files.createTempFile(carpeta, ...)` y después `Files.move(tmp, path, ATOMIC_MOVE, REPLACE_EXISTING)`. Si el programa se corta a mitad, el original sigue intacto.

## Factory, enum y DataBridge

Una conversión configurada sigue este camino: `.properties` → `ConfiguredDataBridge` → `DataBridgeService` → `RepositoryFactory` → repositorio de origen (`findAll`) → repositorio de destino (`create` de cada producto).

```java
// FileFormat.from: ignora mayúsculas y lanza IllegalArgumentException si no existe
for (FileFormat f : values())
    if (f.name().equalsIgnoreCase(value.trim())) return f;
throw new IllegalArgumentException("Formato no reconocido: " + value);

// RepositoryFactory.create: el único sitio que conoce las clases concretas
return switch (format) {
    case CSV  -> new ProductoCsvRepository(path);
    case JSON -> new ProductoJsonRepository(path);
    case XML  -> new ProductoXmlRepository(path);
};

// DataBridgeService.convert: solo usa la interfaz IProductoRepository
List<Producto> productos = factory.create(origenFmt, origen).findAll();
IProductoRepository destinoRepo = factory.create(destinoFmt, destino);
int convertidos = 0;
for (Producto p : productos) if (destinoRepo.create(p)) convertidos++;
return convertidos;
```

- Patrón Factory y polimorfismo: devuelve `IProductoRepository` y no la clase concreta. Por eso DataBridge no contiene código de CSV, JSON ni XML.
- Inyección de dependencias: `ProductoService` y `DataBridgeService` reciben lo que necesitan por el constructor, así los tests pueden usar un repositorio en memoria.
- `ConfiguredDataBridge.execute()` lee `input.format`, `input.file`, `output.format` y `output.file`. Si falta alguna clave o un formato no es válido (IllegalArgumentException), devuelve 0. Si no, `FileFormat.from` + `Path.of` + `convert`.
- `values()` y `name()` vienen de serie en cualquier enum. `valueOf("CSV")` distingue mayúsculas.

## Errores típicos que cometo

Revisa esta lista antes de entregar cada ejercicio del examen.

- [ ] `catch (IOException e)`, nunca `catch (Exception e)` ni un catch vacío.
- [ ] Guardo lo que devuelve el método: `texto = texto.toLowerCase()`. Los String son inmutables.
- [ ] Llamo a los métodos de instancia sobre un objeto, no sobre la clase (`documento.getProductos()`).
- [ ] Uso `readValue` para leer (devuelve algo) y `writeValue` para escribir (es void, sin return).
- [ ] De Morgan: `!(A || B)` = `!A && !B`. Mejor escribir la condición en positivo: `precio < 0`.
- [ ] El null va a la izquierda: `x == null || x.isBlank()`.
- [ ] `add` va después del bucle de búsqueda, y el `return` de éxito dentro del `if`.
- [ ] Recorro el parámetro, no un atributo vacío. Uso listas locales.
- [ ] Compruebo que el orden de `printRecord` coincide con la cabecera y que no falta ninguna columna (`stock`).
- [ ] `Optional.ofNullable`, y no `of`, cuando el valor puede ser null.
- [ ] Ids con `.equals()`, nunca `==`.
- [ ] No copio código de otra práctica sin comprobar que encaja (`super(path)`).
- [ ] Releo la lista completa de correcciones antes de dar algo por terminado.

Preguntas rápidas:

- `lista.remove(3)` en una `List<Integer>` \[10, 3, 7, 5\] borra por posición el 5 y deja \[10, 3, 7\].
- Empates en el máximo: con `>` se queda el primero, con `>=` el último.
- `APPEND` sin `CREATE` sobre un fichero que no existe lanza NoSuchFileException.
- Constructor privado: clase de utilidad que no se instancia (como `Math`).
