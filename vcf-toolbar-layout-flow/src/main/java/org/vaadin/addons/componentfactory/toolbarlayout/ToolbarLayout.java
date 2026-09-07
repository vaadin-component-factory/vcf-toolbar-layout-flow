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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.internal.JacksonUtils;
import tools.jackson.databind.node.ObjectNode;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A layout that provides a toolbar with an overflow menu.
 */
@SuppressWarnings("serial")
@Tag("vcf-toolbar-layout")
@NpmPackage(value = "@vaadin-component-factory/vcf-toolbar-layout", version = "2.1.0")
@JsModule("@vaadin-component-factory/vcf-toolbar-layout/dist/src/vcf-toolbar-layout.js")
// for local testing, copy files from js project to: src/main/resources/META-INF/frontend/
// @JsModule("src/vcf-toolbar-layout.js")
@CssImport("./styles/toolbar-layout-styles.css")
public class ToolbarLayout extends Component implements HasOrderedComponents, HasSize, HasStyle, HasThemeVariant<ToolbarLayoutVariant>
{
    private static final String OVERFLOW_BUTTON_SLOT = "overflow-button";

    // properties passed to children MenuBar components
    private boolean isOpenHover = false;
    private boolean isDropdownIndicatorShown = true;

    private ToolbarLayoutI18n i18n;

    public ToolbarLayout() {
        super();
    }

    public boolean isDropdownIndicatorShown() {
        return isDropdownIndicatorShown;
    }

    public void setDropdownIndicatorShown(boolean isDropdownIndicatorShown) {
        this.isDropdownIndicatorShown = isDropdownIndicatorShown;

        // update existing menu bars
        findAllMenuBars().forEach(menuBar -> {
            if (isDropdownIndicatorShown)
                menuBar.addThemeNames("dropdown-indicators");
            else
                menuBar.removeThemeNames("dropdown-indicators");
        });
    }

    /**
     * Get the delay before the toolbar items are updated after a resize.
     *
     * @return milliseconds to wait before updating the toolbar items
     */
    public int getUpdateDebounceDelay() {
        return getElement().getProperty("updateDebounceDelay", 0);
    }

    /**
     * Sets the delay before the toolbar/overflow items are updated after a resize.
     *
     * @param delay milliseconds to wait before updating the toolbar items
     */
    public void setUpdateDebounceDelay(int delay) {
        getElement().setProperty("updateDebounceDelay", delay);
    }

    // ==================================================
    // MenuBar-like API for easy migration from MenuBar
    // ==================================================

    /**
     * Creates a new {@link MenuItem} component with the provided text content and icon
     * and adds it to the root level of this menu bar.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text, Component icon) {
        MenuBar menuBar = createMenuBar();
        add(menuBar);

        Button button = new Button(text, icon);
        button.addThemeNames("tertiary", "tertiary-inline");
        return menuBar.addItem(button);
    }

    /**
     * Creates a new {@link MenuItem} component with the provided text content
     * and adds it to the root level of this menu bar.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text) {
        MenuBar menuBar = createMenuBar();
        add(menuBar);
        return menuBar.addItem(text);
    }

    /**
     * Adds the given component to the toolbar.
     * <p>
     * Please note, that there is no {@link MenuItem} created. Modify the component directly if necessary.
     * </p>
     * @param component
     *            the component to add inside new item
     */
    public void addItem(Component component) {
        add(component);
    }

    /**
     * Creates a new {@link MenuItem} component with the provided text content
     * and click listener and adds it to the root level of this menu bar.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text,
                            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        MenuItem item = addItem(text);
        item.addClickListener(clickListener);
        return item;
    }

    /**
     * A convenience method to add a component and register a click listener on it. However, if the given component
     * provides a built-in click listener, it is recommended, to use that instead and add the component using
     * {@link #addItem(Component)} (e.g. for {@link Button}.
     * <p>
     * Please note, that there is no {@link MenuItem} created. Modify the component directly if necessary.
     * </p>
     *
     * @param component
     *            the component to add inside the added menu item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends Component> void addItem(T component,
                                              ComponentEventListener<ClickEvent<T>> clickListener) {
        // if the component provides a click listener integration, we use that
        if(component instanceof ClickNotifier cn) {
            cn.addClickListener(clickListener);
        } else {
            component.getElement().addEventListener("click",
                    e -> clickListener.onComponentEvent(new ClickEvent<>(component)));
        }

        add(component);
    }

    /**
     * Creates a new {@link MenuItem} component with the provided text content
     * and the tooltip text and adds it to the root level of this menu bar.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @param tooltipText
     *            the tooltip text for the new item
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text, String tooltipText) {
        var item = addItem(text);
        setMenuItemTooltipText(item, tooltipText);
        return item;
    }

    /**
     * Creates a new {@link MenuItem} component with the provided text content
     * and the tooltip text and click listener and adds it to the root level of
     * this menu bar.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @param tooltipText
     *            the tooltip text for the new item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text, String tooltipText,
                            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        var item = addItem(text, clickListener);
        setMenuItemTooltipText(item, tooltipText);
        return item;
    }

    /**
     * Sets the event which opens the sub menus of the root level buttons.
     *
     * @param openOnHover
     *            {@code true} to make the sub menus open on hover (mouseover),
     *            {@code false} to make them openable by clicking
     */
    public void setOpenOnHover(boolean openOnHover) {
        this.isOpenHover = openOnHover;

        // update existing menu bars
        findAllMenuBars().forEach(menuBar -> menuBar.setOpenOnHover(openOnHover));
    }

    /**
     * Gets whether the sub menus open by clicking or hovering on the root level
     * buttons.
     *
     * @return {@code true} if the sub menus open by hovering on the root level
     *         buttons, {@code false} if they open by clicking
     */
    public boolean isOpenOnHover() {
        return isOpenHover;
    }

    /**
     * Sets reverse collapse order for the menu bar.
     *
     * @param reverseCollapseOrder
     *            If {@code true}, the buttons will be collapsed into the
     *            overflow menu starting from the "start" end of the bar instead
     *            of the "end".
     */
    public void setReverseCollapseOrder(boolean reverseCollapseOrder) {
        getElement().setProperty("reverseCollapse", reverseCollapseOrder);
    }

    /**
     * Gets whether the menu bar uses reverse collapse order.
     *
     * @return {@code true} if the buttons will be collapsed into the overflow
     *         menu starting from the "start" end of the bar instead of the
     *         "end".
     *
     */
    public boolean isReverseCollapseOrder() {
        return getElement().getProperty("reverseCollapse", false);
    }

    /**
     * Provide a custom button to be used as the overflow button. This will replace any other overflow
     * button that may have been set previously.
     * <p>
     * Note that {@link ToolbarLayoutI18n#setMoreOptions(String)} only labels the default overflow
     * button. A custom button keeps whatever accessible name it was given, so an icon-only button
     * should be given one explicitly with
     * {@link com.vaadin.flow.component.HasAriaLabel#setAriaLabel(String)}.
     *
     * @param overflowButton the button to use as the overflow button, or {@code null} to remove any existing overflow button
     *                       and revert to the default overflow button
     */
    public void setOverflowButton(Button overflowButton) {
        // remove current overflow button if it exists
        getChildren()
                .filter(c -> OVERFLOW_BUTTON_SLOT.equals(c.getElement().getAttribute("slot")))
                .forEach(this::remove);

        // add new button if provided
        if (overflowButton != null) {
            // must set slot to overflow-button in order for component to recognize it as the overflow button
            overflowButton.getElement().setAttribute("slot", OVERFLOW_BUTTON_SLOT);
            add(overflowButton);
        }
    }

    /**
     * Gets the internationalization object previously set for this component.
     * <p>
     * NOTE: Updating the instance that is returned from this method will not
     * update the component if not set again using
     * {@link #setI18n(ToolbarLayoutI18n)}
     *
     * @return the i18n object or {@code null} if no i18n object has been set
     */
    public ToolbarLayoutI18n getI18n() {
        return i18n;
    }

    /**
     * Sets the internationalization object for this component.
     * <p>
     * Properties left {@code null} on the given object keep the web component's
     * default value, so a partial object can be used to override a single text.
     *
     * @param i18n
     *            the i18n object, not {@code null}
     */
    public void setI18n(ToolbarLayoutI18n i18n) {
        this.i18n = Objects.requireNonNull(i18n,
                "The i18n properties object should not be null");

        runBeforeClientResponse(ui -> {
            if (i18n == this.i18n) {
                setI18nWithJS();
            }
        });
    }

    private void setI18nWithJS() {
        ObjectNode i18nJson = JacksonUtils.beanToJson(i18n);

        // Assign new I18N object to WC, by merging the existing
        // WC I18N, and the values from the new ToolbarLayoutI18n instance,
        // into an empty object
        getElement().executeJs("this.i18n = Object.assign({}, this.i18n, $0);",
                i18nJson);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Element state is not persisted across attach/detach
        if (this.i18n != null) {
            setI18nWithJS();
        }
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    /**
     * The internationalization properties for {@link ToolbarLayout}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolbarLayoutI18n implements Serializable {
        private String moreOptions;
        private String overflowMenu;

        /**
         * Gets the accessible name of the default overflow button.
         *
         * @return the overflow button aria-label
         */
        public String getMoreOptions() {
            return moreOptions;
        }

        /**
         * Sets the accessible name of the default overflow button.
         * <p>
         * This does not apply to a custom button set with
         * {@link ToolbarLayout#setOverflowButton(Button)}.
         *
         * @param moreOptions
         *            the overflow button aria-label
         * @return this instance for method chaining
         */
        public ToolbarLayoutI18n setMoreOptions(String moreOptions) {
            this.moreOptions = moreOptions;
            return this;
        }

        /**
         * Gets the accessible name of the popup holding the overflowed items.
         *
         * @return the overflow popup aria-label
         */
        public String getOverflowMenu() {
            return overflowMenu;
        }

        /**
         * Sets the accessible name of the popup holding the overflowed items.
         *
         * @param overflowMenu
         *            the overflow popup aria-label
         * @return this instance for method chaining
         */
        public ToolbarLayoutI18n setOverflowMenu(String overflowMenu) {
            this.overflowMenu = overflowMenu;
            return this;
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(isOpenHover);
        menuBar.addThemeNames("dropdown-indicators");
        return menuBar;
    }

    private List<MenuBar> findAllMenuBars() {
        return getChildren()
                .filter(c -> c instanceof MenuBar)
                .map(MenuBar.class::cast)
                .collect(Collectors.toList());
    }

    private void setMenuItemTooltipText(MenuItem item, String tooltipText) {
        findMenuBarParent(item).ifPresentOrElse(
                menuBar -> {
                    menuBar.setTooltipText(item, tooltipText);
                },
                () -> {
                    throw new IllegalStateException("MenuItem is not a child of a MenuBar");
                });
    }

    private Optional<MenuBar> findMenuBarParent(MenuItem menuItem) {
        MenuBar parent = null;

        // loop through the parent components until we find a MenuBar
        Component currentComponent = menuItem;
        while (!(currentComponent instanceof MenuBar) && currentComponent.getParent().isPresent()) {
            currentComponent = currentComponent.getParent().get();
        }

        if (currentComponent instanceof MenuBar) {
            return Optional.of((MenuBar) currentComponent);
        }

        return Optional.empty();
    }

}
