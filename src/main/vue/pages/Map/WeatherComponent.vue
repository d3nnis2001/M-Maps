<script setup>
import {getCurrentWeather, getWeatherForeCast, getWeatherForeCast10Days} from "@/main/vue/api/weather";
import {onBeforeMount, onMounted, ref} from "vue";


const current_weather_data = ref(null)
const weather_forecast_data = ref(null)
const weather_forecast_data_10_days = ref(null)

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
const current_day_min_Temperature = ref("")
const current_day_max_temperature = ref("")
const current_temperature = ref("")

const show_day_info = ref(false)

const show_day_info_array = ref([])


const hours = []
const icons = []
const temperatures = []

const weather_10_days = []
const weather_10_days_structure = ref([])


const props = defineProps({
    lat: Number,
    lon: Number,
})

onBeforeMount(async () => {
    current_weather_data.value = await getCurrentWeather(props.lat, props.lon)
    weather_forecast_data.value = await  getWeatherForeCast(props.lat, props.lon)
    weather_forecast_data_10_days.value = await getWeatherForeCast10Days(props.lat, props.lon)


    for(var i = 0; i <= 10 * 24 + 1; i+= 25) {
        weather_10_days.push(weather_forecast_data_10_days.value.weather.slice(i, i+24))
    }

    current_day_max_temperature.value = Math.round(Math.max(...weather_10_days[0].map(obj => obj.temperature)))
    current_day_min_Temperature.value = Math.round(Math.min(...weather_10_days[0].map(obj => obj.temperature)))

    for(var i = 0; i < weather_10_days.length; ++i) {
        const max_temp = Math.round(Math.max(...weather_10_days[i].map(obj => obj.temperature)))
        const min_temp =  Math.round(Math.min(...weather_10_days[i].map(obj => obj.temperature)))
        var day = ""
        if(i === 0)
            day = "Heute"

        else
            day = getDayToTimeString(weather_10_days[i][0].timestamp)
        weather_10_days_structure.value.push({
            day: day,
            min_temp: min_temp,
            max_temp: max_temp
        })
    }



    city.value = current_weather_data.value.sources[0].station_name
    current_condition.value = current_weather_data.value.weather.condition.toUpperCase()
    current_temperature.value = Math.round(current_weather_data.value.weather.temperature)
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

        let converted_icon = convertIcon(s.icon)
        icons.push(converted_icon)

        temperatures.push(s.temperature)
    })
})

function convertIcon(icon) {
    var converted_icon = icon.replace(new RegExp('-', 'g'), '_');
    if(converted_icon === "rain")
        converted_icon = "rainy"
    if(converted_icon === "fog")
        converted_icon = "foggy"
    if(converted_icon === "wind")
        converted_icon = "air"
    return converted_icon
}

onMounted(() => {
    const link = document.createElement('link');
    link.href = 'https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined';
    link.rel = 'stylesheet';
    document.head.appendChild(link);
})

function getDayToTimeString(timeString) {
    const days = ["Su", "Mo", "Di", "Mi", "Do", "Fr", "Sa"]

    const date = new Date(timeString)

    return days[date.getDay()]
}

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
        return 'Süd-West';
    } else if (degree >= 247.5 && degree < 292.5) {
        return 'West';
    } else {
        return 'Nord-West';
    }
}

function getTemperatureRangeStyle(min_temp, max_temp) {
    const min_color = this.getTemperatureColor(min_temp)
    const max_color = this.getTemperatureColor(max_temp)
    return {
        background: `linear-gradient(to right, ${min_color}, ${max_color})`,
    }
}

function getTemperatureColor(temperature) {
    if (temperature < 0) {
        return "#3f33ff"
    } else if (temperature < 5) {
        return "#33ffff"
    } else if (temperature < 10) {
        return "#33ff8a"
    } else if (temperature < 15) {
        return "#33ff3c"
    } else if (temperature < 20) {
        return "#f3ff33"
    } else if (temperature < 25) {
        return "#ffdc00"
    } else if (temperature < 30) {
        return "#ffaa00"
    } else if (temperature < 35) {
        return "#ff7400"
    }
    else {
        return "#ff0000"
    }
}

function onDayClicked(index) {
    console.log(index)
    show_day_info.value = true
    show_day_info_array.value = weather_10_days[index]

    console.log(show_day_info_array.value)

    const data = []
    for(var i = 0; i < show_day_info_array.value.length; i++) {
        if(i % 3 === 0)
            data.push(show_day_info_array.value[i].temperature)
    }
    const min = Math.round(Math.min(...data) - 1)
    var max = Math.round(Math.max(...data) + 1)
    if((max-min) % 6 !== 0) {
        max = max + 6 - ((max-min) % 6)
    }
    options.value.yaxis.max = max
    options.value.yaxis.min = min
    series.value[0].data = data

    const data2 =  []

    for(var i = 0; i < show_day_info_array.value.length; i++) {
        if(i % 3 === 0)
            data2.push(show_day_info_array.value[i].precipitation_probability)
    }
    seriesTwo.value[0].data = data2
}

const optionsTwo = ref( {
    chart: {
        id: 'PerceptionProb',
        toolbar: {
            show: false
        },
        animations: {
            enabled: false
        },
        responsive: [
            {
                breakpoint: 400,
                options: {
                    chart: {
                        width: '100%'
                    }
                }
            }
        ],
    },

    stroke: {
        curve: 'smooth'
    },
    fill: {
        type: 'gradient',
        gradient: {
            shadeIntensity: 1,
            opacityFrom: 0.7,
            opacityTo: 0.9,
            stops: [0, 90, 100]
        }
    },
    xaxis: {
        categories: ['00 Uhr', '03 Uhr', '06 Uhr', '09 Uhr', '12 Uhr', '15 Uhr', '18 Uhr', '21 Uhr', '24 Uhr'],
        labels: {
            style: {
                colors: '#ffffff',
            }
        }
    },
    yaxis: {
        min: 0,
        max: 100,
        tickAmount: 5,
        labels: {
            formatter: function (val) {
                return val + '%';
            },
            style: {
                colors: '#ffffff',
            }
        }
    },
    grid: {
        borderColor: '#90A4AE',
        strokeDashArray: 4,
    },
})

const seriesTwo = ref( [{
    name: 'Niederschlag',
    data: []
}])

const options = ref( {
    chart: {
        id: 'temperatureChart',
            toolbar: {
            show: false
        },
        animations: {
            enabled: false
        },
        responsive: [
            {
                breakpoint: 400,
                options: {
                    chart: {
                        width: '100%'
                    }
                }
            }
        ],
    },
    stroke: {
        curve: 'smooth'
    },
    fill: {
        type: 'gradient',
            gradient: {
            shadeIntensity: 1,
                opacityFrom: 0.7,
                opacityTo: 0.9,
                stops: [0, 90, 100]
        }
    },
    xaxis: {
        categories: ['00 Uhr', '03 Uhr', '06 Uhr', '09 Uhr', '12 Uhr', '15 Uhr', '18 Uhr', '21 Uhr', '24 Uhr'],
            labels: {
            style: {
                colors: '#ffffff',
            }
        }
    },
    yaxis: {
        min: 0,
        max: 36,
        tickAmount: 6,
        labels: {
            formatter: function (val) {
                return val + '°';
            },
            style: {
                colors: '#ffffff',
            }
        }
    },
    grid: {
        borderColor: '#90A4AE',
            strokeDashArray: 4,
    },
})


const series = ref( [{
    name: 'Temperature',
    data: []
}])

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
                        <div class="temperature">{{current_temperature}}°</div>
                        <div class="weather-status">{{current_condition}}</div>
                    </q-card-section>

                    <q-card-actions align="center" class="q-pt-none q-pb-md upper-section">
                        <div class="lower-section">H: {{current_day_max_temperature}}° T: {{current_day_min_Temperature}}°</div>
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
                        <q-item v-for="(d, index) in weather_10_days_structure" :key="index" class="q-mb-sm" @click="onDayClicked(index)" clickable>
                            <div class="row items-center full-width">
                                <div class="col-3">
                                    {{ d.day }}
                                </div>
                                <div class="col-1">
                                    {{ d.min_temp }}°
                                </div>
                                <div class="col-7 q-pa-sm">
                                    <div class="temperature-bar">
                                        <div
                                            class="temperature-range"
                                            :style="getTemperatureRangeStyle(d.min_temp, d.max_temp)"
                                        ></div>
                                    </div>
                                </div>
                                <div class="col-1">
                                    {{ d.max_temp }}°
                                </div>
                            </div>
                        </q-item>
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

    <q-dialog v-model="show_day_info" class="fixed-dialog">
        <div class="gradient-background">
            <div class="row no-wrap">
                    <div class="icon q-px-xs">
                        <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[0].icon)}}</span>
                    </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[2].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[4].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[6].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[8].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[10].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[12].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[14].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[16].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[18].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[20].icon)}}</span>
                </div>
                <div class="icon q-px-xs">
                    <span class="material-symbols-outlined" style="font-size: 18px;">{{convertIcon(show_day_info_array[22].icon)}}</span>
                </div>
            </div>
            <div class="q-pa-xs">
                <apexchart width="300" type="line" :options="options" :series="series"></apexchart>
            </div>
            <div class="temperaturesmall q-pa-sm" style="color: white;">
                Niederschlagswahrscheinlichkeit
            </div>
            <div class="q-pa-md">
                <apexchart  width="300" type="line" :options="optionsTwo" :series="seriesTwo"></apexchart>
            </div>
        </div>
    </q-dialog>
</template>

<style scoped>
.row {
    align-items: center;
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

.temperature-bar {
    height: 10px;
    width: 100%;
    border-radius: 5px;
    overflow: hidden;
    background-color: #eee;
}
.temperature-range {
    height: 100%;
}

.fixed-dialog .q-dialog__inner {
    width: 100px;
    height: 100px;
}
</style>
