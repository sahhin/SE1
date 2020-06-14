<template id="users-tmpl" v-cloak>
        <app-frame current-page="users">
        <div class="md-layout">
            <div class="md-layout-item md-size-75 md-medium-size-100 md-small-size-100 md-xsmall-size-100 nTable">
                <md-card md-with-hover>
                    <md-card-header>
                        <div class="md-title">User verwalten</div>
                        <div class="md-subhead">User ansehen, verändern und löschen</div>
                    </md-card-header>
                    <md-table>
                        <md-table-row>
                            <md-table-head md-numeric>User ID</md-table-head>
                            <md-table-head>Vorname</md-table-head>
                            <md-table-head>Nachname</md-table-head>
                            <md-table-head>E-Mail-Adresse</md-table-head>
                            <md-table-head>Organisator</md-table-head>
                            <md-table-head>Nachbarschaft</md-table-head>
                            <md-table-head>Löschen</md-table-head>
                        </md-table-row>

                        <tr v-for="user in this.users" v-bind:key="user.id">
                        <md-table-row v-for="user in this.users" v-bind:key="user.id">
                            <md-table-cell md-numeric>{{user.id}}</md-table-cell>
                            <md-table-cell>{{user.firstName}}</md-table-cell>
                            <md-table-cell>{{user.lastName}}</md-table-cell>
                            <md-table-cell>{{user.emailAddress.email}}</md-table-cell>

                            <md-table-cell>
                                {{user.custAdress.adress}}
                            </md-table-cell>

                            <md-table-cell v-if="user._neighborhood != null">
                                {{user._neighborhood.neighborhoodCity}}, {{user._neighborhood.neighborhoodName}}
                            </md-table-cell>

                            <md-table-cell>

                                <md-button class="md-icon-button md-raised md-accent" @click="confirmDelete(user.id)">
                                    <md-icon>delete</md-icon>
                                </md-button>
                            </md-table-cell>
                        </md-table-row>
                    </md-table>
                </md-card>
                <md-dialog-confirm
                        :md-active.sync="active"
                        md-title="User wirklich löschen?"
                        md-content="Der ausgewählte User wird gelöscht und kann nicht wiederhergestellt werden!"
                        md-confirm-text="Löschen"
                        md-cancel-text="Abbrechen"
                        @md-confirm="onConfirm" />
            </div>
            <div class="md-layout-item md-size-25 md-medium-size-100 md-small-size-100 md-xsmall-size-100">
                <form id="formCreateCustomer" @submit="processForm">
                    <md-card md-with-hover>
                        <md-card-header>
                            <div class="md-title">User anlegen</div>
                        </md-card-header>

                        <md-card-content>
                            <md-field md-clearable>
                                <label for="uFirstName">Vorname:</label>
                                <md-input id="uFirstName" v-model="newCustomer.firstName" type="text"></md-input>
                            </md-field>

                            <md-field md-clearable>
                                <label for="uLastName">Nachname:</label>
                                <md-input id="uLastName" v-model="newCustomer.lastName" type="text"></md-input>
                            </md-field>

                            <md-field md-clearable>
                                <label for="uAdress">Adresse:</label>
                                <md-input id="uAdress" v-model="newCustomer.adress" type="text"></md-input>
                            </md-field>

                            <md-field md-clearable>
                                <label for="uEmail">E-Mail:</label>
                                <md-input id="uEmail" v-model="newCustomer.email" type="text"></md-input>
                            </md-field>

                            <md-field>
                                <label for="neighborhoods">Nachbarschaft</label>
                                <md-select name="neighborhoods" id="neighborhoods"
                                           v-model="neighborhoods.neighborhoodId">
                                    <md-option v-for="neighborhood in neighborhoods" :value="neighborhood.neighborhoodId">
                                        {{neighborhood.neighborhoodName}}
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
    Vue.component("users-comp", {
        template: "#users-tmpl",

        components: {
            VueToastr2
        },

        data: () => ({
            newCustomer: {},
            users: [],
            errors: [],
            neighborhoods: [],
            _neighborhood: null,
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
            this.loadUsers();
            this.loadNeighborhoods();
            document.title = "Users - " + document.title;
        },

        methods: {
            confirmDelete(userId){
                this.active = true;
                this.elementToDelete = userId;
            },
            onConfirm () {
                this.removeUser(this.elementToDelete);
                this.elementToDelete = null;
            },
            onCancel () {
                this.elementToDelete = null;
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

            /** Load all neighborhoods from the REST endpoint. */
            loadNeighborhoods: function () {
                axios.get("/api/neighborhoods")
                    .then(response => {
                        this.neighborhoods = response.data;
                    }).catch(() => {
                    this.$toastr.error('Error while fetching neighborhoods', 'GET /api/neighborhoods');
                });
            },

            /** Process the input form to create a new customer. */
            processForm: function (e) {

                // Validate the user's input.
                this.errors = [];
                if (!this.newCustomer.firstName) this.errors.push('Vorname fehlt');
                if (!this.newCustomer.lastName) this.errors.push('Nachname fehlt');
                if (!this.newCustomer.email) this.errors.push('E-Mail Adresse fehlt');
                else if (!this.validEmail(this.newCustomer.email)) {
                    this.errors.push('E-Mail Adresse ist ungültig');
                }

                // Input validation successful! Send POST request to the backend.
                if (this.errors.length == 0) {
                    axios.post("/api/users", {
                        firstName: this.newCustomer.firstName,
                        lastName: this.newCustomer.lastName,
                        emailAddress: {email: this.newCustomer.email},
                        custAdress: {adress: this.newCustomer.adress},
                        _neighborhood: this.neighborhoods.neighborhoodId
                    }).then(response => {
                        console.log("POST successful.");  // Got a success code as response (201).
                        this.loadUsers();             // Reload the customer table.
                        this.newCustomer = {};            // Clear input fields.
                    }, error => {
                        console.error("POST failed! Error:");  // Something failed.
                        console.error(error);                  // Print error message on console.
                    });
                }
                e.preventDefault();
            },

            removeUser: function (userId){
                console.log(userId);
                axios.delete("/api/users/" + userId
                ).then(response => {
                    console.log("DEL successful.");  // Got a success code as response (201).
                    this.loadUsers();             // Reload the customer table.
                }, error => {
                    console.error("DEL failed! Error:");  // Something failed.
                    console.error(error);                  // Print error message on console.
                });
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

    .addNeighborhood{
        font-size: 150%;
    }
    #formCreateCustomer label {
        float: left;
        min-width: 100px;
    }

    #formCreateCustomer input {
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
