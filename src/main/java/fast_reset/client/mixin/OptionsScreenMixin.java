package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    private static Text getButtonText(){
        switch(Client.buttonLocation){
            case 0:
                return Text.literal("bottom right");
            case 1:
                return Text.literal("center");
            case 2:
            default:
                return Text.literal("replace s&q");
        }
    }

    @Inject(method = "init", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/ClickableWidget;ILnet/minecraft/client/gui/widget/Positioner;)Lnet/minecraft/client/gui/widget/ClickableWidget;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void initInject(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder){
        adder.add(ButtonWidget.builder(getButtonText(), (buttonWidget) -> {
            Client.updateButtonLocation();
            buttonWidget.setMessage(getButtonText());
        }).build());
    }
}
