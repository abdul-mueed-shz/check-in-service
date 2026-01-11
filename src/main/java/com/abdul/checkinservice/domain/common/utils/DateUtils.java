package com.abdul.checkinservice.domain.common.utils;

import java.time.Duration;
import java.time.Instant;

public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Integer getDifferenceBetweenInstants(Instant from, Instant to) {
        Duration duration = Duration.between(from, to);
        long hours = duration.toHours();
        if (hours < 0) {
            return 0;
        }
        return (int) hours;
    }
}
