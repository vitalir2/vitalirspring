package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.common.time.DateProvider;
import io.vitalir.vitalirspring.common.time.JavaDateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaDateProviderTest {

    private static final long SPECIFIC_TIME = 100L;

    private DateProvider dateProvider;

    @BeforeEach
    public void init() {
        dateProvider = new JavaDateProvider();
    }

    @Test
    public void whenGetDateWithMillis_returnIt() {
        assertEquals(new Date(SPECIFIC_TIME), dateProvider.getDate(SPECIFIC_TIME, false));
    }
}
