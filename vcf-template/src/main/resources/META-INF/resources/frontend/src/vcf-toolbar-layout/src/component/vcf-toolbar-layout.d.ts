import { LitElement } from 'lit';
import '@vaadin/button';
import '@vaadin/popover';
import '@vaadin/vertical-layout';
declare const VcfToolbarLayout_base: import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/component-base/src/resize-mixin.js").ResizeMixinClass> & import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/component-base/src/dir-mixin").DirMixinClass> & import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/component-base/src/element-mixin.js").ElementMixinClass> & import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js").ThemableMixinClass> & import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/vaadin-themable-mixin/vaadin-theme-property-mixin").ThemePropertyMixinClass> & import("@open-wc/dedupe-mixin").Constructor<import("@vaadin/component-base/src/polylit-mixin.js").PolylitMixinClass> & typeof LitElement;
export declare class VcfToolbarLayout extends VcfToolbarLayout_base {
    static get is(): string;
    static get version(): string;
    static get styles(): import("lit").CSSResult;
    protected readonly _overflowContainerStyles: string;
    theme: string | null;
    /**
     * If true, the buttons will be collapsed into the overflow menu
     * starting from the "start" end of the bar instead of the "end".
     * @attr {boolean} reverse-collapse
     */
    reverseCollapse: boolean;
    /**
     * The delay in milliseconds to debounce the overflow update after a resize.
     */
    updateDebounceDelay: number;
    protected _overflowContainer: HTMLElement;
    protected _overflowButton: HTMLElement;
    private __resizeObserver;
    private __updateTimeout;
    connectedCallback(): void;
    disconnectedCallback(): void;
    protected firstUpdated(): void;
    render(): import("lit-html").TemplateResult<1>;
    /**
     * Request an update to the overflow items. This method debounces the update.
     */
    protected _requestOverflowUpdate(): void;
    /**
     * Move elements between the main container and the overflow container.
     * Elements in the main container are visisble as normal.
     * Elements in the overflow container are hidden and only shown when the overflow button is clicked.
     */
    protected _updateOverflowingItems(): void;
    /**
     * Check if the element is visible in the container.
     * @param element
     * @param container
     * @param extraSpaceRequired
     * @returns
     */
    protected _isElementVisibleInContainer(element: Element, container: Element, extraSpaceRequired?: number): boolean;
    protected _getVisibleItems(): Element[];
    protected _getOverflowedItems(): Element[];
    protected _moveItemToOverflowContainer(item: Element): void;
    protected _moveItemToMainContainer(item: Element): void;
    /**
     * Refresh the overflow button's state.
     */
    protected _updateOverflowButtonState(): void;
    protected _isOverflowButtonVisible(): boolean;
    protected _injectGlobalStyles(): void;
    protected _removeGlobalStyles(): void;
}
export {};
