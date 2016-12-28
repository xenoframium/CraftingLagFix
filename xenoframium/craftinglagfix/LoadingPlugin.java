package xenoframium.craftinglagfix;

import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraft.init.Blocks;

@MCVersion(value = "1.7.10")
public class LoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"xenoframium.craftinglagfix.asm.ClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "xenoframium.craftinglagfix.CraftingLagFix";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}