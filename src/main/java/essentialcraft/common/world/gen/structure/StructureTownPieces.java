package essentialcraft.common.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import DummyCore.Utils.WeightedRandomChestContent;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBaublesResistance;
import essentialcraft.common.registry.BiomeRegistry;
import essentialcraft.common.registry.LootTableRegistry;
import essentialcraft.common.world.gen.ECExplosion;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class StructureTownPieces {

	public static void registerTownComponents() {
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Start.class, "ToStart");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Intersection.class, "ToCross");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Path.class, "ToRoad");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Tower.class, "ToTower");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.House1.class, "ToHouse1");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.House2.class, "ToHouse2");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.House3.class, "ToHouse3");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.House4.class, "ToHouse4");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Torch.class, "ToLight");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Spreader.class, "ToSpreader");
		MapGenStructureIO.registerStructureComponent(StructureTownPieces.Explosion.class, "ToExplosion");
	}

	public static List<StructureTownPieces.PieceWeight> getStructureTownWeightedPieceList() {
		ArrayList<StructureTownPieces.PieceWeight> arraylist = new ArrayList<StructureTownPieces.PieceWeight>();
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.House4.class, 4, 30));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.Tower.class, 20, 10));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.House1.class, 20, 30));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.Explosion.class, 3, 100));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.Spreader.class, 15, 60));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.House2.class, 15, 30));
		arraylist.add(new StructureTownPieces.PieceWeight(StructureTownPieces.House3.class, 8, 30));

		Iterator<StructureTownPieces.PieceWeight> iterator = arraylist.iterator();

		while(iterator.hasNext()) {
			if(iterator.next().townPiecesLimit == 0) {
				iterator.remove();
			}
		}

		return arraylist;
	}

	private static int updatePieceWeight(List<StructureTownPieces.PieceWeight> weights) {
		boolean flag = false;
		int i = 0;

		for(StructureTownPieces.PieceWeight pieceweight : weights) {
			if(pieceweight.townPiecesLimit > 0 && pieceweight.townPiecesSpawned < pieceweight.townPiecesLimit) {
				flag = true;
			}
			i += pieceweight.townPieceWeight;
		}

		return flag ? i : -1;
	}

	private static StructureTownPieces.Town findAndCreateComponentFactory(StructureTownPieces.Start start, StructureTownPieces.PieceWeight weight, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
		Class<? extends StructureTownPieces.Town> oclass = weight.townPieceClass;
		StructureTownPieces.Town object = null;

		if(oclass == StructureTownPieces.House4.class) {
			object = StructureTownPieces.House4.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.Tower.class) {
			object = StructureTownPieces.Tower.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.House1.class) {
			object = StructureTownPieces.House1.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.Explosion.class) {
			object = StructureTownPieces.Explosion.findPieceBox(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.Spreader.class) {
			object = StructureTownPieces.Spreader.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.House2.class) {
			object = StructureTownPieces.House2.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}
		else if(oclass == StructureTownPieces.House3.class) {
			object = StructureTownPieces.House3.createPiece(start, components, rand, sMinX, sMinY, sMinZ, facing, index);
		}

		return object;
	}

	private static StructureTownPieces.Town generateComponent(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
		int j1 = updatePieceWeight(start.structureTownWeightedPieceList);

		if(j1 <= 0) {
			return null;
		}
		else {
			int k1 = 0;

			while(k1 < 5) {
				++k1;
				int l1 = rand.nextInt(j1);
				for(StructureTownPieces.PieceWeight pieceweight : start.structureTownWeightedPieceList) {
					l1 -= pieceweight.townPieceWeight;

					if(l1 < 0) {
						if(!pieceweight.canSpawnMoreTownPiecesOfType(index) || pieceweight == start.structTownPieceWeight && start.structureTownWeightedPieceList.size() > 1) {
							break;
						}

						StructureTownPieces.Town town = findAndCreateComponentFactory(start, pieceweight, components, rand, sMinX, sMinY, sMinZ, facing, index);

						if(town != null) {
							++pieceweight.townPiecesSpawned;
							start.structTownPieceWeight = pieceweight;

							if(!pieceweight.canSpawnMoreTownPieces()) {
								start.structureTownWeightedPieceList.remove(pieceweight);
							}

							return town;
						}
					}
				}
			}

			StructureBoundingBox structureboundingbox = StructureTownPieces.Torch.findPieceBox(start, components, sMinX, sMinY, sMinZ, facing);

			if(structureboundingbox != null) {
				return new StructureTownPieces.Torch(start, index, structureboundingbox, facing);
			}
			else {
				return null;
			}
		}
	}

	private static StructureComponent generateAndAddComponent(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
		if(index > 500) {
			return null;
		}
		else if (Math.abs(sMinX - start.getBoundingBox().minX) <= 600 && Math.abs(sMinZ - start.getBoundingBox().minZ) <= 600) {
			StructureTownPieces.Town town = generateComponent(start, components, rand, sMinX, sMinY, sMinZ, facing, index + 1);

			if(town != null) {
				components.add(town);
				start.pendingHouses.add(town);
				return town;
			}

			return null;
		}
		else {
			return null;
		}
	}

	private static StructureComponent generateAndAddRoadPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
		if(index > 50 + start.terrainType) {
			return null;
		}
		else if(Math.abs(sMinX - start.getBoundingBox().minX) <= 600 && Math.abs(sMinZ - start.getBoundingBox().minZ) <= 600) {
			StructureBoundingBox structureboundingbox = StructureTownPieces.Path.findPieceBox(start, components, rand, sMinX, sMinY, sMinZ, facing);

			if(structureboundingbox != null && structureboundingbox.minY > 10) {
				StructureTownPieces.Path path = new StructureTownPieces.Path(start, index, rand, structureboundingbox, facing);
				components.add(path);
				start.pendingRoads.add(path);
				return path;
			}

			return null;
		}
		else {
			return null;
		}
	}

	public static class Tower extends StructureTownPieces.Town {

		public Tower() {}

		public Tower(StructureTownPieces.Start start, int index, Random rand, StructureBoundingBox structureBB, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
		}

		public static StructureTownPieces.Tower createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 9, 64, 9, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.Tower(start, index, rand, structureboundingbox, facing) : null;
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 63, 0);
			}

			for(int i = 0; i < 4; ++i) {
				int rad = 4-i;
				for(int dy = 0; dy < 16; ++dy) {
					for(int dx = 0; dx <= rad*2; ++dx) {
						for(int dz = 0; dz <= rad*2; ++dz) {
							BlockPos p = new BlockPos(i+dx, i*16+dy, i+dz);
							if(dx == rad+1 && dz == rad+1 || dx == rad-1 && dz == rad-1 || dx == rad+1 && dz == rad-1 || dx == rad-1 && dz == rad+1) {
								this.setBlockState(world, BlocksCore.fence[1].getDefaultState(), i+dx, i*16+dy, i+dz, structureBB);
								if(rad == 4 && dy == 0) {
									this.replaceAirAndLiquidDownwards(world, BlocksCore.fence[1].getDefaultState(), dx, -1, dz, structureBB);
								}
							}
							if((dx == rad*2 || dx == 0 || dz == 0 || dz == rad*2) && dy == 8) {
								this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+dx, i*16+dy, i+dz, structureBB);
								if(rad == 1 && (dx == rad || dz == rad)) {
									this.generateChest(world, structureBB, rand, i+dx, i*16+dy+1, i+dz, LootTableRegistry.CHEST_TOWN_TOWER);
								}
							}
							if((dx == 0 || dx == rad*2) && (dz == 0 || dz == rad*2)) {
								this.setBlockState(world, BlocksCore.fence[2].getDefaultState(), i+dx, i*16+dy, i+dz, structureBB);
								if(dy == 0 && rad == 4) {
									this.replaceAirAndLiquidDownwards(world, BlocksCore.fence[2].getDefaultState(), dx, -1, dz, structureBB);
								}
							}
						}
					}
					if(rad > 1) {
						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+1, i*16+dy, i, structureBB);
						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i, i*16+dy, i+1, structureBB);
						if(dy == 15) {
							this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+1, i*16+dy, i+1, structureBB);
						}

						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2-1, i*16+dy, i, structureBB);
						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2, i*16+dy, i+1, structureBB);
						if(dy == 15) {
							this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2-1, i*16+dy, i+1, structureBB);
						}

						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+1, i*16+dy, i+rad*2, structureBB);
						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i, i*16+dy, i+rad*2-1, structureBB);
						if(dy == 15) {
							this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+1, i*16+dy, i+rad*2-1, structureBB);
						}

						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2-1, i*16+dy, i+rad*2, structureBB);
						this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2, i*16+dy, i+rad*2-1, structureBB);
						if(dy == 15) {
							this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), i+rad*2-1, i*16+dy, i+rad*2-1, structureBB);
						}
					}
				}
				if(rad == 4) {
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 1, -1, 0, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 0, -1, 1, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 8, -1, 0, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 8, -1, 1, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 1, -1, 8, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 0, -1, 7, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 7, -1, 8, structureBB);
					this.replaceAirAndLiquidDownwards(world, BlocksCore.fortifiedStone.getDefaultState(), 8, -1, 7, structureBB);
				}
			}

			return true;
		}
	}

	public static class Spreader extends StructureTownPieces.Town {

		public Spreader() {}

		public Spreader(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
		}

		public static StructureTownPieces.Spreader createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 3, 7, 3, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.Spreader(start, index, structureboundingbox, facing) : null;
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6, 0);
			}

			this.fillWithBlocks(world, structureBB, 0, 0, 0, 2, 6, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
			for(int dx = 0; dx < 3; ++dx) {
				for(int dz = 0; dz < 3; ++dz) {
					for(int dy = 0; dy < 7; ++dy) {
						if(dy == 0) {
							if(dx == 1 && dz == 1) {
								this.setBlockState(world, BlocksCore.magicPlating.getDefaultState(), dx, 0, dz, structureBB);
								if(this.getBlockStateFromPos(world, dx, -1, dz, structureBB).getMaterial() == Material.AIR) {
									this.setBlockState(world, BlocksCore.magicPlating.getDefaultState(), dx, -1, dz, structureBB);
								}
								else {
									this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), dx, -1, dz, structureBB);
								}
							}
							else {
								this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), dx, -1, dz, structureBB);
							}
						}
						else if((dx == 0 || dx == 2) && (dz == 0 || dz == 2) && dy < 5) {
							this.setBlockState(world, BlocksCore.fence[2].getDefaultState(), dx, dy, dz, structureBB);
						}
						else if(dx == 1 && dz == 1 && dy < 6) {
							this.setBlockState(world, BlocksCore.fence[1].getDefaultState(), dx, dy, dz, structureBB);
						}
						else if(dx == 1 && dz == 1 && dy == 6) {
							this.setBlockState(world, BlocksCore.spreader.getDefaultState(), dx, dy, dz, structureBB);
						}
					}
				}
			}

			return true;
		}
	}

	public static class House extends StructureTownPieces.Town {

		public int radius;
		public int floors;

		public House() {}

		public House(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, int floors, int radius, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
			this.floors = floors;
			this.radius = radius;
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt) {
			super.writeStructureToNBT(nbt);
			nbt.setInteger("F", floors);
			nbt.setInteger("R", radius);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager tm) {
			super.readStructureFromNBT(nbt, tm);
			floors = nbt.getInteger("F");
			radius = nbt.getInteger("R");
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + floors*5, 0);
			}

			for(int i = 0; i <= floors; ++i) {
				for(int dx = 0; dx <= radius*2; ++dx) {
					for(int dz = 0; dz <= radius*2; ++dz) {
						if(i == 0) {
							if((dx == 0 || dx == radius*2) && (dz == 0 || dz == radius*2) || dx == 0 && dz == 0) {
								this.setBlockState(world, BlocksCore.levitator.getDefaultState(), dx, -1, dz, structureBB);
							}
						}
						for(int dy = 0; dy < 5; ++dy) {
							if(this.getBlockStateFromPos(world, dx, dy, dz, structureBB).getBlock() != Blocks.WATER) {
								this.setBlockState(world, Blocks.AIR.getDefaultState(), dx, i*5+dy, dz, structureBB);
							}
							int tryInt = dy+1;
							if(rand.nextInt(tryInt) == 0) {
								this.setBlockState(world, BlocksCore.concrete.getDefaultState(), dx, i*5+dy, dz, structureBB);
							}
							if(dy == 0 || dy == 4) {
								this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), dx, i*5+dy, dz, structureBB);
							}
							if(dx == 0 || dx == radius*2) {
								this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), dx, i*5+dy, dz, structureBB);
								if(dy > 0 && dy < 4 && dz > 1 && dz < radius*2-1) {
									this.setBlockState(world, BlocksCore.fortifiedGlass.getDefaultState(), dx, i*5+dy, dz, structureBB);
								}
							}
							if(dz == 0 || dz == radius*2) {
								this.setBlockState(world, BlocksCore.fortifiedStone.getDefaultState(), dx, i*5+dy, dz, structureBB);
								if(dy > 0 && dy < 4 && dx > 1 && dx < radius*2-1) {
									this.setBlockState(world, BlocksCore.fortifiedGlass.getDefaultState(), dx, i*5+dy, dz, structureBB);
								}
							}
							if(rand.nextInt(floors*10) < i) {
								ECExplosion explosion = new ECExplosion(world, null, this.getXWithOffset(dx, dz), this.getYWithOffset(i*5+dy), this.getZWithOffset(dx, dz), 3+i/3);
								explosion.doExplosionA();
								explosion.doExplosionB(true);
							}
						}
					}
				}
			}

			return true;
		}
	}

	public static class House1 extends StructureTownPieces.House {

		public House1() {}

		public House1(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, int floors, EnumFacing facing) {
			super(start, index, structureBB, floors, 5, facing);
		}

		public static StructureTownPieces.House1 createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			int floors = rand.nextInt(2)+1;
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 11, floors*5+1, 11, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.House1(start, index, structureboundingbox, floors, facing) : null;
		}
	}

	public static class House2 extends StructureTownPieces.House {

		public House2() {}

		public House2(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, int floors, EnumFacing facing) {
			super(start, index, structureBB, floors, 4, facing);
		}

		public static StructureTownPieces.House2 createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			int floors = rand.nextInt(5)+1;
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 9, floors*5+1, 9, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.House2(start, index, structureboundingbox, floors, facing) : null;
		}
	}

	public static class House3 extends StructureTownPieces.House {

		public House3() {}

		public House3(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, int floors, EnumFacing facing) {
			super(start, index, structureBB, floors, 3, facing);
		}

		public static StructureTownPieces.House3 createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			int floors = rand.nextInt(9)+1;
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 7, floors*5+1, 7, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.House3(start, index, structureboundingbox, floors, facing) : null;
		}
	}

	public static class House4 extends StructureTownPieces.House {

		public House4() {}

		public House4(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, int floors, EnumFacing facing) {
			super(start, index, structureBB, floors, 7, facing);
		}

		public static StructureTownPieces.House4 createPiece(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			int floors = rand.nextInt(3)+1;
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 15, 5*floors+1, 15, facing);
			return StructureComponent.findIntersecting(components, structureboundingbox) != null ? null : new StructureTownPieces.House4(start, index, structureboundingbox, floors, facing);
		}
	}

	public static class Path extends StructureTownPieces.Road {
		private int length;

		public Path() {}

		public Path(StructureTownPieces.Start start, int index, Random rand, StructureBoundingBox structureBB, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
			this.length = Math.max(structureBB.getXSize(), structureBB.getZSize());
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt) {
			super.writeStructureToNBT(nbt);
			nbt.setInteger("Length", this.length);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager tm) {
			super.readStructureFromNBT(nbt, tm);
			this.length = nbt.getInteger("Length");
		}

		@Override
		public void buildComponent(StructureComponent component, List<StructureComponent> components, Random rand) {
			boolean flag = false;
			int i;
			StructureComponent structurecomponent1;

			for(i = rand.nextInt(18); i < this.length - 8; i += 2 + rand.nextInt(18)) {
				structurecomponent1 = this.getNextComponentNN((StructureTownPieces.Start)component, components, rand, 0, i);

				if(structurecomponent1 != null) {
					i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
					flag = true;
				}
			}

			for(i = rand.nextInt(18); i < this.length - 8; i += 2 + rand.nextInt(18)) {
				structurecomponent1 = this.getNextComponentPP((StructureTownPieces.Start)component, components, rand, 0, i);

				if(structurecomponent1 != null) {
					i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
					flag = true;
				}
			}

			if(flag && rand.nextInt(18) > 0) {
				switch (this.getCoordBaseMode()) {
				case SOUTH:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
					break;
				case WEST:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.SOUTH, this.getComponentType());
					break;
				case NORTH:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
					break;
				case EAST:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.SOUTH, this.getComponentType());
				default:
					break;
				}
			}

			if (flag && rand.nextInt(18) > 0) {
				switch(this.getCoordBaseMode()) {
				case SOUTH:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
					break;
				case WEST:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.NORTH, this.getComponentType());
					break;
				case NORTH:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
					break;
				case EAST:
					StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.NORTH, this.getComponentType());
				default:
					break;
				}
			}
		}

		public static StructureBoundingBox findPieceBox(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing) {
			for(int i1 = 7 * MathHelper.getInt(rand, 3, 5); i1 >= 7; i1 -= 7) {
				StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 5, 5, i1, facing);

				if(StructureComponent.findIntersecting(components, structureboundingbox) == null) {
					return structureboundingbox;
				}
			}

			return null;
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			IBlockState block = this.getBiomeSpecificBlockState(BlocksCore.concrete.getDefaultState());

			for(int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
				for(int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
					BlockPos pos = new BlockPos(i, 64, j);
					if(structureBB.isVecInside(pos)) {
						pos = pos.down();
						if(pos.getY() < world.getSeaLevel())  {
							pos = new BlockPos(pos.getX(), world.getSeaLevel() - 1, pos.getZ());
						}
						while(pos.getY() >= world.getSeaLevel() - 1) {
							IBlockState stateCurrent = world.getBlockState(pos);

							if(stateCurrent.getBlock() == Blocks.GRASS || stateCurrent.getMaterial().isLiquid() ||
									stateCurrent.getBlock() == Blocks.SAND || stateCurrent.getBlock() == Blocks.SANDSTONE ||
									stateCurrent.getBlock() == Blocks.RED_SANDSTONE || stateCurrent.getBlock() == BlocksCore.dreadDirt) {
								world.setBlockState(pos, block, 2);
								break;
							}

							pos = pos.down();
						}
					}
				}
			}

			return true;
		}
	}

	public static class PieceWeight {
		public Class<? extends StructureTownPieces.Town> townPieceClass;
		public final int townPieceWeight;
		public int townPiecesSpawned;
		public int townPiecesLimit;

		public PieceWeight(Class<? extends StructureTownPieces.Town> pieceClass, int weight, int limit) {
			this.townPieceClass = pieceClass;
			this.townPieceWeight = weight;
			this.townPiecesLimit = limit;
		}

		public boolean canSpawnMoreTownPiecesOfType(int index) {
			return this.townPiecesLimit == 0 || this.townPiecesSpawned < this.townPiecesLimit;
		}

		public boolean canSpawnMoreTownPieces() {
			return this.townPiecesLimit == 0 || this.townPiecesSpawned < this.townPiecesLimit;
		}
	}

	public abstract static class Road extends StructureTownPieces.Town {

		public Road() {}

		protected Road(StructureTownPieces.Start start, int index) {
			super(start, index);
		}
	}

	public static class Start extends StructureTownPieces.Intersection {
		public BiomeProvider worldChunkMngr;
		public boolean inDesert;
		public int terrainType;
		public StructureTownPieces.PieceWeight structTownPieceWeight;
		public List<StructureTownPieces.PieceWeight> structureTownWeightedPieceList;
		public List<StructureComponent> pendingHouses = new ArrayList<StructureComponent>();
		public List<StructureComponent> pendingRoads = new ArrayList<StructureComponent>();
		public Biome biome;

		public Start() {}

		public Start(BiomeProvider biomeProvider, int index, Random rand, int x, int z, List<StructureTownPieces.PieceWeight> weights, int terrainType) {
			super(null, 0, rand, x, z);
			this.worldChunkMngr = biomeProvider;
			this.structureTownWeightedPieceList = weights;
			this.terrainType = terrainType;
			Biome biome = biomeProvider.getBiome(new BlockPos(x, 0, z));
			this.inDesert = biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS || biome == BiomeRegistry.desert;
			this.biome = biome;
		}

		public BiomeProvider getBiomeProvider() {
			return this.worldChunkMngr;
		}
	}

	public static class Torch extends StructureTownPieces.Town {

		public Torch() {}

		public Torch(StructureTownPieces.Start start, int index, StructureBoundingBox structureBB, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
		}

		public static StructureBoundingBox findPieceBox(StructureTownPieces.Start start, List<StructureComponent> components, int sMinX, int sMinY, int sMinZ, EnumFacing facing) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 3, 4, 3, facing);
			return StructureComponent.findIntersecting(components, structureboundingbox) != null ? null : structureboundingbox;
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
			}

			this.fillWithBlocks(world, structureBB, 0, 0, 0, 2, 3, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
			this.setBlockState(world, BlocksCore.fence[0].getDefaultState(), 1, 0, 1, structureBB);
			this.setBlockState(world, BlocksCore.fence[0].getDefaultState(), 1, 1, 1, structureBB);
			this.setBlockState(world, BlocksCore.fence[0].getDefaultState(), 1, 2, 1, structureBB);
			this.setBlockState(world, BlocksCore.voidStone.getDefaultState(), 1, 3, 1, structureBB);
			this.setBlockState(world, BlocksCore.torch.getDefaultState(), 0, 3, 1, structureBB);
			this.setBlockState(world, BlocksCore.torch.getDefaultState(), 1, 3, 0, structureBB);
			this.setBlockState(world, BlocksCore.torch.getDefaultState(), 0, 3, 2, structureBB);
			this.setBlockState(world, BlocksCore.torch.getDefaultState(), 2, 3, 0, structureBB);

			return true;
		}
	}

	public abstract static class Town extends StructureComponent {

		protected int averageGroundLvl = -1;
		private boolean inDesert;
		private StructureTownPieces.Start startPiece;

		public Town() {}

		protected Town(StructureTownPieces.Start start, int index) {
			super(index);

			if(start != null) {
				this.inDesert = start.inDesert;
				startPiece = start;
			}
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt) {
			nbt.setInteger("HPos", this.averageGroundLvl);
			nbt.setBoolean("Desert", this.inDesert);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager tm) {
			this.averageGroundLvl = nbt.getInteger("HPos");
			this.inDesert = nbt.getBoolean("Desert");
		}

		protected StructureComponent getNextComponentNN(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int height, int offset) {
			switch(this.getCoordBaseMode()) {
			case SOUTH:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX - 1, this.boundingBox.minY + height, this.boundingBox.minZ + offset, EnumFacing.WEST, this.getComponentType());
			case WEST:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX + offset, this.boundingBox.minY + height, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			case NORTH:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX - 1, this.boundingBox.minY + height, this.boundingBox.minZ + offset, EnumFacing.WEST, this.getComponentType());
			case EAST:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX + offset, this.boundingBox.minY + height, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			default:
				return null;
			}
		}

		protected StructureComponent getNextComponentPP(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int height, int offset) {
			switch (this.getCoordBaseMode()) {
			case SOUTH:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + height, this.boundingBox.minZ + offset, EnumFacing.EAST, this.getComponentType());
			case WEST:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX + offset, this.boundingBox.minY + height, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			case NORTH:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + height, this.boundingBox.minZ + offset, EnumFacing.EAST, this.getComponentType());
			case EAST:
				return StructureTownPieces.generateAndAddComponent(start, components, rand, this.boundingBox.minX + offset, this.boundingBox.minY + height, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			default:
				return null;
			}
		}

		protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
			int i = 0;
			int j = 0;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for(int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
				for(int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
					blockpos$mutableblockpos.setPos(l, 64, k);

					if(structurebb.isVecInside(blockpos$mutableblockpos)) {
						i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
						++j;
					}
				}
			}

			if(j == 0) {
				return -1;
			}
			else {
				return i / j;
			}
		}

		protected static boolean canTownGoDeeper(StructureBoundingBox structureBB) {
			return structureBB != null && structureBB.minY > 10;
		}

		protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
			BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
			MinecraftForge.TERRAIN_GEN_BUS.post(event);
			if(event.getResult() == Result.DENY)
				return event.getReplacement();
			if(this.inDesert) {
				if(blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
					return Blocks.SANDSTONE.getDefaultState();
				}
				if(blockstateIn.getBlock() == Blocks.COBBLESTONE) {
					return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
				}
				if(blockstateIn.getBlock() == Blocks.PLANKS) {
					return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
				}
				if(blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
					return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
				}
				if(blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
					return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
				}
				if(blockstateIn.getBlock() == Blocks.GRAVEL) {
					return Blocks.SANDSTONE.getDefaultState();
				}
			}

			return blockstateIn;
		}

		@Override
		protected void setBlockState(World world, IBlockState block, int x, int y, int z, StructureBoundingBox structureBB) {
			IBlockState block1 = this.getBiomeSpecificBlockState(block);
			super.setBlockState(world, block1, x, y, z, structureBB);
		}

		@Override
		protected void fillWithBlocks(World world, StructureBoundingBox structureBB, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState block0, IBlockState block1, boolean existingOnly) {
			IBlockState block2 = this.getBiomeSpecificBlockState(block0);
			IBlockState block3 = this.getBiomeSpecificBlockState(block1);
			super.fillWithBlocks(world, structureBB, xMin, yMin, zMin, xMax, yMax, zMax, block2, block3, existingOnly);
		}

		@Override
		protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
			IBlockState iblockstate = this.getBiomeSpecificBlockState(blockstateIn);
			super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
		}
	}

	public static class Intersection extends StructureTownPieces.Town {

		public Intersection() {}

		public Intersection(StructureTownPieces.Start start, int index, Random rand, int x, int z) {
			super(start, index);
			this.setCoordBaseMode(EnumFacing.getHorizontal(rand.nextInt(4)));

			switch (this.getCoordBaseMode()) {
			case SOUTH:
			case NORTH:
				this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
				break;
			default:
				this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
			}
		}

		@Override
		public void buildComponent(StructureComponent component, List<StructureComponent> components, Random rand) {
			StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
			StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
			StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			StructureTownPieces.generateAndAddRoadPiece((StructureTownPieces.Start)component, components, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
			}
			return true;
		}
	}

	public static class Explosion extends StructureTownPieces.Town {

		public Explosion() {}

		public Explosion(StructureTownPieces.Start start, int index, Random rand, StructureBoundingBox structureBB, EnumFacing facing) {
			super(start, index);
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBB;
		}

		public static StructureTownPieces.Explosion findPieceBox(StructureTownPieces.Start start, List<StructureComponent> components, Random rand, int sMinX, int sMinY, int sMinZ, EnumFacing facing, int index) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(sMinX, sMinY, sMinZ, 0, 0, 0, 5, 5, 5, facing);
			return canTownGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(components, structureboundingbox) == null ? new StructureTownPieces.Explosion(start, index, rand, structureboundingbox, facing) : null;
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBB) {
			if(this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(world, structureBB);

				if(this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4, 0);
			}

			//do
			ECExplosion explosion = new ECExplosion(world, null, this.getXWithOffset(2, 2), this.getYWithOffset(0), this.getZWithOffset(2, 2), 15F);
			explosion.doExplosionA();
			explosion.doExplosionB(true);

			return true;
		}
	}
}