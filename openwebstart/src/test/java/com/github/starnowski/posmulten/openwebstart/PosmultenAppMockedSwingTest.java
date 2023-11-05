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

import static org.junit.jupiter.api.Assertions.*;

class PosmultenAppMockedSwingTest {
    private FrameFixture window;
    YamlSharedSchemaContextFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        factory = Mockito.mock(YamlSharedSchemaContextFactory.class);
        PosmultenApp frame = GuiActionRunner.execute(() -> new PosmultenApp());
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        frame.setVisible(true);
    }


    @Test
    public void shouldCopyTextInLabelWhenClickingButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        String yaml = "Some yaml";
//        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
//        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        window.textBox("configuration").enterText(yaml);
        window.button("submitBtn").click();
        window.textBox("creationScripts").requireText(yaml);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}