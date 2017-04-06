package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import ec3.common.item.ItemPlayerList;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileMRUCoil;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMRUCoil_Coil extends BlockContainer implements IModelRegisterer {

	protected BlockMRUCoil_Coil(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	public BlockMRUCoil_Coil() {
		super(Material.ROCK);
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileMRUCoil();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if(par1World.isRemote)
		{
			return true;
		}else
		{
			if(!par4EntityPlayer.isSneaking())
			{
				if(par4EntityPlayer.capabilities.isCreativeMode)
				{
					par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
					return true;
				}
				TileMRUCoil tile = (TileMRUCoil)par1World.getTileEntity(par2);
				ItemStack is = tile.getStackInSlot(1);
				if(is != null && is.getItem() instanceof ItemPlayerList)
				{
					NBTTagCompound itemTag = MiscUtils.getStackTag(is);
					if(!itemTag.hasKey("usernames"))
						itemTag.setString("usernames", "||username:null");
					String str = itemTag.getString("usernames");
					DummyData[] dt = DataStorage.parseData(str);
					for(int i = 0; i < dt.length; ++i)
					{
						String username = dt[i].fieldValue;
						String playerName = MiscUtils.getUUIDFromPlayer(par4EntityPlayer).toString();
						if(username.equals(playerName))
						{
							par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
							return true;
						}
					}
					par4EntityPlayer.sendMessage(new TextComponentString(TextFormatting.RED+I18n.translateToLocal("ec3.txt.noPermission")));

				}else
				{
					par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
					return true;
				}

			}
			else
			{
				return false;
			}
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:mruCoil", "inventory"));
	}
}
