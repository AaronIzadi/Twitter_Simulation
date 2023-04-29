package twitter.state.startings;

import twitter.Context;
import twitter.utils.ConsoleColors;
import twitter.state.ExitState;
import twitter.state.State;
import twitter.utils.Logger;

public class StartState extends State {

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "1.Want to start the app? Or 2.Exit?");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        Logger log = context.getLogger();

        String ch = context.getScanner().nextLine();

        switch (ch) {
            case "1":

                System.out.println(ConsoleColors.YELLOW + "1.Want to Create account? Or 2.Log in?");
                log.info("App started.");

                String choice = context.getScanner().nextLine();

                switch (choice) {
                    case "1":
                        log.info("Going to create account state.");
                        return new CreateAccountState();
                    case "2":
                        log.info("Going to login state.");
                        return new LoginState();
                    default:
                        printFinalCliError();
                        log.info("Entered invalid number.");
                        return this;
                }

            case "2":
                return new ExitState();
            default:
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter valid number.");
    }

    @Override
    public void close(Context context) {

    }

}
