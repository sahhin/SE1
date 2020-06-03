<template id="orders-tmpl">
  <app-frame current-page="orders">
    <h1>Bestellübersicht</h1>
    <h2>Wer bist du?</h2>

    <select v-model="selected" @change="selectedCustomerChanged($event)">
      <option disabled value="0">Bitte Kunden auswählen</option>
      <option
        v-for="customer in customers"
        v-bind:key="customer.id"
        v-bind:value="customer.id">
        {{ customer.firstName+" "+customer.lastName+" (#"+customer.id+")" }}
      </option>
    </select>
    <br />

    <div v-if="selected !== 0">
      <h2>Was brauchst du?</h2>
      <p>...</p>

      <h2>Deine bisherigen Bestellungen <small>({{orderList.length}})</small></h2>
      <div v-for="order in orderList" v-bind:key="order.id">
        <p>Bestellung #{{order.id}}</p>
        <ul>
          <li v-for="item in order.items" v-bind:key="item">{{item}}</li>
        </ul>

      </div>
    </div>
  </app-frame>
</template>

<script>
  Vue.component("orders-comp", {
    template: "#orders-tmpl",
    data: () => ({
      customers: [],
      selected: 0,
      orderList: []
    }),
    created() {
      fetch("/api/customers")
        .then(res => res.json())
        .then(res => this.customers = res)
        .catch(() => alert("Error while fetching customers"));
    },
    methods: {
      selectedCustomerChanged: function(event) {
        fetch("/api/customers/"+this.selected)
          .then(res => res.json())
          .then(res => this.orderList = res.orders)
          .catch(() => alert("Error while fetching customer with ID "+this.selected));
      }
    }
  });
</script>

<style scoped>
  select {
    font-size: 1.5em;
  }
</style>
