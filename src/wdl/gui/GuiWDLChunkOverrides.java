package wdl.gui;

import java.io.IOException;

import com.google.common.collect.Multimap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import wdl.WDL;
import wdl.WDLPluginChannels;
import wdl.WDLPluginChannels.ChunkRange;

/**
 * A GUI that Lists... well, will list, the current chunk overrides.  Currently
 * a work in progress.
 * 
 * Also, expect a possible minimap integration in the future.
 */
public class GuiWDLChunkOverrides extends GuiScreen {
	private static final int TOP_MARGIN = 61, BOTTOM_MARGIN = 32;
	
	private TextList list;
	/**
	 * Parent GUI screen; displayed when this GUI is closed.
	 */
	private final GuiScreen parent;
	
	/**
	 * Text fields for creating a new range.
	 */
	private GuiNumericTextField x1Field, z1Field, x2Field, z2Field;

	private GuiButton startDownloadButton;
	
	public GuiWDLChunkOverrides(GuiScreen parent) {
		this.parent = parent;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.list = new TextList(mc, width, height, TOP_MARGIN, BOTTOM_MARGIN);
		list.addLine("\u00A7c\u00A7lThis is a work in progress.");
		list.addLine("You can download in overriden chunks even if you are " +
				"not allowed to download elsewhere on the server.");
		list.addLine("Here is a list of the current chunk overrides; in the " +
				"future, a map will appear here.");
		list.addLine("Maybe also there will be a minimap mod integration.");
		list.addBlankLine();
		list.addLine(WDLPluginChannels.getChunkOverrides().toString());
		list.addBlankLine();
		list.addLine("You are requesting the following ranges " +
				"(to submit your request, go to the permission request page): ");
		for (ChunkRange range : WDLPluginChannels.getChunkOverrideRequests()) {
			list.addLine(range.toString());
		}
		
		this.x1Field = new GuiNumericTextField(0, fontRendererObj,
				this.width / 2 - 138, 18, 33, 20);
		this.z1Field = new GuiNumericTextField(1, fontRendererObj,
				this.width / 2 - 87, 18, 33, 20);
		this.x2Field = new GuiNumericTextField(2, fontRendererObj,
				this.width / 2 - 36, 18, 33, 20);
		this.z2Field = new GuiNumericTextField(3, fontRendererObj,
				this.width / 2 + 15, 18, 33, 20);
		
		this.buttonList.add(new GuiButton(5, width / 2 - 155, 18, 150, 20,
				"Add range request"));
		startDownloadButton = new GuiButton(6, width / 2 + 5, 18, 150, 20,
				"Start download in these ranges");
		startDownloadButton.enabled = WDLPluginChannels.canDownloadAtAll();
		this.buttonList.add(startDownloadButton);
		
		this.buttonList.add(new GuiButton(100, width / 2 - 100, height - 29,
				I18n.format("gui.done")));
		
		this.buttonList.add(new GuiButton(200, this.width / 2 - 155, 39, 100, 20,
				"Current perms"));
		this.buttonList.add(new GuiButton(201, this.width / 2 - 50, 39, 100, 20,
				"Request perms"));
		this.buttonList.add(new GuiButton(202, this.width / 2 + 55, 39, 100, 20,
				"Chunk Overrides"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 5) {
			//TODO
		}
		if (button.id == 6) {
			if (!WDLPluginChannels.canDownloadAtAll()) {
				button.enabled = false;
				return;
			}
			WDL.startDownload();
		}
		
		if (button.id == 100) {
			this.mc.displayGuiScreen(this.parent);
		}
		
		if (button.id == 200) {
			this.mc.displayGuiScreen(new GuiWDLPermissions(this.parent));
		}
		if (button.id == 201) {
			this.mc.displayGuiScreen(new GuiWDLPermissionRequest(this.parent));
		}
		if (button.id == 202) {
			// Would open this GUI; do nothing.
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
	throws IOException {
		x1Field.mouseClicked(mouseX, mouseY, mouseButton);
		z1Field.mouseClicked(mouseX, mouseY, mouseButton);
		x2Field.mouseClicked(mouseX, mouseY, mouseButton);
		z2Field.mouseClicked(mouseX, mouseY, mouseButton);
		list.func_148179_a(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.list.func_178039_p();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (list.func_148181_b(mouseX, mouseY, state)) {
			return;
		}
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		x1Field.textboxKeyTyped(typedChar, keyCode);
		z1Field.textboxKeyTyped(typedChar, keyCode);
		x2Field.textboxKeyTyped(typedChar, keyCode);
		z2Field.textboxKeyTyped(typedChar, keyCode);
		
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void updateScreen() {
		x1Field.updateCursorCounter();
		z1Field.updateCursorCounter();
		x2Field.updateCursorCounter();
		z2Field.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.list == null) {
			return;
		}
		
		this.list.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawCenteredString(this.fontRendererObj, "Chunk overrides",
				this.width / 2, 8, 0xFFFFFF);
		
		x1Field.drawTextBox();
		z1Field.drawTextBox();
		x2Field.drawTextBox();
		z2Field.drawTextBox();
		
		this.drawString(fontRendererObj, "X1:", x1Field.xPosition - 16, 24, 0xFFFFFF);
		this.drawString(fontRendererObj, "Z1:", z1Field.xPosition - 16, 24, 0xFFFFFF);
		this.drawString(fontRendererObj, "X2:", x2Field.xPosition - 16, 24, 0xFFFFFF);
		this.drawString(fontRendererObj, "Z2:", z2Field.xPosition - 16, 24, 0xFFFFFF);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		for (Multimap<String, ChunkRange> group : WDLPluginChannels.getChunkOverrides().values()) {
			for (ChunkRange range : group.values()) {
				drawRange(range, 0xFF);
			}
		}
	}
	
	/**
	 * "seed" for the color RNG.  What actually is happening is that this
	 * changes the value of the hashcode, to make it hopefully look better
	 * (eg an empty string is no longer black)
	 */
	private static final String RNG_SEED = "A seed for better color RNG!";
	
	/**
	 * Draws the given range at the proper position on screen.
	 * 
	 * @param range The range to draw.
	 * @param alpha The transparency.  0xFF: Fully solid, 0x00: Fully transparent
	 */
	private void drawRange(ChunkRange range, int alpha) {
		int color = (range.tag + RNG_SEED).hashCode() & 0x00FFFFFF;
		
		int x1 = range.x1 * 8 + this.width / 2;
		int z1 = range.z1 * 8 + this.height / 2;
		int x2 = range.x2 * 8 + this.width / 2 + 7;
		int z2 = range.z2 * 8 + this.height / 2 + 7;
		
		drawRect(x1, z1, x2, z2, color + (alpha << 24));
		
		int colorDark = darken(color);
		
		drawVerticalLine(x1, z1, z2, colorDark + (alpha << 24));
		drawVerticalLine(x2, z1, z2, colorDark + (alpha << 24));
		drawHorizontalLine(x1, x2, z1, colorDark + (alpha << 24));
		drawHorizontalLine(x1, x2, z2, colorDark + (alpha << 24));
	}
	
	/**
	 * Halves the brightness of the given color.
	 */
	private int darken(int color) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		r /= 2;
		g /= 2;
		b /= 2;

		return (r << 16) + (g << 8) + b;
	}
}
