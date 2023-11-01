package broccolai.tags.paper.menu;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.ActionService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.core.config.LocaleConfiguration;
import broccolai.tags.core.util.FormatingUtilites;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.types.PaginatedTransform;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.slf4j.Logger;

public final class TagsMenuFactory {

    private static final ItemStackElement<ChestPane> BOTTOM_BAR_ELEMENT = new ItemStackElement<>(
        PaperItemBuilder.ofType(Material.WHITE_STAINED_GLASS_PANE)
            .name(Component.empty())
            .build(),
        ClickHandler.cancel()
    );

    private static final ItemStack BACK_ITEM = PaperItemBuilder
        .ofType(Material.RED_STAINED_GLASS_PANE)
        .name(Component.text("Previous Page"))
        .build();

    private static final ItemStack FORWARD_ITEM = PaperItemBuilder
        .ofType(Material.GREEN_STAINED_GLASS_PANE)
        .name(Component.text("Next Page"))
        .build();

    private final Logger logger;
    private final TagsService tagsService;
    private final ActionService actionService;
    private final LocaleConfiguration.MenuLocaleConfiguration locale;

    @Inject
    public TagsMenuFactory(
        final @NonNull Logger logger,
        final @NonNull TagsService tagsService,
        final @NonNull ActionService actionService,
        final @NonNull LocaleConfiguration locale
    ) {
        this.logger = logger;
        this.tagsService = tagsService;
        this.actionService = actionService;
        this.locale = locale.menu;
    }

    public ChestInterface create(final @NonNull TagsUser user) {
        InterfaceProperty<UUID> refreshProperty = InterfaceProperty.of(UUID.randomUUID());
        var tagTransform = this.createTagTransform(user, refreshProperty);

        return ChestInterface.builder()
            .title(this.locale.title.asComponent())
            .rows(4)
            .cancelClicksInPlayerInventory(true)
            .addTransform(this::createFillTransform)
            .addTransform(tagTransform, tagTransform.pageProperty(), refreshProperty)
            .build();
    }

    private ChestPane createFillTransform(ChestPane pane, InterfaceView<ChestPane, PlayerViewer> view) {
        ChestPane result = pane;

        for (int i = 0; i < 9; i++) {
            result = result.element(BOTTOM_BAR_ELEMENT, i, 3);
        }

        return result;
    }

    private PaginatedTransform<ItemStackElement<ChestPane>, ChestPane, PlayerViewer> createTagTransform(
        final @NonNull TagsUser user,
        final @NonNull InterfaceProperty<UUID> refreshProperty
    ) {
        PaginatedTransform<ItemStackElement<ChestPane>, ChestPane, PlayerViewer> transform = new PaginatedTransform<>(
            Vector2.at(0, 0),
            Vector2.at(8, 2),
            () -> this.createTagElements(user, refreshProperty)
        );

        transform.backwardElement(Vector2.at(0, 3), unused -> {
            return ItemStackElement.of(BACK_ITEM, ctx -> {
                transform.previousPage();
                ctx.cancel(true);
            });
        });

        transform.forwardElement(Vector2.at(8, 3), unused -> {
            return ItemStackElement.of(FORWARD_ITEM, ctx -> {
                transform.nextPage();
                ctx.cancel(true);
            });
        });

        return transform;
    }

    private List<ItemStackElement<ChestPane>> createTagElements(
        final @NonNull TagsUser user,
        final @NonNull InterfaceProperty<UUID> refreshProperty
    ) {
        return this.tagsService.allTags(user)
            .stream()
            .map(tag -> this.createTagElement(user, tag, refreshProperty))
            .toList();
    }

    private ItemStackElement<ChestPane> createTagElement(
        final @NonNull TagsUser user,
        final @NonNull ConstructedTag tag,
        final @NonNull InterfaceProperty<UUID> refreshProperty
    ) {
        Material material = this.matchMaterialOrDefault(tag.displayInformation().material());
        ItemStack item = PaperItemBuilder.ofType(material)
            .name(tag.component())
            .lore(this.createTagLore(user, tag))
            .build();

        return ItemStackElement.of(item, ctx -> {
            ctx.cancel(true);
            this.actionService.select(user, tag);
            refreshProperty.set(UUID.randomUUID());
        });
    }

    private List<Component> createTagLore(final @NonNull TagsUser user, final @NonNull ConstructedTag tag) {
        List<Component> result = new ArrayList<>();

        result.addAll(this.formatTagReason(tag.reason()));

        result.add(Component.empty());

        if (this.userHasTagEquipped(user, tag)) {
            result.add(this.locale.currentlyEquipped.asComponent());
        } else {
            result.add(this.locale.equip.asComponent());
        }

        return result;
    }

    private List<Component> formatTagReason(final @NonNull String reason) {
        return FormatingUtilites.splitString(reason, 30)
            .stream()
            .map(text -> Component.text(text, NamedTextColor.WHITE))
            .collect(Collectors.toList());
    }

    private Material matchMaterialOrDefault(final @NonNull String input) {
        Material material = Material.matchMaterial(input);

        if (material == null) {
            this.logger.warn("material {} does not exist", input);
            material = Material.POTATO;
        }

        return material;
    }

    private boolean userHasTagEquipped(final @NonNull TagsUser user, final @NonNull ConstructedTag tag) {
        return user.current().isPresent() && user.current().get() == tag.id();
    }

}
