package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;
import org.assertj.swing.edt.GuiActionRunner;

import static org.mockito.Mockito.mock;

public class PosmultenAppDiffOptionMockedSwingTest extends AbstractSwingTest {

    private YamlSharedSchemaContextFactory factory;
    private SharedSchemaContextComparator sharedSchemaContextComparator;

    @Override
    protected PosmultenApp preparePosmultenApp() {
        factory = mock(YamlSharedSchemaContextFactory.class);
        sharedSchemaContextComparator = mock(SharedSchemaContextComparator.class);
        return GuiActionRunner.execute(() -> new PosmultenApp(factory, sharedSchemaContextComparator));
    }
}
