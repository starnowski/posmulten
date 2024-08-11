package com.github.starnowski.posmulten.configuration.common.yaml

import java.nio.file.Paths

abstract class AbstractSpecification extends spock.lang.Specification {

    protected String resolveFilePath(String filePath) {
        Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()
    }
}
