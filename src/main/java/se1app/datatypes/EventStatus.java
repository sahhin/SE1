package se1app.datatypes;

/**
 * Data type für Status in Event
 */
public enum EventStatus {
    EVENT_PLANNED, // das Event ist geplant
    EVENT_RELEASED, // das Event wurde aktualisiert
    EVENT_CANCELED, // das Event ist abgesagt
    EVENT_OCCURED; // das Event kommt

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



