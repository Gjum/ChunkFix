package gjum.minecraft.forge.chunkfix;

import io.netty.channel.ChannelPipeline;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;

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
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(String.format("%s version %s built at %s", MOD_NAME, VERSION, BUILD_TIME));

        packetHandler = new PacketHandler();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        ChannelPipeline pipe = e.getManager().channel().pipeline();
        pipe.addBefore("fml:packet_handler", PacketHandler.NAME, packetHandler);
    }
}
