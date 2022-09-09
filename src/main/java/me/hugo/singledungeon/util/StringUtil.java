package me.hugo.singledungeon.util;

public final class StringUtil {

    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secondsLeft = seconds - (minutes * 60);

        return minutes + ":" + (secondsLeft < 10 ? "0" + secondsLeft : secondsLeft);
    }

}
