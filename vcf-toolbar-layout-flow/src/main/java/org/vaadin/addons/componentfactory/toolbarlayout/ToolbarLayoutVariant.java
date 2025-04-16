/*
 * Copyright 2025 Vaadin Ltd.
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

import com.vaadin.flow.component.shared.ThemeVariant;

/**
 * Theme variants for {@link ToolbarLayout}.
 */
public enum ToolbarLayoutVariant implements ThemeVariant {

    /**
     * Fixed spacing on the left side of each item in the overflow menu so that the text of each item is aligned.
     * This works for items with, or without, a prefix component/icon.
     */
    FIXED_WIDTH_PREFIX("fixed-width-prefix"),

    /**
     * Icons for {@link com.vaadin.flow.component.button.Button} and {@link com.vaadin.flow.component.menubar.MenuBar}
     * entries are hidden in the overflow menu. The icons are still visible in the toolbar.
     */
    HIDE_ICONS("hide-icons");

    private final String variant;

    ToolbarLayoutVariant(String variant) {
        this.variant = variant;
    }

    /**
     * Gets the variant name.
     *
     * @return variant name
     */
    public String getVariantName() {
        return variant;
    }

}
