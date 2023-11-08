package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.openwebstart.ParametersPanel.*;
import static com.github.starnowski.posmulten.openwebstart.PosmultenApp.*;
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

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
        factory = mock(YamlSharedSchemaContextFactory.class);
        PosmultenApp frame = GuiActionRunner.execute(() -> new PosmultenApp(factory));
        //Hack to fix issue for ubuntu and xvfb : org.assertj.swing.exception.ActionFailedException: The component to click is out of the boundaries of the screen
//        frame.setLocation(0, 0);
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        String osName = System.getProperty("os.name");
        if (osName != null && osName.toLowerCase().contains("linux")) {
//            frame.setLocationByPlatform(true);
            frame.setLocation(-600, -100);
            //Hack to fix issue for ubuntu and xvfb : org.assertj.swing.exception.ActionFailedException: The component to click is out of the boundaries of the screen
//            frame.setUndecorated(true);
        } else {
            frame.setLocation(0, 0);
        }
//        window = new FrameFixture(frame);
//        window.show(); // shows the frame to test
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
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
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
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
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
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
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
    public void shouldDisplayErrorsForConfigurationWithInvalidContextWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        String exceptionMessage = "Missing grantee in configuration";
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenThrow(new MissingRLSGranteeDeclarationException(exceptionMessage));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_TEXTFIELD_NAME).requireText(exceptionMessage);
        // Scripts panel should not be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForSituationThatThrowsRuntimeExceptionsWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        String exceptionMessage = "Exception during processing";
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenThrow(new RuntimeException(exceptionMessage));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_TEXTFIELD_NAME).requireText(exceptionMessage);
        // Scripts panel should not be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForInvalidConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException, InterruptedException {
        // GIVEN
        String yaml = "Some yaml";
        List<String> errorMessages = Arrays.asList("Some fields is required", "Exception during processing", "Missing values");
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenThrow(new YamlInvalidSchema(errorMessages));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_TEXTFIELD_NAME).requireText(errorMessages.stream().collect(Collectors.joining("\n")));
        // Scripts panel should not be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldPassCorrectParametersWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ArgumentCaptor<DefaultDecoratorContext> defaultDecoratorContextArgumentCaptor = ArgumentCaptor.forClass(DefaultDecoratorContext.class);
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), defaultDecoratorContextArgumentCaptor.capture())).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);
        window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).check();
        //Add parameter index 0
        addParameter(0, "{{some_key}}", "value1");
        addParameter(1, "url", "http://host");
        addParameter(2, "username", "kant");

        // WHEN
        window.button("submitBtn").click();

        // THEN
        assertEquals(defaultDecoratorContextArgumentCaptor.getValue().getReplaceCharactersMap(), mapBuilder().put("{{some_key}}", "value1").put("url", "http://host").put("username", "kant").build());
        // Scripts panel should be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireVisible();
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldPassCorrectParametersEvenWhenSomeParametersWereRemovedWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ArgumentCaptor<DefaultDecoratorContext> defaultDecoratorContextArgumentCaptor = ArgumentCaptor.forClass(DefaultDecoratorContext.class);
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), defaultDecoratorContextArgumentCaptor.capture())).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);
        window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).check();
        //Add parameter index 0
        addParameter(0, "{{some_key}}", "value1");
        addParameter(1, "url", "http://host");
        addParameter(2, "username", "kant");
        addParameter(3, "some key", "Simon");
        window.button(PARAMETER_REMOVE_BTN_PREFIX + 1).click();

        // WHEN
        window.button("submitBtn").click();

        // THEN
        assertEquals(defaultDecoratorContextArgumentCaptor.getValue().getReplaceCharactersMap(), mapBuilder().put("{{some_key}}", "value1").put("username", "kant").put("some key", "Simon").build());
        // Scripts panel should be visible
        findPanelFixtureByName(SCRIPTS_PANEL_NAME).requireVisible();
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
    }

    private void addParameter(int index, String key, String value) {
        window.button(ADD_PARAMETER_BTN_NAME).click();
        window.textBox(PARAMETER_KEY_TEXTAREA_NAME_PREFIX + index).enterText(key);
        window.textBox(PARAMETER_VALUE_TEXTAREA_NAME_PREFIX + index).enterText(value);
    }

    private SQLDefinition sqlDef(String creationScript, String dropScript, String... checkingScripts) {
        SQLDefinition sqlDefinition = mock(SQLDefinition.class);
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