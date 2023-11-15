package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.github.starnowski.posmulten.openwebstart.PosmultenApp.*;
import static com.github.starnowski.posmulten.openwebstart.SharedSchemaContextComparableResultsPanel.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PosmultenAppDiffOptionMockedSwingTest extends AbstractSwingTest {

    private YamlSharedSchemaContextFactory factory;
    private SharedSchemaContextComparator sharedSchemaContextComparator;

    @Override
    protected PosmultenApp preparePosmultenApp() {
        factory = mock(YamlSharedSchemaContextFactory.class);
        sharedSchemaContextComparator = mock(SharedSchemaContextComparator.class);
        return GuiActionRunner.execute(() -> new PosmultenApp(factory, sharedSchemaContextComparator));
    }

    @Test
    public void shouldDisplayDifferenceForCreationScriptsForTwoDifferentCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml1 = "Some yaml";
        String yaml2 = "Previous yaml";
        ISharedSchemaContext context1 = mock(ISharedSchemaContext.class);
        ISharedSchemaContext context2 = mock(ISharedSchemaContext.class);
        when(factory.build(eq(yaml1), any(DefaultDecoratorContext.class))).thenReturn(context1);
        when(factory.build(eq(yaml2), any(DefaultDecoratorContext.class))).thenReturn(context2);
        when(sharedSchemaContextComparator.diff(eq(context2), eq(context1)))
                .thenReturn(new SharedSchemaContextComparator.SharedSchemaContextComparableResults(
                        new SharedSchemaContextComparator.ComparableResult(asList("left1", "x132"), asList("something new", "new thing", "some def")),
                        null,
                        null));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml1);
        window.checkBox(DIFF_CONFIGURATIONS_CHECK_BOX_NAME).check();
        window.tabbedPane(MAIN_TAB_PANEL_NAME).selectTab(1);
        window.textBox(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).enterText(yaml2);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME).requireText("left1" + "\n" + "x132");
        window.tabbedPane(CREATION_SCRIPTS_TAB_NAME).selectTab(1);
        window.textBox(CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME).requireText("something new" + "\n" + "new thing" + "\n" + "some def");
        // Error panel should not be visible
//        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
        //TODO
    }

    @Test
    public void shouldDisplayDifferenceForDropScriptsForTwoDifferentCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml1 = "Some yaml";
        String yaml2 = "Previous yaml";
        ISharedSchemaContext context1 = mock(ISharedSchemaContext.class);
        ISharedSchemaContext context2 = mock(ISharedSchemaContext.class);
        when(factory.build(eq(yaml1), any(DefaultDecoratorContext.class))).thenReturn(context1);
        when(factory.build(eq(yaml2), any(DefaultDecoratorContext.class))).thenReturn(context2);
        when(sharedSchemaContextComparator.diff(eq(context2), eq(context1)))
                .thenReturn(new SharedSchemaContextComparator.SharedSchemaContextComparableResults(
                       null,
                        new SharedSchemaContextComparator.ComparableResult(asList("drop1", "x132", "some drop"), asList("drop1554", "some fun")),
                        null));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml1);
        window.checkBox(DIFF_CONFIGURATIONS_CHECK_BOX_NAME).check();
        window.tabbedPane(MAIN_TAB_PANEL_NAME).selectTab(1);
        window.textBox(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).enterText(yaml2);

        // WHEN
        window.button("submitBtn").click();

        // THEN

        window.tabbedPane(DIFF_PANEL_NAME).selectTab(DROP_SCRIPTS_TAB_TITLE);
        window.textBox(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME).requireText("drop1" + "\n" + "x132" + "\n" + "some drop");
        window.tabbedPane(DROP_SCRIPTS_TAB_NAME).selectTab(1);
        window.textBox(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME).requireText("drop1554" + "\n" + "some fun");
        // Error panel should not be visible
//        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
        //TODO
    }

    @Test
    public void shouldDisplayDifferenceForCheckingScriptsForTwoDifferentCorrectConfigurationWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml1 = "Some yaml";
        String yaml2 = "Previous yaml";
        ISharedSchemaContext context1 = mock(ISharedSchemaContext.class);
        ISharedSchemaContext context2 = mock(ISharedSchemaContext.class);
        when(factory.build(eq(yaml1), any(DefaultDecoratorContext.class))).thenReturn(context1);
        when(factory.build(eq(yaml2), any(DefaultDecoratorContext.class))).thenReturn(context2);
        when(sharedSchemaContextComparator.diff(eq(context2), eq(context1)))
                .thenReturn(new SharedSchemaContextComparator.SharedSchemaContextComparableResults(
                        null,
                        null,
                        new SharedSchemaContextComparator.ComparableResult(asList("check37", "x132", "some drop"), asList("SELECT 1 FROM ...", "some fun"))));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml1);
        window.checkBox(DIFF_CONFIGURATIONS_CHECK_BOX_NAME).check();
        window.tabbedPane(MAIN_TAB_PANEL_NAME).selectTab(1);
        window.textBox(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).enterText(yaml2);

        // WHEN
        window.button("submitBtn").click();

        // THEN

        window.tabbedPane(DIFF_PANEL_NAME).selectTab(CHECKING_SCRIPTS_TAB_TITLE);
        window.textBox(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME).requireText("check37" + "\n" + "x132" + "\n" + "some drop");
        window.tabbedPane(CHECKING_SCRIPTS_TAB_NAME).selectTab(1);
        window.textBox(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME).requireText("SELECT 1 FROM ..." + "\n" + "some fun");
        // Error panel should not be visible
//        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
        //TODO
    }

    @Test
    public void shouldDisplayErrorsForTwoInvalidConfigurationsWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml1 = "Some yaml";
        String yaml2 = "Previous yaml";
        String exceptionMessage2 = "Some errors for previous configuration";
        String exceptionMessage1 = "Missing grantee in configuration";
        Mockito.when(factory.build(eq(yaml1), any(DefaultDecoratorContext.class))).thenThrow(new MissingRLSGranteeDeclarationException(exceptionMessage1));
        Mockito.when(factory.build(eq(yaml2), any(DefaultDecoratorContext.class))).thenThrow(new RuntimeException(exceptionMessage2));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml1);
        window.checkBox(DIFF_CONFIGURATIONS_CHECK_BOX_NAME).check();
        window.tabbedPane(MAIN_TAB_PANEL_NAME).selectTab(1);
        window.textBox(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).enterText(yaml2);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_TEXTFIELD_NAME).requireText(exceptionMessage1);
        window.tabbedPane(ERROR_TAB_PANEL_NAME).selectTab(1);
        assertThat(window.textBox(ERROR_PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).text()).startsWith(exceptionMessage2).contains("java.lang.RuntimeException:");
    }

    @Test
    public void shouldDisplayErrorsForPreviousConfigurationsWhenClickingSubmitButton() throws SharedSchemaContextBuilderException, InvalidConfigurationException {
        // GIVEN
        String yaml1 = "Some yaml";
        String yaml2 = "Previous yaml";
        ISharedSchemaContext context1 = mock(ISharedSchemaContext.class);
        String exceptionMessage2 = "Some errors for previous configuration";
        Mockito.when(factory.build(eq(yaml1), any(DefaultDecoratorContext.class))).thenReturn(context1);
        Mockito.when(factory.build(eq(yaml2), any(DefaultDecoratorContext.class))).thenThrow(new MissingRLSGranteeDeclarationException(exceptionMessage2));
        window.textBox(CONFIGURATION_TEXTFIELD_NAME).enterText(yaml1);
        window.checkBox(DIFF_CONFIGURATIONS_CHECK_BOX_NAME).check();
        window.tabbedPane(MAIN_TAB_PANEL_NAME).selectTab(1);
        window.textBox(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).enterText(yaml2);

        // WHEN
        window.button("submitBtn").click();

        // THEN
        window.textBox(ERROR_PREVIOUS_CONFIGURATION_TEXTFIELD_NAME).requireText(exceptionMessage2);
    }
}
