<template id="neighborhoods-tmpl" v-cloak>
    <app-frame current-page="neighborhoods">
        <div class="md-layout">
            <div class="md-layout-item md-size-75 md-medium-size-100 md-small-size-100 md-xsmall-size-100 nTable">
                <md-card md-with-hover>
                    <md-card-header>
                        <div class="md-title">Nachbarschaft erstellen</div>
                        <div class="md-subhead">Nachbarschaften ansehen, verändern und löschen</div>
                    </md-card-header>
                    <md-table>
                        <md-table-row>
                            <md-table-head md-numeric>Nachbarschaft ID</md-table-head>
                            <md-table-head>Name</md-table-head>
                            <md-table-head>Stadtteil</md-table-head>
                            <md-table-head>PLZ</md-table-head>
                            <md-table-head>Land</md-table-head>
                            <md-table-head>Löschen</md-table-head>
                        </md-table-row>

                        <md-table-row v-for="neighborhood in this.neighborhoods" v-bind:key="neighborhood.id">
                            <md-table-cell md-numeric>{{neighborhood.neighborhoodId}}</md-table-cell>
                            <md-table-cell>{{neighborhood.neighborhoodName}}</md-table-cell>
                            <md-table-cell>{{neighborhood.neighborhoodCity}}</md-table-cell>
                            <md-table-cell>{{neighborhood.neighborhoodPostalcode}}</md-table-cell>
                            <md-table-cell>{{neighborhood.neighborhoodCountry}}</md-table-cell>
                            <md-table-cell>
                                <md-button class="md-icon-button md-raised md-accent" @click="confirmDelete(neighborhood.neighborhoodId)">
                                    <md-icon>delete</md-icon>
                                </md-button>
                            </md-table-cell>
                        </md-table-row>
                    </md-table>
                </md-card>
                <md-dialog-confirm
                        :md-active.sync="active"
                        md-title="Nachbarschaft wirklich löschen?"
                        md-content="Die ausgewählte Nachbarschaft wird gelöscht und kann nicht wiederhergestellt werden!"
                        md-confirm-text="Löschen"
                        md-cancel-text="Abbrechen"
                        @md-confirm="onConfirm" />
            </div>
            <div class="md-layout-item md-size-25 md-medium-size-100 md-small-size-100 md-xsmall-size-100">
                <form id="formCreateNeighborhood" @submit="processForm">
                    <md-card md-with-hover>
                        <md-card-header>
                            <div class="md-title">Nachbarschaft anlegen</div>
                        </md-card-header>

                        <md-card-content>

                            <md-field md-clearable>
                                <label for="nNeighborhoodName">Stadtteil</label>
                                <md-input id="nNeighborhoodName" v-model="newNeighborhood.neighborhoodName" type="text"></md-input>
                                <span class="md-error">There is an error</span>
                            </md-field>

                            <md-field md-clearable>
                                <label for="nNeighborhoodCity">Stadt</label>
                                <md-input id="nNeighborhoodCity" v-model="newNeighborhood.neighborhoodCity" type="text"></md-input>
                                <span class="md-error">There is an error</span>
                            </md-field>

                            <md-field md-clearable>
                                <label for="nNeighborhoodPostalcode">PLZ</label>
                                <md-input id="nNeighborhoodPostalcode" v-model="newNeighborhood.neighborhoodPostalcode" type="text"></md-input>
                                <span class="md-error">There is an error</span>
                            </md-field>

                            <md-field md-clearable>
                                <label for="nNeighborhoodCountry">Land</label>
                                <md-input id="nNeighborhoodCountry" v-model="newNeighborhood.neighborhoodCountry" type="text"></md-input>
                                <span class="md-error">There is an error</span>
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
    Vue.component("neighborhoods-comp", {
        template: "#neighborhoods-tmpl",

        data: () => ({
            newNeighborhood: {},
            neighborhoods: [],
            errors: [],
            active: false,
            elementToDelete: null,
            toastrOptions: {
                progressBar: true,
                closeButton: true,
                timeOut: 10000,
                preventDuplicates: true,
                positionClass: 'toast-bottom-right'
            }
        }),

        created() {
            this.loadCustomers();
        },

        methods: {

            confirmDelete(neighborhoodID){
                this.active = true;
                this.elementToDelete = neighborhoodID;
            },
            onConfirm () {
                this.removeNeighborhood(this.elementToDelete);
                this.elementToDelete = null;
            },
            onCancel () {
                this.elementToDelete = null;
            },
            /** Load all neighborhoods from the REST endpoint. */
            loadCustomers: function () {
                axios.get("/api/neighborhoods")
                    .then(response => {
                        this.neighborhoods = response.data;
                    }).catch(() => {
                    alert("Error while fetching neighborhoods");
                });
            },


            removeNeighborhood: function (neighborhoodId){
                console.log(neighborhoodId);
                axios.delete("/api/neighborhoods/" + neighborhoodId
                ).then(response => {
                    console.log("DEL successful.");  // Got a success code as response (201).
                    this.loadCustomers();             // Reload the customer table.
                }, error => {
                    console.error("DEL failed! Error:");  // Something failed.
                    console.error(error);                  // Print error message on console.
                });
            },

            /** Process the input form to create a new customer. */
            processForm: function (e) {


                // Validate the user's input.
                this.errors = [];
                if (!this.newNeighborhood.neighborhoodName) this.errors.push('Stadtteil fehlt');
                if (!this.newNeighborhood.neighborhoodCity) this.errors.push('Stadt fehlt');
                if (!this.newNeighborhood.neighborhoodPostalcode) this.errors.push('PLZ fehlt');
                if (!this.newNeighborhood.neighborhoodCountry) this.errors.push('Land fehlt');

                // Input validation successful! Send POST request to the backend.
                if (this.errors.length === 0) {
                    axios.post("/api/neighborhoods", {
                        neighborhoodName: this.newNeighborhood.neighborhoodName,
                        neighborhoodCity: this.newNeighborhood.neighborhoodCity,
                        neighborhoodPostalcode: this.newNeighborhood.neighborhoodPostalcode,
                        neighborhoodCountry: this.newNeighborhood.neighborhoodCountry
                    }).then(response => {
                        console.log("POST successful.");  // Got a success code as response (201).
                        this.loadCustomers();             // Reload the customer table.
                        this.newNeighborhood = {};            // Clear input fields.
                    }, error => {
                        console.error("POST failed! Error:");  // Something failed.
                        console.error(error);                  // Print error message on console.
                    });
                }
                e.preventDefault();
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

    #formCreateNeighborhood label {
        float: left;
        min-width: 100px;
    }

    #formCreateNeighborhood input {
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
