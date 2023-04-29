package twitter;

import twitter.logic.HandleAccount;
import twitter.logic.HandleTimeLine;
import twitter.logic.HandleTweet;
import twitter.model.Account;
import twitter.state.startings.StartState;
import twitter.state.State;
import twitter.utils.ConsoleColors;
import twitter.utils.Logger;

import java.util.Scanner;
import java.util.Stack;

public class Context {

    private final HandleAccount handleAccount = new HandleAccount();
    private final HandleTweet handleTweet = new HandleTweet();
    private final HandleTimeLine handleTimeLine = new HandleTimeLine();
    private final Stack<State> stateTrace = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);
    private Logger logger= new Logger();

    public void run() {

        stateTrace.push(new StartState());
        while (!stateTrace.empty()) {
            State state = getLastState();


            State nextState = state.doAction(this);
            state.close(this);

            if (nextState == null) {
                if (!stateTrace.empty()) {
                    stateTrace.pop();
                }
            } else {
                while (!stateTrace.empty() && stateTrace.peek() == nextState) {
                    stateTrace.pop();
                }
                stateTrace.add(nextState);
            }
            logStack(); // checking Stack of States by their usage
        }

    }

    public State getLastState() {
        if (!stateTrace.empty()) {
            return stateTrace.peek();
        } else {
            return null;
        }
    }

    public String nameLastState() {
        return getLastState().getClass().getSimpleName();
    }

    public void logStack() {
        System.out.println(ConsoleColors.RED_UNDERLINED);
        System.out.println("@----------LOG-STACK----------@");
        stateTrace.forEach(state -> System.out.println(state.getClass().getName()));
        System.out.println("@-----------------------------@");
    }

    public Logger getLogger() { return logger; }

    public void clearStack() {
        stateTrace.clear();
    }

    Account getUser() {
        return handleAccount.getUser();
    }

    public HandleTimeLine getHandleTimeLine() {
        return handleTimeLine;
    }

    public HandleAccount getHandleAccount() {
        return handleAccount;
    }

    public HandleTweet getHandleTweet() {
        return handleTweet;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
