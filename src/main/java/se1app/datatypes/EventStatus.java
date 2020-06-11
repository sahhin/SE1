package se1app.datatypes;

/**
 * Data type für Status in Event
 */
public enum EventStatus {
    EVENT_PLANNED, // das Event geplant
    EVENT_RELEASED, // das Event veröffentlicht
    EVENT_CANCELED, // das Event abgesagt
    EVENT_OCCURED; // das Event stattgefunden

    private EventStatus succ;
    private EventStatus pred;

    /**
     * um eine richtige Ablauf des Event-Status
     * pred ist der Vorgänger-Status
     * succ ist der Nachfolger-Status
     */
    static {
        EVENT_CANCELED.pred = EVENT_PLANNED;
        EVENT_RELEASED.pred = EVENT_PLANNED;
        EVENT_PLANNED.succ = EVENT_OCCURED;
        EVENT_CANCELED.succ = EVENT_RELEASED;
    }

}



