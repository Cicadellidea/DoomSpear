package com.Cicadellidea.doom_spear.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

// 客户端专用，只在FMLJavaModLoading总线注册
public class KeyBindings {
    public static KeyMapping sweepKey;
    public static KeyMapping dashKey;
    public static KeyMapping slamKey;
    public static KeyMapping hookKey;
    public static KeyMapping fireKey;
    public static void register(RegisterKeyMappingsEvent event) {
        sweepKey = new KeyMapping(
                "key.doom_spear.sweep", // 翻译key
                GLFW.GLFW_KEY_G, // 默认G键
                "category.doom_spear" // 设置分类
        );
        dashKey = new KeyMapping(
                "key.doom_spear.dash",
                GLFW.GLFW_KEY_LEFT_CONTROL,
                "category.doom_spear"
        );
        slamKey = new KeyMapping(
                "key.doom_spear.slam",
                GLFW.GLFW_KEY_LEFT_SHIFT,
                "category.doom_spear"
        );
        hookKey = new KeyMapping(
                "key.doom_spear.hook",
                GLFW.GLFW_KEY_R,
                "category.doom_spear"
        );
//        fireKey = new KeyMapping(
//                "key.doom_spear.fire",
//                GLFW.MOU,
//                "category.doom_spear"
//        );
        event.register(sweepKey);
        event.register(dashKey);
        event.register(slamKey);
        event.register(hookKey);
//        event.register(fireKey);
    }
}
