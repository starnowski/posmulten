package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;

import static com.github.starnowski.posmulten.openwebstart.PosmultenApp.*;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

class PosmultenAppMockedSwingTest {
    YamlSharedSchemaContextFactory factory;
    private FrameFixture window;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    private JTextComponentFixture findTextComponentFixtureByName(String name) {
        return window.textBox(new GenericTypeMatcher<JTextComponent>(JTextComponent.class) {
            @Override
            protected boolean isMatching(JTextComponent jTextComponent) {
                return name.equals(jTextComponent.getName());
            }
        });
    }

    private JPanelFixture findPanelFixtureByName(String name) {
        return window.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel panel) {
                return name.equals(panel.getName());
            }
        });
    }

    @BeforeEach
    public void setUp() {
        factory = Mockito.mock(YamlSharedSchemaContextFactory.class);
        PosmultenApp frame = GuiActionRunner.execute(() -> new PosmultenApp(factory));
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        String osName = System.getProperty("os.name");
        if (osName != null && osName.toLowerCase().contains("linux")) {
            //Hack to fix issue for ubuntu and xvfb : org.assertj.swing.exception.ActionFailedException: The component to click is out of the boundaries of the screen
            frame.setLocation(-100, -100);
        }
    }

    @Test
    public void shouldNotDisplayTextFieldsWithScriptsBeforeSubmittingConfiguration() {
        // THEN
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayCreationScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayDropScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, "DROP fun"), sqlDef(null, "ALTER TABLE Drop some Fun"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(DROP_SCRIPTS_TEXTFIELD_NAME).requireText("ALTER TABLE Drop some Fun" + "\n" + "DROP fun");
        // Error panel should not be visible
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayCheckingScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, null, "Some check1"), sqlDef(null, null, "check1", "check23\naaa"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(CHECKING_SCRIPTS_TEXTFIELD_NAME).requireText("Some check1" + "\n" + "check1" + "\n" + "check23\naaa");
        // Error panel should not be visible
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForInvalidConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        String exceptionMessage = "Missing grantee in configuration";
        ISharedSchemaContext context = Mockito.mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(Mockito.eq(yaml), Mockito.any(DefaultDecoratorContext.class))).thenThrow(new MissingRLSGranteeDeclarationException(exceptionMessage));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_TEXTFIELD_NAME).requireText(exceptionMessage);
        // Scripts panel should not be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    private SQLDefinition sqlDef(String creationScript, String dropScript, String... checkingScripts) {
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