### GUI for Posmulten

This is simple GUI application implemented with Swing library.
It generates DDL statements for creation, drop and checking RLS policy.
It uses yaml configuration based on schema for module [configuration-yaml-interpreter](../configuration-parent/configuration-yaml-interpreter)

After building the whole project, you should have an executable jar with dependencies.:

To run application, just invoke java program with specific jar

```bash
java -jar .\openwebstart-{project.version}-jar-with-dependencies.jar
```

For example:

```bash
java -jar .\openwebstart-0.8.0-SNAPSHOT-jar-with-dependencies.jar
```