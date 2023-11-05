package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;

@Disabled
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
    public void shouldCopyTextInLabelWhenClickingButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
//        window.textBox("textToCopy").enterText("Some random text");
//        window.button("copyButton").click();
//        window.label("copiedText").requireText("Some random text");
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        window.textBox("configuration").enterText(yaml);

        window.button("submitBtn").click();
//        for (ActionListener al : window.button("submitBtn").target().getActionListeners()) {
//            al.actionPerformed(null);
//        }
//        GuiActionRunner.execute(() -> window.button("submitBtn").click());

//        Thread.sleep(20000);
        window.textBox("creationScripts").requireText(yaml);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}
