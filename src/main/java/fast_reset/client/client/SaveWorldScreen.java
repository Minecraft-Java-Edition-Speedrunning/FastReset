package fast_reset.client.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;

public class SaveWorldScreen extends Screen {
    public SaveWorldScreen(){
        super(Text.translatable("menu.savingLevel"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        this.drawCenteredText(matrices, this.textRenderer, Text.translatable("still saving the last world"), this.width / 2, 70, Color.white.getRGB());
    }
}