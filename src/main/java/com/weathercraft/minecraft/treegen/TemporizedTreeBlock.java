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

        //Generate the tree's foliage
        if (currentTreeHeight == 12) {
            placeFoliage(world,pos,currentTreeHeight,LEAF_BLOCK);
        }


        if ( currentTreeHeight % 5 == 0 && currentTreeHeight != 0) {
            placeBlockFilledPreciseCircle(world,pos,currentTreeHeight*2,TRUNK_SIZE*2,OFFSET,LOG_BLOCK);
            placeBlockOutlinePreciseCircle(world,pos,currentTreeHeight*2 + 1,TRUNK_SIZE*2,OFFSET,LEAF_BLOCK);
        }

        for(int i = 0; i <= currentTreeHeight; i++){
            placeBlockFilledPreciseCircle(world,pos,i*2,TRUNK_SIZE,OFFSET,LOG_BLOCK);
            placeBlockFilledPreciseCircle(world,pos,i*2 + 1,TRUNK_SIZE,OFFSET,LOG_BLOCK);
            //Place the ladder on the center of one side of the circle
            //Two instructions: one for each floor generated
            world.setBlockState(new BlockPos(pos.getX() + OFFSET,pos.getY() + i*2,pos.getZ() + (OFFSET - TRUNK_SIZE - 1)),LADDER_BLOCK);
            world.setBlockState(new BlockPos(pos.getX() + OFFSET,pos.getY() + i*2 + 1,pos.getZ() + (OFFSET - TRUNK_SIZE - 1)),LADDER_BLOCK);
        }


        if(currentTreeHeight < 15) {
            currentTreeHeight++;
        }

        world.setBlockState(pos,state.withProperty(AGE,currentTreeHeight));
        System.out.println("SBEM!" + " " + world.getBlockState(pos).getValue(AGE));
        return true;
    }

    /**
     * Draws a filled circle of specified radius at the given height.
     * @param world         Minecraft world.
     * @param pos           Position of the reference block.
     * @param height        Height at which blocks will spawn.
     * @param radius        Radius of the circle.
     * @param offset        Determines how much you the line
     *                      is translated from the given pos.
     * @param blockType     Specify what kind of block will
     *                      be spawned.
     */
    private static void placeBlockFilledPreciseCircle(World world, final BlockPos pos, int height, int radius, int offset, IBlockState blockType) {
        int d = (5 - radius * 4)/4;
        int x = 0;
        int z = radius;
        do {
            drawLineBlocks(world, pos, height, -x, z, offset, blockType);
            drawLineBlocks(world, pos, height, -x, -z, offset, blockType);
            drawLineBlocks(world, pos, height, -z, x, offset, blockType);
            drawLineBlocks(world, pos, height, -z, -x, offset, blockType);

            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - z) + 1;
                z--;
            }
            x++;
        } while (x <= z);

    }

    /**
     * Draws a circle of specified radius at the given height.
     * @param world         Minecraft world.
     * @param pos           Position of the reference block.
     * @param height        Height at which blocks will spawn.
     * @param radius        Radius of the circle.
     * @param offset        Determines how much you the line
     *                      is translated from the given pos.
     * @param blockType     Specify what kind of block will
     *                      be spawned.
     */
    private static void placeBlockOutlinePreciseCircle(World world, final BlockPos pos, int height, int radius, int offset, IBlockState blockType) {
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

    /**
     * Draws a line of blocks of specified block type
     * along the x axis and z axis with specified height
     * @param world         Minecraft world.
     * @param pos           Position of the reference block.
     * @param height        Parameter that determines the
     *                      increment along the y axis.
     * @param xAxis         Starting point along the x axis
     *                      from which blocks will be spawned.
     * @param zAxis         Shifting along z axis.
     * @param offset        Determines how much you the line
     *                      is translated from the given pos.
     * @param blockType     Specify what kind of block will
     *                      be spawned.
     */
    private static void drawLineBlocks(World world, BlockPos pos, int height, int xAxis, int zAxis, int offset, IBlockState blockType) {
        int end = Math.abs(xAxis);
        for (int i = xAxis; i <= end; i++) {
            world.setBlockState(new BlockPos(pos.getX() + offset + i, pos.getY() + height, pos.getZ() + offset + zAxis),blockType);
        }
    }

    /**
     * Spawns a sphere made of whatever material is specified, at the
     * desired height.
     * @param world         Minecraft world object.
     * @param pos           Position of this block.
     * @param height        Desired height at which
     *                      the foliage will spawn.
     * @param blockType     Specify what kind of block it's
     *                      going to be spawned.
     */
    private static void placeFoliage(World world, BlockPos pos, int height, IBlockState blockType) {
        int sphereRadius = 0;
        boolean halfSphereReached = false;
        for (int foliageHeight = height * 2; foliageHeight < MAX_CHIOMA_HEIGHT * 2 + height * 2; foliageHeight++) {
            for (int i = 0; i < sphereRadius; i++) {
                placeBlockFilledPreciseCircle(world, pos, foliageHeight, i, OFFSET, blockType);
            }

            if (sphereRadius == MAX_CHIOMA_HEIGHT)
                halfSphereReached = true;

            if (halfSphereReached)
                sphereRadius--;
            else
                sphereRadius++;
        }
    }

}
