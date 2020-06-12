<template id="app-frame">
    <div class="app-frame page-container md-layout-column">
        <md-toolbar class="md-primary">
            <md-button class="md-icon-button" @click="showNavigation = true">
                <md-icon>menu</md-icon>
            </md-button>
            <span class="md-title">Neighborino</span>
        </md-toolbar>

        <md-drawer :md-active.sync="showNavigation" md-swipeable>
            <md-toolbar class="md-transparent" md-elevation="0">
                <md-button class="md-icon-button" @click="showNavigation = false">
                    <md-icon>close</md-icon>
                </md-button>
            </md-toolbar>

            <md-list>
                <md-list-item>
                    <md-icon>home</md-icon>
                    <span class="md-list-item-text">
            <a href="/" ref="nav-home">Startseite</a>
          </span>
                </md-list-item>

                <md-list-item>
                    <md-icon>people</md-icon>
                    <span class="md-list-item-text">
            <a href="/users" ref="nav-users">User</a>
          </span>
                </md-list-item>

                <md-list-item>
                    <md-icon>event</md-icon>
                    <span class="md-list-item-text">
            <a href="/events" ref="nav-events">Events</a>
          </span>
                </md-list-item>

                <md-list-item>
                    <md-icon>apartment</md-icon>
                    <span class="md-list-item-text">
            <a href="/neighborhoods" ref="nav-neighborhoods">Nachbarschaften</a>
          </span>
                </md-list-item>

                <md-list-item>
                    <md-icon>help</md-icon>
                    <span class="md-list-item-text">
                      <a href="/about" ref="nav-about">Über diese Seite</a>
                    </span>
                </md-list-item>
            </md-list>
        </md-drawer>
        <md-content>
            <slot>
                <!-- Hier wird automatisch der restliche Code des Komponenten-Templates eingefügt! -->
            </slot>
        </md-content>
    </div>
</template>

<script>
    Vue.component("app-frame", {
        template: "#app-frame",
        props: ['currentPage'],
        data: () => ({
            showNavigation: false,
            showSidepanel: false
        }),
        mounted: function () {
            // Wenn der DOM fertig ist, setzen wir den Link der aktuellen Seite auf aktiv.
            this.$refs["nav-" + this.currentPage].classList.add("active");
        },
        methods: {}
    });
</script>

<style lang="scss" scoped>

    .page-container {
        height: 100%;
        min-height: 300px;
        overflow: hidden;
        position: relative;
        border: 1px solid rgba(#000, .12);
    }

    .md-drawer {
        width: 230px;
        max-width: calc(100vw - 125px);
    }

    .md-content {
        padding: 16px;
    }

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
