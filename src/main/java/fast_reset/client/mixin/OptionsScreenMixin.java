package fast_reset.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fast_reset.client.Client;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", shift = At.Shift.AFTER), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;CREDITS_AND_ATTRIBUTION_TEXT:Lnet/minecraft/text/Text;")))
    public void initInject(CallbackInfo ci, @Local GridWidget.Adder adder){
        adder.add(ButtonWidget.builder(getButtonText(), (buttonWidget) -> {
            Client.updateButtonLocation();
            buttonWidget.setMessage(getButtonText());
        }).build());
    }
}
