# Why Is Apache Fory So Fast? Profiling 5 Java Serialization Frameworks

Companion project to the
[blog post](https://www.ej-technologies.com/blog/2026/08/why-is-apache-fory-so-fast/)
on the ej-technologies blog. It serializes a realistic object graph (200 orders with customers,
products, shared references, collections, `BigDecimal`, byte arrays) with Java serialization,
Apache Fory 1.6, Kryo 5.6 and Jackson 2.22 (JSON and CBOR), and profiles each run with JProfiler.

## Running

Requires JDK 25 and a JProfiler 16.2+ installation.

```
./gradlew test -Piterations=2000000     # plain timing runs, no agent
./gradlew profileJavacpu -PjprofilerInstallDir=/path/to/jprofiler
```

Each framework has a `profile<Framework>cpu` and a `profile<Framework>allocation` task
(`profileForycpu`, `profileKryoallocation`, ...), writing snapshots to `build/snapshots-cpu/` and
`build/snapshots-allocation/`.

## Results

| Framework    | Time per round trip | Payload size | Total allocations |
|--------------|--------------------:|-------------:|------------------:|
| Fory         |             3.5 us  |   7,502 bytes |       4.5 GB      |
| Kryo         |             8.9 us  |   7,257 bytes |       4.2 GB      |
| Jackson CBOR |            12.0 us  |   7,819 bytes |       6.5 GB      |
| Jackson JSON |            28.7 us  |   9,814 bytes |       8.8 GB      |
| java.io      |            46.8 us  |   9,152 bytes |      18.4 GB      |

See the blog post for the measurement methodology and the analysis of these numbers.

## Links

- [JProfiler Gradle plugin documentation](https://www.ej-technologies.com/resources/jprofiler/help/doc/commandLine/gradleTasks.html)
- [JProfiler MCP server](https://www.ej-technologies.com/jprofiler/mcp)
- [JProfiler download](https://www.ej-technologies.com/jprofiler/download)
