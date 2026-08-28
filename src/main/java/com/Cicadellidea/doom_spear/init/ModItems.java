package com.Cicadellidea.doom_spear.init;

import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.item.ChainSpearItem;
import com.Cicadellidea.doom_spear.item.Rvager;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DoomSpear.MODID);
    public static final RegistryObject<Item> CHAIN_SPEAR =
            ITEMS.register("chain_spear",
                    () -> new ChainSpearItem(new Item.Properties().stacksTo(1))
            );

    public static final RegistryObject<Item> RAVAGER =
            ITEMS.register("ravager",
                    () -> new Rvager(new Item.Properties().stacksTo(1))
            );
//    public static final RegistryObject<Item> RAW_MATERIAL =
//            ITEMS.register("raw_material",
//                    // 创建一个基础物品实例。
//                    // 当前不添加任何特殊属性，仅用于演示注册流程，
//                    // 后续章节中将作为工业处理单元的加工原料使用。
//                    () -> new Item(new Item.Properties())
//            );
//    public static final RegistryObject<Item> RAW_MATERIAL_BLOCK =
//            ITEMS.register("raw_material_block",
//                    // 创建 BlockItem，将其与已注册的方块绑定。
//                    // ModBlocks.RAW_MATERIAL_BLOCK.get() 获取对应的方块实例，
//                    // 这样玩家在背包中持有该物品时，才能放置出对应的方块。
//                    () -> new BlockItem(
//                            ModBlocks.RAW_MATERIAL_BLOCK.get(),
//                            new Item.Properties())
//            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
