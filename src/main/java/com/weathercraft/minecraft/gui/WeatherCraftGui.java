package com.weathercraft.minecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by Sara on 15/05/2016.
 */
public class WeatherCraftGui {
        int ecoPoints;
        Minecraft mc;

        public WeatherCraftGui(Minecraft mc, int ecoPoints, String tip){
            this.mc = mc;
            this.ecoPoints = ecoPoints;
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            String text = "ECOpoints: " + ecoPoints;
            mc.fontRendererObj.drawString(text, width/2 - mc.fontRendererObj.getStringWidth(text)/2, 10, Integer.parseInt("FFAA00", 16), true);
    }
}
