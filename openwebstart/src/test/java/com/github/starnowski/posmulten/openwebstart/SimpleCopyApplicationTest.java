package com.github.starnowski.posmulten.openwebstart;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCopyApplicationTest {
    private FrameFixture window;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        SimpleCopyApplication frame = GuiActionRunner.execute(() -> new SimpleCopyApplication());
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @Test
    public void shouldCopyTextInLabelWhenClickingButton() {
        window.textBox("textToCopy").enterText("Some random text");
        window.button("copyButton").click();
        window.label("copiedText").requireText("Some random text");
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}