package com.weathercraft.minecraft.mod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Sara on 16/05/2016.
 */

public class ArduinoTickHandler {

    Minecraft mc;
    DataInputStream ins;
    int tickCount = 0;

    public ArduinoTickHandler(Minecraft mc, DataInputStream ins){
        this.mc = mc;
        this.ins = ins;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) throws IOException {
        if (event.phase == TickEvent.Phase.START) {
            int b = ins.read();
            //System.out.println("!!!!!!!!!!!! I GOT A VALUE !!!!!!!!!!! IT'S " + b);
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/time set " + b*1000);
        }
    }
}