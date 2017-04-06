package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import ec3.common.block.BlocksCore;
import ec3.utils.common.ECUtils;
import ec3.utils.common.WindRelations;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemWindTablet extends ItemStoresMRUInNBT implements IModelRegisterer {

	//24
	public static String[] windMessages = new String[] {
			"A wind blow rotates around you...",//0
			"The wind howls...",//1
			"The wind ruffles your hair...",//2
			"The wind blows around your fingers...",//3
			"The wind says something...",//4
			"The wind makes you sneeze!",//5
			"You hear something similar to laugh...",//6
			"The wind rotates around your legs...",//7
			"The wind tickles you...",//8
			"The wind stops all other sounds...",//9
			"The wind whispers 'Owethanna'...",//10
			"The wind whispers your name...",//11
			"The wind brings a very nostalgic smell...",//12
			"The wind creates a miniature tornado...",//13
			"The wind thows some leaves around...",//14
			"The wind says 'Owethanna Else '...",//15
			"The wind pushes you upwards...",//16
			"The wind rotates very fast around you...",//17
			"The wind goes into your lungs...",//18
			"You feel very powerfull!",//19
			"You fly up using the wind!",//20
			"You start seeing other worlds!",//21
			"You and the wind laugh...",//22
			"The wind and you shout:"//23
	};

	public ItemWindTablet() {
		super();	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer, enumHand
	 */
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World world, EntityPlayer player, EnumHand hand)
	{
		if((ECUtils.tryToDecreaseMRUInStorage(player, -500) || this.setMRU(is, -500)))
		{
			int currentWindRev = WindRelations.getPlayerWindRelations(player);
			int maxWindRev = 3500;
			String windName = "Owethanna Else Hugaida";
			int revPos = MathUtils.pixelatedTextureSize(currentWindRev, maxWindRev, windName.length());
			if(!world.isRemote && revPos >= 22)
			{
				if(player.capabilities.isCreativeMode)
					player.capabilities.isCreativeMode = false;
				Block b = world.getBlockState(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ))).getBlock();

				if(b instanceof BlockPortal)
				{
					int x = MathHelper.floor(player.posX);
					int y = MathHelper.floor(player.posY);
					int z = MathHelper.floor(player.posZ);
					int i = 0;
					BlockPos pos = new BlockPos(x,y,z);

					while(world.getBlockState(pos.add(i, 0, 0)).getBlock() instanceof BlockPortal)
					{
						int j = 0;
						while(world.getBlockState(pos.add(i, j, 0)).getBlock() instanceof BlockPortal)
						{
							int k = 0;
							while(world.getBlockState(pos.add(i, j, k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(i, j, k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(i, j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							k = 0;
							while(world.getBlockState(pos.add(i, j, -k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(i, j, -k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(i, j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							++j;
						}
						j = 0;
						while(world.getBlockState(pos.add(i, -j, 0)).getBlock() instanceof BlockPortal)
						{
							int k = 0;
							while(world.getBlockState(pos.add(i, -j, k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(i, -j, k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(i, -j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							k = 0;
							while(world.getBlockState(pos.add(i, -j, -k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(i, -j, -k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(i, -j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							++j;
						}
						++i;
					}
					i = 0;
					while(world.getBlockState(pos.add(-i, 0, 0)).getBlock() instanceof BlockPortal)
					{
						int j = 0;
						while(world.getBlockState(pos.add(-i, j, 0)).getBlock() instanceof BlockPortal)
						{
							int k = 0;
							while(world.getBlockState(pos.add(-i, j, k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(-i, j, k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(-i, j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							k = 0;
							while(world.getBlockState(pos.add(-i, j, -k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(-i, j, -k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(-i, j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							++j;
						}
						j = 0;
						while(world.getBlockState(pos.add(-i, -j, 0)).getBlock() instanceof BlockPortal)
						{
							int k = 0;
							while(world.getBlockState(pos.add(-i, -j, k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(-i, -j, k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(-i, -j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							k = 0;
							while(world.getBlockState(pos.add(-i, -j, -k)).getBlock() instanceof BlockPortal)
							{
								int metadata = world.getBlockState(pos.add(-i, -j, -k)).getBlock().getMetaFromState(world.getBlockState(pos.add(i, j, k)));
								world.setBlockState(pos.add(-i, -j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
								++k;
							}
							++j;
						}
						++i;
					}
				}
				world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 10, 0.01F);
			}
		}
		return new ActionResult(EnumActionResult.PASS,is);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		int currentWindRev = WindRelations.getPlayerWindRelations(par2EntityPlayer);
		int maxWindRev = 3500;
		String windName = "Owethanna Else Hugaida";
		String hidden = "??????????????????????";
		int revPos = MathUtils.pixelatedTextureSize(currentWindRev, maxWindRev, windName.length());
		par3List.add("Wind name:");
		par3List.add(windName.substring(0, revPos)+hidden.substring(revPos));
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/windTablet", "inventory"));
	}
}
