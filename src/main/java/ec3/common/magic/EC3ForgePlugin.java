package ec3.common.magic;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * 
 * @author Modbder
 * @Description ASM features with TConstruct.
 */
public class EC3ForgePlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {ASMHandler.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
