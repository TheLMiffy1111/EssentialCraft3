package essentialcraft.common.tile;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.UnformedItemStack;
import essentialcraft.api.GunRegistry;
import essentialcraft.api.GunRegistry.GunMaterial;
import essentialcraft.api.GunRegistry.LenseMaterial;
import essentialcraft.api.GunRegistry.ScopeMaterial;
import essentialcraft.common.item.ItemGenericEC;
import essentialcraft.common.item.ItemGun;
import essentialcraft.common.item.ItemMRUStorageEC;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileWeaponMaker extends TileMRUGeneric {

	public int index;
	public ItemStack previewStack = ItemStack.EMPTY;

	public TileWeaponMaker() {
		super(0);
		slot0IsBoundGem = false;
		setSlotsNum(19);
	}

	@Override
	public void update() {
		index = this.getBlockMetadata();
		super.update();
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		if(i.hasKey("preview"))
			previewStack = new ItemStack(i.getCompoundTag("preview"));
		else
			previewStack = ItemStack.EMPTY;

		index = i.getInteger("index");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(!previewStack.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			previewStack.writeToNBT(tag);
			i.setTag("preview", tag);
		}
		else
			i.removeTag("preview");

		i.setInteger("index", index);
		return super.writeToNBT(i);
	}

	public String getBase() {
		String baseName = "";
		if(index == 0) {
			for(int i = 5; i < 8; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(baseName.isEmpty())
								baseName = material.id;
							else if(!baseName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 1) {
			for(int i = 5; i < 9; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(baseName.isEmpty())
								baseName = material.id;
							else if(!baseName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 2) {
			for(int i = 7; i < 10; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(baseName.isEmpty())
								baseName = material.id;
							else if(!baseName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 3) {
			for(int i = 6; i < 13; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(baseName.isEmpty())
								baseName = material.id;
							else if(!baseName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		return baseName;
	}

	public String getHandle() {
		String handleName = "";
		if(index == 0) {
			if(!getStackInSlot(9).isEmpty()) {
				for(int i = 0; i < GunRegistry.GUN_MATERIALS.size(); ++i) {
					GunMaterial material = GunRegistry.GUN_MATERIALS.get(i);
					if(getStackInSlot(9).isItemEqual(material.recipe))
						return material.id;
				}
			}
		}
		if(index == 1) {
			for(int i = 10; i < 13; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(handleName.isEmpty())
								handleName = material.id;
							else if(!handleName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 2) {
			for(int i = 12; i < 14; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(handleName.isEmpty())
								handleName = material.id;
							else if(!handleName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 3) {
			for(int i = 15; i < 19; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(handleName.isEmpty())
								handleName = material.id;
							else if(!handleName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		return handleName;
	}

	public String getDevice() {
		String deviceName = "";
		if(index == 0) {
			if(!getStackInSlot(8).isEmpty()) {
				for(int i = 0; i < GunRegistry.GUN_MATERIALS.size(); ++i) {
					GunMaterial material = GunRegistry.GUN_MATERIALS.get(i);
					if(getStackInSlot(8).isItemEqual(material.recipe))
						return material.id;
				}
			}
		}
		if(index == 1) {
			if(!getStackInSlot(9).isEmpty()) {
				for(int i = 0; i < GunRegistry.GUN_MATERIALS.size(); ++i) {
					GunMaterial material = GunRegistry.GUN_MATERIALS.get(i);
					if(getStackInSlot(9).isItemEqual(material.recipe))
						return material.id;
				}
			}
		}
		if(index == 2) {
			for(int i = 10; i < 12; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(deviceName.isEmpty())
								deviceName = material.id;
							else if(!deviceName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		if(index == 3) {
			for(int i = 13; i < 15; ++i) {
				if(!getStackInSlot(i).isEmpty()) {
					for(int j = 0; j < GunRegistry.GUN_MATERIALS.size(); ++j) {
						GunMaterial material = GunRegistry.GUN_MATERIALS.get(j);
						if(getStackInSlot(i).isItemEqual(material.recipe)) {
							if(deviceName.isEmpty())
								deviceName = material.id;
							else if(!deviceName.equalsIgnoreCase(material.id))
								return "";
						}
					}
				}
				else
					return "";
			}
		}
		return deviceName;
	}

	public String getLense() {
		String lenseName = "";
		if(index == 0 || index == 1 || index == 2) {
			if(!getStackInSlot(3).isEmpty()) {
				for(int i = 0; i < GunRegistry.LENSE_MATERIALS.size(); ++i) {
					LenseMaterial material = GunRegistry.LENSE_MATERIALS.get(i);
					if(getStackInSlot(3).isItemEqual(material.recipe))
						return material.id;
				}
			}
		}
		if(index == 3) {
			if(!getStackInSlot(3).isEmpty()) {
				if(getStackInSlot(3).getItem() instanceof ItemGenericEC && getStackInSlot(3).getItemDamage() == 32) {
					for(int i = 4; i < 6; ++i) {
						if(!getStackInSlot(i).isEmpty()) {
							for(int j = 0; j < GunRegistry.LENSE_MATERIALS.size(); ++j) {
								LenseMaterial material = GunRegistry.LENSE_MATERIALS.get(j);
								if(getStackInSlot(i).isItemEqual(material.recipe)) {
									if(lenseName.isEmpty())
										lenseName = material.id;
									else if(!lenseName.equalsIgnoreCase(material.id))
										return "";
								}
							}
						}
						else
							return "";
					}
				}
			}
		}
		return lenseName;
	}

	public String getScope() {
		String scopeName = "";
		if(index == 0 || index == 1) {
			if(!getStackInSlot(4).isEmpty()) {
				for(int i = 0; i < GunRegistry.SCOPE_MATERIALS.size(); ++i) {
					ScopeMaterial material = GunRegistry.SCOPE_MATERIALS.get(i);
					if(getStackInSlot(4).isItemEqual(material.recipe))
						return material.id;
				}
			}
		}

		if(index == 2) {
			if(!getStackInSlot(4).isEmpty()) {
				for(int i = 0; i < GunRegistry.SCOPE_MATERIALS.size(); ++i) {
					ScopeMaterial material = GunRegistry.SCOPE_MATERIALS.get(i);
					if(getStackInSlot(4).isItemEqual(material.recipe)) {
						for(int j = 5; j < 7; ++j) {
							if(!getStackInSlot(j).isEmpty()) {
								for(int k = 0; k < GunRegistry.SCOPE_MATERIALS_SNIPER.size(); ++k) {
									ScopeMaterial material1 = GunRegistry.SCOPE_MATERIALS_SNIPER.get(k);
									if(getStackInSlot(j).isItemEqual(material1.recipe)) {
										if(scopeName.isEmpty())
											scopeName = material1.id;
										else if(!scopeName.equalsIgnoreCase(material1.id))
											return "";
									}
								}
							}
							else
								return "";
						}
					}
				}
			}
		}

		return scopeName;
	}

	public boolean isOreDict(ItemStack stk, String target) {
		UnformedItemStack s = new UnformedItemStack(target);
		boolean ret = s.itemStackMatches(stk);
		s.possibleStacks.clear();
		return ret;
	}

	public boolean areIngridientsCorrect() {
		//If output is empty
		if(getStackInSlot(0).isEmpty()) {
			if(!getStackInSlot(1).isEmpty() && isOreDict(getStackInSlot(1),"coreMagic")) {
				if(!getStackInSlot(2).isEmpty() && getStackInSlot(2).getItem() instanceof ItemMRUStorageEC && getStackInSlot(2).getItemDamage() >= 1) {
					String base = getBase();
					String handle = getHandle();
					String scope = getScope();
					String device = getDevice();
					String lense = getLense();
					if(index == 0) {
						if(!base.isEmpty() && !handle.isEmpty() && !device.isEmpty())
							return true;
					}
					if(index == 1) {
						if(!base.isEmpty() && !handle.isEmpty() && !device.isEmpty())
							return true;
					}
					if(index == 2) {
						if(!base.isEmpty() && !handle.isEmpty() && !device.isEmpty() && !scope.isEmpty())
							return true;
					}
					if(index == 3) {
						if(!base.isEmpty() && !handle.isEmpty() && !device.isEmpty() && !lense.isEmpty())
							return true;
					}
				}
			}
		}
		return false;
	}

	public void previewWeapon() {
		previewStack = ItemStack.EMPTY;
		if(getStackInSlot(0).isEmpty()) {
			if(!getStackInSlot(1).isEmpty() && isOreDict(getStackInSlot(1), "coreMagic")) {
				if(!getStackInSlot(2).isEmpty() && getStackInSlot(2).getItem() instanceof ItemMRUStorageEC && getStackInSlot(2).getItemDamage() >= 1) {
					if(index == 0)
						previewStack = new ItemStack(ItemsCore.pistol);
					if(index == 1)
						previewStack = new ItemStack(ItemsCore.rifle);
					if(index == 2)
						previewStack = new ItemStack(ItemsCore.sniper);
					if(index == 3)
						previewStack = new ItemStack(ItemsCore.gatling);

					String lense = getLense();
					String base = getBase();
					String handle = getHandle();
					String scope = getScope();
					String device = getDevice();
					NBTTagCompound tag = MiscUtils.getStackTag(previewStack);
					tag.setString("lense", lense);
					tag.setString("base", base);
					tag.setString("handle", handle);
					tag.setString("scope", scope);
					tag.setString("device", device);
				}
			}
		}
	}

	public void makeWeapon() {
		if(areIngridientsCorrect()) {
			ItemStack result = previewStack.copy();
			ItemGun.calculateGunStats(result);
			setInventorySlotContents(0, result);
			for(int i = 1; i < getSizeInventory(); ++i)
				decrStackSize(i, 1);
		}
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)  {
		ItemStack is = super.decrStackSize(par1, par2);
		markDirty();
		return is;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		super.setInventorySlotContents(par1, stack);
		markDirty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		previewWeapon();
		syncTick = 0;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {0};
	}

	@Override
	public int[] getSlotsForFace(EnumFacing facing) {
		switch(index) {
		case 0:
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		case 1:
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		case 2:
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		case 3:
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
		}
		return new int[0];
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == 0)
			return false;

		switch(index) {
		case 0:
			return slot <= 9;
		case 1:
			return slot <= 12;
		case 2:
			return slot <= 13;
		case 3:
			return slot <= 18;
		}
		return false;
	}
}
