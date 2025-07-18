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
package org.vaadin.addons.componentfactory.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.componentfactory.demo.helpers.AbstractDemoView;
import org.vaadin.addons.componentfactory.toolbarlayout.ToolbarLayout;
import org.vaadin.addons.componentfactory.toolbarlayout.ToolbarLayoutVariant;

/**
 * View for {@link ToolbarLayout} demo.
 *
 * @author Vaadin Ltd
 */
@SuppressWarnings("serial")
@Route("")
public class ToolbarLayoutDemoView extends AbstractDemoView {

    @Override
    protected void initView() {
        add(new H1("Use examples"));
        createBasicDemo();
        createMultiComponentDemo();
        createCustomOverflowButtonDemo();
        createReverseCollapseDemo();
        createDebounceDelayDemo();
        createMultiLevelDemo();
        createFixedWidthPrefixDemo();
        createHideIconsDemo();
        createMenuBarApiDemo();

        // show the helper methods used in the demos
        addCard("Demo Helper Methods", new Span("Below are some of the helper methods used in the demos."));

    }

    private void createBasicDemo() {
        // begin-source-example
        // source-example-heading: Basic Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();
        toolbarLayout.add(
                new Button("Button 1"),
                new Button("Button 2"),
                new Button("Button 3"),
                new Button("Button 4")
        );
        // end-source-example

        toolbarLayout.setId("basic-demo");
        addCard("Basic Demo", createResizableContainer(toolbarLayout));
    }

    private void createMultiComponentDemo() {
        // begin-source-example
        // source-example-heading: Multi Component Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();

        TextField search = new TextField();
        search.setPlaceholder("Search");
        search.setSuffixComponent(VaadinIcon.SEARCH.create());

        Button download = new Button("Download");
        download.setIcon(VaadinIcon.DOWNLOAD.create());

        Button remove = new Button("Remove");
        remove.setIcon(VaadinIcon.CLOSE.create());
        remove.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        MenuBar dropdown = new MenuBar();
        dropdown.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
        SubMenu subMenu = dropdown.addItem("Dropdown Menu").getSubMenu();
        subMenu.addItem("Item 1");
        subMenu.addItem("Item 2");

        Anchor link = new Anchor("https://vaadin.com", "Vaadin");
        link.setTarget("_blank");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Select an item");
        comboBox.setItems("Item 1", "Item 2", "Item 3");

        toolbarLayout.add(search, download, remove, dropdown, link, comboBox);
        // end-source-example

        toolbarLayout.setId("multi-component-demo");
        addCard("Multi Component Demo", createResizableContainer(toolbarLayout));
    }

    private void createCustomOverflowButtonDemo() {
        // begin-source-example
        // source-example-heading: Custom Overflow Button Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();

        Button customOverflowButton = new Button("Open");
        customOverflowButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        toolbarLayout.setOverflowButton(customOverflowButton);

        addComponentsToToolbar(toolbarLayout);
        // end-source-example

        toolbarLayout.setId("custom-overflow-button-demo");
        addCard("Custom Overflow Button Demo", createResizableContainer(toolbarLayout));
    }

    private void createReverseCollapseDemo() {
        // begin-source-example
        // source-example-heading: Reverse Collapse Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();
        toolbarLayout.setReverseCollapseOrder(true);
        addComponentsToToolbar(toolbarLayout);
        // end-source-example

        toolbarLayout.setId("reverse-collapse-demo");
        addCard("Reverse Collapse Demo", createResizableContainer(toolbarLayout));
    }

    private void createDebounceDelayDemo() {
        // begin-source-example
        // source-example-heading: Debounce Delay Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();
        toolbarLayout.setUpdateDebounceDelay(500);
        addComponentsToToolbar(toolbarLayout);
        // end-source-example

        toolbarLayout.setId("debounce-delay-demo");
        addCard("Debounce Delay Demo", createResizableContainer(toolbarLayout));
    }

    private void createMultiLevelDemo() {
        // begin-source-example
        // source-example-heading: Multi-level Demo
        ToolbarLayout toolbarLayout = new ToolbarLayout();

        // adding an item via the addItem(String) api returns a MenuItem which can be used to add submenus
        MenuItem basicMenu = toolbarLayout.addItem("Basic Menu");
        basicMenu.getSubMenu().addItem("Child Item 1");
        basicMenu.getSubMenu().addItem("Child Item 2");

        //  the same addItem(String, Icon) api but which includes an icon
        MenuItem basicMenuWithIcon = toolbarLayout.addItem("Basic Menu w/ Icon", VaadinIcon.MENU.create());
        basicMenuWithIcon.getSubMenu().addItem("Child Item 1");
        basicMenuWithIcon.getSubMenu().addItem("Child Item 2");

        // you can also create your own MenuBar and add it to the ToolbarLayout
        MenuBar menuBar = new MenuBar();
        MenuItem menuBarItem = menuBar.addItem("Menu Bar");
        menuBarItem.getSubMenu().addItem("Child Item 1");
        menuBarItem.getSubMenu().addItem("Child Item 2");
        toolbarLayout.add(menuBar);

        // end-source-example

        toolbarLayout.setId("multi-level-demo");
        addCard("Multi-level Demo", createResizableContainer(toolbarLayout));
    }

    private void createFixedWidthPrefixDemo() {
        // begin-source-example
        // source-example-heading: Theme: Fixed Width Prefix
        ToolbarLayout toolbarLayout = new ToolbarLayout();
        toolbarLayout.addThemeVariants(ToolbarLayoutVariant.FIXED_WIDTH_PREFIX);

        TextField search = new TextField();
        search.setPlaceholder("Search");
        search.setSuffixComponent(VaadinIcon.SEARCH.create());

        Button download = new Button("Download");
        download.setIcon(VaadinIcon.DOWNLOAD.create());

        Button edit = new Button("Edit");
        edit.setIcon(VaadinIcon.COG.create());
        edit.setIconAfterText(true);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        MenuBar dropdown = new MenuBar();
        dropdown.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
        SubMenu subMenu = dropdown.addItem("Dropdown Menu").getSubMenu();
        subMenu.addItem("Item 1");
        subMenu.addItem("Item 2");

        Anchor link = new Anchor("https://vaadin.com", "Vaadin");
        link.setTarget("_blank");

        toolbarLayout.add(search, download, edit, save, dropdown, link);
        // end-source-example

        toolbarLayout.setId("theme-width-prefix");
        addCard("Theme: Fixed Width Prefix", createResizableContainer(toolbarLayout));
    }

    private void createHideIconsDemo() {
        // begin-source-example
        // source-example-heading: Theme: Hide Icons
        ToolbarLayout toolbarLayout = new ToolbarLayout();
        toolbarLayout.addThemeVariants(ToolbarLayoutVariant.HIDE_ICONS);

        Button prefix = new Button("Prefix", VaadinIcon.BACKWARDS.create());

        Button suffix = new Button("Suffix", VaadinIcon.FORWARD.create());
        suffix.setIconAfterText(true);

        Button iconOnly = new Button("Icon Only", VaadinIcon.COG.create());
        iconOnly.addThemeVariants(ButtonVariant.LUMO_ICON);

        toolbarLayout.add(prefix, suffix, iconOnly);
        // end-source-example

        toolbarLayout.setId("theme-hide-icons");
        addCard("Theme: Hide Icons", createResizableContainer(toolbarLayout));
    }

    private void createMenuBarApiDemo() {
        // begin-source-example
        // source-example-heading: MenuBar-like API

        ToolbarLayout toolbarLayout = new ToolbarLayout();

        // Tier 1: Dashboard (simple)
        toolbarLayout.addItem("Dashboard", VaadinIcon.DASHBOARD.create());

        // Tier 1: Management > Tier 2: Users & Roles > Tier 3: Actions
        MenuItem management = toolbarLayout.addItem("Management", VaadinIcon.USER.create());
        SubMenu mgmtSub = management.getSubMenu();

        MenuItem users = mgmtSub.addItem("Users");
        SubMenu usersSub = users.getSubMenu();
        usersSub.addItem("Create User", e -> Notification.show("Create User clicked"));
        usersSub.addItem("List Users",   e -> Notification.show("List Users clicked"));

        MenuItem roles = mgmtSub.addItem("Roles");
        SubMenu rolesSub = roles.getSubMenu();
        rolesSub.addItem("Create Role",  e -> Notification.show("Create Role clicked"));
        rolesSub.addItem("List Roles",   e -> Notification.show("List Roles clicked"));

        // Tier 1: Settings > Tier 2: Preferences > Tier 3: Appearance > Tier 4: Themes
        MenuItem settings = toolbarLayout.addItem("Settings", VaadinIcon.COG.create());
        SubMenu settingsSub = settings.getSubMenu();

        MenuItem prefs = settingsSub.addItem("Preferences");
        SubMenu prefsSub = prefs.getSubMenu();

        MenuItem appearance = prefsSub.addItem("Appearance");
        SubMenu appearanceSub = appearance.getSubMenu();

        appearanceSub.addItem("Light Theme", e -> Notification.show("Switched to Light Theme"));
        appearanceSub.addItem("Dark Theme",  e -> Notification.show("Switched to Dark Theme"));

        // Tier 1: Help > Tier 2: About > Tier 3: Version & License
        MenuItem help = toolbarLayout.addItem("Help", VaadinIcon.QUESTION_CIRCLE.create());
        SubMenu helpSub = help.getSubMenu();

        MenuItem about = helpSub.addItem("About");
        SubMenu aboutSub = about.getSubMenu();
        aboutSub.addItem("Version", e -> Notification.show("App version 2.3.1"));
        aboutSub.addItem("License", e -> Notification.show("Licensed under Apache 2.0"));

        // end-source-example

        toolbarLayout.setId("menubar-like-api-demo");
        addCard("MenuBar-like API", createResizableContainer(toolbarLayout));
    }

    // begin-source-example
    // source-example-heading: Demo Helper Methods
    private void addComponentsToToolbar(ToolbarLayout toolbarLayout) {
        TextField search = new TextField();
        search.setPlaceholder("Search");
        search.setSuffixComponent(VaadinIcon.SEARCH.create());

        Button download = new Button("Download");
        download.setIcon(VaadinIcon.DOWNLOAD.create());

        Button remove = new Button("Remove");
        remove.setIcon(VaadinIcon.CLOSE.create());
        remove.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        MenuBar dropdown = new MenuBar();
        dropdown.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);
        SubMenu subMenu = dropdown.addItem("Dropdown Menu").getSubMenu();
        subMenu.addItem("Item 1");
        subMenu.addItem("Item 2");

        Anchor link = new Anchor("https://vaadin.com", "Vaadin");
        link.setTarget("_blank");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Select an item");
        comboBox.setItems("Item 1", "Item 2", "Item 3");

        toolbarLayout.add(search, download, remove, dropdown, link, comboBox);
    }

    private SplitLayout createResizableContainer(Component primaryComponent) {
        SplitLayout container = new SplitLayout(primaryComponent, new Div(), SplitLayout.Orientation.HORIZONTAL);
        container.setSplitterPosition(100);
        container.setMinWidth(50, Unit.PIXELS);
        return container;
    }
    // end-source-example
}
