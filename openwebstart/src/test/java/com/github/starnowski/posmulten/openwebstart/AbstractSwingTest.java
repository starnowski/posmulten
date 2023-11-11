package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

import static com.github.starnowski.posmulten.openwebstart.ParametersPanel.*;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.mock;

public abstract class AbstractSwingTest {
    protected FrameFixture window;
    protected PosmultenApp tested;
    protected boolean isRunningOnVirtualScreen;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    protected static boolean isRunningOnVirtualScreen() {
        return Boolean.getBoolean("xvfbRunningTests");
    }

    protected JTextComponentFixture findTextComponentFixtureByName(String name) {
        return window.textBox(new GenericTypeMatcher<JTextComponent>(JTextComponent.class) {
            @Override
            protected boolean isMatching(JTextComponent jTextComponent) {
                return name.equals(jTextComponent.getName());
            }
        });
    }

    protected JPanelFixture findPanelFixtureByName(String name) {
        return window.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel panel) {
                return name.equals(panel.getName());
            }
        });
    }

    protected JTabbedPaneFixture findJTabbedPaneFixtureByName(String name) {
        return window.tabbedPane(new GenericTypeMatcher<JTabbedPane>(JTabbedPane.class) {
            @Override
            protected boolean isMatching(JTabbedPane panel) {
                return name.equals(panel.getName());
            }
        });
    }

    protected abstract PosmultenApp preparePosmultenApp();

    @BeforeEach
    public void setUp() {
        tested = GuiActionRunner.execute(() -> preparePosmultenApp());
        //Hack to fix issue for ubuntu and xvfb : org.assertj.swing.exception.ActionFailedException: The component to click is out of the boundaries of the screen
        isRunningOnVirtualScreen = isRunningOnVirtualScreen();
        System.out.println("Is running in headless environment: " + isRunningOnVirtualScreen);
        window = new FrameFixture(tested);
        window.show(); // shows the frame to test
        tested.setLocation(0, 0);
        window.maximize();
    }

    protected <C extends Component, F extends AbstractComponentFixture<F, C, ?>> F getMovedComponent(F fixtureWithComponent) {
        //Hack to fix issue for ubuntu and xvfb : org.assertj.swing.exception.ActionFailedException: The component to click is out of the boundaries of the screen
        if (isRunningOnVirtualScreen) {
//            tested.setLocation(-fixtureWithComponent.target().getX(), -fixtureWithComponent.target().getY());
        }
        return fixtureWithComponent;
    }

    protected void addParameter(int index, String key, String value) {
        getMovedComponent(window.button(ADD_PARAMETER_BTN_NAME)).click();
        getMovedComponent(window.textBox(PARAMETER_KEY_TEXTAREA_NAME_PREFIX + index)).enterText(key);
        getMovedComponent(window.textBox(PARAMETER_VALUE_TEXTAREA_NAME_PREFIX + index)).enterText(value);
    }

    protected SQLDefinition sqlDef(String creationScript, String dropScript, String... checkingScripts) {
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
