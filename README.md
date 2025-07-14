# Toolbar Layout

ToolbarLayout is a server-side Vaadin component built on top of the [<vcf-toolbar-layout>](https://github.com/vaadin-component-factory/vcf-toolbar-layout) web component. The ToolbarLayout component manages overflowing menu items - automatically moving overflowing items into a dedicated overflow popover. The component supports additional features such as keyboard navigation, customizable theming, and grouping of items.

This component is part of Vaadin Component Factory.

## Features

- **Overflow Handling:**  
  Automatically moves overflowing menu items into a dedicated popover.
- **Reverse Collapse:**  
    Set the component to collapse items from the left side instead of the right.
- **Configurable Collapse Debounce:**  
  Set the debounce delay (in milliseconds) from when a resize event occurs until the overflow items are updated.
- **Theming:**  
  Two additional themes are provided:
    - **`FIXED_WIDTH_PREFIX`** – aligns overflow popover items uniformly regardless of whether they include a prefix icon.
    - **`HIDE_ICONS`** – automatically hides icons on items within the overflow popover.
- **Keyboard Navigation:**  
  Complete keyboard support for menu navigation.
- **Grouping Items:**  
  Group items by passing in a layout (such as HorizontalLayout); grouped items collapse and display together in the overflow popover.


## Running the component demo
Run from the command line:
- `mvn -pl vcf-toolbar-layout-flow-demo -Pwar install jetty:run`

Then navigate to `http://localhost:8080/`

## Running the component demo in docker
Build the image by running this from the command line:
- `docker build -t vcf-toolbar-layout-flow-demo .`

Run the image by running this from the command line:
- `docker run -p 8080:8080 vcf-toolbar-layout-flow-demo`

Then navigate to `http://localhost:8080/`

## Installing the component
Run from the command line:
- `mvn clean install -DskipTests`

## Profiles
### Profile "directory"
This profile, when enabled, will create the zip file for uploading to Vaadin's directory

### Profile "production"
This profile, when enabled, will execute a production build for the demo

## Using the component in a Flow application
To use the component in an application using maven,
add the following dependency to your `pom.xml`:
```
<dependency>
    <groupId>org.vaadin.addons.componentfactory</groupId>
    <artifactId>vcf-toolbar-layout-flow</artifactId>
    <version>${component.version}</version>
</dependency>
```

## How to Use
### Basic Usage

```java
ToolbarLayout toolbarLayout = new ToolbarLayout();
toolbarLayout.add(
        new Button("Button 1"),
        new Button("Button 2"),
        new Button("Button 3"),
        new Button("Button 4")
);
```

### Reverse Collapse

```java
ToolbarLayout toolbarLayout = new ToolbarLayout();
toolbarLayout.setReverseCollapseOrder(true);
addComponentsToToolbar(toolbarLayout);
```

### Debounce Delay
```java
ToolbarLayout toolbarLayout = new ToolbarLayout();
toolbarLayout.setUpdateDebounceDelay(500);
addComponentsToToolbar(toolbarLayout);
```

### Multi-level Menu
```java
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
```

### Grouped Items

```java
toolbarLayout.add(
        new HorizontalLayout(
                new Button("Button 1"),
                new Button("Button 2")
        ),
        new Button("Button 3"),
        new Button("Button 4")
);
```

### Theming

#### Fixed Width Prefix

```java
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
```

#### Hide Icons

```java
ToolbarLayout toolbarLayout = new ToolbarLayout();
toolbarLayout.addThemeVariants(ToolbarLayoutVariant.HIDE_ICONS);

Button prefix = new Button("Prefix", VaadinIcon.BACKWARDS.create());

Button suffix = new Button("Suffix", VaadinIcon.FORWARD.create());
suffix.setIconAfterText(true);

Button iconOnly = new Button("Icon Only", VaadinIcon.COG.create());
iconOnly.addThemeVariants(ButtonVariant.LUMO_ICON);

toolbarLayout.add(prefix, suffix, iconOnly);
```

## Flow documentation
Documentation for Vaadin Flow can be found in [Flow documentation](https://vaadin.com/docs/latest/flow).

## License
Distributed under Apache Licence 2.0. 

### Sponsored development
Major pieces of development of this add-on has been sponsored by multiple customers of Vaadin. Read more about Expert on Demand at: [Support](https://vaadin.com/support) and [Pricing](https://vaadin.com/pricing).
