<template id="events-tmpl" v-cloak>
    <app-frame current-page="events">
        <div class="md-layout">
            <div class="md-layout-item md-size-75 md-medium-size-100 md-small-size-100 md-xsmall-size-100 nTable">
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

                            <md-table-cell v-if="event._user != null">
                                #{{event._user.id}} {{event._user.lastName}}, {{event._user.firstName}}
                            </md-table-cell>

                            <md-table-cell v-if="event._neighborhood != null">{{event._neighborhood.neighborhoodName}}
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

                                <md-button class="md-icon-button md-raised md-accent"
                                           @click="confirmDelete(event.eventId)">
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
                        @md-confirm="onConfirm"/>
            </div>
            <div class="md-layout-item md-size-25 md-medium-size-100 md-small-size-100 md-xsmall-size-100">
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
                                          min="00:00" max="23:59"></md-input>
                            </md-field>

                            <md-field md-clearable>
                                <label for="eStartTime">Endzeit:</label>
                                <md-input style="text-align: right;" type="time" id="eEndTime"
                                          v-model="newEvent.eEndTime"
                                          min="00:00" max="23:59"></md-input>
                            </md-field>

                            <md-field>
                                <label for="neighborhoods">Nachbarschaft</label>
                                <md-select name="neighborhoods" id="neighborhoods"
                                           v-model="neighborhoods.neighborhoodId">
                                    <md-option v-for="neighborhood in neighborhoods"
                                               :value="neighborhood.neighborhoodId">
                                        {{neighborhood.neighborhoodName}}
                                    </md-option>
                                </md-select>
                            </md-field>

                            <md-field>
                                <label for="users">Organisator</label>
                                <md-select name="users" id="users"
                                           v-model="users.id">
                                    <md-option v-for="user in users" :value="user.id">
                                        #{{user.id}} {{user.lastName}}, {{user.firstName}}
                                    </md-option>
                                </md-select>
                            </md-field>

                            <md-field>
                                <label for="participants">Teilnehmer</label>
                                <md-select name="participants" id="participants"
                                           v-model="participants.id" multiple>
                                    <md-option v-for="participant in users" v-if="participant.id !== users.id"
                                               :value="participant.id">
                                        #{{participant.id}} {{participant.firstName}} {{participant.lastName}}
                                    </md-option>
                                </md-select>
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
            VueToastr2
        },
        data: () => ({
            newEvent: {},
            events: [],
            neighborhoods: [],
            users: [],
            participants: [],
            errors: [],
            elementToDelete: null,
            active: false,
            toastrOptions: {
                progressBar: true,
                closeButton: true,
                timeOut: 10000,
                preventDuplicates: true,
                positionClass: 'toast-bottom-right'
            }
        }),

        created() {
            this.loadEvents();
            this.loadUsersNeighborhoods();
           // this.loadNeighborhoods();
           // this.loadUsers();
            document.title = "Events - " + document.title;
        },


        methods: {
            confirmDelete(eventId) {
                this.active = true;
                this.elementToDelete = eventId;
            },
            onConfirm() {
                this.removeEvent(this.elementToDelete);
                this.elementToDelete = null;
            },
            onCancel() {
                this.elementToDelete = null;
            },
            loadUsersNeighborhoods: function () {
                axios.get("/api/neighborhoods")
                    .then(response => {
                        this.neighborhoods = response.data;
                        this.loadUsers();

                        // this.$toastr.success('Success while fetching neighborhoods', 'GET /api/neighborhoods');
                    }).catch(() => {
                    this.$toastr.error('Error while fetching neighborhoods', 'GET /api/neighborhoods');
                    // this.loadNeighborhoods();
                });
            },

            /** Load all neighborhoods from the REST endpoint. */
            loadNeighborhoods: function () {
                axios.get("/api/neighborhoods")
                    .then(response => {
                        this.neighborhoods = response.data;
                        axios.get("/api/users")
                            .then(response => {
                                this.users = response.data;
                                // this.$toastr.success('Success while fetching users', 'GET /api/users');
                            }).catch(() => {
                            this.$toastr.error('Error while fetching users', 'GET /api/users');
                            // this.loadUsers();
                        });
                        // this.$toastr.success('Success while fetching neighborhoods', 'GET /api/neighborhoods');
                    }).catch(() => {
                    this.$toastr.error('Error while fetching neighborhoods', 'GET /api/neighborhoods');
                    // this.loadNeighborhoods();
                });
            },


            /** Load all users from the REST endpoint. */
            loadUsers: function () {
                axios.get("/api/users")
                    .then(response => {
                        this.users = response.data;
                        // this.$toastr.success('Success while fetching users', 'GET /api/users');
                    }).catch(() => {
                    this.$toastr.error('Error while fetching users', 'GET /api/users');
                    // this.loadUsers();
                });
            },

            /** Load all events from the REST endpoint. */
            loadEvents: function () {
                axios.get("/api/events")
                    .then(response => {
                        this.events = response.data;
                        // this.$toastr.success('Success while fetching events', 'GET /api/events', this.toastrOptions);
                    }).catch(() => {
                    this.$toastr.error('Error while fetching events', 'GET /api/events', this.toastrOptions);
                    // this.loadEvents();
                });
            },


            /** Process the input form to create a new customer. */
            processForm: function (e) {
                let newParticipants = this.participants.id.toString();

                // Validate the user's input.
                this.errors = [];
                if (!this.newEvent.eventName) this.errors.push('Titel fehlt');
                if (!this.newEvent.eStartTime) this.errors.push('Startuhrzeit fehlt');
                if (!this.newEvent.eEndTime) this.errors.push('Enduhrzeit fehlt');
                if (!this.neighborhoods.neighborhoodId) this.errors.push('Nachbarschaft fehlt');
                if (!this.users.id) this.errors.push('Organisator fehlt');
                if (!newParticipants) this.errors.push('Teilnehmer fehlen');

                // Input validation successful! Send POST request to the backend.
                if (this.errors.length === 0) {
                    axios.post("/api/events", {
                        eventName: this.newEvent.eventName,
                        eventTime: {time: this.newEvent.eStartTime + " - " + this.newEvent.eEndTime},
                        _neighborhood: this.neighborhoods.neighborhoodId,
                        _user: this.users.id,
                        _eventUser: newParticipants
                    }).then(response => {
                        this.$toastr.success('POST successful', 'POST /api/events', this.toastrOptions);
                        this.loadEvents();             // Reload the customer table.
                        this.newEvent = {};            // Clear input fields.
                        this.neighborhoods = [];
                        this.users = [];
                        this.loadNeighborhoods();
                        this.loadUsers();
                        newParticipants = []
                        this.participants = []
                    }, error => {
                        this.$toastr.error('POST failed! Error: ' + error, 'POST /api/events');
                    });
                }
                e.preventDefault();
            },

            removeEvent: function (eventId) {
                console.log(eventId);
                axios.delete("/api/events/" + eventId
                ).then(response => {
                    this.$toastr.success('DEL successful.', 'DEL /api/events', this.toastrOptions);
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
        margin: 0;
        cursor: default;
    }
</style>
