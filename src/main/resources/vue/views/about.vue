<template id="about-tmpl">
  <app-frame current-page="about">
    <h1>Über diese Seite</h1>
    <p>...</p>
  </app-frame>
</template>

<script>
  Vue.component("about-comp", {
    template: "#about-tmpl",
    data: () => ({
    }),
    created() {
      document.title = "Über - " + document.title;
    }
  });
</script>

<style>
</style>
