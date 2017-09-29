package ec3.common.world.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ec3.common.block.BlocksCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureModernShaftPieces
{
	public static void registerStructurePieces()
	{
		MapGenStructureIO.registerStructureComponent(StructureModernShaftPieces.Corridor.class, "MMSCorridor");
		MapGenStructureIO.registerStructureComponent(StructureModernShaftPieces.Cross.class, "MMSCrossing");
		MapGenStructureIO.registerStructureComponent(StructureModernShaftPieces.Room.class, "MMSRoom");
		MapGenStructureIO.registerStructureComponent(StructureModernShaftPieces.Stairs.class, "MMSStairs");
	}

	@SuppressWarnings("rawtypes")
	private static StructureComponent getRandomComponent(List p_78815_0_, Random p_78815_1_, int p_78815_2_, int p_78815_3_, int p_78815_4_, EnumFacing p_78815_5_, int p_78815_6_)
	{
		int j1 = p_78815_1_.nextInt(100);
		StructureBoundingBox structureboundingbox;

		if (j1 >= 80)
		{
			structureboundingbox = StructureModernShaftPieces.Cross.findValidPlacement(p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
			{
				return new StructureModernShaftPieces.Cross(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
			}
		}
		else if (j1 >= 70)
		{
			structureboundingbox = StructureModernShaftPieces.Stairs.findValidPlacement(p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
			{
				return new StructureModernShaftPieces.Stairs(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
			}
		}
		else
		{
			structureboundingbox = StructureModernShaftPieces.Corridor.findValidPlacement(p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
			{
				return new StructureModernShaftPieces.Corridor(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
			}
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static StructureComponent getNextMineShaftComponent(StructureComponent p_78817_0_, List p_78817_1_, Random p_78817_2_, int p_78817_3_, int p_78817_4_, int p_78817_5_, EnumFacing p_78817_6_, int p_78817_7_)
	{
		if (p_78817_7_ > 8)
		{
			return null;
		}
		else if (Math.abs(p_78817_3_ - p_78817_0_.getBoundingBox().minX) <= 80 && Math.abs(p_78817_5_ - p_78817_0_.getBoundingBox().minZ) <= 80)
		{
			StructureComponent structurecomponent1 = getRandomComponent(p_78817_1_, p_78817_2_, p_78817_3_, p_78817_4_, p_78817_5_, p_78817_6_, p_78817_7_ + 1);

			if (structurecomponent1 != null)
			{
				p_78817_1_.add(structurecomponent1);
				structurecomponent1.buildComponent(p_78817_0_, p_78817_1_, p_78817_2_);
			}

			return structurecomponent1;
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static class Corridor extends StructureComponent
	{
		private boolean hasRails;
		private boolean hasSpiders;
		private boolean spawnerPlaced;
		/** A count of the different sections of this mine. The space between ceiling supports. */
		private int sectionCount;
		public Corridor() {}

		@Override
		protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
		{
			p_143012_1_.setBoolean("hr", this.hasRails);
			p_143012_1_.setBoolean("sc", this.hasSpiders);
			p_143012_1_.setBoolean("hps", this.spawnerPlaced);
			p_143012_1_.setInteger("Num", this.sectionCount);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
		{
			this.hasRails = p_143011_1_.getBoolean("hr");
			this.hasSpiders = p_143011_1_.getBoolean("sc");
			this.spawnerPlaced = p_143011_1_.getBoolean("hps");
			this.sectionCount = p_143011_1_.getInteger("Num");
		}

		public Corridor(int p_i2035_1_, Random p_i2035_2_, StructureBoundingBox p_i2035_3_, EnumFacing p_i2035_4_)
		{
			super(p_i2035_1_);
			this.setCoordBaseMode(p_i2035_4_);
			this.boundingBox = p_i2035_3_;
			this.hasRails = p_i2035_2_.nextInt(3) == 0;
			this.hasSpiders = !this.hasRails && p_i2035_2_.nextInt(23) == 0;

			if (this.getCoordBaseMode().getAxis() != EnumFacing.Axis.Z)
			{
				this.sectionCount = p_i2035_3_.getXSize() / 5;
			}
			else
			{
				this.sectionCount = p_i2035_3_.getZSize() / 5;
			}
		}

		public static StructureBoundingBox findValidPlacement(List p_74954_0_, Random p_74954_1_, int p_74954_2_, int p_74954_3_, int p_74954_4_, EnumFacing p_74954_5_)
		{
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74954_2_, p_74954_3_, p_74954_4_, p_74954_2_, p_74954_3_ + 2, p_74954_4_);
			int i1;

			for (i1 = p_74954_1_.nextInt(3) + 2; i1 > 0; --i1)
			{
				int j1 = i1 * 5;

				switch (p_74954_5_)
				{
				case SOUTH:
					structureboundingbox.maxX = p_74954_2_ + 2;
					structureboundingbox.maxZ = p_74954_4_ + (j1 - 1);
					break;
				case WEST:
					structureboundingbox.minX = p_74954_2_ - (j1 - 1);
					structureboundingbox.maxZ = p_74954_4_ + 2;
					break;
				case NORTH:
					structureboundingbox.maxX = p_74954_2_ + 2;
					structureboundingbox.minZ = p_74954_4_ - (j1 - 1);
					break;
				case EAST:
					structureboundingbox.maxX = p_74954_2_ + (j1 - 1);
					structureboundingbox.maxZ = p_74954_4_ + 2;
					break;
				default:
					break;
				}

				if (StructureComponent.findIntersecting(p_74954_0_, structureboundingbox) == null)
				{
					break;
				}
			}

			return i1 > 0 ? structureboundingbox : null;
		}

		/**
		 * Initiates construction of the Structure Component picked, at the current Location of StructGen
		 */
		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
		{
			int i = this.getComponentType();
			int j = p_74861_3_.nextInt(4);

			switch (this.getCoordBaseMode())
			{
			case SOUTH:
				if (j <= 1)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, this.getCoordBaseMode(), i);
				}
				else if (j == 2)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
				}
				else
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
				}

				break;
			case WEST:
				if (j <= 1)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.getCoordBaseMode(), i);
				}
				else if (j == 2)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				}
				else
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				}

				break;
			case NORTH:
				if (j <= 1)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, this.getCoordBaseMode(), i);
				}
				else if (j == 2)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
				}
				else
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
				}

				break;
			case EAST:
				if (j <= 1)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.getCoordBaseMode(), i);
				}
				else if (j == 2)
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				}
				else
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				}
			default:
				break;
			}

			if (i < 8)
			{
				int k;
				int l;

				if (this.getCoordBaseMode().getAxis() != EnumFacing.Axis.Z)
				{
					for (k = this.boundingBox.minX + 3; k + 3 <= this.boundingBox.maxX; k += 5)
					{
						l = p_74861_3_.nextInt(5);

						if (l == 0)
						{
							StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, k, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
						}
						else if (l == 1)
						{
							StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
						}
					}
				}
				else
				{
					for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5)
					{
						l = p_74861_3_.nextInt(5);

						if (l == 0)
						{
							StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
						}
						else if (l == 1)
						{
							StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
						}
					}
				}
			}
		}

		/**
		 * Used to generate chests with items in it. ex: Temple Chests, Village Blacksmith Chests, Mineshaft Chests.
		 */
		protected boolean generateChest(World p_74879_1_, StructureBoundingBox p_74879_2_, Random p_74879_3_, int p_74879_4_, int p_74879_5_, int p_74879_6_, int p_74879_8_, ResourceLocation loot)
		{
			int i1 = this.getXWithOffset(p_74879_4_, p_74879_6_);
			int j1 = this.getYWithOffset(p_74879_5_);
			int k1 = this.getZWithOffset(p_74879_4_, p_74879_6_);

			if (p_74879_2_.isVecInside(new BlockPos(i1, j1, k1)) && p_74879_1_.getBlockState(new BlockPos(i1, j1, k1)).getMaterial() == Material.AIR)
			{
				int l1 = p_74879_3_.nextBoolean() ? 1 : 0;
				p_74879_1_.setBlockState(new BlockPos(i1, j1, k1), Blocks.RAIL.getDefaultState(), 2);
				p_74879_1_.setBlockState(new BlockPos(i1, j1-1, k1), BlocksCore.voidStone.getStateFromMeta(0), 2);
				EntityMinecartChest entityminecartchest = new EntityMinecartChest(p_74879_1_, i1 + 0.5F, j1 + 0.5F, k1 + 0.5F);
				entityminecartchest.setLootTable(loot, p_74879_3_.nextLong());
				p_74879_1_.spawnEntity(entityminecartchest);
				return true;
			}
			else
			{
				return false;
			}
		}

		/**
		 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
		 * Mineshafts at the end, it adds Fences...
		 */
		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
		{
			if (this.isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
			{
				return false;
			}
			else
			{
				int i = this.sectionCount * 5 - 1;
				this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 2, 1, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				//random destruction
				this.func_189914_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.8F, 0, 2, 0, 2, 2, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);

				if (this.hasSpiders)
				{
					//webs
					this.func_189914_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.6F, 0, 0, 0, 2, 1, i, Blocks.WEB.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);
				}

				int j;
				int k;

				for (j = 0; j < this.sectionCount; ++j)
				{
					k = 2 + j * 5;
					//fences
					//this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, k, 0, 1, k, Blocks.fence, Blocks.air, false);
					//this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 0, k, 2, 1, k, Blocks.fence, Blocks.air, false);

					if (p_74875_2_.nextInt(4) == 0)
					{
						//supports
						this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, k, 0, 2, k, BlocksCore.voidStone.getStateFromMeta(0), BlocksCore.voidStone.getStateFromMeta(0), false);
						this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 2, k, 2, 2, k, BlocksCore.voidStone.getStateFromMeta(0), BlocksCore.voidStone.getStateFromMeta(0), false);
					}
					else
					{
						this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, k, 2, 2, k, BlocksCore.voidStone.getStateFromMeta(0), Blocks.AIR.getDefaultState(), false);
					}

					//Spawner
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k - 1, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k - 1, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k + 1, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k + 1, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k - 2, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k - 2, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k + 2, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k + 2, Blocks.WEB.getDefaultState());
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k - 1, Blocks.TORCH.getStateFromMeta(0));
					this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k + 1, Blocks.TORCH.getStateFromMeta(0));

					if (p_74875_2_.nextInt(100) == 0)
					{
						this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 2, 0, k - 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
					}

					if (p_74875_2_.nextInt(100) == 0)
					{
						this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 0, 0, k + 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
					}

					if (this.hasSpiders && !this.spawnerPlaced)
					{
						int l = this.getYWithOffset(0);
						int i1 = k - 1 + p_74875_2_.nextInt(3);
						int j1 = this.getXWithOffset(1, i1);
						i1 = this.getZWithOffset(1, i1);

						if (p_74875_3_.isVecInside(new BlockPos(j1, l, i1)))
						{
							this.spawnerPlaced = true;
							p_74875_1_.setBlockState(new BlockPos(j1, l, i1), Blocks.MOB_SPAWNER.getDefaultState(), 2);
							TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)p_74875_1_.getTileEntity(new BlockPos(j1, l, i1));

							if (tileentitymobspawner != null)
							{
								tileentitymobspawner.getSpawnerBaseLogic().setEntityName("CaveSpider");
							}
						}
					}
				}

				for (j = 0; j <= 2; ++j)
				{
					for (k = 0; k <= i; ++k)
					{
						byte b0 = -1;
						IBlockState block1 = this.getBlockStateFromPos(p_74875_1_, j, b0, k, p_74875_3_);

						if (block1.getMaterial() == Material.AIR)
						{
							byte b1 = -1;
							this.setBlockState(p_74875_1_, BlocksCore.voidStone.getStateFromMeta(0), j, b1, k, p_74875_3_);
						}
					}
				}

				if (this.hasRails)
				{
					for (j = 0; j <= i; ++j)
					{
						this.getBlockStateFromPos(p_74875_1_, 1, -1, j, p_74875_3_);

						if (true)
						{
							this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, 1, 0, j, Blocks.RAIL.getDefaultState());
							this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, 0, -1, j, BlocksCore.voidStone.getStateFromMeta(0));
							this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, 1, -1, j, BlocksCore.voidStone.getStateFromMeta(0));
							this.randomlyPlaceBlock(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, -1, -1, j, BlocksCore.voidStone.getStateFromMeta(0));
						}
					}
				}

				return true;
			}
		}
	}

	public static class Cross extends StructureComponent
	{
		private EnumFacing corridorDirection;
		private boolean isMultipleFloors;
		public Cross() {}

		@Override
		protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
		{
			p_143012_1_.setBoolean("tf", this.isMultipleFloors);
			p_143012_1_.setInteger("D", this.corridorDirection.getIndex());
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
		{
			this.isMultipleFloors = p_143011_1_.getBoolean("tf");
			this.corridorDirection = EnumFacing.getFront(p_143011_1_.getInteger("D"));
		}

		public Cross(int p_i2036_1_, Random p_i2036_2_, StructureBoundingBox p_i2036_3_, EnumFacing p_i2036_4_)
		{
			super(p_i2036_1_);
			this.corridorDirection = p_i2036_4_;
			this.boundingBox = p_i2036_3_;
			this.isMultipleFloors = p_i2036_3_.getYSize() > 3;
		}
		@SuppressWarnings({ "rawtypes" })
		public static StructureBoundingBox findValidPlacement(List p_74951_0_, Random p_74951_1_, int p_74951_2_, int p_74951_3_, int p_74951_4_, EnumFacing p_74951_5_)
		{
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74951_2_, p_74951_3_, p_74951_4_, p_74951_2_, p_74951_3_ + 2, p_74951_4_);

			if (p_74951_1_.nextInt(4) == 0)
			{
				structureboundingbox.maxY += 4;
			}

			switch (p_74951_5_)
			{
			case SOUTH:
				structureboundingbox.minX = p_74951_2_ - 1;
				structureboundingbox.maxX = p_74951_2_ + 3;
				structureboundingbox.maxZ = p_74951_4_ + 4;
				break;
			case WEST:
				structureboundingbox.minX = p_74951_2_ - 4;
				structureboundingbox.minZ = p_74951_4_ - 1;
				structureboundingbox.maxZ = p_74951_4_ + 3;
				break;
			case NORTH:
				structureboundingbox.minX = p_74951_2_ - 1;
				structureboundingbox.maxX = p_74951_2_ + 3;
				structureboundingbox.minZ = p_74951_4_ - 4;
				break;
			case EAST:
				structureboundingbox.maxX = p_74951_2_ + 4;
				structureboundingbox.minZ = p_74951_4_ - 1;
				structureboundingbox.maxZ = p_74951_4_ + 3;
				break;
			default:
				break;
			}

			return StructureComponent.findIntersecting(p_74951_0_, structureboundingbox) != null ? null : structureboundingbox;
		}

		/**
		 * Initiates construction of the Structure Component picked, at the current Location of StructGen
		 */
		@Override
		@SuppressWarnings({ "rawtypes" })
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
		{
			int i = this.getComponentType();

			switch (this.corridorDirection)
			{
			case SOUTH:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				break;
			case WEST:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				break;
			case NORTH:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				break;
			case EAST:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				break;
			default:
				break;
			}

			if (this.isMultipleFloors)
			{
				if (p_74861_3_.nextBoolean())
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				}

				if (p_74861_3_.nextBoolean())
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				}

				if (p_74861_3_.nextBoolean())
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				}

				if (p_74861_3_.nextBoolean())
				{
					StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				}
			}
		}

		/**
		 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
		 * Mineshafts at the end, it adds Fences...
		 */
		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
		{
			if (this.isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
			{
				return false;
			}
			else
			{
				if (this.isMultipleFloors)
				{
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				}
				else
				{
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
					this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				}

				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, BlocksCore.voidStone.getStateFromMeta(0), Blocks.AIR.getDefaultState(), false);
				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, BlocksCore.voidStone.getStateFromMeta(0), Blocks.AIR.getDefaultState(), false);
				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, BlocksCore.voidStone.getStateFromMeta(0), Blocks.AIR.getDefaultState(), false);
				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, BlocksCore.voidStone.getStateFromMeta(0), Blocks.AIR.getDefaultState(), false);

				for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
				{
					for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
					{
						if (this.getBlockStateFromPos(p_74875_1_, i, this.boundingBox.minY - 1, j, p_74875_3_).getMaterial() == Material.AIR)
						{
							this.setBlockState(p_74875_1_, BlocksCore.voidStone.getStateFromMeta(0), i, this.boundingBox.minY - 1, j, p_74875_3_);
						}
					}
				}

				return true;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static class Room extends StructureComponent
	{
		/** List of other Mineshaft components linked to this room. */
		private List roomsLinkedToTheRoom = new LinkedList();
		public Room() {}

		public Room(int p_i2037_1_, Random p_i2037_2_, int p_i2037_3_, int p_i2037_4_)
		{
			super(p_i2037_1_);
			this.boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_, p_i2037_3_ + 7 + p_i2037_2_.nextInt(6), 54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7 + p_i2037_2_.nextInt(6));
		}

		/**
		 * Initiates construction of the Structure Component picked, at the current Location of StructGen
		 */
		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
		{
			int i = this.getComponentType();
			int k = this.boundingBox.getYSize() - 3 - 1;

			if (k <= 0)
			{
				k = 1;
			}

			int j;
			StructureComponent structurecomponent1;
			StructureBoundingBox structureboundingbox;

			for (j = 0; j < this.boundingBox.getXSize(); j += 4)
			{
				j += p_74861_3_.nextInt(this.boundingBox.getXSize());

				if (j + 3 > this.boundingBox.getXSize())
				{
					break;
				}

				structurecomponent1 = StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + j, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);

				if (structurecomponent1 != null)
				{
					structureboundingbox = structurecomponent1.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
				}
			}

			for (j = 0; j < this.boundingBox.getXSize(); j += 4)
			{
				j += p_74861_3_.nextInt(this.boundingBox.getXSize());

				if (j + 3 > this.boundingBox.getXSize())
				{
					break;
				}

				structurecomponent1 = StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + j, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);

				if (structurecomponent1 != null)
				{
					structureboundingbox = structurecomponent1.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.maxZ - 1, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.maxZ));
				}
			}

			for (j = 0; j < this.boundingBox.getZSize(); j += 4)
			{
				j += p_74861_3_.nextInt(this.boundingBox.getZSize());

				if (j + 3 > this.boundingBox.getZSize())
				{
					break;
				}

				structurecomponent1 = StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ + j, EnumFacing.WEST, i);

				if (structurecomponent1 != null)
				{
					structureboundingbox = structurecomponent1.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.minX + 1, structureboundingbox.maxY, structureboundingbox.maxZ));
				}
			}

			for (j = 0; j < this.boundingBox.getZSize(); j += 4)
			{
				j += p_74861_3_.nextInt(this.boundingBox.getZSize());

				if (j + 3 > this.boundingBox.getZSize())
				{
					break;
				}

				structurecomponent1 = StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ + j, EnumFacing.EAST, i);

				if (structurecomponent1 != null)
				{
					structureboundingbox = structurecomponent1.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ));
				}
			}
		}

		/**
		 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
		 * Mineshafts at the end, it adds Fences...
		 */
		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
		{
			if (this.isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
			{
				return false;
			}
			else
			{
				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.DIRT.getStateFromMeta(0), Blocks.AIR.getDefaultState(), true);
				this.fillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				Iterator iterator = this.roomsLinkedToTheRoom.iterator();

				while (iterator.hasNext())
				{
					StructureBoundingBox structureboundingbox1 = (StructureBoundingBox)iterator.next();
					this.fillWithBlocks(p_74875_1_, p_74875_3_, structureboundingbox1.minX, structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY, structureboundingbox1.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				}

				this.randomlyRareFillWithBlocks(p_74875_1_, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), false);
				return true;
			}
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
		{
			NBTTagList nbttaglist = new NBTTagList();
			Iterator iterator = this.roomsLinkedToTheRoom.iterator();

			while (iterator.hasNext())
			{
				StructureBoundingBox structureboundingbox = (StructureBoundingBox)iterator.next();
				nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
			}

			p_143012_1_.setTag("Entrances", nbttaglist);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
		{
			NBTTagList nbttaglist = p_143011_1_.getTagList("Entrances", 11);

			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
			}
		}
	}

	public static class Stairs extends StructureComponent
	{
		public Stairs() {}

		public Stairs(int p_i2038_1_, Random p_i2038_2_, StructureBoundingBox p_i2038_3_, EnumFacing p_i2038_4_)
		{
			super(p_i2038_1_);
			this.setCoordBaseMode(p_i2038_4_);
			this.boundingBox = p_i2038_3_;
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {}

		@Override
		protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {}

		/**
		 * Trys to find a valid place to put this component.
		 */
		@SuppressWarnings({ "rawtypes" })
		public static StructureBoundingBox findValidPlacement(List p_74950_0_, Random p_74950_1_, int p_74950_2_, int p_74950_3_, int p_74950_4_, EnumFacing p_74950_5_)
		{
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74950_2_, p_74950_3_ - 5, p_74950_4_, p_74950_2_, p_74950_3_ + 2, p_74950_4_);

			switch (p_74950_5_)
			{
			case SOUTH:
				structureboundingbox.maxX = p_74950_2_ + 2;
				structureboundingbox.maxZ = p_74950_4_ + 8;
				break;
			case WEST:
				structureboundingbox.minX = p_74950_2_ - 8;
				structureboundingbox.maxZ = p_74950_4_ + 2;
				break;
			case NORTH:
				structureboundingbox.maxX = p_74950_2_ + 2;
				structureboundingbox.minZ = p_74950_4_ - 8;
				break;
			case EAST:
				structureboundingbox.maxX = p_74950_2_ + 8;
				structureboundingbox.maxZ = p_74950_4_ + 2;
				break;
			default:
				break;
			}

			return StructureComponent.findIntersecting(p_74950_0_, structureboundingbox) != null ? null : structureboundingbox;
		}

		/**
		 * Initiates construction of the Structure Component picked, at the current Location of StructGen
		 */
		@Override
		@SuppressWarnings({ "rawtypes" })
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
		{
			int i = this.getComponentType();

			switch (this.getCoordBaseMode())
			{
			case SOUTH:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				break;
			case WEST:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
				break;
			case NORTH:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				break;
			case EAST:
				StructureModernShaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
				break;
			default:
				break;
			}
		}

		/**
		 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
		 * Mineshafts at the end, it adds Fences...
		 */
		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
		{
			if (this.isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
			{
				return false;
			}
			else
			{
				this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 0, 2, 7, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 7, 2, 2, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

				for (int i = -20; i < 5; ++i)
				{
					this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				}

				return true;
			}
		}
	}
}