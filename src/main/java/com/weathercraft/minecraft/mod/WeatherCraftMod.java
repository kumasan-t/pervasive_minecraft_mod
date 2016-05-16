package com.weathercraft.minecraft.mod;

import com.weathercraft.minecraft.gui.RenderGuiHandler;
import com.weathercraft.minecraft.treegen.TemporizedTreeBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import gnu.io.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * /////////////////////////////////////
 * Created by randomBEAR on 05/05/2016.
 * /////////////////////////////////////
 */

@Mod(modid = WeatherCraftMod.MODID, version = WeatherCraftMod.VERSION)
public class WeatherCraftMod {

    public static final String MODID = "WeatherCraft";
    public static final String VERSION = "0.01";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        TemporizedTreeBlock ttB = new TemporizedTreeBlock();

        try {
            serialCommunicationInit(minecraft);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
            RenderItem r = minecraft.getRenderItem();
            ItemModelMesher m = r.getItemModelMesher();
            m.register(Item.getItemFromBlock(ttB), 0, new ModelResourceLocation("weathercraft:TreeHouse", "inventory"));
        }
    }

    public void serialCommunicationInit(Minecraft minecraft) throws IOException {
        System.out.println("!!!!!!!!!!!! ARDUINO PLS TALK TO ME !!!!!!!!!!!!!!!");
        for(String s:NRSerialPort.getAvailableSerialPorts()){
            System.out.println("!!! I FOUND THIS PORT: "+s);
        }
        System.out.println("!!!!!!!!! DUDE I'M DONE !!!!!!!!");
        String port = "COM3";
        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        while(!serial.connect()) {
            System.out.println("FUCK. THIS. SHIT.");
        }
        DataInputStream ins = new DataInputStream(serial.getInputStream());

        FMLCommonHandler.instance().bus().register(new ArduinoTickHandler(minecraft, ins));


    }
}
