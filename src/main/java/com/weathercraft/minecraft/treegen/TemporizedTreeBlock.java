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


/**
 * /////////////////////////////////////
 * Created by randomBEAR on 09/05/2016.
 * /////////////////////////////////////
 */
public final class TemporizedTreeBlock extends Block {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static IBlockState LEAF_BLOCK = Blocks.leaves.getDefaultState();
    private static IBlockState SPECIAL_LEAF_BLOCK = Blocks.leaves2.getDefaultState();
    private static IBlockState LOG_BLOCK = Blocks.log.getDefaultState();
    private static int TREE_HEIGHT = 6;

    public TemporizedTreeBlock() {
        super(Material.rock);
        GameRegistry.registerBlock(this,"GoldTree");
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
        BlockPos blockPos = pos;
        int currentTreeHeight = world.getBlockState(pos).getValue(AGE);
        for(int i = 0; i <= currentTreeHeight; i++){

            if (i >= 5) {
                for(int xi = 0;xi < 5;xi++){

                    for(int j = 0;j < 5;j ++){

                        int xoffest = -2 + xi;

                        int zoffest = -2 + j;

                        placeLeaf(world, new BlockPos(pos.getX()+xoffest, pos.getY() + i, pos.getZ()+zoffest));

                    }

                }
            }
            if(i != 0) {
                world.setBlockState(blockPos,LOG_BLOCK);
                blockPos = new BlockPos(pos.getX(),pos.getY()+i,pos.getZ());
            }
        }

        BlockPos place = pos;


        if(currentTreeHeight < 7) {
            currentTreeHeight++;
        }
        world.setBlockState(pos,state.withProperty(AGE,currentTreeHeight));
        System.out.println("SBEM!" + " " + world.getBlockState(pos).getValue(AGE));
        return true;
    }

    public void placeLeaf(World world, BlockPos pos){
        world.setBlockState(pos, LEAF_BLOCK);
    }

}
