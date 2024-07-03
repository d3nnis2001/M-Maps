<script>
import {ref, computed} from "vue";
import {useQuasar} from "quasar";
import {useRouter, useRoute} from "vue-router";

export default {
  setup(){
      const datapoint = ref('')
      const pointID = ref('')
      const routeID = ref('')
      const fromStrKm = ref('')
      const toStrKm = ref('')
      const $q = useQuasar()
      const router = useRouter()
      const route = useRoute()
      const site = ref(0)

      const NOTIFY_OPTIONS = {
          noDatapoint: {
              type: 'negative',
              message: 'Please enter a Datapoint Long and Lat'
          },
          noRoute: {
              type: 'negative',
              message: 'Please enter a Route ID'
          },
          wrongRoute: {
              type: 'negative',
              message: 'The Route ID does not exist. Enter existing Route ID'
          }
      };

      function showData() {
          if (pointID.value === '') {
              console.log(false)
              $q.notify(NOTIFY_OPTIONS.noDatapoint)
              datapoint.value = ''
          } else {
              console.log(true)
              const pointId = pointID.value
              router.push(`/dataviewer/point/${pointId}`)
          }
      }

      function showRoute() {
          if (routeID.value !== '') {
              console.log(true)
              const routeId2 = routeID.value
              const fromStrKm2 = fromStrKm.value
              const toStrKm2 = toStrKm.value
              if (fromStrKm2 === '' || toStrKm2 === '') {
                  //router.push(`/dataviewer/route/${routeId2}`)
                  router.push(`/dataviewer/route/${routeId2}/from/${null}/to/${null}`)
              } else {
                  router.push(`/dataviewer/route/${routeId2}/from/${fromStrKm2}/to/${toStrKm2}`)
              }

          } else if (routeID.value === '') {
              console.log(false)
              $q.notify(NOTIFY_OPTIONS.noRoute)
              routeID.value = ''
          } else {
              console.log(false)
              $q.notify(NOTIFY_OPTIONS.wrongRoute)
              routeID.value = ''
          }
      }

      function onClickHome() {
          router.push(`/dataviewer`)
      }

      /*function onClickRoute() {
          router.push(`/dataviewer/route/0/from/0/to/0`)
      }*/

      /*function onClickPoint() {
          router.push(`/dataviewer/point/null/null`)
      }*/

      function refreshRoute() {}

      const isPointPath = computed(() => route.path.includes('/dataviewer/point'))
      const isRoutePath = computed(() => route.path.includes('/dataviewer/route'))

      return {
          site,
          pointID,
          routeID,
          fromStrKm,
          toStrKm,
          showData,
          showRoute,
          isPointPath,
          isRoutePath,
          refreshRoute,
          onClickHome,
          //onClickRoute,
          //onClickPoint
      }
  }
};
</script>

<template>
<div>
    <div class="q-pa-xs">
        <div class="align-mult">
            <div v-if="!isPointPath && !isRoutePath" class="align-basic">
                <p>Point ID</p>
                <q-input class="q-pa-xs" outlined v-model="pointID"></q-input>
                <div class="q-pa-xs">
                    <q-btn label="Daten anzeigen" @click=showData class=""></q-btn>
                </div>
            </div>
            <div v-if="!isPointPath && !isRoutePath">
                <div class="align-basic">
                    <p>Strecken ID</p>
                    <q-input class="q-pa-xs" outlined v-model="routeID" ></q-input>
                </div>
                <div class="align-mult">
                    <div>
                        <p>von</p>
                        <q-input class="q-pa-xs" outlined v-model="fromStrKm" ></q-input>
                    </div>
                    <div>
                        <p>bis</p>
                        <q-input class="q-pa-xs" outlined v-model="toStrKm" ></q-input>
                    </div>
                </div>
                <div class="align-mult">
                    <div v-if="!isRoutePath" class="q-pa-xs">
                        <q-btn label="Strecke visualisieren" @click=showRoute class=""></q-btn>
                    </div>
                </div>
            </div>
        </div>
        <q-page-sticky position="bottom-right" :offset="[10, 10]" >
            <div class="q-mt-lg">
                <q-fab
                    label="Actions"
                    vertical-actions-align="left"
                    color="red"
                    icon="keyboard_arrow_left"
                    direction="left"
                >
                    <q-fab-action
                        color="red"
                        @click="onClickHome"
                        icon="home"
                    />
                    <!--
                    <q-fab-action
                        v-if="!isRoutePath"
                        color="red"
                        @click="onClickRoute"
                        icon="analytics"
                    />
                    <q-fab-action
                        v-if="!isPointPath"
                        color="red"
                        @click="onClickPoint"
                        icon="place"
                    />-->
                </q-fab>
            </div>
        </q-page-sticky>
    </div>
</div>
</template>

<style scoped>

</style>
