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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class ToolbarLayoutMenuBarTest {

    private UI ui;
    private ToolbarLayout toolbarLayout;

    @BeforeEach
    public void setUp() {
        ui = new UI();
        UI.setCurrent(ui);
        toolbarLayout = new ToolbarLayout();
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    public void addItemWithText_menuBarUsesTabNavigation() {
        toolbarLayout.addItem("Item");

        assertAllMenuBarsUseTabNavigation();
    }

    @Test
    public void addItemWithIcon_menuBarUsesTabNavigation() {
        toolbarLayout.addItem("Item", VaadinIcon.COG.create());

        assertAllMenuBarsUseTabNavigation();
    }

    @Test
    public void addItemWithClickListener_menuBarUsesTabNavigation() {
        toolbarLayout.addItem("Item", e -> {
        });

        assertAllMenuBarsUseTabNavigation();
    }

    /**
     * Every toolbar item is its own tab stop, so a root level menu bar item
     * must be reachable by tab rather than by the arrow keys of a roving
     * tabindex.
     */
    private void assertAllMenuBarsUseTabNavigation() {
        List<MenuBar> menuBars = toolbarLayout.getChildren()
                .filter(MenuBar.class::isInstance).map(MenuBar.class::cast)
                .toList();

        assertTrue(!menuBars.isEmpty(), "Expected the toolbar to hold a menu bar");
        menuBars.forEach(menuBar -> assertTrue(menuBar.isTabNavigation(),
                "Menu bar should be traversable by tab"));
    }
}
