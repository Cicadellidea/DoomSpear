package com.Cicadellidea.doom_spear.init;

import com.Cicadellidea.doom_spear.entity.RavagerBullet;
import com.Cicadellidea.doom_spear.entity.SpearHook;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Cicadellidea.doom_spear.DoomSpear.MODID;

public class ModEntity {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<SpearHook>>  SPEAR_HOOK =
            ENTITIES.register("spear_hook",
                    () -> EntityType.Builder.<SpearHook>of(SpearHook::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .build(MODID + ":spear_hook"));

    public static final RegistryObject<EntityType<RavagerBullet>>  RAVAGER_BULLET =
            ENTITIES.register("ravager_bullet",
                    () -> EntityType.Builder.<RavagerBullet>of(RavagerBullet::new, MobCategory.MISC)
                            .sized(0.1F, 0.1F)
                            .build(MODID + "ravager_bullet"));
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
