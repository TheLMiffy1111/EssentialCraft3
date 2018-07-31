package essentialcraft.utils.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import essentialcraft.api.ApiCore;
import essentialcraft.api.CorruptionEffectRegistry;
import essentialcraft.api.DiscoveryEntry;
import essentialcraft.api.ICorruptionEffect;
import essentialcraft.api.PageEntry;
import essentialcraft.api.WorldEventRegistry;
import essentialcraft.client.gui.GuiResearchBook;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBaublesSpecial;
import essentialcraft.common.item.ItemComputerArmor;
import essentialcraft.common.item.ItemComputerBoard;
import essentialcraft.common.item.ItemGenericArmor;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.ResearchRegistry;
import essentialcraft.common.world.dim.WorldProviderHoanna;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerTickHandler {
	public HashMap<EntityPlayer, Integer> ticks = new HashMap<EntityPlayer, Integer>();
	public HashMap<EntityPlayer, Integer> wticks = new HashMap<EntityPlayer, Integer>();

	public HashMap<EntityPlayer, Boolean> isWearingBoots = new HashMap<EntityPlayer, Boolean>();

	public HashMap<EntityPlayer, Boolean> isFlightAllowed = new HashMap<EntityPlayer, Boolean>();

	public boolean client_flightAllowed;

	public static int tickAmount;

	public boolean isRKeyPressed = false;

	public void manageWorldSync(EntityPlayer e) {
		if(!wticks.containsKey(e)) {
			wticks.put(e, 10);
			ECUtils.requestCurrentEventSyncForPlayer((EntityPlayerMP) e);
		}
		else {
			int i = wticks.get(e).intValue();

			if(i <= 0) {
				i = 10;
				ECUtils.requestCurrentEventSyncForPlayer((EntityPlayerMP) e);
			}
			else {
				--i;
			}
			wticks.put(e, i);
		}
	}

	@SubscribeEvent
	public void tickEvent(WorldTickEvent event) {
		++tickAmount;
	}

	public void manageSync(EntityPlayer e) {
		if(!ticks.containsKey(e)) {
			ticks.put(e, 10);
			ECUtils.requestSync(e);
		}
		else {
			int i = ticks.get(e).intValue();

			if(i <= 0) {
				i = 10;
				ECUtils.requestSync(e);
			}
			else {
				--i;
			}
			ticks.put(e, i);
		}
	}

	@SideOnly(Side.CLIENT)
	public void client_manageBook(EntityPlayer e) {
		if(Keyboard.isKeyDown(Keyboard.KEY_R) && !isRKeyPressed) {
			isRKeyPressed = true;
			ItemStack currentPlayerItem = e.getHeldItemMainhand();
			if(currentPlayerItem.getItem() == ItemsCore.research_book) {
				boolean foundItem = false;
				Vec3d playerLookVec = e.getLookVec();
				Vec3d itemSearchVec = new Vec3d(playerLookVec.x, playerLookVec.y, playerLookVec.z);
				//Searching for EntityItem
				s:for(int o = 0; o < 4; ++o) {
					itemSearchVec = new Vec3d(itemSearchVec.x*(o+1),itemSearchVec.y*(o+1),itemSearchVec.z*(o+1));
					List<EntityItem> lst = e.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(e.posX+itemSearchVec.x-0.5D, e.posY+itemSearchVec.y-0.5D, e.posZ+itemSearchVec.z-0.5D, e.posX+itemSearchVec.x+0.5D, e.posY+itemSearchVec.y+0.5D, e.posZ+itemSearchVec.z+0.5D));
					if(!lst.isEmpty()) {
						for(EntityItem itm : lst) {
							if(itm != null && !itm.isDead) {
								ItemStack heldIS = itm.getItem().copy();
								if(ApiCore.findDiscoveryByIS(heldIS) != null) {
									DiscoveryEntry switchTo = ApiCore.findDiscoveryByIS(heldIS);
									GuiResearchBook.currentPage = 0;
									if(GuiResearchBook.currentCategory == null) {
										GuiResearchBook.currentCategory = ResearchRegistry.basic;
									}
									GuiResearchBook.currentPage_discovery = 0;
									GuiResearchBook.currentDiscovery = switchTo;
									if(switchTo != null) {
										f:for(int i = 0; i < switchTo.pages.size(); ++i) {
											PageEntry entry = switchTo.pages.get(i);
											if(entry != null) {
												if(entry.displayedItems != null && entry.displayedItems.length > 0) {
													for(ItemStack is : entry.displayedItems) {
														if(heldIS.isItemEqual(is)) {
															GuiResearchBook.currentPage = i - i%2;
															break f;
														}
													}
												}
												if(entry.pageRecipe != null) {
													ItemStack result = entry.pageRecipe.getRecipeOutput();
													if(result.isItemEqual(heldIS)) {
														GuiResearchBook.currentPage = i - i%2;
														break f;
													}
												}
											}
										}
									}
									EssentialCraftCore.proxy.openBookGUIForPlayer();
									GuiResearchBook book = (GuiResearchBook) Minecraft.getMinecraft().currentScreen;
									book.initGui();
									foundItem = true;
									break s;
								}
							}
						}
					}
				}
				if(!foundItem) {
					Vec3d blockSearchVec = new Vec3d(playerLookVec.x, playerLookVec.y, playerLookVec.z);
					//Searching for Block
					s:for(int o = 0; o < 4; ++o) {
						blockSearchVec = new Vec3d(blockSearchVec.x*(o+1),blockSearchVec.y*(o+1),blockSearchVec.z*(o+1));
						BlockPos pos = new BlockPos(MathHelper.floor(blockSearchVec.x+e.posX), MathHelper.floor(blockSearchVec.y+e.posY), MathHelper.floor(blockSearchVec.z+e.posZ));
						IBlockState state = e.getEntityWorld().getBlockState(pos);
						Block blk = state.getBlock();
						if(blk != null && blk != Blocks.AIR && Item.getItemFromBlock(blk) != null) {
							ItemStack heldIS = new ItemStack(blk, 1, blk.getMetaFromState(state));
							if(ApiCore.findDiscoveryByIS(heldIS) != null) {
								DiscoveryEntry switchTo = ApiCore.findDiscoveryByIS(heldIS);
								GuiResearchBook.currentPage = 0;
								if(GuiResearchBook.currentCategory == null) {
									GuiResearchBook.currentCategory = ResearchRegistry.basic;
								}
								GuiResearchBook.currentPage_discovery = 0;
								GuiResearchBook.currentDiscovery = switchTo;
								if(switchTo != null) {
									f:for(int i = 0; i < switchTo.pages.size(); ++i) {
										PageEntry entry = switchTo.pages.get(i);
										if(entry != null) {
											if(entry.displayedItems != null && entry.displayedItems.length > 0) {
												for(ItemStack is : entry.displayedItems) {
													if(heldIS.isItemEqual(is)) {
														GuiResearchBook.currentPage = i - i%2;
														break f;
													}
												}
											}
											if(entry.pageRecipe != null) {
												ItemStack result = entry.pageRecipe.getRecipeOutput();
												if(result.isItemEqual(heldIS)) {
													GuiResearchBook.currentPage = i - i%2;
													break f;
												}
											}
										}
									}
								}
								EssentialCraftCore.proxy.openBookGUIForPlayer();
								GuiResearchBook book = (GuiResearchBook) Minecraft.getMinecraft().currentScreen;
								book.initGui();
								break s;
							}
						}
					}
				}
			}
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_R) && isRKeyPressed) {
			isRKeyPressed = false;
		}
	}

	@SideOnly(Side.CLIENT)
	public void client_manageWorldEvents(EntityPlayer e) {
		if(e.dimension == Config.dimensionID) {
			if(e.getEntityWorld().provider.getDimension() == Config.dimensionID) {
				((WorldProviderHoanna)e.getEntityWorld().provider).generateLightBrightnessTable();
				if(ECUtils.isEventActive("essentialcraft.event.darkness")) {
					if(e.getEntityWorld().rand.nextFloat() < 0.01F) {
						e.getEntityWorld().playSound(e.posX,e.posY,e.posZ, SoundEvents.AMBIENT_CAVE,SoundCategory.AMBIENT, 1, e.getEntityWorld().rand.nextFloat()*2, true);
					}
					if(e.getEntityWorld().rand.nextFloat() < 0.001F) {
						SoundEvent[] sound = {SoundEvents.ENTITY_ZOMBIE_DEATH,SoundEvents.ENTITY_ZOMBIE_AMBIENT,SoundEvents.ENTITY_BLAZE_DEATH,SoundEvents.ENTITY_SKELETON_STEP,SoundEvents.ENTITY_ENDERMEN_STARE,SoundEvents.ENTITY_SPIDER_STEP,SoundEvents.ENTITY_SPIDER_DEATH,SoundEvents.ENTITY_SPIDER_AMBIENT,SoundEvents.ENTITY_CREEPER_DEATH};

						e.getEntityWorld().playSound(e.posX+MathUtils.randomDouble(e.getEntityWorld().rand)*16,e.posY,e.posZ+MathUtils.randomDouble(e.getEntityWorld().rand)*16, sound[e.getEntityWorld().rand.nextInt(sound.length)], SoundCategory.HOSTILE, 1, 0.01F, true);
					}

				}
				boolean ignoreEarthquake = false;
				IBaublesItemHandler b = BaublesApi.getBaublesHandler(e);
				if(b != null) {
					for(int i = 0; i < b.getSlots(); ++i) {
						ItemStack is = b.getStackInSlot(i);
						if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 19) {
							ignoreEarthquake = true;
						}
					}
				}
				if(ECUtils.isEventActive("essentialcraft.event.earthquake")) {
					if(!e.capabilities.isCreativeMode) {
						e.cameraPitch += MathUtils.randomFloat(e.getEntityWorld().rand);
						e.rotationYaw += MathUtils.randomFloat(e.getEntityWorld().rand);
						e.motionX += MathUtils.randomFloat(e.getEntityWorld().rand)/30;
						e.motionY += MathUtils.randomFloat(e.getEntityWorld().rand)/30;
						e.motionZ += MathUtils.randomFloat(e.getEntityWorld().rand)/30;
						if(e.getEntityWorld().rand.nextFloat() < 0.01F) {
							if(!ignoreEarthquake) {
								e.cameraPitch += MathUtils.randomFloat(e.getEntityWorld().rand)*90;
								e.rotationYaw += MathUtils.randomFloat(e.getEntityWorld().rand)*90;
								e.motionX += MathUtils.randomFloat(e.getEntityWorld().rand)*3;
								e.motionY += MathUtils.randomFloat(e.getEntityWorld().rand)*3;
								e.motionZ += MathUtils.randomFloat(e.getEntityWorld().rand)*3;
							}
							e.getEntityWorld().playSound(e.posX,e.posY,e.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.1F, 0.1F, true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void save(PlayerEvent.SaveToFile event) {
		EntityPlayer e = event.getEntityPlayer();
		UUID uuid = MiscUtils.getUUIDFromPlayer(e);
		if(!e.getEntityWorld().isRemote) {
			File f = event.getPlayerDirectory();
			if(f != null) {
				String fPath = f.getAbsolutePath();
				File saveFile = new File(fPath+"//"+uuid+".ecdat");
				if(saveFile.isDirectory()) {
					//???
					saveFile.delete();
					try {
						saveFile.createNewFile();
					}
					catch(IOException Ex) {
						Ex.printStackTrace();
					}
				}
				if(!saveFile.exists()) {
					try {
						saveFile.createNewFile();
					}
					catch(IOException Ex) {
						Ex.printStackTrace();
					}
				}

				try {
					FileOutputStream oStream = new FileOutputStream(saveFile);
					try {
						NBTTagCompound tag = new NBTTagCompound();
						ECUtils.getData(uuid).writeToNBTTagCompound(tag);
						CompressedStreamTools.writeCompressed(tag, oStream);
					}
					catch(Exception Ex) {
						FMLCommonHandler.instance().raiseException(Ex, "EssentialCraft4 Encountered an exception whlist saving playerdata NBT of player "+e.getName()+"!Report the error to the forum - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105", false);
					}
					finally {
						oStream.close();
					}
				}
				catch(Exception Exx) {
					FMLCommonHandler.instance().raiseException(Exx, "EssentialCraft4 Encountered an exception whlist wrighting playerdata file of player "+e.getName()+"! Make sure, that the file is not being accessed by other applications and is not threated as a virus by your anti-virus software! Also make sure, that you have some harddrive space. If everything above is correct - report the error to the forum - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105", true);
				}
			}
		}
	}

	@SubscribeEvent
	public void load(PlayerEvent.LoadFromFile event) {
		EntityPlayer e = event.getEntityPlayer();
		UUID uuid = MiscUtils.getUUIDFromPlayer(e);
		if(!e.getEntityWorld().isRemote) {
			File f = event.getPlayerDirectory();
			if(f != null) {
				String fPath = f.getAbsolutePath();
				File saveFile = new File(fPath+"//"+uuid+".ecdat");
				if(saveFile.isDirectory()) {
					//???
					saveFile.delete();
					try {
						saveFile.createNewFile();
					}
					catch(IOException Ex) {
						Ex.printStackTrace();
					}
				}
				if(!saveFile.exists()) {
					try {
						saveFile.createNewFile();
					}
					catch(IOException Ex) {
						Ex.printStackTrace();
					}
				}

				try {
					FileInputStream iStream = new FileInputStream(saveFile);
					try {
						NBTTagCompound tag = null;
						try {
							tag = CompressedStreamTools.readCompressed(iStream);
						}
						catch(java.io.EOFException EOFE) {
							//NBT could not be read, probably a first login.
							FMLLog.log(Level.WARN, "[EC4]Player data for player "+e.getName()+" could not be read. If it is the first time of the player to log in - it is fine. Otherwise, report the error to the author!");
						}

						if(tag != null)
							ECUtils.readOrCreatePlayerData(uuid, tag);
						else
							ECUtils.createPlayerData(uuid);
					}
					catch(Exception Ex) {
						FMLCommonHandler.instance().raiseException(Ex, "EssentialCraft4 Encountered an exception whlist reading playerdata NBT of player "+e.getName()+"! It is totally fine if this is your first time opening the save. If it is not - report the error to the forum - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105", false);
						ECUtils.readOrCreatePlayerData(uuid, new NBTTagCompound());
					}
					finally {
						iStream.close();
					}
				}
				catch(Exception Exx) {
					FMLCommonHandler.instance().raiseException(Exx, "EssentialCraft4 Encountered an exception whlist opening playerdata file of player "+e.getName()+"! Make sure, that the file is not being accessed by other applications and is not threated as a virus by your anti-virus software! Also make sure, that you have some harddrive space. If everything above is correct - report the error to the forum - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105", true);
				}
			}
		}
	}

	@SubscribeEvent
	public void tickEvent(PlayerTickEvent event) {
		if(event.phase == Phase.END) {
			EntityPlayer e = event.player;

			if(e == null) {
				return;
			}

			//TODO lagFix

			//Client overview
			if(e.getEntityWorld().isRemote) {
				client_manageBook(e);
				client_manageWorldEvents(e);
			}

			if(!isFlightAllowed.containsKey(e)) {
				isFlightAllowed.put(e, false);
			}

			//Step assistant, should 100% work with TC's system, not sure about Vazkii's one... If she calls it once per tick, it is fine. If it is only onEquiped(), then his baubles need to be re-equipped in order to work.
			if(!isWearingBoots.containsKey(e)) {
				isWearingBoots.put(e, false);
			}

			if(!e.inventory.armorInventory.get(0).isEmpty() && e.inventory.armorInventory.get(0).getItem() instanceof ItemComputerArmor) {
				e.fallDistance = 0;
			}

			if(!e.inventory.armorInventory.get(3).isEmpty() && e.inventory.armorInventory.get(3).getItem() instanceof ItemComputerArmor) {
				e.setAir(300);
				e.getFoodStats().addStats(1, 1);
			}

			if(!e.inventory.armorInventory.get(1).isEmpty() && e.inventory.armorInventory.get(1).getItem() instanceof ItemComputerArmor) {
				if(e.isBurning() && !e.getEntityWorld().isRemote && e.ticksExisted%20 == 0) {
					e.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,100,0,true,true));
				}
			}

			if(!e.inventory.armorInventory.get(0).isEmpty() && (e.inventory.armorInventory.get(0).getItem() instanceof ItemGenericArmor || e.inventory.armorInventory.get(0).getItem() instanceof ItemComputerArmor || e.inventory.armorInventory.get(0).getItem() == ItemsCore.magicArmorItems[7]) && (!isWearingBoots.get(e) || e.stepHeight < 1.2F)) {
				isWearingBoots.put(e, true);
				e.stepHeight = 1.2F;
			}

			if((e.inventory.armorInventory.get(0).isEmpty() || !(e.inventory.armorInventory.get(0).getItem() instanceof ItemGenericArmor || e.inventory.armorInventory.get(0).getItem() instanceof ItemComputerArmor || e.inventory.armorInventory.get(0).getItem() == ItemsCore.magicArmorItems[7])) && isWearingBoots.get(e)) {
				isWearingBoots.put(e, false);
				e.stepHeight = 0.6F;
			}

			if(BaublesApi.getBaublesHandler(e) != null) {
				ItemStack belt = BaublesApi.getBaublesHandler(e).getStackInSlot(3);
				if(!belt.isEmpty()) {
					if(belt.getItem() instanceof ItemComputerBoard) {
						Side s = FMLCommonHandler.instance().getEffectiveSide();
						if(s.isClient()) {
							this.client_flightAllowed = true;
							if(!e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
								e.capabilities.allowFlying = true;
							}
							e.capabilities.setFlySpeed(0.2F);

						}
						else {
							isFlightAllowed.put(e, true);
							if(!e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
								e.capabilities.allowFlying = true;
							}
						}
					}
					else {
						Side s = FMLCommonHandler.instance().getEffectiveSide();
						if(s.isClient()) {
							if(client_flightAllowed) {
								client_flightAllowed = false;
								if(e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
									e.capabilities.allowFlying = false;
									if(e.capabilities.isFlying) {
										e.capabilities.isFlying = false;
									}
								}
								e.capabilities.setFlySpeed(0.05F);

							}
						}
						else {
							if(isFlightAllowed.get(e)) {
								isFlightAllowed.put(e, false);
								if(e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
									e.capabilities.allowFlying = false;
									if(e.capabilities.isFlying) {
										e.capabilities.isFlying = false;
									}
								}
							}
						}
					}
				}
				else {
					Side s = FMLCommonHandler.instance().getEffectiveSide();
					if(s == Side.CLIENT) {
						if(client_flightAllowed) {
							client_flightAllowed = false;
							if(e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
								e.capabilities.allowFlying = false;
								if(e.capabilities.isFlying) {
									e.capabilities.isFlying = false;
								}
								e.capabilities.setFlySpeed(0.05F);
							}

						}
					}
					else {
						if(isFlightAllowed.get(e)) {
							isFlightAllowed.put(e, false);
							if(e.capabilities.allowFlying && !e.capabilities.isCreativeMode) {
								e.capabilities.allowFlying = false;
								if(e.capabilities.isFlying) {
									e.capabilities.isFlying = false;
								}
							}
						}
					}
				}
			}

			//Server overview
			if(!e.getEntityWorld().isRemote) {
				RadiationManager.playerTick(e);
				if(e.ticksExisted % 20 != 0) {
					return;
				}
				manageSync(e);
				manageWorldSync(e);
				WindRelations.playerTick(e);
				//System.out.println(ECUtils.getData(e).damage);
				if(e.ticksExisted % 200 == 0) {
					ECUtils.requestSync(e);
				}
				if(e.ticksExisted % 1200 == 0) {
					PlayerGenericData data = ECUtils.getData(e);
					if(e.getEntityWorld().rand.nextInt(36000) <= data.getOverhaulDamage()) {
						ArrayList<ICorruptionEffect> possibleEffects = new ArrayList<ICorruptionEffect>();
						List<ICorruptionEffect> playerEffects = data.getEffects();
						ArrayList<ICorruptionEffect> costSelected = CorruptionEffectRegistry.findSuitableEffects(data.getOverhaulDamage());
						for(int i = 0; i < costSelected.size(); ++i) {
							ICorruptionEffect selected = costSelected.get(i);
							boolean canAdd = selected != null;
							if(selected != null) {
								if(!playerEffects.isEmpty())
									J:for(int j = 0; j < playerEffects.size(); ++j) {
										ICorruptionEffect playerEffect = playerEffects.get(j);
										if(playerEffect == null)
											continue J;
										if(selected.effectEquals(playerEffect)) {
											if(!selected.canMultiply()) {
												canAdd = false;
												break J;
											}
										}
									}
								if(canAdd)
									possibleEffects.add(selected);
							}
						}
						if(!possibleEffects.isEmpty()) {
							ICorruptionEffect added = possibleEffects.get(e.getEntityWorld().rand.nextInt(possibleEffects.size())).copy();
							data.modifyOverhaulDamage(data.getOverhaulDamage() - added.getStickiness());
							data.getEffects().add(added);
							e.sendMessage(new TextComponentTranslation("essentialcraft.txt.corruption").setStyle(new Style().setColor(TextFormatting.RED)));
							ECUtils.requestSync(e);
						}
					}
				}
				for(int i = 0; i < ECUtils.getData(e).getEffects().size(); ++i) {
					ICorruptionEffect effect = ECUtils.getData(e).getEffects().get(i);
					effect.onPlayerTick(e);
				}

				if(WorldEventRegistry.currentEvent != null)
					WorldEventRegistry.currentEvent.playerTick(e, WorldEventRegistry.currentEventDuration);

				World wrd = e.getEntityWorld();
				List<EntityItem> itemList = wrd.<EntityItem>getEntitiesWithinAABB(EntityItem.class,new AxisAlignedBB(e.posX-0.5D, e.posY-0.5D, e.posZ-0.5D, e.posX+0.5D, e.posY+0.5D, e.posZ+0.5D).grow(2, 1, 2));
				for(int i = 0; i < itemList.size(); ++i) {
					doGroundItemChecks(itemList.get(i));
				}
			}
		}
	}

	public void doGroundItemChecks(EntityItem item) {
		if(item.getItem().getItem() == Items.BLAZE_POWDER) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.NETHERRACK) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(
						item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getMaterial() == Material.LAVA &&
						b_check_b4.getMaterial() == Material.LAVA &&
						b_check_b2.getMaterial() == Material.LAVA &&
						b_check_b3.getMaterial() == Material.LAVA) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.8F) {
						item.getEntityWorld().setBlockState(item.getPosition(), BlocksCore.drops.getStateFromMeta(0), 3);
					}
				}
			}
		}
		else if(item.getItem().getItem() == Items.CLAY_BALL) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.ICE) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getMaterial() == Material.WATER &&
						b_check_b4.getMaterial() == Material.WATER &&
						b_check_b2.getMaterial() == Material.WATER &&
						b_check_b3.getMaterial() == Material.WATER) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.8F) {
						item.getEntityWorld().setBlockState(item.getPosition(), BlocksCore.drops.getStateFromMeta(1), 3);
					}
				}
			}
		}
		else if(item.getItem().getItem() == Items.SLIME_BALL) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.SLIME_BLOCK) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getBlock() == Blocks.GRASS &&
						b_check_b4.getBlock() == Blocks.GRASS &&
						b_check_b2.getBlock() == Blocks.GRASS &&
						b_check_b3.getBlock() == Blocks.GRASS) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.8F) {
						item.getEntityWorld().setBlockState(item.getPosition(), BlocksCore.drops.getStateFromMeta(2), 3);
					}
				}
			}
		}
		else if(item.getItem().getItem() == Items.GUNPOWDER) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.QUARTZ_BLOCK) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getBlock() == Blocks.SAND &&
						b_check_b4.getBlock() == Blocks.SAND &&
						b_check_b2.getBlock() == Blocks.SAND &&
						b_check_b3.getBlock() == Blocks.SAND) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.8F) {
						item.getEntityWorld().setBlockState(item.getPosition(), BlocksCore.drops.getStateFromMeta(3), 3);
					}
				}
			}
		}
		else if(item.getItem().getItem() == Items.DIAMOND) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.EMERALD_BLOCK) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getBlock() == Blocks.SOUL_SAND &&
						b_check_b4.getBlock() == Blocks.SOUL_SAND &&
						b_check_b2.getBlock() == Blocks.SOUL_SAND &&
						b_check_b3.getBlock() == Blocks.SOUL_SAND) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.6F) {
						ECUtils.increaseCorruptionAt(item.getEntityWorld(), item.getPosition(), 5000);
					}
				}
			}
		}
		else if(item.getItem().getItem() == Items.EMERALD) {
			Block b = item.getEntityWorld().getBlockState(item.getPosition().down()).getBlock();
			if(b == Blocks.EMERALD_BLOCK) {
				IBlockState b_check_b1 = item.getEntityWorld().getBlockState(item.getPosition().add(1, -1, 0));
				IBlockState b_check_b2 = item.getEntityWorld().getBlockState(item.getPosition().add(-1, -1, 0));
				IBlockState b_check_b3 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, 1));
				IBlockState b_check_b4 = item.getEntityWorld().getBlockState(item.getPosition().add(0, -1, -1));
				if(item.getEntityWorld().isAirBlock(item.getPosition()) &&
						b_check_b1.getBlock() == Blocks.SOUL_SAND &&
						b_check_b4.getBlock() == Blocks.SOUL_SAND &&
						b_check_b2.getBlock() == Blocks.SOUL_SAND &&
						b_check_b3.getBlock() == Blocks.SOUL_SAND) {
					if(item.getItem().getCount() == 1)
						item.lifespan = 0;
					else {
						item.getItem().shrink(1);
					}
					MiscUtils.spawnParticlesOnServer("explosion_normal", (float)item.posX, (float)item.posY, (float)item.posZ, 0D, 0D, 0D);
					if(item.getEntityWorld().rand.nextFloat() <= 0.5F) {
						EntityItem soulStone = new EntityItem(item.getEntityWorld(),(float)item.posX, (float)item.posY, (float)item.posZ,new ItemStack(ItemsCore.soulStone,1,0));
						item.getEntityWorld().spawnEntity(soulStone);
					}
				}
			}
		}
	}
}
