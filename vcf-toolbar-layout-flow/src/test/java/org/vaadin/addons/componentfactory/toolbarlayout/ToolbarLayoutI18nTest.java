/*
 * Copyright 2025 - 2026 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.addons.componentfactory.toolbarlayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.internal.PendingJavaScriptInvocation;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import net.jcip.annotations.NotThreadSafe;
import org.mockito.Mockito;
import org.vaadin.addons.componentfactory.toolbarlayout.ToolbarLayout.ToolbarLayoutI18n;

@NotThreadSafe
public class ToolbarLayoutI18nTest {

    private UI ui;
    private ToolbarLayout toolbarLayout;

    @BeforeEach
    public void setUp() {
        ui = new UI();
        // a session is needed to read back the pending JS invocations
        ui.getInternals().setSession(new AlwaysLockedVaadinSession());
        UI.setCurrent(ui);
        toolbarLayout = new ToolbarLayout();
        ui.add(toolbarLayout);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    public void default_noI18nSet() {
        assertNull(toolbarLayout.getI18n());
    }

    @Test
    public void setI18n_getI18nReturnsSameInstance() {
        ToolbarLayoutI18n i18n = new ToolbarLayoutI18n()
                .setMoreOptions("Plus d'options");

        toolbarLayout.setI18n(i18n);

        assertSame(i18n, toolbarLayout.getI18n());
    }

    @Test
    public void setI18n_null_throws() {
        assertThrows(NullPointerException.class, () -> toolbarLayout.setI18n(null));
    }

    @Test
    public void setI18n_mergesIntoWebComponentI18n() {
        toolbarLayout.setI18n(new ToolbarLayoutI18n()
                .setMoreOptions("Plus d'options")
                .setOverflowMenu("Éléments masqués"));

        String invocation = getSingleI18nInvocation();

        // the assignment must merge, so that keys this add-on does not know
        // about keep their web component defaults
        assertTrue(invocation.contains("this.i18n = Object.assign({}, this.i18n,"),
                invocation);
    }

    @Test
    public void setI18n_partialObject_onlySetKeysAreSent() {
        toolbarLayout.setI18n(
                new ToolbarLayoutI18n().setMoreOptions("Plus d'options"));

        // an unset property must be omitted rather than sent as null, which
        // would otherwise blank out the web component's default
        assertEquals("{\"moreOptions\":\"Plus d'options\"}",
                getSingleI18nParameter());
    }

    @Test
    public void setI18nTwiceBeforeClientResponse_onlyLastValueSent() {
        toolbarLayout.setI18n(new ToolbarLayoutI18n().setMoreOptions("First"));
        toolbarLayout.setI18n(new ToolbarLayoutI18n().setMoreOptions("Second"));

        assertEquals("{\"moreOptions\":\"Second\"}", getSingleI18nParameter());
    }

    @Test
    public void reattach_i18nIsSentAgain() {
        toolbarLayout.setI18n(new ToolbarLayoutI18n().setMoreOptions("Plus d'options"));
        // flush the invocation caused by the initial setI18n
        getSingleI18nParameter();

        ui.remove(toolbarLayout);
        ui.add(toolbarLayout);

        // element state does not survive detach, so it must be re-applied
        assertEquals("{\"moreOptions\":\"Plus d'options\"}",
                getSingleI18nParameter());
    }

    private String getSingleI18nInvocation() {
        List<PendingJavaScriptInvocation> invocations = flushInvocations();
        assertEquals(1, invocations.size(),
                "Expected exactly one i18n invocation");
        return invocations.get(0).getInvocation().getExpression();
    }

    private String getSingleI18nParameter() {
        List<PendingJavaScriptInvocation> invocations = flushInvocations();
        assertEquals(1, invocations.size(),
                "Expected exactly one i18n invocation");
        return invocations.get(0).getInvocation().getParameters().get(0)
                .toString();
    }

    /**
     * A session that reports itself as locked, so that the pending JS
     * invocations can be read back outside a real request.
     */
    private static class AlwaysLockedVaadinSession extends VaadinSession {
        AlwaysLockedVaadinSession() {
            super(mockService());
        }

        private static VaadinService mockService() {
            VaadinService service = Mockito.mock(VaadinService.class);
            Mockito.when(service.getDeploymentConfiguration())
                    .thenReturn(Mockito.mock(DeploymentConfiguration.class));
            return service;
        }

        @Override
        public boolean hasLock() {
            return true;
        }

        @Override
        public void checkHasLock() {
            // always locked
        }
    }

    private List<PendingJavaScriptInvocation> flushInvocations() {
        ui.getInternals().getStateTree()
                .runExecutionsBeforeClientResponse();
        return ui.getInternals().dumpPendingJavaScriptInvocations().stream()
                .filter(invocation -> invocation.getInvocation()
                        .getExpression().contains("this.i18n"))
                .collect(Collectors.toList());
    }
}
