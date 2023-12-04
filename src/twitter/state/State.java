package twitter.state;

import twitter.Context;

import java.io.IOException;

public abstract class State {

    public abstract void printCliMenu(Context context) throws IOException;

    public abstract State doAction(Context context) throws IOException;

    public abstract void printFinalCliError();

    public abstract void close(Context context);

}
