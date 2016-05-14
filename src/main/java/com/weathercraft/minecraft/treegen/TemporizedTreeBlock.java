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

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    private static IBlockState LEAF_BLOCK = Blocks.leaves.getDefaultState();
    private static IBlockState SPECIAL_LEAF_BLOCK = Blocks.leaves2.getDefaultState();
    private static IBlockState LOG_BLOCK = Blocks.log.getDefaultState();
    private static IBlockState LADDER_BLOCK = Blocks.ladder.getDefaultState();

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
        int currentTreeHeight = world.getBlockState(pos).getValue(AGE);
        if (currentTreeHeight == 7 || currentTreeHeight == 14 ) {
                    placeFloor(world,pos,currentTreeHeight);
                    placeLeaf(world,pos,currentTreeHeight+1);
        }
        if (currentTreeHeight == 15)
           placeBlockCircleFloor(world,pos,currentTreeHeight,14,LOG_BLOCK);


        for(int i = 0; i <= currentTreeHeight; i++){
            placeTrunk(world,pos,i*2);
            placeTrunk(world,pos,i*2 + 1);
        }

        if(currentTreeHeight < 15) {
            currentTreeHeight++;
        }

        world.setBlockState(pos,state.withProperty(AGE,currentTreeHeight));
        System.out.println("SBEM!" + " " + world.getBlockState(pos).getValue(AGE));
        return true;
    }

    private static void placeTrunk(World world, BlockPos pos, int height) {

        world.setBlockState(new BlockPos(pos.getX() +1, pos.getY() + height, pos.getZ()),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +2, pos.getY() + height, pos.getZ()),LOG_BLOCK);

        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + height, pos.getZ()+1),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +1, pos.getY() + height, pos.getZ()+1),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +2, pos.getY() + height, pos.getZ()+1),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +3, pos.getY() + height, pos.getZ()+1),LOG_BLOCK);

        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + height, pos.getZ()+2),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +1, pos.getY() + height, pos.getZ()+2),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +2, pos.getY() + height, pos.getZ()+2),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +3, pos.getY() + height, pos.getZ()+2),LOG_BLOCK);

        world.setBlockState(new BlockPos(pos.getX() +1, pos.getY() + height, pos.getZ()+3),LOG_BLOCK);
        world.setBlockState(new BlockPos(pos.getX() +2, pos.getY() + height, pos.getZ()+3),LOG_BLOCK);

        world.setBlockState(new BlockPos(pos.getX()+1, pos.getY() + height, pos.getZ()-1), LADDER_BLOCK);

    }

    private static void placeFloor(World world, final BlockPos pos, int height) {
        BlockPos referencePosition = new BlockPos(pos.getX()-1,pos.getY()+height,pos.getZ()-1);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i == 0 && j == 0) || (i == 5 && j == 0) || (i == 0 && j == 5) || (i == 5 && j == 5)) {
                } else {
                    world.setBlockState(new BlockPos(referencePosition.getX()+i,referencePosition.getY(),referencePosition.getZ()+j),LOG_BLOCK);
                }
            }
        }
    }

    private static void placeLeaf(World world, BlockPos pos, int height){
        BlockPos referencePosition = new BlockPos(pos.getX()-1,pos.getY()+height,pos.getZ()-1);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i == 0 && j == 0) || (i == 5 && j == 0) || (i == 0 && j == 5) || (i == 5 && j == 5)) {
                } else {
                    world.setBlockState(new BlockPos(referencePosition.getX()+i,referencePosition.getY(),referencePosition.getZ()+j),LEAF_BLOCK);
                }
            }
        }
    }

    private static void placeBlockCircleFloor(World world, final BlockPos pos, final int height, final int size, IBlockState blockType) {
        int shift = (size/2) - 2;
        BlockPos referencePosition = new BlockPos(pos.getX()- shift,pos.getY()+height,pos.getZ()-shift);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i+j < shift || (size -1  - j + i  < shift) || (size - 1 - i + j < shift) || (size - i + size - j - 2 < shift)) {
                } else {
                    world.setBlockState(new BlockPos(referencePosition.getX()+i,referencePosition.getY(),referencePosition.getZ()+j),blockType);
                }
            }
        }
    }


}
