package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
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
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
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
        PosmultenApp frame = GuiActionRunner.execute(() -> new PosmultenApp(factory));
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        frame.setVisible(true);
    }

    @Test
    public void shouldDisplayCreationScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox("configuration").enterText(yaml);
        window.button("submitBtn").click();
        window.textBox("creationScripts").requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
    }

    @Test
    public void shouldDisplayDropScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, "DROP fun"), sqlDef(null, "ALTER TABLE Drop some Fun"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox("configuration").enterText(yaml);
        window.button("submitBtn").click();
        window.textBox("dropScripts").requireText("ALTER TABLE Drop some Fun" + "\n" + "DROP fun");
    }

    @Test
    public void shouldDisplayCheckingScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, null, "Some check1"), sqlDef(null, null, "check1", "check23\naaa"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox("configuration").enterText(yaml);
        window.button("submitBtn").click();
        window.textBox("checkingScripts").requireText("Some check1" + "\n" + "check1" + "\n" + "check23\naaa");
    }

    private SQLDefinition sqlDef(String creationScript, String dropScript, String... checkingScripts)
    {
        SQLDefinition sqlDefinition = Mockito.mock(SQLDefinition.class);
        Mockito.when(sqlDefinition.getCreateScript()).thenReturn(creationScript);
        Mockito.when(sqlDefinition.getDropScript()).thenReturn(dropScript);
        Mockito.when(sqlDefinition.getCheckingStatements()).thenReturn(asList(ofNullable(checkingScripts).orElseGet(() -> new String[0])));
        return sqlDefinition;
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}