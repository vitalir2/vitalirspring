package io.vitalir.vitalirspring.common;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JavaDateProvider implements DateProvider {

    @Override
    @NonNull
    public Date getDate(long time, boolean isDateFromCurrent) {
        if (isDateFromCurrent) {
            return new Date(System.currentTimeMillis() + time);
        }
        return new Date(time);
    }

    @Override
    @NonNull
    public Date getDate() {
        return new Date();
    }
}
