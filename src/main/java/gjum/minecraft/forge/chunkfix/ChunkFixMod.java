package gjum.minecraft.forge.chunkfix;

import gjum.minecraft.forge.chunkfix.config.ChunkFixConfig;
import io.netty.channel.ChannelPipeline;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = ChunkFixMod.MOD_ID,
        name = ChunkFixMod.MOD_NAME,
        version = ChunkFixMod.VERSION,
        guiFactory = "gjum.minecraft.forge.chunkfix.config.ConfigGuiFactory",
        clientSideOnly = true)
public class ChunkFixMod {

    public static final String MOD_ID = "chunkfix";
    public static final String MOD_NAME = "ChunkFix";
    public static final String VERSION = "@VERSION@";
    public static final String BUILD_TIME = "@BUILD_TIME@";

    public static Logger logger;

    private PacketHandler packetHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        File configFile = event.getSuggestedConfigurationFile();
        logger.info("Loading config from " + configFile);
        ChunkFixConfig.instance.load(configFile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(String.format("%s version %s built at %s", MOD_NAME, VERSION, BUILD_TIME));

        packetHandler = new PacketHandler();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ChunkFixMod.MOD_ID)) {
            ChunkFixConfig.instance.afterGuiSave();
        }
    }

    @SubscribeEvent
    public void injectPacketHandler(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        if (!ChunkFixConfig.instance.enabled) return;
        ChannelPipeline pipe = e.getManager().channel().pipeline();
        pipe.addBefore("fml:packet_handler", PacketHandler.NAME, packetHandler);
    }
}
