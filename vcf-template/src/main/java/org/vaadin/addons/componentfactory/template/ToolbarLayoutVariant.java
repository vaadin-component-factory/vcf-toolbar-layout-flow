package org.vaadin.addons.componentfactory.template;

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
