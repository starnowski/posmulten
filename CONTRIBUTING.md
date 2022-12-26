* [Setup project locally](#setup-project-locally)
    * [Setup maven toolchain](#setup-maven-toolchain)
    * [Build project](#build-project)
    * [Run integration test locally](#run-integration-test-locally)
* [Commit message](#commit-message)
* [Branch naming convention](#branch-naming-convention)
* [Issues or suggestions](#issues-or-suggestions)

## Setup project locally
Although main code is compiled to java version 8, the postgresql-core-functional-tests module required java 11.
This module is not deployed to maven central repository. It contains only functional tests for core module.
To build project you need to install maven (or at least setup user repository directory and required environment variables).
There are also two jdk required to install. One for version 8 and second for version 11.

### Setup maven toolchain
For build process maven would require to have configuration of [toolchain](https://maven.apache.org/guides/mini/guide-using-toolchains.html) feature.
In your toolchains.xml file you need to have configuration for you jdk 11.

```xml
<?xml version="1.0" encoding="UTF8"?>
<toolchains>
    <!-- JDK toolchains -->
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>11</version>
        </provides>
        <configuration>
            <jdkHome>/path/to/jdk/11</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
```

**Important**, please bear in mind that your "JAVA_HOME" environment property should point to JDK with version 8!

### Build project
Execute maven wrapper script

On Unix:
```bash
mvnw clean install
```

On Windows:
```powershell
mvnw.cmd clean install
```

### Run integration test locally
TODO

## Commit message
  * Each commit message should start with prefix which contains hash and issue number, for example "#132"
  * Commits which doesn't affect project build status like updating docs files "README.md" (except "README.md" file for configuration module) doesn't have to build by Github action. To do that commit message should contain sufix "[skip ci]" 

## Branch naming convention
Try to name your branch based on type of issue related to it, for example:
    
  * Feature - Prefix "feature/", issue number and some short description, for example:
    
    Branch name for feature related to integration with Travis CI and with number 113.
    "feature/113_travis_integration"
  * Bugfix - Prefix "bugfix/", issue number and some short description, for example:
  
    Branch name for bugfix related to NullPointer exception and with number 227.
    "bugfix/227_nullpointer_exception"
    
## Tests
A developer who adds changes should implement tests that cover added code for any bug or a new feature.
There should be added unit tests for any new code generally (there might be exceptions).
If a developer does not have an idea for unit tests, can always ask about this in a comment for pull-request.

A developer should also implement integration tests if the applied changes for new code or already existing change somehow how the SQL is being generated.
Tests should be implemented in postgresql-core and postgresql-core-functional-tests modules.

    
## Issues or suggestions
Please report any typos or suggestions about document by creating an issue to the github project.

IN PROGRESS - suggestions are welcomed!
Please be in mind that this document is still in progress.
That means that if your have any suggestions or questions about this document or even whole project than please don't hesitate to write about.
If you don't like to report your suggestions as issue than you can also write to me on my [linkedin account](https://pl.linkedin.com/in/szymon-tarnowski-a104b4150) 
 
