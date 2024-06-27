<script setup>

import {getCurrentWeather, getWeatherForeCast} from "@/main/vue/api/weather";
import {onBeforeMount, onMounted, ref} from "vue";



const current_weather_data = ref(null)
const weather_forecast_data = ref(null)

const city = ref ("")
const current_condition = ref("")
const hour24_forecast = ref([])
const current_humidity = ref("")
const current_dew_point = ref("")
const current_perceptration = ref("")
const current_airPressure = ref("")
const current_windspeed = ref("")
const current_windGust = ref("")
const current_wind_direction = ref("")
const current_windGust_direction = ref("")

const hours = []
const icons = []
const temperatures = []




const props = defineProps({
    lat: Number,
    lon: Number,
})

onBeforeMount(async () => {
    current_weather_data.value = await getCurrentWeather(props.lat, props.lon)
    weather_forecast_data.value = await  getWeatherForeCast(props.lat, props.lon)

    console.log(current_weather_data.value)

    city.value = current_weather_data.value.sources[0].station_name
    current_condition.value = current_weather_data.value.weather.condition.toUpperCase()
    current_humidity.value = current_weather_data.value.weather.relative_humidity
    current_dew_point.value = Math.round(current_weather_data.value.weather.dew_point)
    current_perceptration.value = current_weather_data.value.weather.precipitation_60
    current_airPressure.value = current_weather_data.value.weather.pressure_msl
    current_windspeed.value = current_weather_data.value.weather.wind_speed_60
    current_windGust.value = current_weather_data.value.weather.wind_gust_speed_60
    current_wind_direction.value = windDirection(current_weather_data.value.weather.wind_direction_60);
    current_windGust_direction.value = windDirection(current_weather_data.value.weather.wind_gust_direction_60);

    hour24_forecast.value = weather_forecast_data.value.weather.slice(0,25)

    hour24_forecast.value.forEach((s) => {
        let date = new Date(s.timestamp);
        let hour = date.getHours();
        hours.push(hour)

        let converted_icon = s.icon.replace(new RegExp('-', 'g'), '_');
        icons.push(converted_icon)

        temperatures.push(s.temperature)
    })
})

onMounted(() => {
    const link = document.createElement('link');
    link.href = 'https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined';
    link.rel = 'stylesheet';
    document.head.appendChild(link);
})

function windDirection(degree) {
    degree = (degree + 360) % 360;

    if ((degree >= 337.5 && degree <= 360) || (degree >= 0 && degree < 22.5)) {
        return 'Nord';
    } else if (degree >= 22.5 && degree < 67.5) {
        return 'Nord-Ost';
    } else if (degree >= 67.5 && degree < 112.5) {
        return 'Ost';
    } else if (degree >= 112.5 && degree < 157.5) {
        return 'Süd-Ost';
    } else if (degree >= 157.5 && degree < 202.5) {
        return 'Süd';
    } else if (degree >= 202.5 && degree < 247.5) {
        return 'SÜd-West';
    } else if (degree >= 247.5 && degree < 292.5) {
        return 'West';
    } else {
        return 'Nord-West';
    }
}


</script>

<template>
    <div class="gradient-background">
        <div class="q-pa-md">
        <div class="q-pa-md flex flex-center">
            <div class="weather-card-container">
                <q-card class="weather-card">
                    <q-card-section class="q-pt-md q-pb-none">
                        <div class="text-center location">{{city}}</div>
                    </q-card-section>

                    <q-card-section class="q-py-none text-center temperature-section">
                        <div class="temperature">27°</div>
                        <div class="weather-status">{{current_condition}}</div>
                    </q-card-section>

                    <q-card-actions align="center" class="q-pt-none q-pb-md upper-section">
                        <div class="lower-section">H: 28° T: 19°</div>
                    </q-card-actions>
                </q-card>
            </div>
        </div>
        <div class=" flex flex-center">
            <div class="weather-card-container">
                <q-card class="weather-cardTwo">
                    <q-virtual-scroll
                        :items="hour24_forecast"
                        virtual-scroll-horizontal
                        v-slot="{ item, index }"
                    >
                        <div
                            class="weather-container"
                            :key="index"
                        >
                            <div class="time">{{hours[index]}} Uhr</div>
                            <div class="icon">
                                <span class="material-symbols-outlined">{{icons[index]}}</span>
                            </div>
                            <div class="temperaturesmall">{{temperatures[index]}}°</div>
                        </div>
                    </q-virtual-scroll>
                </q-card>
            </div>
        </div>

        <div class=" flex flex-center">
            <div class="weather-card-container">
                <q-card class="weather-cardTwo">
                    <div class="q-pa-md">
                        <div class="row">
                            <span class="material-symbols-outlined">calendar_month</span>
                            <div class="q-px-xs">10-TAGE-VORHERSAGE</div>
                        </div>
                    </div>
                    <q-list bordered separator>

                    </q-list>
                </q-card>
            </div>
        </div>

        <div class="flex-center">
            <div class="weather-card-container">
                <q-card class="weather-cardTwo">
                    <div class="q-pa-md">
                        <div class="row">
                            <span class="material-symbols-outlined">humidity_mid</span>
                            <div class="q-px-xs">FEUCHTIGKEIT</div>
                        </div>
                        <div class="temperature">{{current_humidity}} %</div>
                        <div></div>
                        <div class="time">Der Taupunkt liegt derzeit bei {{current_dew_point}}°.</div>
                    </div>
                </q-card>
            </div>
        </div>

        <div class="flex-center">
            <div class="weather-card-container">
                <q-card class="weather-cardTwo">
                    <div class="q-pa-md">
                        <div class="row">
                            <span class="material-symbols-outlined">water_drop</span>
                            <div class="q-px-xs">Niederschlag</div>
                        </div>
                        <div class="temperature">{{current_perceptration}} mm</div>
                        <div class="temperaturesmall">in der letzten Stunde.</div>
                    </div>
                </q-card>
            </div>
        </div>
            <div class="flex-center">
                <div class="weather-card-container">
                    <q-card class="weather-cardTwo">
                        <div class="q-pa-md">
                            <div class="row">
                                <span class="material-symbols-outlined">airwave</span>
                                <div class="q-px-xs">Luftdruck</div>
                                </div>
                                <div class="row">
                                    <div class="temperature">{{current_airPressure}}</div>
                                    <div class="q-pl-sm">
                                        <div class="temperaturesmall">hPa</div>
                                    </div>
                                </div>
                            </div>
                    </q-card>
                </div>
            </div>
            <div class="flex-center">
                <div class="weather-card-container">
                    <q-card class="weather-cardTwo">
                        <div class="q-pa-md">
                            <div class="row">
                                <span class="material-symbols-outlined">air</span>
                                <div class="q-px-xs">WIND</div>
                            </div>
                            <div class="q-py-sm">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="temperature">{{current_windspeed}}</div>
                                    </div>
                                    <div class="col-7">
                                        <div class="q-pl-sm">
                                            <div class="temperaturesmallTwo">km/h</div>
                                            <div class="temperaturesmallTwo">Wind</div>
                                            <div class="timeTwo">aus Richtung {{current_wind_direction}}.</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="q-pl-sm">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="temperature">{{current_windGust}}</div>
                                    </div>
                                    <div class="col-7">
                                        <div class="q-pl-sm">
                                            <div class="temperaturesmallTwo">km/h</div>
                                            <div class="temperaturesmallTwo">Windböen</div>
                                            <div class="timeTwo">aus Richtung {{current_windGust_direction}}.</div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            </div>
                    </q-card>
                </div>
            </div>

    </div>
    </div>
</template>

<style scoped>
.row {
    display: flex; /* Use Flexbox */
    align-items: center; /* Align items vertically in the center */
}

.weather-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(to bottom, #1e5799 0%,#2989d8 50%,#207cca 100%);
    color: white;
    width: 100px;
    padding: 10px;
}



.time {
    font-size: 15px;
    margin: 5px 0;
}
.timeTwo {
    font-size: 15px;
    margin: 0px 0;
}


.temperaturesmall {
    font-size: 20px;
    margin: 5px 0;
    font-weight: bold;
}

.temperaturesmallTwo {
    font-size: 20px;
    font-weight: bold;
}

.icon {
    margin: 10px 0;
}

.weather-card-container {
    display: flex;
    justify-content: center;
    padding-top: 20px;
    width: 100%;
}

.gradient-background {
    background: linear-gradient(to top, #007bff, #d0e6ff);
    width: 100%;
    height: 100%;
    justify-content: center;
    align-items: center;
}

.weather-card {
    width: 200px;
    background: linear-gradient(to bottom, rgba(0, 0, 128, 0.5), rgba(0, 0, 255, 0.2));
    color: white;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.weather-cardTwo {
    width: 325px;
    background: linear-gradient(to bottom, rgba(0, 0, 128, 0.5), rgba(0, 0, 255, 0.2));
    color: white;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.location {
    font-size: 1.2em;
    font-weight: bold;
}

.temperature-section {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.temperature {
    font-size: 4em;
    font-weight: bold;
    margin: 0;
}

.weather-status {
    font-size: 1.2em;
    margin: 0;
}

.upper-section {
    font-size: 1em;
    display: flex;
    justify-content: center;
    width: 100%;
}

.lower-section {
    font-weight: bold;
}
</style>
