package essentialcraft.common.world.gen.structure;

import java.util.List;
import java.util.Random;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.WeightedRandomChestContent;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBaublesResistance;
import essentialcraft.common.registry.LootTableRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureOldCatacombs {

	public static class Start extends StructureStart {

		public Start() {}

		public Start(World world, Random rand, int chunkX, int chunkZ) {
			super(chunkX, chunkZ);
			int x = (chunkX << 4) + 2;
			int y = MathHelper.getInt(rand, 6, 32);
			int z = (chunkZ << 4) + 2;
			StructureBoundingBox structureBB = new StructureBoundingBox(x, y, z, x+4, y+4, z+4);
			StructureOldCatacombs.Room room = new StructureOldCatacombs.Room(0, rand, structureBB, EnumFacing.DOWN);
			this.components.add(room);
			room.buildComponent(room, this.components, rand);
			this.updateBoundingBox();
		}
	}

	public static void registerCatacombComponents() {
		MapGenStructureIO.registerStructureComponent(StructureOldCatacombs.Room.class, "OCRoom");
		MapGenStructureIO.registerStructureComponent(StructureOldCatacombs.Corridor.class, "OCCorridor");
	}

	public static class Room extends StructureComponent {

		public EnumFacing fromDirection;
		public boolean broken;
		public boolean grown;
		public boolean generateExit;

		public boolean south = false;
		public boolean west = false;
		public boolean north = false;
		public boolean east = false;

		public Room() {}

		public Room(int index, Random rand, StructureBoundingBox structureBB, EnumFacing facing) {
			super(index);
			this.setCoordBaseMode(EnumFacing.SOUTH);

			this.boundingBox = structureBB;
			this.fromDirection = facing;
			this.broken = rand.nextDouble() < 0.125D;
			this.grown = rand.nextDouble() < 0.125D;
			this.generateExit = rand.nextDouble() < 0.0625D;

			switch(facing) {
			case SOUTH:
				this.south = true;
				break;
			case WEST:
				this.west = true;
				break;
			case NORTH:
				this.north = true;
				break;
			case EAST:
				this.east = true;
				break;
			default:
				break;
			}
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound) {
			tagCompound.setInteger("Direction", this.fromDirection.getIndex());

			tagCompound.setBoolean("Broken", this.broken);
			tagCompound.setBoolean("Grown", this.grown);
			tagCompound.setBoolean("Exit", this.generateExit);

			tagCompound.setBoolean("South", this.south);
			tagCompound.setBoolean("West", this.west);
			tagCompound.setBoolean("North", this.north);
			tagCompound.setBoolean("East", this.east);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager tm) {
			this.fromDirection = EnumFacing.getFront(tagCompound.getInteger("Direction"));

			this.broken = tagCompound.getBoolean("Broken");
			this.grown = tagCompound.getBoolean("Grown");
			this.generateExit = tagCompound.getBoolean("Exit");

			this.south = tagCompound.getBoolean("South");
			this.west = tagCompound.getBoolean("West");
			this.north = tagCompound.getBoolean("North");
			this.east = tagCompound.getBoolean("East");
		}

		public static StructureBoundingBox getValidPlacement(List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing) {
			StructureBoundingBox structureBB = new StructureBoundingBox(x, y, z, x, y+4, z);

			switch(facing) {
			case SOUTH:
				structureBB.maxX = x+4;
				structureBB.maxZ = z+4;
				break;
			case WEST:
				structureBB.minX = x-4;
				structureBB.maxZ = z+4;
				break;
			case NORTH:
			default:
				structureBB.maxX = x+4;
				structureBB.minZ = z-4;
				break;
			case EAST:
				structureBB.maxX = x+4;
				structureBB.maxZ = z+4;
				break;
			}

			return StructureComponent.findIntersecting(components, structureBB) != null ? null : structureBB;
		}

		@Override
		public void buildComponent(StructureComponent parent, List<StructureComponent> compenents, Random rand) {
			int index = this.getComponentType();
			if(index > 7) {
				return;
			}
			if(this.fromDirection != EnumFacing.SOUTH) {
				StructureBoundingBox structureBB = StructureOldCatacombs.Corridor.getValidPlacement(compenents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ+1, EnumFacing.SOUTH);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Corridor(index+1, structureBB, EnumFacing.SOUTH);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
					this.south = true;
				}
			}
			if(this.fromDirection != EnumFacing.WEST) {
				StructureBoundingBox structureBB = StructureOldCatacombs.Corridor.getValidPlacement(compenents, rand, this.boundingBox.minX-1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Corridor(index+1, structureBB, EnumFacing.WEST);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
					this.west = true;
				}
			}
			if(this.fromDirection != EnumFacing.NORTH) {
				StructureBoundingBox structureBB = StructureOldCatacombs.Corridor.getValidPlacement(compenents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ-1, EnumFacing.NORTH);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Corridor(index+1, structureBB, EnumFacing.NORTH);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
					this.north = true;
				}
			}
			if(this.fromDirection != EnumFacing.EAST) {
				StructureBoundingBox structureBB = StructureOldCatacombs.Corridor.getValidPlacement(compenents, rand, this.boundingBox.maxX+1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Corridor(index+1, structureBB, EnumFacing.EAST);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
					this.east = true;
				}
			}
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			this.fillWithBlocks(world, structureBB, 0, 0, 0, 4, 4, 4, BlocksCore.fortifiedStone.getDefaultState(), Blocks.AIR.getDefaultState(), false);
			if(this.south) {
				this.fillWithAir(world, structureBB, 1, 1, 4, 3, 3, 4);
			}
			if(this.west) {
				this.fillWithAir(world, structureBB, 0, 1, 1, 0, 3, 3);
			}
			if(this.north) {
				this.fillWithAir(world, structureBB, 1, 1, 0, 3, 3, 0);
			}
			if(this.east) {
				this.fillWithAir(world, structureBB, 4, 1, 1, 4, 3, 3);
			}

			if(this.generateExit) {
				int i;
				for(i = 10; i < 256; ++i) {
					if(this.getBlockStateFromPos(world, 2, i, 2, structureBB).getMaterial() == Material.AIR) {
						break;
					}
				}
				this.fillWithBlocks(world, structureBB, 0, 4, 0, 4, i, 4, BlocksCore.fortifiedStone.getDefaultState(), Blocks.AIR.getDefaultState(), false);
				this.fillWithAir(world, structureBB, 1, 4, 1, 3, 4, 3);
				this.fillWithAir(world, structureBB, 1, i, 1, 3, i, 3);
				this.fillWithBlocks(world, structureBB, 1, 4, 2, 1, i, 2, Blocks.LADDER.getStateFromMeta(5), Blocks.AIR.getDefaultState(), false);

				this.generateChest(world, structureBB, rand, 2, 2, 2, LootTableRegistry.CHEST_CATACOMBS);
				this.setBlockState(world, BlocksCore.voidStone.getDefaultState(), 2, 1, 2, structureBB);
			}

			if(this.broken) {
				for(int i = 1; i < 4; ++i) {
					for(int j = 1; j < 4; ++j) {
						for(int k = 1; k < 4; ++k) {
							if(rand.nextInt(j+3) == 0) {
								this.setBlockState(world, BlocksCore.concrete.getDefaultState(), i, j, k, structureBB);
							}
						}
					}
				}
			}

			if(this.grown) {
				Vec3d rootVec = new Vec3d(MathUtils.randomDouble(rand)*3, -6, MathUtils.randomDouble(rand)*3);
				for(int vi = 0; vi <= 6; ++vi) {
					this.setBlockState(world, BlocksCore.root.getDefaultState(), 3+(int)(rootVec.x/vi), 1+(int)(rootVec.x/vi), 3+(int)(rootVec.x/vi), structureBB);
				}
				for(int i = 0; i < 5; ++i) {
					for(int j = 0; j < 5; ++j) {
						for(int k = 0; k < 5; ++k) {
							if(rand.nextInt(3) == 0) {
								Block b = this.getBlockStateFromPos(world, i, j, k, structureBB).getBlock();
								if(b != Blocks.AIR && b != BlocksCore.concrete && b != BlocksCore.root) {
									this.setBlockState(world, Blocks.LEAVES.getStateFromMeta(4), i, j, k, structureBB);
								}
							}
						}
					}
				}
			}

			return true;
		}
	}

	public static class Corridor extends StructureComponent {

		public static int corridorMinLength = 32;
		public static int corridorMaxLength = 64;

		public EnumFacing direction;

		public Corridor() {}

		public Corridor(int index, StructureBoundingBox structureBB, EnumFacing facing) {
			super(index);
			this.setCoordBaseMode(EnumFacing.SOUTH);

			this.boundingBox = structureBB;
			this.direction = facing;
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound) {
			tagCompound.setInteger("Direction", this.direction.getIndex());
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager tm) {
			this.direction = EnumFacing.getFront(tagCompound.getInteger("Direction"));
		}

		public static StructureBoundingBox getValidPlacement(List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing) {
			StructureBoundingBox structureBB = new StructureBoundingBox(x, y, z, x, y+4, z);
			int i = rand.nextInt(corridorMaxLength-corridorMinLength) + corridorMinLength;

			switch(facing) {
			case SOUTH:
				structureBB.maxX = x+4;
				structureBB.maxZ = z+i-1;
				break;
			case WEST:
				structureBB.minX = x-i+1;
				structureBB.maxZ = z+4;
				break;
			case NORTH:
			default:
				structureBB.maxX = x+4;
				structureBB.minZ = z-i+1;
				break;
			case EAST:
				structureBB.maxX = x+i-1;
				structureBB.maxZ = z+4;
				break;
			}

			return StructureComponent.findIntersecting(components, structureBB) != null ? null : structureBB;
		}

		@Override
		public void buildComponent(StructureComponent parent, List<StructureComponent> compenents, Random rand) {
			int index = this.getComponentType();
			if(index > 6) {
				return;
			}
			switch(this.direction) {
			case SOUTH: {
				StructureBoundingBox structureBB = StructureOldCatacombs.Room.getValidPlacement(compenents, rand, this.boundingBox.maxX+1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.SOUTH);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Room(index+1, rand, structureBB, EnumFacing.NORTH);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
				}
				break;
			}
			case WEST: {
				StructureBoundingBox structureBB = StructureOldCatacombs.Room.getValidPlacement(compenents, rand, this.boundingBox.minX-1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Room(index+1, rand, structureBB, EnumFacing.EAST);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
				}
				break;
			}
			case NORTH:
			default: {
				StructureBoundingBox structureBB = StructureOldCatacombs.Room.getValidPlacement(compenents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ-1, EnumFacing.NORTH);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Room(index+1, rand, structureBB, EnumFacing.SOUTH);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
				}
				break;
			}
			case EAST: {
				StructureBoundingBox structureBB = StructureOldCatacombs.Room.getValidPlacement(compenents, rand, this.boundingBox.maxX+1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
				if(structureBB != null) {
					StructureComponent component = new StructureOldCatacombs.Room(index+1, rand, structureBB, EnumFacing.WEST);
					compenents.add(component);
					component.buildComponent(this, compenents, rand);
				}
				break;
			}
			}
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			this.fillWithBlocks(world, structureBB, 0, 0, 0, this.boundingBox.getXSize()-1, 4, this.boundingBox.getZSize()-1, BlocksCore.fortifiedStone.getDefaultState(), Blocks.AIR.getDefaultState(), false);
			switch(this.direction.getAxis()) {
			case X:
				this.fillWithAir(world, structureBB, 0, 1, 1, 0, 3, 3);
				this.fillWithAir(world, structureBB, this.boundingBox.getXSize()-1, 1, 1, this.boundingBox.getXSize()-1, 3, 3);
				break;
			case Z:
				this.fillWithAir(world, structureBB, 1, 1, 0, 3, 3, 0);
				this.fillWithAir(world, structureBB, 1, 1, this.boundingBox.getZSize()-1, 3, 3, this.boundingBox.getZSize()-1);
				break;
			default:
				break;
			}
			return true;
		}
	}
}
