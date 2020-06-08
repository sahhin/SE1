package se1app.datatypes;

public enum EventStatus {
    EVENT_PLANNED,
    EVENT_RELEASED,
    EVENT_CANCELED,
    EVENT_OCCURED;
    private EventStatus succ;
    private EventStatus pred;

    static {
        EVENT_CANCELED.pred = EVENT_PLANNED;
        EVENT_RELEASED.pred = EVENT_PLANNED;
        EVENT_PLANNED.succ = EVENT_OCCURED;
        EVENT_CANCELED.succ = EVENT_RELEASED;
    }

}



