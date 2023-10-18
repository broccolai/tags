package broccolai.tags.bukkit.menu;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.TagsService;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.transform.types.PaginatedTransform;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.List;

public final class TagsMenuFactory {

    private final TagsService tagsService;

    @Inject
    public TagsMenuFactory(final @NonNull TagsService tagsService) {
        this.tagsService = tagsService;
    }

    public ChestInterface create(final @NonNull TagsUser user) {
        return ChestInterface.builder()
                .addReactiveTransform(this.createTagTransform(user))
                .build();
    }

    private PaginatedTransform<ItemStackElement<ChestPane>, ChestPane, PlayerViewer> createTagTransform(final @NonNull TagsUser user) {
        return new PaginatedTransform<>(
                Vector2.at(0, 0),
                Vector2.at(3, 8),
                () -> this.createTagElements(user)
        );
    }

    private List<ItemStackElement<ChestPane>> createTagElements(final @NonNull TagsUser user) {
        return this.tagsService.allTags(user)
                .stream()
                .map(this::createTagElement)
                .toList();
    }

    private ItemStackElement<ChestPane> createTagElement(final @NonNull ConstructedTag tag) {
        Material material = Material.matchMaterial(tag.displayInformation().material());
        ItemStack item = PaperItemBuilder.ofType(material)
                .name(tag.component())
                .build();

        return ItemStackElement.of(item);
    }

}
