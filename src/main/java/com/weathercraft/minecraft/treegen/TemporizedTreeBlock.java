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

    private static int TRUNK_SIZE = 2;
    private static int OFFSET = 9;
    private static int MAX_CHIOMA_HEIGHT = 7;
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


        //Generate the tree's capocchiella
        if (currentTreeHeight == 12) {
            placeCapocchiella(world,pos,currentTreeHeight);
        }


        if ( currentTreeHeight % 5 == 0 && currentTreeHeight != 0) {
            for (int i = 0; i < TRUNK_SIZE+3; i++ ) {
                placeBlockPreciseCircle(world,pos,currentTreeHeight*2,i,OFFSET,LOG_BLOCK);
            }
        }
        for(int i = 0; i <= currentTreeHeight; i++){
            for (int j = 0; j <= TRUNK_SIZE; j++) {
                placeBlockPreciseCircle(world,pos,i*2,j,OFFSET,LOG_BLOCK);
                placeBlockPreciseCircle(world,pos,i*2 + 1,j,OFFSET,LOG_BLOCK);
            }
            //Place the ladder on the center of one side of the circle
            //Two instructions: one for each
            world.setBlockState(new BlockPos(pos.getX() + OFFSET,pos.getY() + i*2,pos.getZ() + (OFFSET - TRUNK_SIZE -1)),LADDER_BLOCK);
            world.setBlockState(new BlockPos(pos.getX() + OFFSET,pos.getY() + i*2 + 1,pos.getZ() + (OFFSET - TRUNK_SIZE -1)),LADDER_BLOCK);
        }


        if(currentTreeHeight < 15) {
            currentTreeHeight++;
        }

        world.setBlockState(pos,state.withProperty(AGE,currentTreeHeight));
        System.out.println("SBEM!" + " " + world.getBlockState(pos).getValue(AGE));
        return true;
    }

    private static void placeBlockPreciseCircle(World world, final BlockPos pos, int height, int radius, int offset, IBlockState blockType) {
        int d = (5 - radius * 4)/4;
        int x = 0;
        int z = radius;
        do {
            world.setBlockState(new BlockPos(pos.getX() + offset + x, pos.getY() + height, pos.getZ() + offset + z),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset + x, pos.getY() + height, pos.getZ() + offset - z),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset - x, pos.getY() + height, pos.getZ() + offset + z),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset - x, pos.getY() + height, pos.getZ() + offset - z),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset + z, pos.getY() + height, pos.getZ() + offset + x),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset + z, pos.getY() + height, pos.getZ() + offset - x),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset - z, pos.getY() + height, pos.getZ() + offset + x),blockType);
            world.setBlockState(new BlockPos(pos.getX() + offset - z, pos.getY() + height, pos.getZ() + offset - x),blockType);
            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - z) + 1;
                z--;
            }
            x++;
        } while (x <= z);

    }

    private static void placeCapocchiella(World world, BlockPos pos, int currentTreeHeight) {
        int sphereRadius = 0;
        boolean halfSphereRechead = false;
        for (int chiomaHeight = currentTreeHeight * 2; chiomaHeight < MAX_CHIOMA_HEIGHT * 2 + currentTreeHeight * 2; chiomaHeight++) {
            for (int i = 0; i < sphereRadius; i++) {
                placeBlockPreciseCircle(world, pos, chiomaHeight, i, OFFSET, Blocks.diamond_block.getDefaultState());
            }

            if (sphereRadius == MAX_CHIOMA_HEIGHT)
                halfSphereRechead = true;

            if (halfSphereRechead)
                sphereRadius--;
            else
                sphereRadius++;
        }
    }

}
