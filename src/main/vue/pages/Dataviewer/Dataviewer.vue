<script>
import {ref} from "vue";
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";

export default {
  setup(){
      const datapoint = ref('')
      const routeID = ref('')
      const $q = useQuasar()
      const router = useRouter()

      function showData() {
          if (datapoint.value === '') {
              console.log(false)
              $q.notify({
                  type: 'negative',
                  message: "Please enter a Datapoint ID"
              })
          } else {
              console.log(true)
              const pointId = datapoint.value
              router.push(`/dataviewer/point/${pointId}`)
          }
      }

      function showRoute() {
          if (routeID.value === '') {
              console.log(false)
              $q.notify({
                  type: 'negative',
                  message: "Please enter a Route ID"
              })
          } else {
              console.log(true)
              const routeId = routeID.value
              router.push(`/dataviewer/route/${routeId}`)
          }
      }

      return {
          datapoint,
          routeID,
          showData,
          showRoute
      }
  }
};
</script>

<template>
<div>
    <div class="q-pa-xs">
        <div class="align-mult">
            <div class="align-basic">
                <p>Datenpunkt ID</p>
                <q-input class="extra-mar" outlined v-model="datapoint"></q-input>
                <div class="q-pa-xs">
                    <q-btn label="Daten anzeigen" @click=showData class=""></q-btn>
                </div>
            </div>
            <div class="align-basic">
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
