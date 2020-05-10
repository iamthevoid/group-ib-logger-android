package iam.thevoid.logger;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Общие условия:
 * - поддержка API14+;
 * - реализация библиотеки на Java (не использовать гибридные платформы);
 * - все потенциально ресурсоемкие операции должны выполняться не в UI-thread;
 * - минимальное, а лучше нулевое, вмешательство в код тестового приложения для
 * выполнения требуемой функциональности библиотеки (sdk). Инициализация библиотеки в рамках тестового приложения разрешена.
 *
 * Задача: создать тестовое приложение с одним Activity и библиотеку на Java, которая реализует:
 * A. сбор информации об UI-элементах тестового приложения;
 * B. сбор информации о действиях пользователя в рамках Activity;
 * C. сохранение данной информации в background thread сервиса в файл.
 *
 * Бонусная задача: в рамках библиотеки из предыдущей задачи по возможности реализовать сбор и сохранение дополнительных данных:
 * A. uptime OS Android;
 * B. времени заливки OS Android;
 * C. времени начала использования OS Android.
 */

/*
    В качестве приложения взят один из дефолтных пресетов для создания андроид проекта


    Минимальное, а лучше нулевое, вмешательство в код тестового приложения для выполнения требуемой
    функциональности библиотеки (sdk). Инициализация библиотеки в рамках тестового приложения разрешена.

    Реализовано при помощи Application.ActivityLifecycleCallbacks и FragmentManager.FragmentLifecycleCallbacks,
    так как это не требует вмешательства в код приложения. Мы можем реагировать на события жизненного
    цикла основных компонентов системы и получать из них необходимые сведения



    A. сбор информации об UI-элементах тестового приложения;

    Осуществляется при помощи дампа иерархии корневой View активити и фрагмента. На данный момент
    реализовано при помощи метода toString на View, при необходимости это легко можно изменить
    (инкапсулировано в отдельный метод). Происходит в методах Activity#onStart и Fragment#onViewCreated,
    так как в этот момент View создано и его можно инспектировать



    B. сбор информации о действиях пользователя в рамках Activity

    Реализован при помощи TouchDelegate. На каждое View навешивается делегат с размерами самого View,
    который перехватывает Touch-евенты. В этот момент внутри делегата срабатывает callback OnInterceptTouch
    Который мы можем реализовать при объявлении делегата. В этот callback прилетает сам MotionEvent,
    а так же View, для которой реализован делегат (она хранится в WeakReference, для того, чтобы
    сборщик мусора при необходиости мог прибить ссылку на неё для избежания утечек в памяти)
    При необходимости можно заменить реализацию минимальными телодвижениями - в коде идёт обращение
    к самой View и к callback'у, сама реализация скрыта внутри метода.



    C. сохранение данной информации в background thread сервиса в файл.

    Logger хранит в себе ссылку на Executor, который оперирует одинм потоком. В него отправляются
    объекты класса Message, которые имплементируют интерфейс Runnable и внутри в синхронизированном
    блоке (по типу Message) записывают содежимое в файл. Это гарантирует последовательную запись
    логов и мы не получим ситуации, при которой одним файлом завладеют сразу несколько потоков (коллапс).

    Сама запись в файл реализована в классе FileWriter. Файл хранится в папке к которой у библиотеки
    всегда будет доступ (её личное файловое пространство) и при необходимости может быть взят оттуда
    в любой момент


    * A. uptime OS Android;
    * B. времени заливки OS Android;

    реализовано в методе Logger#getSystemInfo().



    * C. времени начала использования OS Android.

   Не реализовано. Не нашёл способа сделать это не имея root доступа. Беглый осмотр файлов показал,
   что на эмуляторе есть что то похожее на эту проперти в /data/property/persistent_properties
   (reboot,factory_reset), но на физическом девайсе этой проперти нет из чего делаю вывод, что код
   получения её не будет универсальным

 */

public final class Logger {
    private Logger() {}

    private static Executor executor = Executors.newSingleThreadExecutor();

    public static void init(Context context) {
        FileWriter.init(context);
        ActivityActionsLogger instance = ActivityActionsLogger.getInstance();
        ((Application) context.getApplicationContext())
                .registerActivityLifecycleCallbacks(instance);
        Thread.setDefaultUncaughtExceptionHandler(
                new ExceptionsHandler(Thread.getDefaultUncaughtExceptionHandler()));
        logSystemInfo();
    }


    static void log(final String tag, final Object message) {
        log(tag, message, null);
    }

    static void log(final String tag, final Object message, final Throwable throwable) {
        executor.execute(new Message(tag, message == null ? "" : message.toString(), throwable));
    }

    private static void logSystemInfo() {
        long message = SystemClock.elapsedRealtime();
        Logger.log("logSystemInfo",
                "Uptime from: " + (System.currentTimeMillis() - message));
        Logger.log("logSystemInfo",
                "Uptime millis: " + message);
        Logger.log("logSystemInfo",
                "System flashed unix time: " + Terminal.getOutput(Terminal.CMD_FLASHING_DATE));
    }
}
