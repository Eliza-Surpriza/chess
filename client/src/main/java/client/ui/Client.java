package client.ui;

import java.io.IOException;

public interface Client {
    public String eval(String input) throws IOException;
    public String help();

}
