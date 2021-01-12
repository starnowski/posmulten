package com.github.starnowski.posmulten.configuration


import spock.lang.Specification
import spock.lang.Unroll

class FileExtensionExtractorTest extends Specification {

    def tested = new FileExtensionExtractor()

    @Unroll
    def "should return expected extension (#expectedExtension) for file path '#filePath'"()
    {
        expect:
            expectedExtension == tested.extract(filePath)

        where:
            filePath                        ||  expectedExtension
            "file.yml"                      ||  "yml"
            "somePath/file.yml"             ||  "yml"
            "C:/Program Files/config.yaml"  ||  "yaml"
            "/home/cfg.xml"                 ||  "xml"
            "/var/cfg.XML"                  ||  "XML"
            ""                              ||  null
            "     "                         ||  null
            null                            ||  null
    }
}
