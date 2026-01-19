package org.vaadin.addons.componentfactory.demo.helpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.router.Route;

/**
 * Modified version of {@link com.vaadin.flow.demo.DemoView} to support use of custom {@link SpringSourceContentResolver}
 * which is required to support Spring Boot.
 */
@Tag(Tag.DIV)
public abstract class AbstractDemoView extends Component
        implements HasComponents {
    static final String VARIANT_TOGGLE_BUTTONS_DIV_ID = "variantToggleButtonsDiv";
    static final String COMPONENT_WITH_VARIANTS_ID = "componentWithVariantsDemo";

    private final Div container = new Div();

    protected AbstractDemoView() {
        Route annotation = getClass().getAnnotation(Route.class);
        if (annotation == null) {
            throw new IllegalStateException(
                    getClass().getName() + " should be annotated with @"
                            + Route.class.getName() + " to be a valid view");
        }
        add(container);

        initView();
    }

    /**
     * Builds the content of the view.
     */
    protected abstract void initView();
    
    protected void addCard(String title, Component content) {
      H2 cardTitle = new H2(title);
      
      Div card = new Div();
      card.getStyle().setMargin("40px auto");
      card.getStyle().setMaxWidth("750px");
      card.add(cardTitle);
      card.add(new Hr());      
      card.add(content);

      add(card);
    }

//   
//    /**
//     * Adds a demo that shows how the component looks like with specific
//     * variants applied.
//     *
//     * @param componentSupplier
//     *            a method that creates the component to which variants will be
//     *            applied to
//     * @param addVariant
//     *            a function that adds the new variant to the component
//     * @param removeVariant
//     *            a function that removes the variant from the component
//     * @param variantToThemeName
//     *            function that converts variant to an html theme name
//     * @param variants
//     *            list of variants to show in the demos
//     * @param <T>
//     *            variants' type
//     * @param <C>
//     *            component's type
//     */
//    protected <T extends Enum<?>, C extends Component & HasTheme> void addVariantsDemo(
//            Supplier<C> componentSupplier, BiConsumer<C, T> addVariant,
//            BiConsumer<C, T> removeVariant,
//            Function<T, String> variantToThemeName, T... variants) {
//
//        C component = componentSupplier.get();
//        component.setId(COMPONENT_WITH_VARIANTS_ID);
//
//        Div message = new Div();
//        message.setText(
//                "Toggle a variant to see how the component's appearance will change.");
//
//        Div variantsToggles = new Div();
//        variantsToggles.setId(VARIANT_TOGGLE_BUTTONS_DIV_ID);
//        for (T variant : variants) {
//            if (variant.name().startsWith("LUMO_")) {
//                String variantName = variantToThemeName.apply(variant);
//                variantsToggles
//                        .add(new NativeButton(
//                                getButtonText(variantName,
//                                        component.getThemeNames()
//                                                .contains(variantName)),
//                                event -> {
//                                    boolean variantPresent = component
//                                            .getThemeNames()
//                                            .contains(variantName);
//                                    if (variantPresent) {
//                                        removeVariant.accept(component,
//                                                variant);
//                                    } else {
//                                        addVariant.accept(component, variant);
//                                    }
//                                    event.getSource().setText(getButtonText(
//                                            variantName, !variantPresent));
//                                }));
//
//            }
//        }
//        addCard("Theme variants usage", message, component, variantsToggles);
//    }
//
//    private String getButtonText(String variantName, boolean variantPresent) {
//        return String.format(
//                variantPresent ? "Remove '%s' variant" : "Add '%s' variant",
//                variantName);
//    }
}