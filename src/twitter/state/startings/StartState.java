package twitter.state.startings;

import twitter.main.Context;
import twitter.utils.ConsoleColors;
import twitter.state.ExitState;
import twitter.state.State;
import twitter.utils.Logger;

public class StartState extends State {

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "1. Start the app or 2. Exit?");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        Logger log = context.getLogger();

        String ch = context.getScanner().nextLine();

        switch (ch) {
            case "1":

                System.out.println(ConsoleColors.YELLOW + "1. Create an account or 2. Log in?");

                String choice = context.getScanner().nextLine();

                switch (choice) {
                    case "1":
                        log.info("Session setup: create account");
                        return new CreateAccountState();
                    case "2":
                        log.info("Session setup: login");
                        return new LoginState();
                    default:
                        printFinalCliError();
                        log.warn("Invalid menu selection");
                        return this;
                }

            case "2":
                return new ExitState();
            default:
                printFinalCliError();
                log.warn("Invalid menu selection");
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter a valid number.");
    }

    @Override
    public void close(Context context) {

    }

}
