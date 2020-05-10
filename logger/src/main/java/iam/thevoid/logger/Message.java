package iam.thevoid.logger;

import androidx.annotation.Nullable;

import java.io.IOException;

public class Message implements Runnable {

    private final String tag;
    private final String message;
    private final Throwable throwable;

    Message(String tag, String message, @Nullable Throwable throwable) {
        this.tag = tag;
        this.message = message;
        this.throwable = throwable;
    }

    @Override
    public void run() {
        synchronized (Message.class) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n").append(tag).append(": ");
            builder.append(Thread.currentThread().getName())
                    .append("   :   ")
                    .append(message);

            if (throwable != null) {
                builder.append("\n");
                builder.append(throwable.toString()).append("\n");
                StackTraceElement[] trace = throwable.getStackTrace();
                for (StackTraceElement traceElement : trace)
                    builder.append("\tat ").append(traceElement).append("\n");
            }

            try {
                // запись будет последовательной, так как у нас singleThreadExecutor. Для безопасности
                // метод синхронизирован по классу Message, что предотвратит доступ к этому блоку
                // нескольких потоков
                FileWriter.writeToFile(builder.toString());
            } catch (IOException ignored) {
                // Здесь имеет смысл добавить отправку ошибки на бэкенд, но не стоит показывать ее
                // в логах
            }
        }
    }
}
