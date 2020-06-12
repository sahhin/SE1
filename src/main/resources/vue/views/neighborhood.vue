<template id="neighborhoods-tmpl">
    <app-frame current-page="neighborhoods">
        <h1>Nachbarschaft erstellen</h1>
        <table cellspacing="0" border="1">
            <thead>
            <tr>
                <th>Nachbarschaft ID</th>
                <th>Name</th>
                <th>Stadtteil</th>
                <th>PLZ</th>
                <th>Land</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="neighborhood in this.neighborhoods" v-bind:key="neighborhood.id">
                <td>{{neighborhood.neighborhoodId}}</td>
                <td>{{neighborhood.neighborhoodName}}</td>
                <td>{{neighborhood.neighborhoodCity}}</td>
                <td>{{neighborhood.neighborhoodPostalcode}}</td>
                <td>{{neighborhood.neighborhoodCountry}}</td>
                <td v-on:click="removeNeighborhood(neighborhood.neighborhoodId)">X</td>
            </tr>
            </tbody>
        </table>

        <h2>Neue Nachbarschaft anlegen</h2>
        <form id="formCreateNeighborhood" @submit="processForm">

            <div id="formErrorMsg" v-if="errors.length">
                <h3>Bitte korrigiere die folgenden Fehler:</h3>
                <ul>
                    <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
                </ul>
            </div>
            <p>
                <label for="nNeighborhoodName">Stadtteil:</label>
                <input id="nNeighborhoodName" v-model="newNeighborhood.neighborhoodName" type="text"/>
            </p>
            <p>
                <label for="nNeighborhoodCity">Stadt:</label>
                <input id="nNeighborhoodCity" v-model="newNeighborhood.neighborhoodCity" type="text"/>
            </p>
            <p>
                <label for="nNeighborhoodPostalcode">PLZ:</label>
                <input id="nNeighborhoodPostalcode" v-model="newNeighborhood.neighborhoodPostalcode" type="text"/>
            </p>
            <p>
                <label for="nNeighborhoodCountry">Land:</label>
                <input id="nNeighborhoodCountry" v-model="newNeighborhood.neighborhoodCountry" type="text"/>
            </p>
            <p>
                <input type="submit" value="Abschicken"/>
            </p>
        </form>
    </app-frame>
</template>


<script>
    Vue.component("neighborhoods-comp", {
        template: "#neighborhoods-tmpl",

        data: () => ({
            newNeighborhood: {},
            neighborhoods: [],
            errors: []
        }),

        created() {
            this.loadCustomers();
        },

        methods: {

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
