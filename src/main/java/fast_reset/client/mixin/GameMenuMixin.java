package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {

    protected GameMenuMixin(Text title) {
        super(title);
    }

    private static final int bottomRightWidth = 102;

    // kill save on the shutdown
    @Redirect(method = "initWidgets", at = @At(value = "NEW", target = "(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget;"), slice = @Slice(from = @At(value = "CONSTANT:FIRST", args = "stringValue=menu.returnToMenu")))
    private ButtonWidget createExitButton(int defaultX, int defaultY, int defaultWidth, int height, Text message, ButtonWidget.PressAction onPress){
        int x = Client.buttonLocation == 2 ? (int) (this.width - (bottomRightWidth * 1.5) - 4) : defaultX;
        int y = Client.buttonLocation == 2 ? this.height - 24 : defaultY;
        int width = Client.buttonLocation == 2 ? (int) (bottomRightWidth * 1.5) : defaultWidth;

        return new ButtonWidget(x, y, width, height, message, onPress);
    }

    @Inject(method = "initWidgets", at=@At(value ="TAIL"))
    private void createSaveButton(CallbackInfo ci){
        int height = 20;

        int width;
        int x;
        int y;
        switch(Client.buttonLocation){
            // bottom right build
            case 0:
                width = 102;
                x = this.width - width - 4;
                y = this.height - height - 4;
                break;
            // center build
            case 1:
                width = 204;
                x = this.width / 2 - width/2;
                y = this.height / 4 + 148 - height;
                break;
            case 2:
            default:
                width = 204;
                x = this.width / 2 - width/2;
                y = this.height / 4 + 124 - height;
                break;
        }


        this.addDrawableChild(new ButtonWidget(x, y, width, height, Text.translatable("menu.quitWorld"), (buttonWidgetX) -> {
            Client.saveOnQuit = false;

            boolean bl = this.client.isInSingleplayer();
            boolean bl2 = this.client.isConnectedToRealms();
            buttonWidgetX.active = false;
            this.client.world.disconnect();
            if (bl) {
                this.client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
            } else {
                this.client.disconnect();
            }


            TitleScreen titleScreen = new TitleScreen();
            if (bl) {
                this.client.setScreen(titleScreen);
            } else if (bl2) {
                this.client.setScreen(new RealmsMainScreen(titleScreen));
            } else {
                this.client.setScreen(new MultiplayerScreen(titleScreen));
            }
            Client.saveOnQuit = true;
        }));
    }
}
