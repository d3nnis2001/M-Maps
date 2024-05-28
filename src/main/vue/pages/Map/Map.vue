<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted, ref} from 'vue';
import {getGeoData, getTrack, getPartOfTrack, getTimeFromHeatmap} from "@/main/vue/api/map";
import {useQuasar} from "quasar";
import DateInput from "@/main/vue/pages/Map/DateInput.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import router from "@/main/vue/router";

const map = ref(null);
var markers = [];
const streckenID = ref('')
const date2 = ref('')
const date = ref('')
const alert =  ref(false)
const alert2 =  ref(false)
const dialogVisible = ref(false);
const $q = useQuasar()
const markerStart = ref('')
const markerEnd = ref('')
const kmStart = ref('')
const kmEnd = ref('')
const selectedMarker = ref('')


onMounted(async () => {
    map.value = L.map('map', {
        center: [51.1657, 10.4515],
        zoom: 6,
        maxBounds: [
            [55.0583, 5.8662],
            [47.2701, 15.0419]
        ]
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map.value);

    new L.TileLayer('http://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png',
        {
            attribution: '<a href="https://www.openstreetmap.org/copyright">© OpenStreetMap contributors</a>, Style: <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA 2.0</a> <a href="http://www.openrailwaymap.org/">OpenRailwayMap</a> and OpenStreetMap',
            minZoom: 6,
            maxZoom: 19,
            tileSize: 256
        }).addTo(map.value);
    const data = await getGeoData();
    data.forEach((m) => markers.push({
        marker : L.circle([m.longitude, m.latitude], {color: "black", radius: 50}),
        data: m,
    }));
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
    map.value.on('locationfound', onLocationFound);
    map.value.locate({setView: false});
});

const onMarkerClicked = (event) => {
    const circle = event.target;
    const marker = markers.find((m) => {
            return m.data.longitude === circle.getLatLng().lat && m.data.latitude === circle.getLatLng().lng;
    });
    selectedMarker.value = marker
    dialogVisible.value = true;
    console.log(marker.data.latitude)
    console.log(marker.data.longitude)
};

const deleteStart = () => {
    markerStart.value = null;
    kmStart.value = '';
};

const deleteEnd = () => {
    markerEnd.value = null;
    kmEnd.value = '';
};

const addStart = () => {
    markerStart.value = selectedMarker;
    kmStart.value = selectedMarker.value.data.track_km;
};
const addEnd = () => {
    markerEnd.value = selectedMarker;
    kmEnd.value = selectedMarker.value.data.track_km;
};

const refreshMarkers = async () => {
    if (streckenID.value === "") {
        $q.notify({
            type: 'negative',
            message: "Please enter a Track ID",
            caption: 'Choose please'
        });
    } else {
        const data = await getTrack(streckenID.value);
        console.log(data.length)
        if (data.length === 0) {
            $q.notify({
                type: 'negative',
                message: "Track ID doesn't exist",
                caption: 'Please choose a different Track ID'
            });
        }
        markers.forEach((m) => map.value.removeLayer(m.marker));
        markers = []
        for (let i = 0;i<data.length;i++) {
            markers.push({
                marker : L.circle([data[i].longitude, data[i].latitude], {color: "black", radius: 50}),
                data: data[i],
            });
        }
        markers.forEach((m) => {
            m.marker.addTo(map.value);
            m.marker.on('click', onMarkerClicked);
        });
        checkForChanges()
    }
};

const createRepairOrder = async () => {
    console.log(selectedMarker.value.data)
    const longitude = selectedMarker.value.data.longitude
    const latitude = selectedMarker.value.data.latitude
    const streckenID = selectedMarker.value.data.strecken_id
    await router.push({
        path: "/repair/create",
        query: {
            longitude: longitude,
            latitude: latitude,
            streckenID: streckenID
        }
    });
}


const onLocationFound = (e) => {
    const radius = e.accuracy;
    const userLocation = e.latlng;
    L.circle(userLocation, { color: 'blue', radius: 200}).addTo(map.value);
}
const centerToUserLocation = async () => {
    const options = {
        setView: true,
        maxZoom: 12,
        animate: true
    }
    map.value.locate(options).on('locationfound', onLocationFound);
    const gleis = await getPartOfGleislage(streckenID.value)
    console.log(gleis)
};

const checkForChanges = async () => {
    if (kmStart.value !== '' && kmEnd.value !== '' && streckenID.value !== "") {
        console.log(kmStart.value)
        const data = await getPartOfTrack(kmStart.value, kmEnd.value)
        console.log(data)
        markers.forEach((m) => map.value.removeLayer(m.marker));
        markers = []
        for (let i = 0;i<data.length;i++) {
            markers.push({
                marker : L.circle([data[i].longitude, data[i].latitude], {color: "black", radius: 50}),
                data: data[i],
            });
        }
        markers.forEach((m) => {
            m.marker.addTo(map.value);
            m.marker.on('click', onMarkerClicked);
        });
    } else {
    }
}

async function getTimeRangeData() {
    if (date !== '' && date2 !== '') {
        const response = await getTimeFromHeatmap(streckenID.value, date.value, date2.value)
        console.log(response)
    } else {
        $q.notify({
            type: 'negative',
            message: "Please choose a valid time range",
            caption: 'You have to fill in both values'
        });
    }
}


</script>

<template>
    <div class="mapSettings">
        <div class="row putStart">
            <q-expansion-item
                icon="settings"
                label="Verfügbare Filter"
                expand-icon="arrow_drop_down"
                dense
                class="expansion_settings"
            >
                <q-list>
                    <q-item clickable v-ripple>
                        <q-item-section>
                            <div class="expan_items" @click="alert2=true">
                                Zeitraum
                            </div>
                        </q-item-section>
                    </q-item>
                    <q-item clickable v-ripple>
                        <q-item-section>
                            <div class="expan_items" @click="alert=true">
                                Strecken-ID auswählen
                            </div>
                        </q-item-section>
                    </q-item>
                </q-list>
            </q-expansion-item>
            <div class="location_button">
                <q-btn
                    class="button_settings"
                    icon="my_location"
                    round
                    flat
                    @click="centerToUserLocation"
                    aria-label="Center to User's Location"
                />
            </div>
        </div>
        <div id="map"></div>
        <div class="flexbox_map">
            <q-dialog v-model="alert2">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">Wähle einen Zeitraum</div>
                    </q-card-section>

                    <q-card-section class="q-pt-none">
                        <div class="q-pa-md" style="max-width: 300px">
                            <DateInput v-model="date"></DateInput>
                        </div>
                        <div class="q-pa-md" style="max-width: 300px">
                            <DateInput v-model="date2"></DateInput>
                        </div>
                    </q-card-section>

                    <q-card-actions align="center">
                        <q-btn flat @click="getTimeRangeData" label="Filter anwenden" color="primary" v-close-popup/>
                        <q-btn flat label="Abbrechen" color="primary" v-close-popup />
                    </q-card-actions>
                </q-card>
            </q-dialog>
            <q-dialog v-model="alert">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">Wähle eine Strecken-ID</div>
                    </q-card-section>
                    <StandardInput v-model="streckenID" label="Strecken-ID Eingabe"></StandardInput>
                    <q-card-actions align="center">
                        <q-btn @click="refreshMarkers" flat label="Filter anwenden" color="primary" v-close-popup/>
                        <q-btn flat label="Abbrechen" color="primary" v-close-popup />
                    </q-card-actions>
                </q-card>
            </q-dialog>
        </div>
    </div>
    <q-dialog class="dialog_map" v-model="dialogVisible">
        <div class="">
            <div class="col">
                <div class="row">
                    <q-card flat square class="col borderFirst">
                        <div class="row">
                            <q-input class="full-width" v-model="kmStart" label="Kilometer Start" readonly/>
                            <q-btn
                                size="l"
                                flat
                                round
                                icon="add"
                                @click="addStart"
                            />
                            <q-btn
                                size="l"
                                flat
                                round
                                icon="delete"
                                @click="deleteStart"
                            />
                        </div>
                    </q-card>
                    <q-card flat square class="col borderSecond">
                        <div class="row">
                            <q-input class="full-width" v-model="kmEnd" label="Kilometer End" readonly/>
                            <q-btn
                                size="l"
                                flat
                                round
                                icon="add"
                                @click="addEnd"
                            />
                            <q-btn
                                size="l"
                                flat
                                round
                                icon="delete"
                                @click="deleteEnd"
                            />
                        </div>
                    </q-card>
                </div>
                <q-card flat square bordered>
                    <q-card-actions>
                        <q-btn
                            style="align-items: end; justify-content: end"
                            flat
                            no-caps
                            @click="createRepairOrder"
                            label="Reparatur anlegen"
                            color="primary"
                            v-close-popup
                        >
                            <q-icon name="build" style="margin-left: 8px;" />
                        </q-btn>
                    </q-card-actions>
                </q-card>
                <q-card flat square bordered>
                    <q-card-actions>
                        <q-btn
                            flat
                            no-caps
                            @click="checkForChanges"
                            label="Speichern"
                            color="primary"
                            v-close-popup
                        />
                    </q-card-actions>
                </q-card>

            </div>
        </div>
    </q-dialog>
</template>

<style lang="scss">

.full-width {
    flex: 1;
    margin: 0;
    width: 100%;
}

.borderFirst {
    border-right: 1px solid black;
    border-bottom: 2px solid black;
}

.borderSecond {
    border-left: 1px solid black;
    border-bottom: 2px solid black;
}

.expansion_settings {
    background-color: #e0e0e0;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
}

.margin-right-con {
    display: flex;
    flex-direction: row;
    justify-content: end;
}

.expan_items {
    font-weight: bold;
}

#map {
    width: 95vw;
    height: 80vh;
    border: 3px solid $primary;
    border-radius: 15px;
}

.mapSettings {
    flex-direction: column;
    align-items: center;
    display: flex;
    justify-content: center;
}

.putStart {
    display: flex;
    width: 100%;
    justify-content: start;
    margin-left: 5vw;
    margin-top: 20px;
}

.flexbox_map {
    margin: 20px;
    display: flex;
    flex-direction: column;
}

.expansion_settings {
    position: absolute;
    z-index: 2000;
}

.location_button {
    position: relative;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    width: 100%;
    margin-right: 5vw;
}

</style>

<script>

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
