package com.weathercraft.minecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sara on 15/05/2016.
 */
public class RenderGuiHandler {

    int ecoPoints = 0;
    int tickCount = 0;
    String[] tips = {"Plant a tree today", "Switch off the lights when you leave a room", ":D :D :D", "oh no"};
    int tipIndex = 0;

    public RenderGuiHandler(){}

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event){
        new WeatherCraftGui(Minecraft.getMinecraft(), ecoPoints, tips[tipIndex]);
        tickCount++;
        if (tickCount%2000 == 0){
            tipIndex++;
            if (tipIndex>=3) tipIndex = 0;
        }
    }

}
