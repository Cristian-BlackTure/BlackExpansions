package ne.fnfal113.fnamplifications.gems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.gems.handlers.OnDamageHandler;
import ne.fnfal113.fnamplifications.gems.implementation.Gem;
import ne.fnfal113.fnamplifications.utils.WeaponArmorEnum;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class ArrowAvertGem extends AbstractGem implements OnDamageHandler {

    @Getter
    private final int chance;

    public ArrowAvertGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 14);

        this.chance = FNAmplifications.getInstance().getConfigManager().getValueById(this.getId() + "-percent-chance");
    }

    @Override
    public void onDrag(InventoryClickEvent event, Player player) {
        if(event.getCursor() == null){
            return;
        }

        ItemStack currentItem = event.getCurrentItem();

        SlimefunItem slimefunItem = SlimefunItem.getByItem(event.getCursor());

        if(slimefunItem != null && currentItem != null) {
            if (WeaponArmorEnum.HELMET.isTagged(currentItem.getType()) || WeaponArmorEnum.CHESTPLATE.isTagged(currentItem.getType()) ||
            WeaponArmorEnum.LEGGINGS.isTagged(currentItem.getType()) || WeaponArmorEnum.BOOTS.isTagged(currentItem.getType())) {
                new Gem(slimefunItem, currentItem, player).onDrag(event, false);
            } else {
                player.sendMessage(Utils.colorTranslator("&eInvalid item to socket! Gem works on armors only"));
            }
        }
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event) {
        if(ThreadLocalRandom.current().nextInt(100) < getChance() &&
                event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
            event.getEntity().sendMessage(Utils.colorTranslator("&6Enemy arrow deflected! arrow avert gem has taken effect."));
            event.setCancelled(true);
        }
    }
}