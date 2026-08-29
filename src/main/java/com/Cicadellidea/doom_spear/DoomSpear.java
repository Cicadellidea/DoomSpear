package com.Cicadellidea.doom_spear;

import com.Cicadellidea.doom_spear.client.key.KeyBindings;
import com.Cicadellidea.doom_spear.client.render.RenderRegister;
import com.Cicadellidea.doom_spear.client.render.ViewSmoother;
import com.Cicadellidea.doom_spear.init.ModBlocks;
import com.Cicadellidea.doom_spear.init.ModCreativeModeTabs;
import com.Cicadellidea.doom_spear.init.ModEntity;
import com.Cicadellidea.doom_spear.init.ModItems;
import com.Cicadellidea.doom_spear.network.*;
import com.Cicadellidea.doom_spear.tracker.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(DoomSpear.MODID)
public class DoomSpear {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "doom_spear";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PROTOCOL_VER = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VER::equals)
            .serverAcceptedVersions(PROTOCOL_VER::equals)
            .networkProtocolVersion(() -> PROTOCOL_VER)
            .simpleChannel();
    private int ID = 0;

    public DoomSpear(){
        IEventBus modEventBus=FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntity.register(modEventBus);
        modEventBus.addListener(KeyBindings::register);
        modEventBus.register(new RenderRegister());

        ModCreativeModeTabs.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(new CapabilityTracker());
        MinecraftForge.EVENT_BUS.register(new ClientInputReader());
        MinecraftForge.EVENT_BUS.register(new WeaponEventHandler());
//        MinecraftForge.EVENT_BUS.register(new GoalHandler());
//        MinecraftForge.EVENT_BUS.register(new EntityStunHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());

        MinecraftForge.EVENT_BUS.register(new PlayerShootTracker());
        MinecraftForge.EVENT_BUS.register(new ViewSmoother());
//        MinecraftForge.EVENT_BUS.register(new ModelRegisterEvent());


        // 在mod主类构造，mod加载总线
        CHANNEL.registerMessage(ID++, PlayerForwardPacket.class,
                PlayerForwardPacket::encode,
                PlayerForwardPacket::decode,
                PlayerForwardPacket::handle);
        CHANNEL.registerMessage(ID++, PlayerLRPacket.class,
                PlayerLRPacket::encode,
                PlayerLRPacket::decode,
                PlayerLRPacket::handle);
        CHANNEL.registerMessage(ID++, SweepAttackPacket.class,
                SweepAttackPacket::encode,
                SweepAttackPacket::decode,
                SweepAttackPacket::handle);
        CHANNEL.registerMessage(ID++, SyncAttackCooldownPacket.class,
                SyncAttackCooldownPacket::encode,
                SyncAttackCooldownPacket::decode,
                SyncAttackCooldownPacket::handle);
        CHANNEL.registerMessage(ID++, DashPacket.class,
                DashPacket::encode,
                DashPacket::decode,
                DashPacket::handle);
        CHANNEL.registerMessage(ID++, SlamPacket.class,
                SlamPacket::encode,
                SlamPacket::decode,
                SlamPacket::handle);
        CHANNEL.registerMessage(ID++, SyncPlayerHookingPacket.class,
                SyncPlayerHookingPacket::encode,
                SyncPlayerHookingPacket::decode,
                SyncPlayerHookingPacket::handle);
        CHANNEL.registerMessage(ID++, SyncPlayerChargePacket.class,
                SyncPlayerChargePacket::encode,
                SyncPlayerChargePacket::decode,
                SyncPlayerChargePacket::handle);
        CHANNEL.registerMessage(ID++, SyncSpearActionPacket.class,
                SyncSpearActionPacket::encode,
                SyncSpearActionPacket::decode,
                SyncSpearActionPacket::handle);
        CHANNEL.registerMessage(ID++, ShootHookPacket.class,
                ShootHookPacket::encode,
                ShootHookPacket::decode,
                ShootHookPacket::handle);
        CHANNEL.registerMessage(ID++, PlayerLeftHoldingPacket.class,
                PlayerLeftHoldingPacket::encode,
                PlayerLeftHoldingPacket::decode,
                PlayerLeftHoldingPacket::handle);
    }


//    public DoomSpear(FMLJavaModLoadingContext context) {
//        IEventBus modEventBus = context.getModEventBus();
//        modEventBus.addListener(KeyBindings::register);
//        modEventBus.register(new RenderRegister());

//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        modEventBus.addListener(this::commonSetup);


//    }





    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");
//        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

//        if(Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }
//
//
//    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
//    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class ClientModEvents {
//
//        @SubscribeEvent
//        public static void onClientSetup(FMLClientSetupEvent event) {
//            // Some client setup code
//            LOGGER.info("HELLO FROM CLIENT SETUP");
//            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
//        }
//    }
}
