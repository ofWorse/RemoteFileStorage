package util;

import exception.AppException;

import java.util.concurrent.Callable;

public class Util {
    public static <T> T wrap(Callable<T> action) {
        try {
            return action.call();
        }
        catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }
}
