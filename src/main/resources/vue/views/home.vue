<!-- HTML template for this component -->
<template id="home-tmpl">
    <app-frame current-page="home" style="overflow: hidden;">

  <div style="text-align: center; width: 100%; margin-top: 16px">
    <p class="title" style="margin:0;padding:0;display:block; font-size: 25pt; font-weight: bold;">Neighborino</p>
    <p style="margin:0;padding:0;display:block; font-size: 15pt; font-style: italic;"><q>Meet your neighbors.</q></p>
    <p style="margin:0;padding:0;display:block; font-size: 15pt;">
      Neighborino wurde geschaffen, um die Beziehungen mit den Menschen in der unmittelbaren Umgebung aufzubauen und zu stärken.
    </p>
  </div>
  <div id="oval">
  </div>
  <div style="left:0;height: 300px; position: absolute;margin: 0 auto; display: flex; justify-content: flex-start;">
    <div style="position:absolute; overflow: hidden; display: inline-block; width: 200px; height: 300px;">
      <div style="position: relative; width: 200px; height: 100px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 5px dashed silver; background: gray; width: 75px; height: 50px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-2.svg" style="width: 33%"/>
      </div>
    </div>

    <div style="position:absolute; overflow: hidden; display: inline-block; width: 200px; height: 300px; margin-top: 75px; z-index: 1">
      <div style="position: relative; width: 250px; height: 200px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 10px dashed silver; background: gray; width: 125px; height: 100px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-4.svg" style="animation-delay: .2s;"
             width="50%"/>
      </div>
    </div>


    <div style="position:absolute; overflow: hidden; display: inline-block; width: 300px; height: 300px; margin-top: 175px; z-index: 1">
      <div style="position: relative; width: 300px; height: 300px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 15px dashed silver; background: gray; width: 225px; height: 150px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-5.svg" style="animation-delay: .5s;"
             width="75%"/>
      </div>
    </div>
  </div>
  <div style="right:0;height: 300px; position: absolute;transform: scale(1) scaleX(-1);margin: 0 auto; display: flex; justify-content: flex-start;">

    <div style="animation-delay: : ; position:absolute; overflow: hidden; display: inline-block; width: 200px; height: 300px;">
      <div style="position: relative; width: 200px; height: 100px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 5px dashed silver; background: gray; width: 75px; height: 50px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-12.svg" style="width: 33%" style="animation-delay: .3s;"/>
      </div>
    </div>

    <div style="animation-delay: ;position:absolute; overflow: hidden; display: inline-block; width: 200px; height: 300px; margin-top: 75px; z-index: 1">
      <div style="position: relative; width: 250px; height: 200px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 10px dashed silver; background: gray; width: 125px; height: 100px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-13.svg" width="50%" style="animation-delay: .75s;"/>
      </div>
    </div>


    <div style="animation-delay: ;position:absolute; overflow: hidden; display: inline-block; width: 300px; height: 300px; margin-top: 175px; z-index: 1">
      <div style="position: relative; width: 300px; height: 300px;">
        <div class="balcony"
             style="position: absolute; bottom: 0; border-top: 15px dashed silver; background: gray; width: 225px; height: 150px;">

        </div>
        <img alt="Neighbor" class="nodrag neighbor" src="standing-14.svg" width="75%"/>
      </div>
      <!--  <div style="position:absolute; overflow: hidden; display: inline-block; width: 200px; height: 300px;">-->
      <!--    <div style="position: relative; width: 400px; height: 500px;">-->
      <!--      <div class="balcony" style="position: absolute; bottom: 0; border-top: 15px dashed silver; background: gray; width: 400px; height: 250px;">-->

      <!--      </div>-->
      <!--      <img class="nodrag neighbor" src="standing-2.svg"  alt="Neighbor" />-->
      <!--    </div>-->
      <!--  </div>-->

    </div>
  </div>
    </app-frame>
</template>

<!-- Definition of the component and its data. Reference to the template. -->
<script>
    Vue.component("home-comp", {
        template: "#home-tmpl",
        data: () => ({
            greeting: "Startseite"
        }),
        created() {
            document.title = "Startseite - " + document.title;
        },
    });
</script>


<!-- Design rules for the template. -->
<style>

  p{
    quotes: "„" "“";
  }
  #oval {
    position: absolute;
    left: 0;
    width: 100%;
    height: 475px;
    background: lightskyblue;
    -moz-border-radius: 70px;
    -webkit-border-radius: 70px;
    border-top-left-radius: 500px;
    border-top-right-radius: 500px;
    transform: translateY(100%);
    opacity: 0;
    animation: sky 1s linear forwards;
    animation-delay: 2s;
  }
    .hello-world {
        color: black;
    }

    .neighbor {
       animation: walkOutside 1.5s linear;
      animation-fill-mode: forwards;
        position: absolute;
      transform: translateX(-100%);

    }

    .balcony {
        z-index: 1;
    }

    @keyframes walkOutside {
        0% {
            transform: translateX(-100%);
        }
        100% {
          opacity: 1 !important;
          transform: translateX(0);
        }
    }

    @keyframes sky {
        0% {
          opacity: 0;
            transform: translateY(100%);
        }
        100% {
          opacity: 1;
          transform: translateY(0);
        }
    }
</style>
