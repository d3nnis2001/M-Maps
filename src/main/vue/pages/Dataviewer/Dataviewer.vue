<script>
import {ref, computed} from "vue";
import {useQuasar} from "quasar";
import {useRouter, useRoute} from "vue-router";

export default {
  setup(){
      const datapoint = ref('')
      const routeID = ref('')
      const $q = useQuasar()
      const router = useRouter()
      const route = useRoute()
      const site = ref(0)

      const NOTIFY_OPTIONS = {
          noDatapoint: {
              type: 'negative',
              message: 'Please enter a Datapoint ID'
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
          if (datapoint.value === '') {
              console.log(false)
              $q.notify(NOTIFY_OPTIONS.noDatapoint)
              datapoint.value = ''
          } else {
              console.log(true)
              const pointId = datapoint.value
              router.push(`/dataviewer/point/${pointId}`)
          }
      }

      function showRoute() {
          if (routeID.value === '6100') {
              console.log(true)
              const routeId = routeID.value
              router.push(`/dataviewer/route/${routeId}`)
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

      const isPointPath = computed(() => route.path.includes('/dataviewer/point'))
      const isRoutePath = computed(() => route.path.includes('/dataviewer/route'))

      return {
          site,
          datapoint,
          routeID,
          showData,
          showRoute,
          isPointPath,
          isRoutePath
      }
  }
};
</script>

<template>
<div>
    <div class="q-pa-xs">
        <div class="align-mult">
            <div v-if="!isPointPath" class="align-basic">
                <p>Datenpunkt ID</p>
                <q-input class="extra-mar" outlined v-model="datapoint"></q-input>
                <div class="q-pa-xs">
                    <q-btn label="Daten anzeigen" @click=showData class=""></q-btn>
                </div>
            </div>
            <div v-if="!isRoutePath" class="align-basic">
                <p>Strecken ID</p>
                <q-input class="extra-mar" outlined v-model="routeID" ></q-input>
                <div class="q-pa-xs">
                    <q-btn label="Strecke visualisieren" @click=showRoute class=""></q-btn>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<style scoped>

</style>
