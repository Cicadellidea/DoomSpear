package com.Cicadellidea.doom_spear.init;

import com.Cicadellidea.doom_spear.DoomSpear;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DoomSpear.MODID);

    // 注册本模组的创造模式标签。
    // "tutorial" 为该标签的注册名，会作为内部 ID 使用。
    public static final RegistryObject<CreativeModeTab> TUTORIAL =
            CREATIVE_MODE_TABS.register("doom_spear",
                    () -> CreativeModeTab.builder()

                            // 设置创造标签在界面中显示的图标。
                            // 这里使用石头作为示例图标，后续可以替换为模组物品。
                            .icon(() -> new ItemStack(ModItems.CHAIN_SPEAR.get()))
                            // 设置标签的显示名称。
                            // 使用可本地化文本（语言文件中定义）。
                            .title(Component.translatable("tab.doom_spear"))
                            // 定义该标签中显示的物品内容。
                            // output.accept(...) 用于向标签中添加物品。
                            .displayItems((itemDisplayParameters, output) -> {
//                                output.accept(ModItems.RAW_MATERIAL.get());
//                                output.accept(ModBlocks.RAW_MATERIAL_BLOCK.get());
                                output.accept(ModItems.CHAIN_SPEAR.get());
                                output.accept(ModItems.RAVAGER.get());

                            })

                            // 构建最终的 CreativeModeTab 实例。
                            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
