package twitter.state;

import twitter.Context;

public abstract class State {

    public abstract void printCliMenu(Context context);

    public abstract State doAction(Context context);

    public abstract void printFinalCliError();

    public abstract void close(Context context);

}
