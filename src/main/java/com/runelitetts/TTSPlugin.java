package com.runelitetts;

import com.runelitetts.engine.AbstractEngine;
import com.runelitetts.engine.GoogleCloudEngine;
import com.runelitetts.engine.TTSEngine;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.IOException;

@Slf4j
@PluginDescriptor(
	name = "Text-to-Speech"
)
public class TTSPlugin extends Plugin
{
	private String lastNpcDialogueText = null;
	private String lastPlayerDialogueText = null;
	private Widget[] dialogueOptions;

	private final TTSEngine ttsEngine = new TTSEngine(GoogleCloudEngine.class);

	private static final int WIDGET_CHILD_ID_DIALOG_PLAYER_CLICK_HERE_TO_CONTINUE = 4;
	private static final int WIDGET_CHILD_ID_DIALOG_NPC_CLICK_HERE_TO_CONTINUE = 4;
	private static final int WIDGET_CHILD_ID_DIALOG_PLAYER_NAME = 3; // For some reason there is no WidgetInfo for this despite there being an (innaccessible to me) wi

	@Inject
	private Client client;

	@Inject
	private TTSPluginConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	/**
	 * This subscription is used to cancel audio that's currently playing when the user clicks the "continue" blue text.
	 * This does NOT handle spacebar hotkey skipping.
	 * @param event
	 */
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		final int groupId = WidgetInfo.TO_GROUP(event.getWidgetId());
		final int childId = WidgetInfo.TO_CHILD(event.getWidgetId());

		// Stop playing audio if the user clicks continue on a dialog option and the NPC is speaking
		if (groupId == WidgetID.DIALOG_NPC_GROUP_ID && childId == WIDGET_CHILD_ID_DIALOG_NPC_CLICK_HERE_TO_CONTINUE) {
			ttsEngine.stopAudio();
		// Stop playing audio if the user clicks continue on a dialog option and the player is speaking
		} else if (groupId == WidgetID.DIALOG_PLAYER_GROUP_ID && childId == WIDGET_CHILD_ID_DIALOG_PLAYER_CLICK_HERE_TO_CONTINUE) {
			ttsEngine.stopAudio();
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick) {

		Widget npcDialogueTextWidget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);

		// Convert the NPC dialog into speech only if we haven't seen it before
		if (npcDialogueTextWidget != null && npcDialogueTextWidget.getText() != lastNpcDialogueText) {
			String npcText = npcDialogueTextWidget.getText();
			lastNpcDialogueText = npcText;

			// Strip the line break
			String strippedNpcText = npcText.replace("<br>", " ");

			String npcName = client.getWidget(WidgetInfo.DIALOG_NPC_NAME).getText();
			try {
				ttsEngine.textToSpeech(AbstractEngine.SpeechType.NPC_MAN, strippedNpcText, true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			log.info(npcName + ": " + strippedNpcText);
		}

		// This should be in WidgetInfo under DialogPlayer, but isn't currently.
		Widget playerDialogueTextWidget = client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT);

		if (playerDialogueTextWidget != null && playerDialogueTextWidget.getText() != lastPlayerDialogueText) {
			String playerText = playerDialogueTextWidget.getText();
			lastPlayerDialogueText = playerText;

			String strippedPlayerText = playerText.replace("<br>", " ");

			log.info("Player: " + strippedPlayerText);

			try {
				ttsEngine.textToSpeech(AbstractEngine.SpeechType.PLAYER_MAN, strippedPlayerText, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Widget playerDialogueOptionsWidget = client.getWidget(WidgetID.DIALOG_OPTION_GROUP_ID, 1);
		if (playerDialogueOptionsWidget != null && playerDialogueOptionsWidget.getChildren() != dialogueOptions) {
			dialogueOptions = playerDialogueOptionsWidget.getChildren();
			for (int i = 1; i < dialogueOptions.length - 2; i++) {
				System.out.println(dialogueOptions[i].getText());
			}
		}
	}

	@Provides
	TTSPluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TTSPluginConfig.class);
	}
}