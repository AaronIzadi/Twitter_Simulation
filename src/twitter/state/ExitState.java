package twitter.state;

import twitter.Context;
import twitter.model.Account;

import java.io.IOException;

public class ExitState extends State{
    @Override
    public void printCliMenu(Context context) {
    }

    @Override
    public State doAction(Context context) throws IOException {

        if (context.nameLastState().equals("MenuState")){
            context.getAccountManager().updateStatus(Account.OFFLINE);
        }

        context.clearStack();

        return null;
    }

    @Override
    public void printFinalCliError() {

    }

    @Override
    public void close(Context context) {

    }
}
