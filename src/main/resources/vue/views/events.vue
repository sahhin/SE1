<template id="events-tmpl" v-cloak>
    <app-frame current-page="events">
        <div class="md-layout md-gutter">
            <div class="md-layout-item md-size-75 md-medium-size-75 md-small-size-100 md-xsmall-size-100 nTable">
                <md-card md-with-hover>
                    <md-card-header>
                        <div class="md-title">Events verwalten</div>
                        <div class="md-subhead">Events ansehen, verändern und löschen</div>
                    </md-card-header>
                    <md-table>
                        <md-table-row>
                            <md-table-head md-numeric>Event ID</md-table-head>
                            <md-table-head>Titel</md-table-head>
                            <md-table-head>Zeitraum</md-table-head>
                            <md-table-head>Status</md-table-head>
                            <md-table-head>Event-Organisator</md-table-head>
                            <md-table-head>Event-Nachbarschaft</md-table-head>
                            <md-table-head>Teilnehmer</md-table-head>
                            <md-table-head>Löschen</md-table-head>
                        </md-table-row>

                        <md-table-row v-for="(event, index) in this.events" v-bind:key="event.id">
                            <md-table-cell md-numeric>{{event.eventId}}</md-table-cell>
                            <md-table-cell>{{event.eventName}}</md-table-cell>
                            <md-table-cell>{{event.eventTime.time}}</md-table-cell>
                            <md-table-cell>{{getEventStatus(event.eventStatusId)}}</md-table-cell>

                            <!--                    user select-->
                            <md-table-cell v-if="event._user != null">
                                #{{event._user.id}} {{event._user.lastName}}, {{event._user.firstName}}
                            </md-table-cell>
                            <md-table-cell v-if="event._user == null">
                                <md-field>
                                    <label :for="'users_' + index">Organisator</label>
                                    <md-select :name="'users_' + index" :id="'users_' + index" v-model="users.userId">
                                        <md-option v-for="user in users" :value="user.userId">
                                            {{user.firstName}} {{user.lastName}}
                                        </md-option>
                                    </md-select>
                                </md-field>
                                <md-button style="display: block" class="md-button md-raised md-primary"
                                           v-on:click="addOrganizer(event.eventId, users.userId)">
                                    Speichern
                                    <md-icon>done</md-icon>
                                </md-button>
                            </md-table-cell>

                            <!--                    neighborhood select-->
                            <md-table-cell v-if="event._neighborhood != null">{{event._neighborhood.neighborhoodName}}
                            </md-table-cell>
                            <md-table-cell v-if="event._neighborhood == null">
                                <md-field style="display: block">
                                    <label :for="'neighborhoods_' + index">Nachbarschaft</label>
                                    <md-select :name="'neighborhoods_' + index" :id="'neighborhoods_' + index"
                                               v-model="neighborhoods.neighborhoodId">
                                        <md-option v-for="neighborhood in neighborhoods"
                                                   :value="neighborhood.neighborhoodId">
                                            {{neighborhood.neighborhoodName}}
                                        </md-option>
                                    </md-select>
                                </md-field>

                                <md-button style="display: block" class="md-button md-raised md-primary"
                                           v-on:click="addNeighborhood(event.eventId, neighborhoods.neighborhoodId)">
                                    Speichern
                                    <md-icon>done</md-icon>
                                </md-button>
                            </md-table-cell>

                            <md-table-cell class="participants">
                                <p v-if="event._eventUser != null" v-for="participant in event._eventUser">
                                    #{{participant.id}} {{participant.lastName}}, {{participant.firstName}}
                                </p>
                                <p v-if="event._eventUser == null" v-for="participant in event._eventUser">
                                    Keine Teilnehmer
                                </p>
                            </md-table-cell>
                            <md-table-cell>

                                <md-button class="md-icon-button md-raised md-accent" @click="confirmDelete(event.eventId)">
                                    <md-icon>delete</md-icon>
                                </md-button>
                            </md-table-cell>
                        </md-table-row>
                    </md-table>
                </md-card>
                <md-dialog-confirm
                        :md-active.sync="active"
                        md-title="Event wirklich löschen?"
                        md-content="Das ausgewählte Event wird gelöscht und kann nicht wiederhergestellt werden!"
                        md-confirm-text="Löschen"
                        md-cancel-text="Abbrechen"
                        @md-confirm="onConfirm" />
            </div>
            <div class="md-layout-item md-size-25 md-medium-size-25 md-small-size-100 md-xsmall-size-100">
                <form id="formCreateEvent" @submit="processForm">
                <md-card md-with-hover>
                    <md-card-header>
                        <div class="md-title">Event anlegen</div>
                    </md-card-header>

                    <md-card-content>
                        <md-field md-clearable>
                            <label for="eEventName">Titel:</label>
                            <md-input id="eEventName" v-model="newEvent.eventName" type="text"></md-input>
                            <span class="md-error">There is an error</span>
                        </md-field>

                        <md-field md-clearable>
                            <label for="eStartTime">Startzeit:</label>
                            <md-input style="text-align: right;" type="time" id="eStartTime"
                                      v-model="newEvent.eStartTime"
                                      min="09:00" max="18:00"></md-input>
                        </md-field>

                        <md-field md-clearable>
                            <label for="eStartTime">Endzeit:</label>
                            <md-input style="text-align: right;" type="time" id="eEndTime"
                                      v-model="newEvent.eEndTime"
                                      min="09:00" max="18:00"></md-input>
                        </md-field>

                    </md-card-content>

                    <md-card-actions>
                        <md-button class="md-raised md-primary" type="submit">
                            Speichern
                            <md-icon>save</md-icon>
                        </md-button>
                        <md-button class="md-raised md-accent" type="reset" v-on:click="errors = []">
                            Felder leeren
                            <md-icon>delete</md-icon>
                        </md-button>
                    </md-card-actions>

                    <md-card id="cardFormError" v-if="errors.length">
                        <md-card-header>
                            <div class="md-title">Bitte korrigiere die folgenden Fehler:</div>
                        </md-card-header>
                        <div style="padding: 16px">
                            <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
                        </div>
                    </md-card>
                </md-card>
                </form>
            </div>
        </div>
    </app-frame>
</template>


<script>

    Vue.component("events-comp", {
        template: "#events-tmpl",
        components: {
            vuejsDatepicker
        },
        data: () => ({
            newEvent: {},
            events: [],
            neighborhoods: [],
            users: [],
            errors: [],
            elementToDelete: null,
            selectNeighborhoodState: false,
            selectOrganizerState: false,
            active: false
        }),

        created() {
            this.loadEvents();
            this.loadNeighborhoods();
            this.loadUsers();
        },

        methods: {
            confirmDelete(eventId){
                this.active = true;
                this.elementToDelete = eventId;
            },
            onConfirm () {
                this.removeEvent(this.elementToDelete);
                this.elementToDelete = null;
            },
            onCancel () {
                this.elementToDelete = null;
            },
            addNeighborhoodState: function () {
                this.selectNeighborhoodState = !this.selectNeighborhoodState;
            },

            getOrganizerState: function (id) {
                // console.log(id)
                return this.selectOrganizerState[2].selected;
            },

            addOrganizerState: function (id) {
                this.selectOrganizerState[1].selected = !this.selectOrganizerState[1].selected;
            },

            /** Load all neighborhoods from the REST endpoint. */
            loadNeighborhoods: function () {
                axios.get("/api/neighborhoods")
                    .then(response => {
                        this.neighborhoods = response.data;
                    }).catch(() => {
                    alert("Error while fetching neighborhoods");
                });
            },


            /** Load all users from the REST endpoint. */
            loadUsers: function () {
                axios.get("/api/users")
                    .then(response => {
                        this.users = response.data;
                    }).catch(() => {
                    alert("Error while fetching users");
                });
            },

            addNeighborhood: function (eventId, neighborhoodId) {
                console.log(eventId + " " + neighborhoodId);
                axios.put("/api/events/" + eventId, {
                    neighborhoodId: neighborhoodId,
                }).then(response => {
                    console.log("PUT successful.");  // Got a success code as response (201).
                    this.loadEvents();             // Reload the customer table.
                    this.newEvent = {};            // Clear input fields.
                    this.selectNeighborhoodState = false;
                }, error => {
                    console.error("PUT failed! Error:");  // Something failed.
                    console.error(error);                  // Print error message on console.
                });
            },

            addOrganizer: function (eventId, userId) {
                axios.put("/api/events/" + eventId, {
                    userId: userId,
                }).then(response => {
                    console.log("PUT successful.");  // Got a success code as response (201).
                    this.loadEvents();             // Reload the customer table.
                    this.newEvent = {};            // Clear input fields.
                    this.selectNeighborhoodState = false;
                }, error => {
                    console.error("PUT failed! Error:");  // Something failed.
                    console.error(error);                  // Print error message on console.
                });
            },

            /** Load all events from the REST endpoint. */
            loadEvents: function () {
                axios.get("/api/events")
                    .then(response => {
                        this.events = response.data;
                        for (let i = 0; i < this.events.length; i++) {
                            this.selectOrganizerState.push(
                                {
                                    id: i,
                                    selected: false
                                }
                            );
                        }
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
                        this.loadEvents();             // Reload the customer table.
                        this.newEvent = {};            // Clear input fields.
                    }, error => {
                        console.error("POST failed! Error:");  // Something failed.
                        console.error(error);                  // Print error message on console.
                    });
                }
                e.preventDefault();
            },

            removeEvent: function (eventId) {
                console.log(eventId);
                axios.delete("/api/events/" + eventId
                ).then(response => {
                    console.log("DEL successful.");  // Got a success code as response (201).
                    this.loadEvents();             // Reload the customer table.
                }, error => {
                    console.error("DEL failed! Error:");  // Something failed.
                    console.error(error);                  // Print error message on console.
                });
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
    .nTable {
        width: 100%;
    }

    .md-table-head {
        text-align: center;
    }

    .md-table-cell-container {
        justify-content: center;
        align-items: center;
    }

    #cardFormError {
        color: red;
        background-color: rgb(255, 210, 210);
    }
</style>
