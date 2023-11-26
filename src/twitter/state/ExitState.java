package twitter.state;

import twitter.Context;
import twitter.model.Account;

public class ExitState extends State{
    @Override
    public void printCliMenu(Context context) {
    }

    @Override
    public State doAction(Context context) {

        if (context.nameLastState().equals("MenuState")){
            context.getHandleAccount().updateStatus(Account.OFFLINE);
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
