package iam.thevoid.logger;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

final class FileWriter {
    private FileWriter() {
    }

    private static File outputFile;

    //
    static void init(Context context) {
        String outputDirName = "logs";
        File outputDir = new File(context.getFilesDir(), outputDirName);
        if (!outputDir.exists())
            outputDir.mkdirs();

        outputFile = new File(outputDir, "logs");
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    static void writeToFile(String message) throws IOException {
        Log.d("writeToFile", message);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter writer = null;
        try {
            fileOutputStream = new FileOutputStream(outputFile, true);
            writer = new OutputStreamWriter(fileOutputStream);
            writer.append(message);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null)
                fileOutputStream.close();
            if (writer != null)
                writer.close();
        }
    }
}
