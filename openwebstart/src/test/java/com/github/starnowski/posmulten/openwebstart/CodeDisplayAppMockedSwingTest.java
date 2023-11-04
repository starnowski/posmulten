package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CodeDisplayAppMockedSwingTest {

    FrameFixture window;
    YamlSharedSchemaContextFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        factory = Mockito.mock(YamlSharedSchemaContextFactory.class);
        CodeDisplayApp codeDisplayApp = GuiActionRunner.execute(() -> new CodeDisplayApp(factory));
        window = new FrameFixture(codeDisplayApp.getFrame());
        window.show(); // shows the frame to test
    }

    @Test
    public void shouldCopyTextInLabelWhenClickingButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
//        window.textBox("textToCopy").enterText("Some random text");
//        window.button("copyButton").click();
//        window.label("copiedText").requireText("Some random text");
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        window.textBox("configuration").enterText(yaml);

        window.click();

        window.textBox("creationScripts").requireText("XXXX1");
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}
