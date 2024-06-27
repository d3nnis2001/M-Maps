<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted, ref, watch} from 'vue';
import {
    getGeoData,
    getHeatmap,
    getInformationForGeoPoint,
    getReparaturForMap,
    getTimeFromHeatmap
} from "@/main/vue/api/map";
import {useQuasar} from "quasar";
import DateInput from "@/main/vue/pages/Map/DateInput.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import router from "@/main/vue/router";
import WeatherComponent from "@/main/vue/pages/Map/WeatherComponent.vue";

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
let heatData = ref([])
const toggle_value = ref(false)
const silver_filter = ref(true)
const green_filter = ref(true)
const orange_filter = ref(true)
const red_filter = ref(true)
const cityName = ref("")
let information = ref([])
const geoJsonData = ref([])
const regions = [{label:"BY", value: []},
                {label:"BW", value: []},
                {label:"SW", value: []},
                {label:"H", value: []},
                {label:"SO", value: []},
                {label:"NRW", value: []},
                {label:"NO", value: []},
                {label:"NB", value: []},
                {label:"N", value: []}]

const BY = ref(false)
const BW = ref(false)
const SW = ref(false)
const H = ref(false)
const SO = ref(false)
const NRW = ref(false)
const NO = ref(false)
const NB = ref(false)
const N = ref(false)
const reparatur = ref([])
const showWeather = ref(false)
var weatherLat = 0.0
var weatherLon = 0.0

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
    fetch('https://raw.githubusercontent.com/isellsoap/deutschlandGeoJSON/main/2_bundeslaender/4_niedrig.geo.json')
        .then(response => response.json())
        .then(data => {
            geoJsonData.value = L.geoJSON(data, {
                style: {
                    color: 'blue',
                    weight: 2,
                    opacity: 0.8,
                    fillOpacity: 0.0,
                },
            })
            geoJsonData.value.addTo(map.value);
            data.features.forEach((s) => {
                switch(s.properties.id) {
                    case 'DE-BW': regions[1].value.push(s); break;
                    case 'DE-BY': regions[0].value.push(s); break;
                    case 'DE-BE': regions[6].value.push(s); break;
                    case 'DE-BB': regions[6].value.push(s); break;
                    case 'DE-HB': regions[7].value.push(s); break;
                    case 'DE-HH': regions[8].value.push(s); break;
                    case 'DE-HE': regions[3].value.push(s); break;
                    case 'DE-MV': regions[6].value.push(s); break;
                    case 'DE-NI': regions[7].value.push(s); break;
                    case 'DE-NW': regions[5].value.push(s); break;
                    case 'DE-RP': regions[2].value.push(s); break;
                    case 'DE-SL': regions[2].value.push(s); break;
                    case 'DE-ST': regions[4].value.push(s); break;
                    case 'DE-SN': regions[4].value.push(s); break;
                    case 'DE-SH': regions[8].value.push(s); break;
                    case 'DE-TH': regions[4].value.push(s); break;

                }
            })
        });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map.value);

    new L.TileLayer('http://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png',
        {
            attribution: '<a href="https://www.openstreetmap.org/copyright">© OpenStreetMap contributors</a>, Style: <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA 2.0</a> <a href="http://www.openrailwaymap.org/">OpenRailwayMap</a> and OpenStreetMap',
            tileSize: 256
        }).addTo(map.value);

    var Icon = L.icon({
        iconUrl: 'src/main/resources/construction.png',
        iconSize: [45, 45],
        iconAnchor: [0, 30]
    });
    const reparaturData = await getReparaturForMap()
    reparaturData.forEach((rep) => { reparatur.value.push( {
        marker: L.marker([rep.geocords.longitude,rep.geocords.latitude], {icon: Icon} ),
        reparaturAuftrag: rep
    }
    )
       reparatur.value[reparatur.value.length - 1].marker.addTo(map.value)
        reparatur.value[reparatur.value.length - 1].marker.on('click', onReparaturClicked);
    })

    map.value.on('dblclick', doubleClickOnMap);


    const data = await getGeoData();
    await setGeoData(data)
});

//-------------------------- Wetter ---------------------------
function doubleClickOnMap(e) {
    const { lat, lng } = e.latlng;
    console.log(`Double click: lat: ${lat}, longitude: ${lng}`);
    weatherLat = lat
    weatherLon = lng
    showWeather.value = true
}


// -------------------------- REGIONS --------------------------

/**
 * Region Nord (N): Schleswigwig Holstein und Hamburg
 * Region Niedersachsen (NB): Bremen, Niedersachsen
 * Region Nordost (NO): Berlin, Brandenburg, Mecklenburg Vorpommern
 * Region NRW: NRW
 * Region Südost (SO) : Sachsenanhalt, Sachsen, Thüringen
 * Region Hessen(H) : Hessen
 * Region Südwest(SW): Saarland, Rheinland -Pfalz
 * Region Badenwüttenberg(BW): Badenwüttenberg
 * Region Bayern (BY): Bayern **/

/**
 * Sets map up and gets all GeoData for standard view
 **/

watch(BY, onRegionChange)
watch(BW, onRegionChange)
watch(SW, onRegionChange)
watch(H, onRegionChange)
watch(SO, onRegionChange)
watch(NRW, onRegionChange)
watch(NO, onRegionChange)
watch(NB, onRegionChange)
watch(N, onRegionChange)

function datapointsFromRegion(region) {
    for (let i = 0; i < markers.length; i++) {
        var result = false;
        for (let j = region.length - 1; j >= 0; j--) {
            if (!result) {
                result = isMarkerInsidePolygon(markers[i], region[j]);
                if (!result)
                    result = isMarkerInsidePolygon2(markers[i], region[j])
            } else break;
        }
        if (result) {
            markers[i].marker.setStyle({opacity: 1, fillOpacity: 1});
        } else {
            markers[i].marker.setStyle({opacity: 0, fillOpacity: 0});
        }
    }
}

function onRegionChange() {

    map.value.removeLayer(geoJsonData.value)
    var temp = []
    if(BY.value === true) {
        temp = temp.concat(regions[0].value)
    }
    if(BW.value === true) {
        temp = temp.concat(regions[1].value)
    }
    if(SW.value === true) {
        temp = temp.concat(regions[2].value)
    }
    if(H.value === true) {
        temp = temp.concat(regions[3].value)
    }
    if(SO.value === true) {
        temp = temp.concat(regions[4].value)
    }
    if(NRW.value === true) {
        temp = temp.concat(regions[5].value)
    }
    if(NO.value === true) {
        temp = temp.concat(regions[6].value)
    }

    if(NB.value === true) {
        temp = temp.concat(regions[7].value)
    }
    if(N.value === true) {
        temp = temp.concat(regions[8].value)
    }
    geoJsonData.value = L.geoJSON(temp, {
        style: {
            color: 'blue',
            weight: 2,
            opacity: 0.5,
            fillOpacity: 0.0,
        },
    })
    geoJsonData.value.addTo(map.value);
    datapointsFromRegion(temp)
}


// Von https://stackoverflow.com/questions/31790344/determine-if-a-point-reside-inside-a-leaflet-polygon

function isMarkerInsidePolygon(marker, poly) {
    var polyPoints = [];
    for (var i = 0; i < poly.geometry.coordinates.length; i++) {
        polyPoints = polyPoints.concat(poly.geometry.coordinates[i][0]);
    }

    var x = marker.marker.getLatLng().lat, y = marker.marker.getLatLng().lng;
    var inside = false;
    for (var i = 0, j = polyPoints.length - 1; i < polyPoints.length; j = i++) {
        var xi = polyPoints[i][1], yi = polyPoints[i][0];
        var xj = polyPoints[j][1], yj = polyPoints[j][0];

        var intersect = ((yi > y) != (yj > y))
            && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }
    return inside;
}

function isMarkerInsidePolygon2(marker, poly) {
    var polyPoints = [];
    for (var i = 0; i < poly.geometry.coordinates.length; i++) {
        for (var j = 0; j < poly.geometry.coordinates[i].length; j++) {
            polyPoints.push(L.latLng(poly.geometry.coordinates[i][j][1], poly.geometry.coordinates[i][j][0]));
        }
    }
    var x = marker.marker.getLatLng().lat;
    var y = marker.marker.getLatLng().lng;

    var inside = false;
    for (var i = 0, j = polyPoints.length - 1; i < polyPoints.length; j = i++) {
        var xi = polyPoints[i].lat, yi = polyPoints[i].lng;
        var xj = polyPoints[j].lat, yj = polyPoints[j].lng;

        var intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }

    return inside;
}



// --------------------- SET DATA ----------------------

async function setGeoData(data) {
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
    var minlat = 180, maxlat = -180, minlong = 180, maxlong = -180, counter = 0
    markers.forEach((m) => {
        if (String(m.data.strecken_id) === String(trackID)) {
            counter++
            if(m.data.latitude < minlat)
                minlat = m.data.latitude
            if(m.data.latitude > maxlat)
                maxlat = m.data.latitude
            if(m.data.longitude < minlong)
                minlong = m.data.longitude
            if(m.data.longitude > maxlong)
                maxlong = m.data.longitude
            m.marker.setStyle({opacity: 1, fillOpacity: 1})
        }
        else {
            m.marker.setStyle({opacity: 0, fillOpacity: 0})
        }
    });
    if(counter > 0) {
        var lat = L.latLng(minlong, minlat)
        var lon = L.latLng(maxlong, maxlat)
        var bounds = L.latLngBounds(lat, lon);
        map.value.fitBounds(bounds)
    }
}

async function setPartGeoDataKm(km_start, km_end) {
    if (km_start > km_end) {
        let temp = km_start
        km_start = km_end
        km_end = temp
    }
    markers.forEach((m) => {
        if (!(m.data.track_km >= km_start && m.data.track_km <= km_end)) {
            m.marker.setStyle({opacity: 0, fillOpacity: 0})
        }
    });
}

const onReparaturClicked = async (event) => {
    const marker = event.target
    const lat = marker.getLatLng().lat
    const lng = marker.getLatLng().lng
    const rep = reparatur.value.find((r) => {
        return (lat == r.reparaturAuftrag.geocords.longitude && lng == r.reparaturAuftrag.geocords.latitude)
    })
    router.push(`/repair/${rep.reparaturAuftrag.id}/edit`)
}

const onMarkerClicked = async (event) => {
    const circle = event.target;
    const marker = markers.find((m) => {

        return m.data.longitude === circle.getLatLng().lat && m.data.latitude === circle.getLatLng().lng;
    });
    selectedMarker.value = marker
    dialogVisible.value = true;
    information.value = []
    const informationGeo = await getInformationForGeoPoint(selectedMarker.value.data.id)
    displayInformation(informationGeo)
};

async function displayInformation(info) {
    let output = []
    output.push("Strecken-ID: " + selectedMarker.value.data.strecken_id);
    for (let i = 0;i < info.length;i++) {
        switch(i) {
            case 0:
                output.push("Maximale Linksabweichung: "+info[i].toString())
                break;
            case 1:
                output.push("Maximale Rechtsabweichung: "+info[i].toString())
                break;
            case 2:
                output.push("Zugelassene Maximal Geschwindigkeit: "+info[i].toString())
                break;
            case 3:
                output.push("Durchschnittliche Geschwindigkeit: "+info[i].toString())
                break;
        }
    }
    information.value = output
 }

// ---------------------------- Filter -----------------------------------

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

async function zoomToCity(cityName) {
    try {
        var url = `https://nominatim.openstreetmap.org/search?format=json&q=${cityName}`;

        const response = await fetch(url);
        const data = await response.json();

        if (data.length > 0) {
            var city = data[0];
            var lat = city.lat;
            var lon = city.lon;
            map.value.setView([lat, lon], 10);
        } else {
            console.log('City not found');
            if (window.cityBoundaryLayer) {
                map.value.removeLayer(window.cityBoundaryLayer);
            }
            return;
        }

        const boundaryResponse = await fetch(`https://nominatim.openstreetmap.org/search.php?q=${cityName}&polygon_geojson=1&format=json`);
        const boundaryData = await boundaryResponse.json();

        if (boundaryData.length > 0) {
            const cityBoundaries = boundaryData[0].geojson;

            if (window.cityBoundaryLayer) {
                map.value.removeLayer(window.cityBoundaryLayer);
            }

            window.cityBoundaryLayer = L.geoJSON(cityBoundaries).addTo(map.value);
        } else {
            console.log('City boundaries not found');
        }
    } catch (error) {
        console.error('Error fetching data:', error);
        console.log('Error fetching data');
    }
}

// ---------------------------- HEATMAP ----------------------------------


const onChange = (newValue, oldValue) => {
    if (newValue === true) {
        getAllHeatmapData()
    } else {
        markers.forEach((m) => map.value.removeLayer(m.marker));
        markers = []
        setGeoData()
    }
};

const onChangeSilver = (newValue, oldValue) => {
    if (newValue === true) {
        getAllHeatDataColor("silver")
    } else {
        let deleted = [];
        for (let i = markers.length - 1; i >= 0; i--) {
            var temp = markers[i];
            if (temp.data[1] === "silver") {
                markers.splice(i, 1);
                deleted.push(temp);
            }
        }
        deleted.forEach((m) => map.value.removeLayer(m.marker));
    }
};

const onChangeGreen = (newValue, oldValue) => {
    if (newValue === true) {
        getAllHeatDataColor("green")
    } else {
        let deleted = [];
        for (let i = markers.length - 1; i >= 0; i--) {
            var temp = markers[i];
            if (temp.data[1] === "green") {
                markers.splice(i, 1);
                deleted.push(temp);
            }
        }
        deleted.forEach((m) => map.value.removeLayer(m.marker));
    }
};

const onChangeOrange = (newValue, oldValue) => {
    if (newValue === true) {
        getAllHeatDataColor("orange")
    } else {
        let deleted = [];
        for (let i = markers.length - 1; i >= 0; i--) {
            var temp = markers[i];
            if (temp.data[1] === "orange") {
                markers.splice(i, 1);
                deleted.push(temp);
            }
        }
        deleted.forEach((m) => map.value.removeLayer(m.marker));
    }
};

const onChangeRed = (newValue, oldValue) => {
    if (newValue === true) {
        getAllHeatDataColor("red")
    } else {
        let deleted = [];
        for (let i = markers.length - 1; i >= 0; i--) {
            var temp = markers[i];
            if (temp.data[1] === "red") {
                markers.splice(i, 1);
                deleted.push(temp);
            }
        }
        deleted.forEach((m) => map.value.removeLayer(m.marker));
    }
};

watch(toggle_value, onChange);
watch(silver_filter, onChangeSilver);
watch(green_filter, onChangeGreen);
watch(orange_filter, onChangeOrange);
watch(red_filter, onChangeRed);

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
            console.log(dataPoint[i].NORMAL)
            output.push([getLatLng(dataPoint[i].NORMAL), "silver", dataPoint[i].NORMAL])
        } else if (dataPoint[i].LOW !== undefined) {
            output.push([getLatLng(dataPoint[i].LOW), "green", dataPoint[i].LOW])
        } else if (dataPoint[i].MEDIUM !== undefined) {
            output.push([getLatLng(dataPoint[i].MEDIUM), "orange", dataPoint[i].MEDIUM])
        } else if (dataPoint[i].HIGH !== undefined) {
            output.push([getLatLng(dataPoint[i].HIGH), "red", dataPoint[i].HIGH])
        }
    }
    return output
}

async function getAllHeatDataColor(color) {
    var colors = getAllGeoPointsWithColor(heatData)
    var temp = []
    for (let i = 0; i < colors.length; i++) {
        if (colors[i][1] === color) {
            markers.push({
                marker: L.circleMarker([colors[i][0][0], colors[i][0][1]], {
                    color: colors[i][1],
                    radius: 5,
                    fillColor: colors[i][1],
                    fillOpacity: 1.0
                }),
                data: { latitude: colors[i][0][1], longitude: colors[i][0][0], id: colors[i][2]},
            });
            temp.push(markers[markers.length-1])
        }
    }
    temp.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}

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
            data: { latitude: colors[i][0][1], longitude: colors[i][0][0], id: colors[i][2]},
        });
    }
    markers.forEach((m) => {
        m.marker.addTo(map.value);
        m.marker.on('click', onMarkerClicked);
    });
}


async function getHeatmapWithTime() {
    console.log(streckenID)
    console.log(date._value)
    const timeData = await getTimeFromHeatmap(streckenID._value, date._value, date2._value)
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
                    <q-item v-ripple :disable="streckenID === ''">
                        <q-item-section>
                            <div class="expan_items" @click="streckenID !== '' && (alert2=true)">
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
                            <q-toggle
                                v-model="toggle_value"
                                color="red"
                                keep-color
                                readonly
                                label="Geodata / Heatmap"
                            />
                        </q-item-section>
                    </q-item>
                    <q-item>
                        <div class="q-gutter-sm">
                            <q-checkbox v-model="silver_filter"  val="Silver" label="Silver" color="silver" :disable="!toggle_value"/>
                            <q-checkbox v-model="green_filter" val="Green" label="Green" color="green" :disable="!toggle_value"/>
                            <q-checkbox v-model="orange_filter" val="Orange" label="Orange" color="orange" :disable="!toggle_value"/>
                            <q-checkbox v-model="red_filter" val="Red" label="Red" color="red" :disable="!toggle_value"/>
                        </div>
                    </q-item>
                    <q-item>
                        <StandardInput v-model="cityName">

                        </StandardInput>
                        <q-btn
                            class=""
                            icon="search"
                            round
                            flat
                            @click="zoomToCity(cityName)"
                            aria-label="Center to City"
                        />
                    </q-item>
                    <q-item>
                        <div class="col">
                            <p>Regionen</p>
                            <div class="q-gutter-sm">
                                <q-checkbox v-model="BY"  val="BY" label="BY" color="black"/>
                                <q-checkbox v-model="BW"  val="BW" label="BW" color="black"/>
                                <q-checkbox v-model="SW"  val="SW" label="SW" color="black"/>
                                <q-checkbox v-model="H"  val="H" label="H" color="black"/>
                                <q-checkbox v-model="SO"  val="SO" label="SO" color="black"/>
                            </div>
                            <div class="q-gutter-sm">
                                <q-checkbox v-model="NRW"  val="NRW" label="NRW" color="black"/>
                                <q-checkbox v-model="NO"  val="NO" label="NO" color="black"/>
                                <q-checkbox v-model="NB"  val="NB" label="NB" color="black"/>
                                <q-checkbox v-model="N"  val="N" label="N" color="black"/>
                            </div>
                        </div>
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
            <q-dialog v-model="alert2" >
                <q-card :disable="!toggle_value">
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
                <q-card>
                    <q-card-actions>
                        <q-icon name="info" style="margin-left: 8px;">
                            <q-tooltip anchor="top middle">
                                <p v-for="info in information" :key="info">{{info}}</p>
                            </q-tooltip>
                        </q-icon>
                    </q-card-actions>
                </q-card>

            </div>
        </div>
    </q-dialog>
    <q-dialog v-model="showWeather" class="fixed-dialog">
        <WeatherComponent :lat="weatherLat" :lon="weatherLon"/>
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

.fixed-dialog .q-dialog__inner {
    width: 390px;  /* Set the desired width */
    height: 800px; /* Set the desired height */
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
