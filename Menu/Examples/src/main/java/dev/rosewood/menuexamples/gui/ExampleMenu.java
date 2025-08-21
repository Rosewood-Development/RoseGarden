package dev.rosewood.menuexamples.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.action.type.CloseMenuAction;
import dev.rosewood.rosegarden.gui.action.type.CommandAction;
import dev.rosewood.rosegarden.gui.action.type.MessageAction;
import dev.rosewood.rosegarden.gui.action.type.ModifyItemAction;
import dev.rosewood.rosegarden.gui.action.type.NextPageAction;
import dev.rosewood.rosegarden.gui.action.type.OpenMenuAction;
import dev.rosewood.rosegarden.gui.action.type.PlaySoundAction;
import dev.rosewood.rosegarden.gui.action.type.PreviousPageAction;
import dev.rosewood.rosegarden.gui.fill.CheckeredFill;
import dev.rosewood.rosegarden.gui.fill.MenuFill;
import dev.rosewood.rosegarden.gui.fill.OutlineFill;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.slot.MultiSlotProvider;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;

public class ExampleMenu extends RoseMenuWrapper {

    public static final String ID = "example";

    public ExampleMenu(RosePlugin rosePlugin) {
        super(rosePlugin, ID);
    }

    @Override
    public void create() {

        RoseItem borderItem = new RoseItem(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("");

        RoseItem nextPageItem = new RoseItem(Material.ARROW)
                .setDisplayName("&cNext Page")
                .setLore("&7Click to open the next page");

        RoseItem previousPageItem = new RoseItem(Material.ARROW)
                .setDisplayName("&cPrevious Page")
                .setLocalizedName("&7Click to open the previous page");

        RoseItem infoItem = new RoseItem(Material.SPRUCE_SIGN)
                .setDisplayName("&aInfo");

        this.addPage("&cExample | Page 1", 54, (page) -> {
            page.fill(new OutlineFill(), borderItem);
            page.addIcon(50, nextPageItem)
                    .on(Providers.LEFT_CLICK, NextPageAction.of());
            page.addIcon(49, infoItem.copy().setLore("&7This page displays different actions that icons can have."));

            page.addIcon(10, RoseItem.of(Material.BARRIER, "&cClose Menu")
                    .setLore("&eLeft-Click: &7Close Menu",
                            "&eRight-Click: &7Close Menu"))
                    .on(Providers.LEFT_CLICK, CloseMenuAction.of())
                    .on(Providers.RIGHT_CLICK, CloseMenuAction.of());

            page.addIcon(11, RoseItem.of(Material.COMMAND_BLOCK, "&aRun Command")
                    .setLore("&eLeft-Click: &7Player Command",
                            "&eRight-Click: &7Server Command"))
                    .on(Providers.LEFT_CLICK, CommandAction.of(List.of("say hi"), List.of()))
                    .on(Providers.RIGHT_CLICK,
                            CommandAction.of("say hi"));

            page.addIcon(12, RoseItem.of(Material.PAPER, "&bMessage")
                    .setLore("&eLeft-Click: &7Send Message"))
                    .on(Providers.LEFT_CLICK, MessageAction.of("&eHello :)"));

            page.addIcon(13, RoseItem.of(Material.DIAMOND, "&cModify Item")
                    .setLore("&eLeft-Click: &7Modify Entire Item",
                            "&eRight-Click: &7Modify Single Property"))
                    .on(Providers.LEFT_CLICK, ModifyItemAction.of(RoseItem.of(Material.COAL, "&aModified Item")))
                    .on(Providers.RIGHT_CLICK, ModifyItemAction.of(RoseItem.empty().setDisplayName("&bModified Display Name")));

            page.addIcon(14, RoseItem.of(Material.MAP, "&dOpen Menu")
                    .setLore("&eLeft-Click: &7Open file-example Menu"))
                    .on(Providers.LEFT_CLICK, OpenMenuAction.of("file-example"));

            page.addIcon(15, RoseItem.of(Material.NOTE_BLOCK, "&ePlay Sound")
                    .setLore("&eLeft-Click: &7Play Sound",
                            "&eRight-Click: &7Play Sound with Properties"))
                    .on(Providers.LEFT_CLICK, PlaySoundAction.of(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .on(Providers.RIGHT_CLICK, PlaySoundAction.of(Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F, 0.3F));
        });

        this.addPage("&cExample | Page 2", 54, (page) -> {
            page.fill(new MenuFill(), RoseItem.of(Material.RED_STAINED_GLASS_PANE, "&cArea Fill"), MultiSlotProvider.range(0, 26));
            page.fill(new CheckeredFill(), RoseItem.of(Material.BLACK_STAINED_GLASS_PANE, "&aCheckered Fill"), MultiSlotProvider.range(27, 53));
            page.fill(new CheckeredFill(), RoseItem.of(Material.WHITE_STAINED_GLASS_PANE, "&bCheckered Fill"), MultiSlotProvider.range(28, 53));
            page.addIcon(48, previousPageItem)
                    .on(Providers.LEFT_CLICK, PreviousPageAction.of());
            page.addIcon(50, nextPageItem)
                    .on(Providers.LEFT_CLICK, NextPageAction.of());
            page.addIcon(49, infoItem.copy().setLore("&7This page displays different fill types."));

        });

        this.addPage("&cTicking Menu! | Page 3", 54, (page) -> {
            page.fill(new OutlineFill(), borderItem);
            page.addIcon(48, previousPageItem)
                    .on(Providers.LEFT_CLICK, PreviousPageAction.of());
            page.addIcon(50, nextPageItem)
                    .on(Providers.LEFT_CLICK, NextPageAction.of());
            page.addIcon(49, infoItem.copy().setLore("&7This page displays different fill types."));
            page.on(Providers.TICK, PlaySoundAction.of(Sound.BLOCK_NOTE_BLOCK_PLING));

        }, 10);
    }

}
