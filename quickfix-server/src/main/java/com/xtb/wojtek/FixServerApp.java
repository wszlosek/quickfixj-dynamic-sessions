package com.xtb.wojtek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import quickfix.ConfigError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TimeZone;

/**
 * Hello world!
 *
 * @author yanghuadong
 * @date 2019 -03-12
 */
@SpringBootApplication
public class FixServerApp {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws ConfigError the config error
     */
    public static void main(String[] args) throws Exception
    {

        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.out.println(stacktrace);
            }
        });

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(FixServerApp.class, args);
    }
}
