<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted, ref} from 'vue';
import {geoData} from "@/main/vue/api/map";

const map = ref(null);
var markers = [];
const streckenID = ref('')
const date2 = ref('')
const date = ref('')
const alert =  ref(false)
const alert2 =  ref(false)
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
    map.value.on('locationfound', onLocationFound);
    map.value.locate({setView: false});
});

const onMarkerClicked = (event) => {
    const circle = event.target;
    dialogVisible.value = true;
    console.log(circle.getLatLng());
};

const onLocationFound = (e) => {
    const radius = e.accuracy;
    const userLocation = e.latlng;
    L.circle(userLocation, { color: 'blue', radius: 200}).addTo(map.value);
}
const centerToUserLocation = () => {
    const options = {
        setView: true,
        maxZoom: 12,
        animate: true
    }
    map.value.locate(options).on('locationfound', onLocationFound);
};


</script>

<template>
    <div class="mapSettings">
        <div id="map"></div>
        <div class="flexbox_map">
            <button @click="centerToUserLocation">Center to User's Location</button>
            <q-card class="my-card">
                <q-card-section>
                    <div class="text-h6">Filter</div>
                </q-card-section>
                <q-separator />

                <q-card-actions vertical>
                    <q-btn @click="alert2 = true" no-caps flat>Zeitraum</q-btn>
                    <q-btn no-caps flat>ID des Streckenabschnitts</q-btn>
                </q-card-actions>
                <q-dialog v-model="alert2">
                    <q-card>
                        <q-card-section>
                            <div class="text-h6">Wähle einen Zeitraum</div>
                        </q-card-section>

                        <q-card-section class="q-pt-none">
                            <div class="q-pa-md" style="max-width: 300px">
                                <q-input filled v-model="date">
                                    <template v-slot:prepend>
                                        <q-icon name="event" class="cursor-pointer">
                                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                <q-date v-model="date" mask="YYYY-MM-DD HH:mm">
                                                    <div class="row items-center justify-end">
                                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                                    </div>
                                                </q-date>
                                            </q-popup-proxy>
                                        </q-icon>
                                    </template>

                                    <template v-slot:append>
                                        <q-icon name="access_time" class="cursor-pointer">
                                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                <q-time v-model="date" mask="YYYY-MM-DD HH:mm" format24h>
                                                    <div class="row items-center justify-end">
                                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                                    </div>
                                                </q-time>
                                            </q-popup-proxy>
                                        </q-icon>
                                    </template>
                                </q-input>
                            </div>
                            <div class="q-pa-md" style="max-width: 300px">
                                <q-input filled v-model="date2">
                                    <template v-slot:prepend>
                                        <q-icon name="event" class="cursor-pointer">
                                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                <q-date v-model="date2" mask="YYYY-MM-DD HH:mm">
                                                    <div class="row items-center justify-end">
                                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                                    </div>
                                                </q-date>
                                            </q-popup-proxy>
                                        </q-icon>
                                    </template>

                                    <template v-slot:append>
                                        <q-icon name="access_time" class="cursor-pointer">
                                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                <q-time v-model="date2" mask="YYYY-MM-DD HH:mm" format24h>
                                                    <div class="row items-center justify-end">
                                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                                    </div>
                                                </q-time>
                                            </q-popup-proxy>
                                        </q-icon>
                                    </template>
                                </q-input>
                            </div>
                        </q-card-section>

                        <q-card-actions align="center">
                            <q-btn flat label="Filter anwenden" color="primary" v-close-popup/>
                            <q-btn flat label="Abbrechen" color="primary" v-close-popup />
                        </q-card-actions>
                    </q-card>
                </q-dialog>
            </q-card>
            <q-card class="my-card">
                <q-card-section>
                    <div class="text-h6">Optionen</div>
                </q-card-section>

                <q-separator />

                <q-card-actions vertical>
                    <q-btn no-caps flat>Streckenabschnitt auswählen</q-btn>
                    <q-btn @click="alert = true" no-caps flat>Strecken-ID auswählen</q-btn>
                </q-card-actions>
                <q-dialog v-model="alert">
                    <q-card>
                        <q-card-section>
                            <div class="text-h6">Wähle eine StreckenID</div>
                        </q-card-section>

                        <q-card-section class="q-pt-none">
                            <q-input label="Strecken-ID Eingabe" name="streckeneingabe" v-model="streckenID">
                            </q-input>
                        </q-card-section>

                        <q-card-actions align="center">
                            <q-btn flat label="Eingabe bestätigen" color="primary" v-close-popup/>
                            <q-btn flat label="Abbrechen" color="primary" v-close-popup />
                        </q-card-actions>
                    </q-card>
                </q-dialog>
            </q-card>
        </div>

    </div>
</template>
<style>
.my-card {
    width: 100%;
    max-width: 250px;
    margin-bottom: 20px;
}
#map {
    width: 70vw;
    height: 85vh;
}
.mapSettings {
    margin-top: 35px;
    margin-left: 40px;
    display: flex;
    justify-content: left;
}
.flexbox_map {
    margin: 20px;
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
const options =  [
    "Zeitraum",
    "ID des Streckenabschnitts",
    "Abschnitt der Karte"
]

</script>
