### GUI for Posmulten

This is simple GUI application implemented with Swing library.
It generates DDL statements for creation, drop and checking RLS policy.
It uses yaml configuration based on schema for module [configuration-yaml-interpreter](../configuration-parent/configuration-yaml-interpreter)

After building the whole project, you should have an executable jar with dependencies.:

### Running application

To run application, just invoke java program with specific jar

```bash
java -jar .\openwebstart-{project.version}-jar-with-dependencies.jar
```

For example:

```bash
java -jar .\openwebstart-0.8.0-SNAPSHOT-jar-with-dependencies.jar
```

After running application we can see default screen

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/opewebstart/Basic_view.PNG">
</p>

To generate DDL script we just need to enter configuration content in "Yaml configuration" tab and click "Submit"

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/opewebstart/Basic_view_1.PNG">
</p>

The is a possibility to add template parameters, we just need to select "Use template parameters" checkbox

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/opewebstart/Parameters.PNG">
</p>

### Comparing configurations

The application has a feature that allows one to compare two configurations.
For example, you can compare the current and previous configurations, and you will see new changes or changes that were in the previous configuration but do not exist in the current one.

First select the "Compare configurations" checkbox.
Then pass content of the previous configuration to "Previous yaml configuration to compare with" tab.

At the end, just click the "Submit" button.

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/opewebstart/Compare.PNG">
</p>

