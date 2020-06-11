<template id="events-tmpl">
    <app-frame current-page="events">
        <h1>Event erstellen</h1>
        <table cellspacing="0" border="1">
            <thead>
            <tr>
                <th>Event ID</th>
                <th>Titel</th>
                <th>Zeitraum</th>
                <th>Status</th>
                <th>Event-Organisator</th>
                <th>Event-Nachbarschaft</th>
                <th>Teilnehmer</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="event in this.events" v-bind:key="event.id">
                <td>{{event.eventId}}</td>
                <td>{{event.eventName}}</td>
                <td>{{event.eventTime.time}}</td>
                <td>{{getEventStatus(event.eventStatusId)}}</td>
                <td v-if="event._user != null">
                    {{event._user.firstName}}, {{event._user.lastName}}
                </td>
                <td v-if="event._user == null">Kein Organisator</td>
                <td v-if="event._neighborhood != null">{{event._neighborhood.neighborhoodPostalcode}},
                    {{event._neighborhood.neighborhoodCity}}, {{event._neighborhood.neighborhoodName}}
                </td>
                <td v-if="event._neighborhood == null">Keine Nachbarschaft</td>
            </tr>
            </tbody>
        </table>

        <h2>Neues Event anlegen</h2>
        <form id="formCreateEvent" @submit="processForm">

            <div id="formErrorMsg" v-if="errors.length">
                <h3>Bitte korrigiere die folgenden Fehler:</h3>
                <ul>
                    <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
                </ul>
            </div>
            <p>
                <label for="eEventName">Titel:</label>
                <input id="eEventName" v-model="newEvent.eventName" type="text"/>
            </p>
            <p>
                <label for="eStartTime">Zeitraum:</label>
                <input type="time" id="eStartTime" v-model="newEvent.eStartTime"
                       min="09:00" max="18:00">
                <input type="time" id="eEndTime" v-model="newEvent.eEndTime"
                       min="09:00" max="18:00">
            </p>
            <p>
                <input type="submit" value="Abschicken"/>
            </p>
        </form>
    </app-frame>
</template>


<script>
    Vue.component("events-comp", {
        template: "#events-tmpl",

        data: () => ({
            newEvent: {},
            events: [],
            errors: []
        }),

        created() {
            this.loadCustomers();
        },

        methods: {

            /** Load all events from the REST endpoint. */
            loadCustomers: function () {
                axios.get("/api/events")
                    .then(response => {
                        this.events = response.data;
                    }).catch(() => {
                    alert("Error while fetching events");
                });
            },

            /** Process the input form to create a new customer. */
            processForm: function (e) {


                // Validate the user's input.
                this.errors = [];
                if (!this.newEvent.eventName) this.errors.push('Titel fehlt');
                if (!this.newEvent.eStartTime) this.errors.push('Startuhrzeit fehlt');
                if (!this.newEvent.eEndTime) this.errors.push('Enduhrzeit fehlt');

                // Input validation successful! Send POST request to the backend.
                if (this.errors.length === 0) {
                    axios.post("/api/events", {
                        eventName: this.newEvent.eventName,
                        eventTime: {time: this.newEvent.eStartTime + " - " + this.newEvent.eEndTime},
                    }).then(response => {
                        console.log("POST successful.");  // Got a success code as response (201).
                        this.loadCustomers();             // Reload the customer table.
                        this.newEvent = {};            // Clear input fields.
                    }, error => {
                        console.error("POST failed! Error:");  // Something failed.
                        console.error(error);                  // Print error message on console.
                    });
                }
                e.preventDefault();
            },

            // EVENT_PLANNED, // das Event geplant
            // EVENT_RELEASED, // das Event veröffentlicht
            // EVENT_CANCELED, // das Event abgesagt
            // EVENT_OCCURED; // das Event stattgefunden

            getEventStatus: function (eventStatus) {
                switch (eventStatus) {
                    case "EVENT_PLANNED":
                        return "Geplant";
                    case "EVENT_RELEASED":
                        return "Veröffentlicht";
                    case "EVENT_CANCELED":
                        return "Abgesagt";
                    case "EVENT_OCCURED":
                        return "Stattgefunden";
                    default:
                        return "Keine Informationen";
                }
            },
            /** Validate an e-mail address.
             * @param email Input to be checked if it is a valid e-mail expression.
             * @returns 'true' if the e-mail check was successful, 'false' otherwise. */
            validEmail: function (email) {
                var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
        }
    });
</script>


<style>
    table th {
        background-color: lightgrey;
    }

    #formCreateEvent label {
        float: left;
        min-width: 100px;
    }

    #formCreateEvent input {
        font-family: monospace;
        font-size: 0.9em;
    }

    #formErrorMsg {
        color: red;
        background-color: rgb(255, 210, 210);
        border: 1px solid rgb(160, 0, 0);
        border-radius: 8px;
        margin-bottom: 20px;
        padding: 2px 15px;
        width: 350px;
    }

    #formErrorMsg h3 {
        margin: 5px 2px;
    }

    #formErrorMsg li {
        color: red;
    }
</style>
