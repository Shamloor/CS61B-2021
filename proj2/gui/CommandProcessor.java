package gui;

class CommandProcessor {
    private CommandProcessor() {
    }

    public void processCmd(String command) {
        System.out.println("User command: " + command);
    }

    public static CommandProcessor getInstance() {
        return CommandProcessorHolder.INSTANCE;
    }

    private static final class CommandProcessorHolder {
        static final CommandProcessor INSTANCE = new CommandProcessor();
    }
}