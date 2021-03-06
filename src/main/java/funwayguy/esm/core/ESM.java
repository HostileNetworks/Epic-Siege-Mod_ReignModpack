package funwayguy.esm.core;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import funwayguy.esm.ai.interop.ModAccessors;
import funwayguy.esm.core.proxies.CommonProxy;
import funwayguy.esm.entities.EntityESMGhast;
import funwayguy.esm.entities.EntityNeatZombie;
import funwayguy.esm.handlers.ESM_EventManager;
import funwayguy.esm.world.dimensions.WorldProviderNewHell;
import funwayguy.esm.world.dimensions.WorldProviderSpace;
import funwayguy.esm.world.gen.WorldGenFortress;

@Mod(modid = ESM_Settings.ID, name = ESM_Settings.Name, version = ESM_Settings.Version, guiFactory = "funwayguy.esm.client.ESMGuiFactory")
public class ESM
{
	@Instance(ESM_Settings.ID)
    public static ESM instance;
	
	@SidedProxy(clientSide = ESM_Settings.Proxy + ".ClientProxy", serverSide = ESM_Settings.Proxy + ".CommonProxy")
	public static CommonProxy proxy;
	
	public static org.apache.logging.log4j.Logger log;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		log = event.getModLog();
		ESM_Settings.LoadMainConfig(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		ESM_EventManager manager = new ESM_EventManager();
		MinecraftForge.EVENT_BUS.register(manager);
		MinecraftForge.TERRAIN_GEN_BUS.register(manager);
		FMLCommonHandler.instance().bus().register(manager);
		// CosmicDan - unofficial fork, remove update stuff
		//FMLCommonHandler.instance().bus().register(new ESM_UpdateNotification());
		
		GameRegistry.registerWorldGenerator(new WorldGenFortress(), 0);
		
		int ghastID = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(EntityESMGhast.class, "ESM_Ghast", ghastID);
		EntityRegistry.registerModEntity(EntityESMGhast.class, "ESM_Ghast", ghastID, instance, 128, 1, true);
		
		int zombieID = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(EntityNeatZombie.class, "NEAT_Zombie", zombieID);
		EntityRegistry.registerModEntity(EntityNeatZombie.class, "NEAT_Zombie", zombieID, instance, 128, 1, true);
		
		// load cross-mod compatibility
		ModAccessors.init();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		if(ESM_Settings.NewEnd)
		{
			WorldProviderSpace.oldClass = DimensionManager.createProviderFor(1).getClass();
			DimensionManager.unregisterDimension(1);
			DimensionManager.unregisterProviderType(1);
			DimensionManager.registerProviderType(1, WorldProviderSpace.class, false);
			DimensionManager.registerDimension(1, 1);
		}
		
		if(ESM_Settings.NewHell)
		{
			WorldProviderNewHell.oldClass = DimensionManager.createProviderFor(-1).getClass();
			DimensionManager.unregisterDimension(-1);
			DimensionManager.unregisterProviderType(-1);
			DimensionManager.registerProviderType(-1, WorldProviderNewHell.class, false);
			DimensionManager.registerDimension(-1, -1);
		}
	}
}
