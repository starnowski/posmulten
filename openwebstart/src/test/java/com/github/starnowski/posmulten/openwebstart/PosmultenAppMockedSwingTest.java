package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.openwebstart.ParametersPanel.PARAMETERS_LABELS_PANEL_NAME;
import static com.github.starnowski.posmulten.openwebstart.ParametersPanel.PARAMETER_REMOVE_BTN_PREFIX;
import static com.github.starnowski.posmulten.openwebstart.PosmultenApp.*;
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

class PosmultenAppMockedSwingTest extends AbstractSwingTest {

    private YamlSharedSchemaContextFactory factory;

    protected PosmultenApp preparePosmultenApp() {
        factory = mock(YamlSharedSchemaContextFactory.class);
        return GuiActionRunner.execute(() -> new PosmultenApp(factory));
    }

    @Test
    public void shouldNotDisplayTextFieldsWithScriptsBeforeSubmittingConfiguration() {
        // THEN
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
        findPanelFixtureByName(PARAMETERS_LABELS_PANEL_NAME).requireNotVisible();
        findJTabbedPaneFixtureByName(DIFF_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayCreationScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        getMovedComponent(window.textBox(CONFIGURATION_TEXTFIELD_NAME)).enterText(yaml);

        // WHEN
        getMovedComponent(window.button("submitBtn")).click();

        // THEN
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayDropScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, "DROP fun"), sqlDef(null, "ALTER TABLE Drop some Fun"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        getMovedComponent(window.textBox(CONFIGURATION_TEXTFIELD_NAME)).enterText(yaml);

        // WHEN
        getMovedComponent(window.button("submitBtn")).click();

        // THEN
        window.tabbedPane(SCRIPTS_PANEL_NAME).selectTab(DROP_SCRIPTS_TAB_NAME);
        window.textBox(DROP_SCRIPTS_TEXTFIELD_NAME).requireText("ALTER TABLE Drop some Fun" + "\n" + "DROP fun");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayCheckingScriptsForCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef(null, null, "Some check1"), sqlDef(null, null, "check1", "check23\naaa"));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        getMovedComponent(window.textBox(CONFIGURATION_TEXTFIELD_NAME)).enterText(yaml);

        // WHEN
        getMovedComponent(window.button("submitBtn")).click();

        // THEN
        window.tabbedPane(SCRIPTS_PANEL_NAME).selectTab(CHECKING_STATEMENTS_SCRIPTS_TAB_NAME);
        window.textBox(CHECKING_SCRIPTS_TEXTFIELD_NAME).requireText("Some check1" + "\n" + "check1" + "\n" + "check23\naaa");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForConfigurationWithInvalidContextWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
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
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForSituationThatThrowsRuntimeExceptionsWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        String exceptionMessage = "Exception during processing";
        Mockito.when(factory.build(eq(yaml), any(DefaultDecoratorContext.class))).thenThrow(new RuntimeException(exceptionMessage));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        assertThat(window.textBox(ERROR_TEXTFIELD_NAME).text()).startsWith(exceptionMessage).contains("java.lang.RuntimeException:");
        // Scripts panel should not be visible
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayErrorsForInvalidConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
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
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireNotVisible();
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
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireVisible();
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
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
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireVisible();
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldDisplayLabelsPanelWhenAtLeastOneParameterIsGoingToBeAdded() {
        // GIVEN
        getMovedComponent(window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).check());
        findPanelFixtureByName(PARAMETERS_LABELS_PANEL_NAME).requireNotVisible();

        // WHEN
        addParameter(0, "{{some_key}}", "value1");

        // THEN
        window.panel(PARAMETERS_LABELS_PANEL_NAME).requireVisible();
    }

    @Test
    public void shouldNotDisplayLabelsPanelWhenAllParametersWhereRemoved() {
        // GIVEN
        getMovedComponent(window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).check());
        addParameter(0, "{{some_key}}", "value1");
        window.panel(PARAMETERS_LABELS_PANEL_NAME).requireVisible();

        // WHEN
        getMovedComponent(window.button(PARAMETER_REMOVE_BTN_PREFIX + 0)).click();

        // THEN
        findPanelFixtureByName(PARAMETERS_LABELS_PANEL_NAME).requireNotVisible();
    }

    @Test
    public void shouldNotPassAnyParametersWhenAfterAddingParametersUncheckCheckbox() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml = "Some yaml";
        ArgumentCaptor<DefaultDecoratorContext> defaultDecoratorContextArgumentCaptor = ArgumentCaptor.forClass(DefaultDecoratorContext.class);
        ISharedSchemaContext context = mock(ISharedSchemaContext.class);
        Mockito.when(factory.build(eq(yaml), defaultDecoratorContextArgumentCaptor.capture())).thenReturn(context);
        List<SQLDefinition> definitions = asList(sqlDef("DEF 1", null), sqlDef("ALTER DEFINIT and Function", null));
        Mockito.when(context.getSqlDefinitions()).thenReturn(definitions);
        getMovedComponent(window.textBox(CONFIGURATION_TEXTFIELD_NAME)).enterText(yaml);
        window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).check();
        //Add parameter index 0
        addParameter(0, "{{some_key}}", "value1");
        addParameter(1, "url", "http://host");
        addParameter(2, "username", "kant");
        addParameter(3, "some key", "Simon");
        window.checkBox(DISPLAY_PARAMETERS_CHECK_BOX_NAME).uncheck();

        // WHEN
        window.button("submitBtn").click();

        // THEN
        assertEquals(defaultDecoratorContextArgumentCaptor.getValue().getReplaceCharactersMap(), new HashMap<>());
        // Scripts panel should be visible
        findJTabbedPaneFixtureByName(SCRIPTS_PANEL_NAME).requireVisible();
        window.textBox(CREATION_SCRIPTS_TEXTFIELD_NAME).requireText("DEF 1" + "\n" + "ALTER DEFINIT and Function");
        // Error panel should not be visible
        findJTabbedPaneFixtureByName(ERROR_TAB_PANEL_NAME).requireNotVisible();
    }
}