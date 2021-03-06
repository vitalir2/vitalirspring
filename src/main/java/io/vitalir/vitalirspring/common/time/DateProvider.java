package io.vitalir.vitalirspring.common.time;

import org.springframework.lang.NonNull;

import java.util.Date;

public interface DateProvider {

    @NonNull
    Date getDate(long time, boolean isDateFromCurrent);

    @NonNull
    Date getDate();
}
