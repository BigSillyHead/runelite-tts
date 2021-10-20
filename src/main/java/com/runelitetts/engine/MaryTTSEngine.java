package com.runelitetts.engine;

//@Slf4j
//public class MaryTTSEngine implements AbstractEngine {
//
//    static final String NAME = "txt2wav";
//    static final String IN_OPT = "input";
//    static final String OUT_OPT = "output";
//    static final String VOICE_OPT = "voice";
//
//    @Override
//    public byte[] textToMp3Bytes(String input) throws IOException {
//
//        // get output option
//        String outputFileName = "output.wav";
//
//        // get inputthe voice name
//        String voiceName = "cmu-slt-hsmm";
//
//        // init mary
//        LocalMaryInterface mary = null;
//        try {
//            mary = new LocalMaryInterface();
//        } catch (MaryConfigurationException e) {
//            throw new IOException("Could not initialize MaryTTS interface", e);
//        }
//
//        // Set voice / language
//        mary.setVoice(voiceName);
//
//        // synthesize
//        AudioInputStream audio = null;
//        try {
//            audio = mary.generateAudio(input);
//        } catch (SynthesisException e) {
//            System.err.println("Synthesis failed: " + e.getMessage());
//            System.exit(1);
//        }
//
//        // write to output
//        double[] samples = MaryAudioUtils.getSamplesAsDoubleArray(audio);
//
////        ByteBuffer bb = ByteBuffer.allocate(samples.length * 8);
////        for(double d : samples) {
////            bb.putDouble(d);
////        }
////
////        InputStream myInputStream = new ByteArrayInputStream(bb.array());
////
////        Thread test = new Thread(() -> {
////            try{
//////                    FileInputStream fis = new FileInputStream("output.mp3");
////                Player playMP3 = new Player(myInputStream);
////
////                playMP3.play();
////            } catch(Exception e){System.out.println(e);}
////        });
////
////        test.start();
//        try {
//            MaryAudioUtils.writeWavFile(samples, outputFileName, audio.getFormat());
//            System.out.println("Output written to " + outputFileName);
//        } catch (IOException e) {
//            System.err.println("Could not write to file: " + outputFileName + "\n" + e.getMessage());
//        }
//
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(outputFileName).getAbsoluteFile());
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.start();
//        } catch(Exception ex) {
//            System.out.println("Error with playing sound.");
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    public byte[] textToSpeechPlayer(String input) throws IOException {
//        return new byte[0];
//    }
//}