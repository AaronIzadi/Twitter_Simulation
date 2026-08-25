package twitter.main;

import twitter.logic.AccountManager;
import twitter.logic.TimeLineManager;
import twitter.logic.TweetManager;
import twitter.repository.Repository;
import twitter.state.State;
import twitter.state.startings.StartState;
import twitter.utils.Logger;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Context {

    private final TweetManager tweetManager = new TweetManager();
    private final AccountManager accountManager = new AccountManager(tweetManager);
    private final TimeLineManager timeLineManager = new TimeLineManager();
    private final Stack<State> stateTrace = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = new Logger();
    private final Repository repository = Repository.getInstance();

    public void run() throws IOException {
        repository.getIdCounter();
        logger.info("Application started");

        stateTrace.push(new StartState());
        while (!stateTrace.empty()) {
            State state = getLastState();

            State nextState = state.doAction(this);
            logStateTransition(state, nextState);
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
        }

        logger.info("Application stopped");
    }

    public State getLastState() {
        if (stateTrace.empty()) {
            return null;
        }
        return stateTrace.peek();
    }

    public String nameLastState() {
        State state = getLastState();
        return state == null ? "" : state.getClass().getSimpleName();
    }

    public Logger getLogger() {
        return logger;
    }

    private void logStateTransition(State current, State next) {
        if (current == null) {
            return;
        }
        String from = current.getClass().getSimpleName();
        if (next == null) {
            logger.info("Navigation: " + from + " -> Menu");
        } else if (current != next) {
            logger.info("Navigation: " + from + " -> " + next.getClass().getSimpleName());
        }
    }

    public void clearStack() {
        stateTrace.clear();
    }

    public TimeLineManager getTimeLineManager() {
        return timeLineManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public TweetManager getTweetManager() {
        return tweetManager;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
