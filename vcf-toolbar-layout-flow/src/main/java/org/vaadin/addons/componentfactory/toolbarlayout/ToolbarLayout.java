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

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.shared.HasThemeVariant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A layout that provides a toolbar with an overflow menu.
 */
@SuppressWarnings("serial")
@Tag("vcf-toolbar-layout")
@NpmPackage(value = "@vaadin-component-factory/vcf-toolbar-layout", version = "1.0.0")
@JsModule("@vaadin-component-factory/vcf-toolbar-layout/dist/src/vcf-toolbar-layout.js")
public class ToolbarLayout extends Component implements HasOrderedComponents, HasSize, HasStyle, HasThemeVariant<ToolbarLayoutVariant>
{
    // properties passed to children MenuBar components
    private boolean isOpenHover = false;
    private boolean isDropdownIndicatorShown = true;

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
                menuBar.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
            else
                menuBar.removeThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
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


    public MenuItem addItem(String text, Component icon) {
        MenuBar menuBar = createMenuBar();
        add(menuBar);

        Button button = new Button(text, icon);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return menuBar.addItem(button);
    }

    // ==================================================
    // MenuBar-like API for easy migration from MenuBar
    // ==================================================

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
     * Creates a new {@link MenuItem} component and adds it to the root level of
     * this menu bar. The provided component is added into the created
     * {@link MenuItem}.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
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
     * Creates a new {@link MenuItem} component with the provided click listener
     * and adds it to the root level of this menu bar. The provided component is
     * added into the created {@link MenuItem}.
     * <p>
     * The added {@link MenuItem} component is placed inside a button in the
     * menu bar. If this button overflows the menu bar horizontally, the
     * {@link MenuItem} is moved out of the button, into a context menu openable
     * via an overflow button at the end of the button row.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param component
     *            the component to add inside the added menu item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     */
    public void addItem(Component component,
                            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        component.getElement().addEventListener("click", e -> clickListener.onComponentEvent(new ClickEvent<>(component)));
        addItem(component, clickListener);
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

//    /**
//     * Creates a new {@link MenuItem} component with the provided tooltip text
//     * and adds it to the root level of this menu bar. The provided component is
//     * added into the created {@link MenuItem}.
//     * <p>
//     * The added {@link MenuItem} component is placed inside a button in the
//     * menu bar. If this button overflows the menu bar horizontally, the
//     * {@link MenuItem} is moved out of the button, into a context menu openable
//     * via an overflow button at the end of the button row.
//     * <p>
//     * To add content to the sub menu opened by clicking the root level item,
//     * use {@link MenuItem#getSubMenu()}.
//     *
//     * @param component
//     *            the component to add inside new item
//     * @param tooltipText
//     *            the tooltip text for the new item
//     * @return the added {@link MenuItem} component
//     */
//    public MenuItem addItem(Component component, String tooltipText) {
//        var item = addItem(component);
//        setMenuItemTooltipText(item, tooltipText);
//        return item;
//    }

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

//    /**
//     * Creates a new {@link MenuItem} component with the provided click listener
//     * and the tooltip text and adds it to the root level of this menu bar. The
//     * provided component is added into the created {@link MenuItem}.
//     * <p>
//     * The added {@link MenuItem} component is placed inside a button in the
//     * menu bar. If this button overflows the menu bar horizontally, the
//     * {@link MenuItem} is moved out of the button, into a context menu openable
//     * via an overflow button at the end of the button row.
//     * <p>
//     * To add content to the sub menu opened by clicking the root level item,
//     * use {@link MenuItem#getSubMenu()}.
//     *
//     * @param component
//     *            the component to add inside the added menu item
//     * @param tooltipText
//     *            the tooltip text for the new item
//     * @param clickListener
//     *            the handler for clicking the new item, can be {@code null} to
//     *            not add listener
//     * @return the added {@link MenuItem} component
//     */
//    public MenuItem addItem(Component component, String tooltipText,
//                            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
//        var item = addItem(component, clickListener);
//        setTooltipText(item, tooltipText);
//        return item;
//    }

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

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(isOpenHover);
        menuBar.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
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
