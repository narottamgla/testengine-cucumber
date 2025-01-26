package com.api.dto;

import lombok.Getter;

public enum Wait {
    MIN_WAIT(10L), SMALL_WAIT(30L), MED_WAIT(60L), LONG_WAIT(120L), MAX_WAIT(300L), HIGHEST_WAIT(600L);

    @Getter
    private Long timeout;

    Wait(final Long timeout) {
        this.timeout = timeout;
    }
}

