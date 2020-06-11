<template id="users-tmpl">
    <app-frame current-page="users">
        <h1>Userverwaltung</h1>
        <table cellspacing="0" border="1">
            <thead>
            <tr>
                <th>User ID</th>
                <th>Vorname</th>
                <th>Nachname</th>
                <th>E-Mail-Adresse</th>
                <th>Organisator</th>
                <th>Nachbarschaft</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in this.users" v-bind:key="user.id">
                <td>{{user.id}}</td>
                <td>{{user.firstName}}</td>
                <td>{{user.lastName}}</td>
                <td>{{user.emailAddress.email}}</td>
                <td>{{user.custAdress.adress}}</td>
                <td v-if="user._neighborhood != null">{{user._neighborhood.neighborhoodPostalcode}},
                    {{user._neighborhood.neighborhoodCity}}, {{user._neighborhood.neighborhoodName}}
                </td>
                <td v-if="user._neighborhood == null">Keine Nachbarschaft</td>
            </tr>
            </tbody>
        </table>

        <h2>Neuen User anlegen</h2>
        <form id="formCreateCustomer" @submit="processForm">

            <div id="formErrorMsg" v-if="errors.length">
                <h3>Bitte korrigiere die folgenden Fehler:</h3>
                <ul>
                    <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
                </ul>
            </div>

            <p>
                <label for="uFirstName">Vorname:</label>
                <input id="uFirstName" v-model="newCustomer.firstName" type="text"/>
            </p>
            <p>
                <label for="uLastName">Nachname:</label>
                <input id="uLastName" v-model="newCustomer.lastName" type="text"/>
            </p>
            <p>
                <label for="uAdress">Adresse:</label>
                <input id="uAdress" v-model="newCustomer.adress" type="text" size="30"/>
            </p>
            <p>
                <label for="uEmail">E-Mail:</label>
                <input id="uEmail" v-model="newCustomer.email" type="text" size="30"/>
            </p>
            <p>
                <input type="submit" value="Abschicken"/>
            </p>
        </form>
    </app-frame>
</template>


<script>
    Vue.component("users-comp", {
        template: "#users-tmpl",

        data: () => ({
            newCustomer: {},
            users: [],
            errors: []
        }),

        created() {
            this.loadCustomers();
        },

        methods: {

            /** Load all users from the REST endpoint. */
            loadCustomers: function () {
                axios.get("/api/users")
                    .then(response => {
                        this.users = response.data;
                    }).catch(() => {
                    alert("Error while fetching users");
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
                    console.log(this.newCustomer.firstName, this.newCustomer.lastName, this.newCustomer.adress, this.newCustomer.email);
                    axios.post("/api/users", {
                        firstName: this.newCustomer.firstName,
                        lastName: this.newCustomer.lastName,
                        emailAddress: {email: this.newCustomer.email},
                        custAdress: {adress: this.newCustomer.adress}
                    }).then(response => {
                        console.log("POST successful.");  // Got a success code as response (201).
                        this.loadCustomers();             // Reload the customer table.
                        this.newCustomer = {};            // Clear input fields.
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
