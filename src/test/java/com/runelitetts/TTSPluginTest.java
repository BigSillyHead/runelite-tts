package com.runelitetts;

import com.runelitetts.engine.AbstractEngine;
import com.runelitetts.engine.GoogleCloudEngine;
//import com.bsh.engine.MaryTTSEngine;
import com.runelitetts.engine.MaryTTSEngine;
import com.runelitetts.engine.TTSEngine;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TTSPluginTest
{

	public static void main(String[] args) throws Exception
	{
//		final TTSEngine googleCloudEngine = new TTSEngine(GoogleCloudEngine.class);
//		googleCloudEngine.textToSpeech(AbstractEngine.SpeechType.PLAYER_MAN, "Hello, I am a player.");
////		Thread.sleep(4000);
//		googleCloudEngine.textToSpeech(AbstractEngine.SpeechType.NPC_MAN, "Hello, I am an NPC.");
//
//		googleCloudEngine.shutdown();

//		final TTSEngine maryTtsEngine = new TTSEngine(MaryTTSEngine.class);
//		maryTtsEngine.textToSpeech(AbstractEngine.SpeechType.NPC_MAN, "Hello, I am an NPC.", false);

		ExternalPluginManager.loadBuiltin(TTSPlugin.class);
		RuneLite.main(args);

//		final String exampleDialogue = "The chance to get a higher tier clue from geodes, bottles and nests is lower the higher the tier. But if you chop down higher-level trees you will have a higher chance of receiving a nest which contains a clue. Same with fishing and mining.";
//		final String test = "Hello Amelia, this is a computer!";
//
//		try (OutputStream out = new FileOutputStream("output.mp3")) {
//			GoogleCloudEngine engine = new GoogleCloudEngine();
//			engine.textToSpeech(test, out);
//		}
	}
}