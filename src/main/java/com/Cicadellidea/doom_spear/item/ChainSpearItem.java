package com.Cicadellidea.doom_spear.item;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;


import java.util.UUID;

public class ChainSpearItem extends Item {
    //武器属性UUID，原版剑用的固定uuid
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
//    private final float attackDamage;
//    private final float attackSpeedModifier;
    private final Multimap<Attribute, AttributeModifier> incineratorAttributes;



    public ChainSpearItem(Item.Properties props) {
        super(props);
        float damage = 6;
        float speed = -3;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", damage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", speed, AttributeModifier.Operation.ADDITION));
//        initAttributes(builder);
        this.incineratorAttributes = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.incineratorAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }


//    @Override
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
//        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();
//
//        if (slot == EquipmentSlot.MAINHAND) {
//            modifiers.put(
//                    Attributes.ATTACK_DAMAGE,
//                    new AttributeModifier(ATTACK_DAMAGE_UUID, "throw_weapon_damage", 6.0D, AttributeModifier.Operation.ADDITION)
//            );
//            modifiers.put(
//                    Attributes.ATTACK_SPEED,
//                    new AttributeModifier(ATTACK_SPEED_UUID, "throw_weapon_speed", 0.0D, AttributeModifier.Operation.ADDITION)
//            );
//        }
//        return modifiers;
//    }


//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack heldStack = player.getItemInHand(hand);
//        SpearActions.ShootHook(level,player);
////        player.getCooldowns().addCooldown(this,20);
//        return InteractionResultHolder.success(heldStack);
//    }


}

