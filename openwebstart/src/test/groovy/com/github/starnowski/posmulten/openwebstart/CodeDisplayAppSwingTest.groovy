package com.github.starnowski.posmulten.openwebstart

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.fixture.FrameFixture
import org.mockito.Mockito
import spock.lang.Specification

class CodeDisplayAppSwingTest extends Specification {

    FrameFixture window
    YamlSharedSchemaContextFactory factory

    def setup() {
        factory = Mock(YamlSharedSchemaContextFactory)
        CodeDisplayApp app = GuiActionRunner.execute({ new CodeDisplayApp(factory)})
        window = new FrameFixture(app.getFrame())
        window.show() // shows the frame to test
    }

    def "should display results for all section when configuration is valid"() {
        given:
            def yaml = "Some yaml"
            def context = Mock(ISharedSchemaContext)
            factory.build(yaml, _) >> context
            window.textBox("configuration").enterText(yaml)
            context.getSqlDefinitions() >> [ sqlDef("XXXX1")]

        when:
            window.button("Submit").click()

        then:
            window.textBox("creationScripts").requireText("XXXX1")
    }

    def cleanup() {
        window.cleanUp()
    }

    private static SQLDefinition sqlDef(String creationScript){
        SQLDefinition sqlDef = Mockito.mock(SQLDefinition)
        Mockito.when(sqlDef.getCreateScript()).thenReturn(creationScript)
        sqlDef
    }
}
