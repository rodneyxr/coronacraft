package io.github.rodneyxr.coronacraft;

public enum CoronaLevel {
    MILD(0),
    SEVERE(1),
    CRITICAL(4);

    public final int value;


    CoronaLevel(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (value) {
            case 0:
                return "mild";
            case 1:
                return "severe";
            case 4:
                return "critical";
            default:
                return "unknown";
        }
    }
}
