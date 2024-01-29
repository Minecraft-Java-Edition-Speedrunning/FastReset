package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuMixin extends Screen {
    protected GameMenuMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void disconnect();

    @Unique
    private static final int bottomRightWidth = 102;

    @Redirect(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/ClickableWidget;I)Lnet/minecraft/client/gui/widget/ClickableWidget;"))
    private <T extends ClickableWidget> T addButtons(GridWidget.Adder instance, T widget, int occupiedColumns) {
        final ButtonWidget.Builder saveButton = ButtonWidget.builder(Text.translatable("menu.quitWorld"), (buttonWidgetX) -> {
            Client.saveOnQuit = false;
            this.disconnect();
            Client.saveOnQuit = true;
        });

        if (Client.buttonLocation == 2) {
            // add menu.quitWorld button instead of save button
            instance.add(saveButton.width(204).build(), occupiedColumns);
            return this.addDrawableChild(widget);
        }

        int height = 20;
        int width;
        int x;
        int y;
        switch (Client.buttonLocation) {
            // bottom right build
            case 0 -> {
                width = 102;
                x = this.width - width - 4;
                y = this.height - height - 4;
            }
            // center build
            case 1, default -> {
                width = 204;
                x = this.width / 2 - width / 2;
                y = this.height / 4 + 148 - height;
            }
        }


        this.addDrawableChild(saveButton.dimensions(x, y, width, height).build());
        return instance.add(widget, occupiedColumns);
    }

    @Redirect(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 1))
    private ButtonWidget.Builder moveExitButton(ButtonWidget.Builder instance, int width) {
        if (Client.buttonLocation != 2) {
            return instance.width(width);
        }
        return instance.position((int) (this.width - (bottomRightWidth * 1.5) - 4), this.height - 24).width((int) (bottomRightWidth * 1.5));
    }
}
