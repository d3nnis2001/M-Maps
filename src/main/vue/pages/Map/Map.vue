<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted, ref} from 'vue';
import {getGeoData, getHeatmap, getTimeFromHeatmap} from "@/main/vue/api/map";
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
let data = ref([])
let heatData = ref([])

/**
 * Sets map up and gets all GeoData for standard view
 **/
onMounted(async () => {
    map.value = L.map('map', {
        center: [51.1657, 10.4515],
        zoom: 7,
        maxZoom: 15,
        minZoom: 7,
        maxBounds: [
            [55.0583, 5.8662],
            [47.2701, 15.0419]
        ]
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map.value);

    new L.TileLayer('http://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png',
        {
            attribution: '<a href="https://www.openstreetmap.org/copyright">© OpenStreetMap contributors</a>, Style: <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA 2.0</a> <a href="http://www.openrailwaymap.org/">OpenRailwayMap</a> and OpenStreetMap',
            tileSize: 256
        }).addTo(map.value);
    data = await getGeoData();
    setGeoData()
});

// --------------------- SET DATA ----------------------

async function setGeoData() {
    markers.forEach((m) => map.value.removeLayer(m.marker));
    data.forEach((m) => markers.push({
        marker : L.circle([m.longitude, m.latitude], {color: "black", radius: 50}),
        data: m,
    }));
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}

async function setPartGeoData(trackID) {
    markers.forEach((m) => map.value.removeLayer(m.marker));
    markers = []
    data.forEach((m) => {
        if (String(m.strecken_id) === String(trackID)) {
            markers.push({marker: L.circle([m.longitude, m.latitude], {color: "black", radius: 50}), data: m})
        }
    });
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}

async function setPartGeoDataKm(km_start, km_end) {
    markers.forEach((m) => map.value.removeLayer(m.marker));
    markers = []
    if (km_start > km_end) {
        let temp = km_start
        km_start = km_end
        km_end = temp
    }
    data.forEach((m) => {
        if (m.track_km >= km_start && m.track_km <= km_end) {
            markers.push({marker: L.circle([m.longitude, m.latitude], {color: "black", radius: 50}), data: m})
        }
    });
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}

const onMarkerClicked = (event) => {
    const circle = event.target;
    const marker = markers.find((m) => {
        return m.data.longitude === circle.getLatLng().lat && m.data.latitude === circle.getLatLng().lng;
    });
    selectedMarker.value = marker
    dialogVisible.value = true;
};
// ---------------------------- Filter -----------------------------------

// ---------------------------- HEATMAP ----------------------------------


function getLatLng(geopoint) {
    for (let i = 0;i<data.length;i++) {
        if (data[i].id === geopoint) {
            return [data[i].longitude, data[i].latitude]
        }
    }
}


function getAllGeoPointsWithColor(dataPoint) {
    let output = []
    for(let i = 0;i < dataPoint.length;i++) {
        if(dataPoint[i].NORMAL !== undefined) {
            output.push([getLatLng(dataPoint[i].NORMAL), "black"])
        } else if (dataPoint[i].LOW !== undefined) {
            output.push([getLatLng(dataPoint[i].LOW), "green"])
        } else if (dataPoint[i].MEDIUM !== undefined) {
            output.push([getLatLng(dataPoint[i].MEDIUM), "orange"])
        } else if (dataPoint[i].HIGH !== undefined) {
            output.push([getLatLng(dataPoint[i].HIGH), "red"])
        }
    }
    return output
}


const setValueStreckenID = async () => {
    if (streckenID.value === "") {
        $q.notify({
            type: 'negative',
            message: "Please enter a Track ID",
            caption: 'Choose please'
        });
    } else {
        setPartGeoData(streckenID.value)
        if (markers.length === 0) {
            $q.notify({
                type: 'negative',
                message: "Track ID doesn't exist",
                caption: 'Please choose a different Track ID'
            });
        }
    }
};

async function getAllHeatmapData() {
    heatData = await getHeatmap()
    var colors = getAllGeoPointsWithColor(heatData)
    console.log(colors)
    if (data.length === 0) {
        $q.notify({
            type: 'negative',
            message: "Track ID doesn't exist",
            caption: 'Please choose a different Track ID'
        });
    }
    markers.forEach((m) => map.value.removeLayer(m.marker));
    markers = []
    for (let i = 0; i < colors.length; i++) {
        markers.push({
            marker: L.circleMarker([colors[i][0][0], colors[i][0][1]], {
                color: colors[i][1],
                radius: 5,
                fillColor: colors[i][1],
                fillOpacity: 1.0
            }),
            data: colors[i],
        });
    }
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}


async function getHeatmapWithTime() {
    const timeData = await getTimeFromHeatmap(streckenID, date._value, date2._value)
    console.log(timeData)
}

// ------------------------ LOCATION ZOOM ---------------------------

const onLocationFound = (e) => {
    const userLocation = e.latlng;
    L.circle(userLocation, {color: 'blue', radius: 100}).addTo(map.value);
    map.value.setView(userLocation, 12);
}

const onLocationError = (e) => {
    $q.notify({
        type: 'negative',
        message: `Location error: ${e.message}`,
        caption: 'Could not determine your location'
    });
}

const centerToUserLocation = async () => {
    map.value.locate().on('locationfound', onLocationFound).on("locationerror", onLocationError)
};

// -------------------- REPAIR ORDER ------------------------

const createRepairOrder = async () => {
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

// ------------------------- HANDLING FOR PART OF MAP -------------------------

const getPartOfGeoData = async () => {
    if (kmStart.value !== '' && kmEnd.value !== '') {
        setPartGeoDataKm(kmStart.value, kmEnd.value)
    }
}

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

// ---------------------------------------------------------------------------

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
                    <q-item clickable v-ripple>
                        <q-item-section>
                            <div class="expan_items" @click="getAllHeatmapData">
                                Heatmap anzeigen
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
                        <q-btn flat @click="getHeatmapWithTime" label="Filter anwenden" color="primary" v-close-popup/>
                        <q-btn flat label="Abbrechen" color="primary" v-close-popup/>
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
                        <q-btn @click="setValueStreckenID" flat label="Filter anwenden" color="primary" v-close-popup/>
                        <q-btn flat label="Abbrechen" color="primary" v-close-popup/>
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
                            <q-icon name="build" style="margin-left: 8px;"/>
                        </q-btn>
                    </q-card-actions>
                </q-card>
                <q-card flat square bordered>
                    <q-card-actions>
                        <q-btn
                            flat
                            no-caps
                            @click="getPartOfGeoData"
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
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.3);
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

import {ref} from 'vue'

const columns = [
    {
        name: 'desc',
        required: true,
        align: 'left',
        field: row => row.name,
        sortable: true
    },
    {name: 'StreckenID', align: 'center', label: 'Strecken-ID', field: 'streckenid', sortable: true}
]
const rows = [
    {
        streckenid: '6060'
    },
    {
        streckenid: '6061'
    }
]
const options = [
    "Zeitraum",
    "ID des Streckenabschnitts",
    "Abschnitt der Karte"
]

</script>
