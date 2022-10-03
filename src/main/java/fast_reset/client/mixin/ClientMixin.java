package fast_reset.client.mixin;

import com.mojang.serialization.Lifecycle;
import fast_reset.client.Client;
import fast_reset.client.client.SaveWorldScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServerLoader.class)
public abstract class ClientMixin {

    // had to change the minecraft class to mix into, kept the class name to keep it in sync with the other versions

    @Inject(method = "tryLoad", at=@At("HEAD"))
    private static void worldWait(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, CallbackInfo ci){
        client.setScreen(new SaveWorldScreen());
        synchronized(Client.saveLock){
            System.out.println("done waiting for save lock");
        }
    }
}