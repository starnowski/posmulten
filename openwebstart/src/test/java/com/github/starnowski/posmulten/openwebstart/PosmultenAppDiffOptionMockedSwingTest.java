package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.Test;

import static com.github.starnowski.posmulten.openwebstart.PosmultenApp.*;
import static com.github.starnowski.posmulten.openwebstart.SharedSchemaContextComparableResultsPanel.CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME;
import static com.github.starnowski.posmulten.openwebstart.SharedSchemaContextComparableResultsPanel.CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME;
import static java.util.Arrays.asList;
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
        window.textBox(CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME).requireText("something new" + "\n" + "new thing" + "\n" + "some def");
        // Error panel should not be visible
//        findPanelFixtureByName(ERROR_PANEL_NAME).requireNotVisible();
        //TODO
    }
}
