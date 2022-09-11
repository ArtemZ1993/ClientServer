package part2.server;

import java.io.*;

public interface IHandler {
    public void Handle(InputStream inClient, OutputStream outClient) throws IOException, ClassNotFoundException, Exception;
}