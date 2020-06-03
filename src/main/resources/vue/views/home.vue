
<!-- HTML template for this component -->
<template id="home-tmpl">
  <app-frame current-page="home">
    <h1 class="hello-world">{{greeting}}</h1>
    <p>Dies ist das Frontend für das Beispielprojekt.</p>
    <p>
      Aktuell ist nur eine Anzeige der Testdaten (Kunden und Bestellungen möglich, aber
      noch keine Manipulation oder Erstellung neuer Daten.<br />
      Es wird in den nächsten Tagen noch weiter ausgebaut!
    </p>
  </app-frame>
</template>


<!-- Definition of the component and its data. Reference to the template. -->
<script>
  Vue.component("home-comp", {
    template: "#home-tmpl",
    data: () => ({
      greeting: "Startseite"
    })
  });
</script>


<!-- Design rules for the template. -->
<style>
  .hello-world {
    color: black;
  }
</style>
