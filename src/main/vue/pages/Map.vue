<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted, ref} from 'vue';
import {geoData} from "@/main/vue/api/map";

const map = ref(null);
var markers = [];
const dialogVisible = ref(false);

onMounted(async () => {
    map.value = L.map('map', {
        center: [51.1657, 10.4515],
        zoom: 6,
        maxBounds: [
            [55.0583, 5.8662],
            [47.2701, 15.0419]
        ]
    });

    new L.TileLayer('http://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png',
        {
            attribution: '<a href="https://www.openstreetmap.org/copyright">© OpenStreetMap contributors</a>, Style: <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA 2.0</a> <a href="http://www.openrailwaymap.org/">OpenRailwayMap</a> and OpenStreetMap',
            minZoom: 6,
            maxZoom: 19,
            tileSize: 256
        }).addTo(map.value);
    const data = await geoData();
    markers = data.map((m) => L.circle([m.longitude, m.latitude], {color: "black", radius: 50}));
    markers.forEach((m) => {
        m.addTo(map.value);
        m.on('click', onMarkerClicked);
    });
});

const onMarkerClicked = (event) => {
    const circle = event.target;
    dialogVisible.value = true;
    console.log(circle.getLatLng());
};

</script>

<template>
    <div class="mapSettings">
        <div id="map"></div>
        <div class="">
            <div class="q-pa-md">
                <div class="q-gutter-md row items-start">
                    <q-select
                        filled
                        v-model="model2"
                        multiple
                        :options="options"
                        counter
                        label="filter"
                        style="width: 250px"
                    />
                </div>
            </div>
        </div>

    </div>
</template>

<style>
#map {
    width: 70vw;
    height: 85vh;
}
.mapSettings {
    margin-top: 35px;
    margin-left: 40px;
    display: flex;
    justify-content: left;
    align-items: center;
}
.flexbox_map {
    display: flex;
    flex-direction: column;
}
</style>
<script>

import { useQuasar } from 'quasar'
import { ref } from 'vue'

const columns = [
    {
        name: 'desc',
        required: true,
        align: 'left',
        field: row => row.name,
        sortable: true
    },
    { name: 'StreckenID', align: 'center', label: 'Strecken-ID', field: 'streckenid', sortable: true }
]
const rows = [
    {
        streckenid: '6060'
    },
    {
        streckenid: '6061'
    }
]
const model2 = ref(null)
const options =  [
    "Zeitraum",
    "ID des Streckenabschnitts",
    "Abschnitt der Karte"
]

</script>
