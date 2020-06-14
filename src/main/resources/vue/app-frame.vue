<template id="app-frame">
    <div class="page-container">
        <md-app md-mode="reveal">
            <md-app-toolbar style="overflow: hidden" class="md-primary md-medium">
                <md-button @click="menuVisible = !menuVisible" class="md-icon-button">
                    <md-icon>menu</md-icon>
                </md-button>
                <span class="md-title nodrag"><span class="title">Neighborino</span> - {{this.currentPageCapitalized}}</span>
                <img class="nodrag" src="potara.png" id="logo" alt="QuickShop Demo App Logo" />
                <p style="text-align:right; width:100%;margin-right: 16px">Made with <md-icon style="font-size: 75%">music_note</md-icon> in Hamburg. Stay healthy. Stay at home.</p>

            </md-app-toolbar>


            <md-app-drawer :md-active.sync="menuVisible">
                <md-toolbar class="md-transparent md-medium" md-elevation="0">
                    <md-button @click="menuVisible = !menuVisible" class="md-icon-button">
                        <md-icon>close</md-icon>
                    </md-button>
                </md-toolbar>

                <md-list>
                    <md-list-item>
                        <md-icon class="icon-active" ref="nav-icon-home">home</md-icon>
                        <span class="md-list-item-text">
            <a href="/" ref="nav-home" class="animoBorderLeftRight">Startseite</a>
          </span>
                    </md-list-item>

                    <md-list-item>
                        <md-icon>people</md-icon>
                        <span class="md-list-item-text">
            <a href="/users" ref="nav-users" class="animoBorderLeftRight">User</a>
          </span>
                    </md-list-item>

                    <md-list-item>
                        <md-icon>event</md-icon>
                        <span class="md-list-item-text">
            <a href="/events" ref="nav-events" class="animoBorderLeftRight">Events</a>
          </span>
                    </md-list-item>

                    <md-list-item>
                        <md-icon>apartment</md-icon>
                        <span class="md-list-item-text">
            <a href="/neighborhoods" ref="nav-neighborhoods" class="animoBorderLeftRight">Nachbarschaften</a>
          </span>
                    </md-list-item>

                    <md-list-item>
                        <md-icon>help</md-icon>
                        <span class="md-list-item-text">
                      <a href="/about" ref="nav-about" class="animoBorderLeftRight">Über diese Seite</a>
                    </span>
                    </md-list-item>
                </md-list>
            </md-app-drawer>
            <md-app-content>
                <slot>
                    <!-- Hier wird automatisch der restliche Code des Komponenten-Templates eingefügt! -->
                </slot>
            </md-app-content>

        </md-app>
    </div>

</template>

<script>
    Vue.component("app-frame", {
        template: "#app-frame",
        props: ['currentPage'],
        data: () => ({
            menuVisible: false,
            currentPageCapitalized: ""
        }),
        mounted: function () {
            // Wenn der DOM fertig ist, setzen wir den Link der aktuellen Seite auf aktiv.
            this.$refs["nav-" + this.currentPage].classList.add("active");
            // this.$refs["nav-icon-" + this.currentPage].classList.add("icon-active");
            this.currentPageCapitalized = this.currentPage.charAt(0).toUpperCase() + this.currentPage.slice(1);
        },
        methods: {}
    });
</script>

<style lang="scss" scoped>


    .md-layout-item{
        height: 100%;
        position: sticky;
        top: 0;
    }
    .md-toolbar {
        z-index: 1000;
    }

    .md-app {
        height: 100vh;
    }

    .md-content {
        padding: 16px;
    }

    .animoBorderLeftRight {
        display: inline-block;
        position: relative;
        cursor: pointer
    }

    .animoBorderLeftRight::after {
        content: '';
        position: absolute;
        width: 100%;
        transform: scaleX(0);
        height: 2px;
        bottom: 0;
        left: 0;
        background-color: #448aff;
        transform-origin: bottom right;
        transition: transform .4s cubic-bezier(.86, 0, .07, 1)
    }

    .icon-active{
        color: #448aff;
    }

    .animoBorderLeftRight:hover::after {
        transform: scaleX(1);
        transform-origin: bottom left
    }

    a:not(.md-button):hover{
        text-decoration: none;
    }
    .md-theme-default a.active {
        font-weight: bold;
        letter-spacing: 8px;
    }

    footer {
        border-top: 1px solid lightgrey;
        margin-top: 25px;
        padding: 5px;
    }

    .title{
        font-size: 25pt;
        font-family: 'Pacifico', cursive;
        line-height: 50pt;
    }
</style>
