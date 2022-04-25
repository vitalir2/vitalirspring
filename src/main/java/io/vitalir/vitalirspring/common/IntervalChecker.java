package io.vitalir.vitalirspring.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class IntervalChecker {

    public boolean intersect(
            LocalDateTime fromFirst,
            LocalDateTime toFirst,
            LocalDateTime fromSecond,
            LocalDateTime toSecond
    ) {
        if (fromFirst.isAfter(toFirst) || fromSecond.isAfter(toSecond)) {
            throw new IllegalArgumentException(
                    "Invalid interval:" +
                            " from1=" + fromFirst + ", to1=" + toFirst +
                            ", from2=" + fromSecond + ", to2=" + toSecond
            );
        }
        var happensBefore = toFirst.isBefore(fromSecond);
        var happensAfter = fromFirst.isAfter(toSecond);
        return !(happensBefore || happensAfter);
    }
}
