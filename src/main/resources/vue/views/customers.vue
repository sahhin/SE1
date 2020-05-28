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
  </app-frame>
</template>


<script>
  Vue.component("customers-comp", {
    template: "#customers-tmpl",
    data: () => ({
      customers: []
    }),
    created() {
      fetch("/api/customers")
        .then(res => res.json())
        .then(res => {
          this.customers = res;
          //console.log(res);
        })
        .catch(() => alert("Error while fetching customers"));
      console.log("CustomerFrame created. Calling emit!");
      //this.$emit('enlarge-text', 0.1);
    }
  });
</script>


<style>
  table th {
    background-color: lightgrey;
  }
</style>
