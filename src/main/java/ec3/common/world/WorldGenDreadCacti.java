package ec3.common.world;

import java.util.Random;

import ec3.common.block.BlocksCore;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDreadCacti extends WorldGenerator
{

    public boolean generate(World p_76484_1_, Random p_76484_2_, BlockPos p_76484_3_)
    {
        for (int l = 0; l < 10; ++l)
        {
            int i1 = p_76484_3_.getX() + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
            int j1 = p_76484_3_.getY()+ p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            int k1 = p_76484_3_.getZ() + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

            if (p_76484_1_.isAirBlock(new BlockPos(i1, j1, k1)))
            {
                int l1 = 1 + p_76484_2_.nextInt(p_76484_2_.nextInt(3) + 1);

                for (int i2 = 0; i2 < l1; ++i2)
                {
                    if (Blocks.CACTUS.canBlockStay(p_76484_1_, new BlockPos(i1, j1 + i2, k1)))
                    {
                        p_76484_1_.setBlockState(new BlockPos(i1, j1 + i2, k1), BlocksCore.cacti.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}