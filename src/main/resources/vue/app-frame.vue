<template id="app-frame">
  <div class="app-frame">
    <header>
      <img src="logo.png" id="logo" alt="QuickShop Demo App Logo" />
      <ul id="navigation">
        <li><a href="/" ref="nav-home">Startseite</a></li>
        <li><a href="/customers" ref="nav-customers">Kundenverwaltung</a></li>
        <li><a href="/orders" ref="nav-orders">Bestellung aufgeben</a></li>
        <li><a href="/about" ref="nav-about">Über diese Seite</a></li>
      </ul>
    </header>
    <slot>
      <!-- Hier wird automatisch der restliche Code des Komponenten-Templates eingefügt! -->
    </slot>
    <footer>
      &copy; Sven Heitsch &amp; Jan Dalski<br />
      Software Engineering 1 im Sommersemester 2020, HAW Hamburg
    </footer>
  </div>
</template>

<script>
  Vue.component("app-frame", {
    template: "#app-frame",
    props: ['currentPage'],
    data: () => ({
    }),
    mounted: function() {
      // Wenn der DOM fertig ist, setzen wir den Link der aktuellen Seite auf aktiv.
      this.$refs["nav-"+this.currentPage].classList.add("active");
    },
    methods: {
    }
  });
</script>

<style>
  .app-frame > header {
    padding: 20px;
    font-size: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  #navigation {
    border: 1px solid #e7e7e7;
    background-color: #f3f3f3;
  }

  #navigation li {
    display: inline;
  }

  ul a.active {
    color: red;
  }

  ul a:hover {
    color: orange;
  }

  li a {
    color: #666;
  }

  footer {
    border-top: 1px solid lightgrey;
    margin-top: 25px;
    padding: 5px;
  }
</style>
