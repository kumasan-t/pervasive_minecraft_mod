package com.weathercraft.minecraft.treegen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.weathercraft.minecraft.treegen.utils.TemporizedTreeGeneratorUtils.*;


/**
 * /////////////////////////////////////
 * Created by randomBEAR on 09/05/2016.
 * /////////////////////////////////////
 */
public final class TemporizedTreeBlock extends Block {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    private static IBlockState LEAF_BLOCK = Blocks.leaves.getDefaultState();
    private static IBlockState LOG_BLOCK = Blocks.log.getDefaultState();
    private static IBlockState LADDER_BLOCK = Blocks.ladder.getDefaultState();

    private static int TRUNK_SIZE = 2;
    private static int OFFSET = 9;
    private static int MAX_FOLIAGE_HEIGHT = 7;

    public TemporizedTreeBlock() {
        super(Material.rock);
        GameRegistry.registerBlock(this,"TreeHouse");
        setDefaultState(this.blockState.getBaseState().withProperty(AGE,0));
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(0.5f);
        setLightLevel(1f);
        setTickRandomly(true);
    }

    @Override
    public int getMetaFromState(IBlockState state) { return state.getValue(AGE); }

    @Override
    public IBlockState getStateFromMeta(int meta) { return this.getDefaultState().withProperty(AGE,meta); }


    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {AGE});
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        generateAdjacentTrees(world,pos,state,TRUNK_SIZE, MAX_FOLIAGE_HEIGHT, OFFSET,LOG_BLOCK,LEAF_BLOCK,LADDER_BLOCK,AGE);
        System.out.println("SBEM!" + " " + world.getBlockState(pos).getValue(AGE));
        return true;
    }

}
