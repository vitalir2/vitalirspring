package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.common.time.IntervalChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class IntervalCheckerTest {

    private static final LocalDateTime INIT_FROM = createTimeInTestDate(15, 20);

    private static final LocalDateTime INIT_TO = createTimeInTestDate(15, 50);

    private final IntervalChecker intervalChecker = new IntervalChecker();

    @Test
    void whenIntersectByFromDate_returnTrue() {
        var fromDate = createTimeInTestDate(15, 30);
        var toDate = createTimeInTestDate(16, 50);

        var result = intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate);

        assertThat(result).isTrue();
    }

    @Test
    void whenIntersectByToDate_returnTrue() {
        var fromDate = createTimeInTestDate(14, 30);
        var toDate = createTimeInTestDate(15, 40);

        var result = intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate);

        assertThat(result).isTrue();
    }

    @Test
    void whenIntersectByInInterval_returnTrue() {
        var fromDate = createTimeInTestDate(15, 30);
        var toDate = createTimeInTestDate(15, 40);

        var result = intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate);

        assertThat(result).isTrue();
    }

    @Test
    void whenIntersectByOutInterval_returnTrue() {
        var fromDate = createTimeInTestDate(14, 30);
        var toDate = createTimeInTestDate(16, 50);

        var result = intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate);

        assertThat(result).isTrue();
    }

    @Test
    void whenDoesNotIntersect_returnFalse() {
        var fromDate = createTimeInTestDate(14, 30);
        var toDate = createTimeInTestDate(15, 0);

        var result = intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate);

        assertThat(result).isFalse();
    }

    @Test
    void whenInvalidInterval_throwIllegalArgument() {
        var fromDate = createTimeInTestDate(16, 30);
        var toDate = createTimeInTestDate(15, 0);

        assertThatThrownBy(() -> intervalChecker.intersect(INIT_FROM, INIT_TO, fromDate, toDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static LocalDateTime createTimeInTestDate(int hours, int minutes) {
        return LocalDateTime.of(
                2022,
                1,
                15,
                hours,
                minutes
        );
    }
}
