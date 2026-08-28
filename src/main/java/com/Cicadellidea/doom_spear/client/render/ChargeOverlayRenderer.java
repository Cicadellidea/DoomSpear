package com.Cicadellidea.doom_spear.client.render;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChargeOverlayRenderer {

    // 距离屏幕中心（准星）向下偏移多少像素
    private static final int OFFSET_Y = 7+10;

    // 矩形参数
    private static final int RECT_WIDTH = 10;
    private static final int RECT_HEIGHT = 4;
    private static final int SPACING = 3;
    private static final int POS = RECT_WIDTH+ SPACING;
    private static final int COLOR = 0x04EE4040;
    private static final int EMPTY_COLOR = 0x02FFFFFF;

    // 圆形半径
    private static final int CIRCLE_RADIUS = 16;

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent event) {
        // 只在全部HUD阶段渲染


        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(player == null || player.isRemoved()) {
            return;
        }
        if (!FunctionLib.hasChainSpear(player)){return;}
        var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        // 屏幕中心点 = 准星位置
        int centerX = screenW / 2;
        int centerY = screenH / 2;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack pose = guiGraphics.pose();

        RenderSystem.enableBlend();
        int charge = playerData.getSpearChargeCount();
        int rectX = centerX - POS - RECT_WIDTH / 2;
        int rectY = centerY + OFFSET_Y;
        drawBox(guiGraphics,rectX,rectY);
//        guiGraphics.fill(rectX, rectY,
//                rectX + RECT_WIDTH,
//                rectY + RECT_HEIGHT,
//                COLOR);
        if (charge >= 1){
            fillBox(guiGraphics,rectX,rectY);
        }
        rectX = centerX - RECT_WIDTH / 2;
        rectY = centerY + OFFSET_Y;
        drawBox(guiGraphics,rectX,rectY);


        // fill( x1,y1,x2,y2, ARGB ) 蓝色半透明矩形
//        guiGraphics.fill(rectX, rectY,
//                rectX + RECT_WIDTH,
//                rectY + RECT_HEIGHT,
//                COLOR);
        if (charge >= 2){
            fillBox(guiGraphics,rectX,rectY);
        }
        rectX = centerX + POS - RECT_WIDTH / 2;
        rectY = centerY + OFFSET_Y;
        drawBox(guiGraphics,rectX,rectY);

//        guiGraphics.fill(rectX, rectY,
//                rectX + RECT_WIDTH,
//                rectY + RECT_HEIGHT,
//                COLOR);
        if (charge >= 3){
            fillBox(guiGraphics,rectX,rectY);
        }


        RenderSystem.disableBlend();

    }
    private static void drawBox(GuiGraphics guiGraphics,int rectX,int rectY){
        guiGraphics.hLine(rectX,rectX+RECT_WIDTH,rectY,COLOR);
        guiGraphics.hLine(rectX,rectX+RECT_WIDTH,rectY+RECT_HEIGHT,COLOR);
        guiGraphics.vLine(rectX,rectY,rectY+RECT_HEIGHT,COLOR);
        guiGraphics.vLine(rectX+RECT_WIDTH,rectY,rectY+RECT_HEIGHT,COLOR);
    }
    private static void fillBox(GuiGraphics guiGraphics,int rectX,int rectY){
        guiGraphics.fill(rectX+1, rectY+1,
                rectX + RECT_WIDTH,
                rectY + RECT_HEIGHT,
                COLOR);
    }



}
