package org.devathon.contest2016.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public enum ArmorCategory {

    HELMET((entity, itemStack) -> entity.getEquipment().setHelmet(itemStack)),
    CHESTPLATE((entity, itemStack) -> entity.getEquipment().setChestplate(itemStack)),
    LEGGINGS((entity, itemStack) -> entity.getEquipment().setLeggings(itemStack)),
    BOOTS((entity, itemStack) -> entity.getEquipment().setBoots(itemStack)),
    SHIELD((entity, itemStack) -> entity.getEquipment().setItemInOffHand(itemStack));

    private final BiConsumer<LivingEntity, ItemStack> applier;

    ArmorCategory(BiConsumer<LivingEntity, ItemStack> applier) {
        this.applier = applier;
    }

    public void applyTo(LivingEntity entity, ItemStack itemStack) {
        applier.accept(entity, itemStack);
    }
}
