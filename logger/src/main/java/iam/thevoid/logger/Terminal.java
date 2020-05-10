package iam.thevoid.logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public final class Terminal {
    private Terminal() {
    }

    public static String CMD_FLASHING_DATE = "getprop ro.build.date.utc";

    public static String getOutput(String command) {
        String output = "";
        InputStream inputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec(command);
            inputStream = exec.getInputStream();
            output = readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }
}
