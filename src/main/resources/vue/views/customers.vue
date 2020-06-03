<template id="customers-tmpl">
  <app-frame current-page="customers">
    <h1>Kundenverwaltung</h1>
    <table cellspacing="0" border="1">
      <thead>
        <tr>
          <th>Kd-Nr.</th>
          <th>Vorname</th>
          <th>Nachname</th>
          <th>E-Mail-Adresse</th>
          <th>Bestellungen</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="customer in customers" v-bind:key="customer.id">
          <td>{{customer.id}}</td>
          <td>{{customer.firstName}}</td>
          <td>{{customer.lastName}}</td>
          <td>{{customer.emailAddress.email}}</td>
          <td>
            <span v-for="order in customer.orders" v-bind:key="order.id">
              #{{order.id}}
            </span>
          </td>
        </tr>
      </tbody>
    </table>

    <h2>Neuen Kunden anlegen</h2>
    <form id="formCreateCustomer" @submit="processForm">
      <div id="formErrorMsg" v-if="errors.length">
        <h3>Bitte korrigiere die folgenden Fehler:</h3>
        <ul>
          <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
        </ul>
      </div>
      <p>
        <label for="cFirstName">Vorname:</label>
        <input id="cFirstName" v-model="newCustomer.firstName" type="text" />
      </p>
      <p>
        <label for="cLastName">Nachname:</label>
        <input id="cLastName" v-model="newCustomer.lastName" type="text" />
      </p>
      <p>
        <label for="cEmail">E-Mail:</label>
        <input id="cEmail" v-model="newCustomer.email" type="text" size="30" />
      </p>
      <p>
        <input type="submit" value="Abschicken" />
      </p>
    </form>
  </app-frame>
</template>


<script>
  Vue.component("customers-comp", {
    template: "#customers-tmpl",

    data: () => ({
      newCustomer: {},
      customers: [],
      errors: []
    }),

    created() {
      this.loadCustomers();
    },

    methods: {

      /** Load all customers from the REST endpoint. */
      loadCustomers: function() {
        axios.get("/api/customers")
          .then(response => {
            this.customers = response.data;
          }).catch(() => {
            alert("Error while fetching customers");
          });
      },

      /** Process the input form to create a new customer. */
      processForm: function(e) {

        // Validate the user's input.
        this.errors = [];
        if (!this.newCustomer.firstName) this.errors.push('Vorname fehlt');
        if (!this.newCustomer.lastName)  this.errors.push('Nachname fehlt');
        if (!this.newCustomer.email)     this.errors.push('E-Mail Adresse fehlt');
        else if (!this.validEmail(this.newCustomer.email)) {
          this.errors.push('E-Mail Adresse ist ungültig');
        }

        // Input validation successful! Send POST request to the backend.
        if (this.errors.length == 0) {
          axios.post("/api/customers", {
            firstName: this.newCustomer.firstName,
            lastName: this.newCustomer.lastName,
            emailAddress: { email: this.newCustomer.email }
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
      validEmail: function(email) {
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
