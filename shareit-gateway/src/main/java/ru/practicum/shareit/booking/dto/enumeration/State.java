package ru.practicum.shareit.booking.dto.enumeration;

public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static Status from(String stateParam) {
        for (Status value : Status.values()) {
            if (value.name().equals(stateParam)) {
                return value;
            }
        }
        return null;
    }
}
