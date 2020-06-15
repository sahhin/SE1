<template id="about-tmpl">
    <app-frame current-page="about">
        <h1 class="titel">Über Neighborino</h1>
        <p class="description">In Zeiten einer Pandemie mussten wir auf die Idee stoßen,
            das Haus zum ersten Mal nicht verlassen zu können!
            Aber zu Hause zu bleiben muss nicht gleichbedeutend mit
            Langeweile und Isolation sein. Neighborino eröffnet die
            Möglichkeit, auf viel einfachere und unterhaltsamere Weise
            mit unseren Nachbarn zu interagieren und Kontakte zu knüpfen. Haben Sie sich jemals vorgestellt, von Ihrem
            Balkon aus ein Konzert besuchen zu können? Jetzt ist es möglich.
        </p>
    </app-frame>
</template>

<script>
    Vue.component("about-comp", {
        template: "#about-tmpl",
        data: () => ({}),
        created() {
            document.title = "Über - " + document.title;
        }
    });
</script>

<style>
    .titel{
        font-family: Georgia;
        color: coral;
    }
    .description{
        font-size: 20pt;
        color: #448aff;
        font-family: Elephant;
    }

</style>
