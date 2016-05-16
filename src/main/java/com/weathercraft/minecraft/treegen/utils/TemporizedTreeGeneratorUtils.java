package com.weathercraft.minecraft.treegen.utils;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * /////////////////////////////////////
 * Created by randomBEAR on 15/05/2016.
 * /////////////////////////////////////
 */
public class TemporizedTreeGeneratorUtils {

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
    public static void placeBlockFilledPreciseCircle(World world, final BlockPos pos, int height, int radius, int offset, IBlockState blockType) {
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
    public static void placeBlockOutlinePreciseCircle(World world, final BlockPos pos, int height, int radius, int offset, IBlockState blockType) {
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
    public static void drawLineBlocks(World world, BlockPos pos, int height, int xAxis, int zAxis, int offset, IBlockState blockType) {
        int end = Math.abs(xAxis);
        for (int i = xAxis; i <= end; i++) {
            world.setBlockState(new BlockPos(pos.getX() + offset + i, pos.getY() + height, pos.getZ() + offset + zAxis),blockType);
        }
    }

    /**
     * Spawns a sphere made of whatever material is specified, at the
     * desired height.
     * @param world             Minecraft world object.
     * @param pos               Position of this block.
     * @param height            Desired height at which
     *                          the foliage will spawn.
     * @param offset            Determines how much you the line
     *                          is translated from the given pos.
     * @param maxFoliageHeight  Maximum height for the foliage.
     * @param blockType         Specify what kind of block it's
     *                          going to be spawned.
     */
    public static void placeFoliage(World world, BlockPos pos, int height, int offset, int maxFoliageHeight, IBlockState blockType) {
        int sphereRadius = 0;
        boolean halfSphereReached = false;
        for (int foliageHeight = height * 2; foliageHeight < maxFoliageHeight * 2 + height * 2; foliageHeight++) {
            for (int i = 0; i < sphereRadius; i++) {
                placeBlockFilledPreciseCircle(world, pos, foliageHeight, i, offset, blockType);
            }

            if (sphereRadius == maxFoliageHeight)
                halfSphereReached = true;

            if (halfSphereReached)
                sphereRadius--;
            else
                sphereRadius++;
        }
    }

    /**
     * Generates a tree without modifying the current
     * state of the block. Used multiple tree spawning.
     * @param world             Minecraft world.
     * @param pos               Position of the reference block.
     * @param ageState          Current state of the reference block.
     * @param trunkSize         Size of the trunk of both trees.
     * @param offset            Determines how much you the line
     *                          is translated from the given pos.
     * @param trunkBlockType    Specifies the material for the trunk.
     * @param foliageBlockType  Specifies the material for the foliage
     * @param ladderBlockType   Specifies the material for the ladder.
     */
    public static void generateTree(World world, BlockPos pos, int ageState,
                              int trunkSize,int maxFoliageHeight, int offset, IBlockState trunkBlockType,
                              IBlockState foliageBlockType, IBlockState ladderBlockType) {

        //Generate the tree's foliage
        if (ageState == 12) {
            placeFoliage(world,pos,ageState,offset,maxFoliageHeight,foliageBlockType);
        }


        if ( ageState % 5 == 0 && ageState != 0) {
            placeBlockFilledPreciseCircle(world,pos,ageState*2,trunkSize*2,offset,trunkBlockType);
            placeBlockOutlinePreciseCircle(world,pos,ageState*2 + 1,trunkSize*2,offset,foliageBlockType);
        }

        for(int i = 0; i <= ageState; i++){
            placeBlockFilledPreciseCircle(world,pos,i*2,trunkSize,offset,trunkBlockType);
            placeBlockFilledPreciseCircle(world,pos,i*2 + 1,trunkSize,offset,trunkBlockType);
            //Place the ladder on the center of one side of the circle
            //Two instructions: one for each floor generated
            world.setBlockState(new BlockPos(pos.getX() + offset,pos.getY() + i*2,pos.getZ() + (offset - trunkSize - 1)),ladderBlockType);
            world.setBlockState(new BlockPos(pos.getX() + offset,pos.getY() + i*2 + 1,pos.getZ() + (offset - trunkSize - 1)),ladderBlockType);
        }

    }
    /**
     * Generates a single tree that will take care of updating .
     * reference block state each time an event hits him.
     * @param world             Minecraft world.
     * @param pos               Position of the reference block.
     * @param trunkSize         Size of the trunk of both trees.
     * @param offset            Determines how much you the line
     *                          is translated from the given pos.
     * @param trunkBlockType    Specifies the material for the trunk.
     * @param foliageBlockType  Specifies the material for the foliage
     * @param ladderBlockType   Specifies the material for the ladder.7
     * @param age               Property of the placed block, needed in order
     *                          to make the trees grow. The new block MUST
     *                          have a PropertyInteger with value range
     *                          from 0 to 15 in order to work with this
     *                          set of functions.
     */
    public static void generateTree(World world, BlockPos pos,IBlockState state,
                              int trunkSize,int maxFoliageHeight, int offset, IBlockState trunkBlockType,
                              IBlockState foliageBlockType, IBlockState ladderBlockType,PropertyInteger age) {

        int currentTreeHeight = world.getBlockState(pos).getValue(age);

        //Generate the tree's foliage
        if (currentTreeHeight == 12) {
            placeFoliage(world,pos,currentTreeHeight,offset,maxFoliageHeight,foliageBlockType);
        }


        if ( currentTreeHeight % 5 == 0 && currentTreeHeight != 0) {
            placeBlockFilledPreciseCircle(world,pos,currentTreeHeight*2,trunkSize*2,offset,trunkBlockType);
            placeBlockOutlinePreciseCircle(world,pos,currentTreeHeight*2 + 1,trunkSize*2,offset,foliageBlockType);
        }

        for(int i = 0; i <= currentTreeHeight; i++){
            placeBlockFilledPreciseCircle(world,pos,i*2,trunkSize,offset,trunkBlockType);
            placeBlockFilledPreciseCircle(world,pos,i*2 + 1,trunkSize,offset,trunkBlockType);
            //Place the ladder on the center of one side of the circle
            //Two instructions: one for each floor generated
            world.setBlockState(new BlockPos(pos.getX() + offset,pos.getY() + i*2,pos.getZ() + (offset - trunkSize - 1)),ladderBlockType);
            world.setBlockState(new BlockPos(pos.getX() + offset,pos.getY() + i*2 + 1,pos.getZ() + (offset - trunkSize - 1)),ladderBlockType);
        }

        if(currentTreeHeight < 15) {
            currentTreeHeight++;
        }
        world.setBlockState(pos,state.withProperty(age,currentTreeHeight));

    }

    /**
     * Generates two adjacent trees distant 4 blocks each
     * for a fancier look.
     * @param world             Minecraft world.
     * @param pos               Position of the reference block.
     * @param state             Current state of the reference block.
     * @param trunkSize         Size of the trunk of both trees.
     * @param offset            Determines how much you the line
     *                          is translated from the given pos.
     * @param trunkBlockType    Specifies the material for the trunk.
     * @param foliageBlockType  Specifies the material for the foliage
     * @param ladderBlockType   Specifies the material for the ladder.
     * @param age               Property of the placed block, needed in order
     *                          to make the trees grow. The new block MUST
     *                          have a PropertyInteger with value range
     *                          from 0 to 15 in order to work with this
     *                          set of functions.
     */
    public static void generateAdjacentTrees(World world, BlockPos pos, IBlockState state,
                                       int trunkSize, int maxFoliageHeight, int offset, IBlockState trunkBlockType,
                                       IBlockState foliageBlockType, IBlockState ladderBlockType,PropertyInteger age) {

        int currentTreeHeight = world.getBlockState(pos).getValue(age);
        generateTree(world,pos,currentTreeHeight,trunkSize,maxFoliageHeight, offset,trunkBlockType,foliageBlockType,ladderBlockType);
        generateTree(world,new BlockPos(pos.getX()-4,pos.getY(),pos.getZ()),currentTreeHeight,trunkSize,maxFoliageHeight,offset,trunkBlockType,foliageBlockType,ladderBlockType);
        if(currentTreeHeight < 15) {
            currentTreeHeight++;
        }
        world.setBlockState(pos,state.withProperty(age,currentTreeHeight));
    }
}
