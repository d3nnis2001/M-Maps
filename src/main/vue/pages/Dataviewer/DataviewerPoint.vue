<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {onMounted, onUnmounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {getPointData, getPointInfo} from "@/main/vue/api/dataviewer";
import VueApexCharts from "vue3-apexcharts";

export default {
    components: {
        Dataviewer,
        apexchart: VueApexCharts
    },
    setup(){
        const pointId = ref('')
        const lon = ref('')
        const lat = ref('')
        const trackId = ref('')
        const route = useRoute();
        const tab = ref('pointData')
        const loaded = ref(false)
        const smallScreen = ref(false);
        const largeScreen = ref(true);
        const showDialog = ref(false);
        const currentRow = reactive({});

        //----------------Tables-------------------

        const table = reactive( {
            filter: '',
            columns:[
                {
                    name: 'setId',
                    required: true,
                    label: 'Set ID',
                    align: 'left',
                    field: 'setId',
                    format: val => `${val}`,
                    sortable: true
                },
                {name: 'str_km', label: 'Str km', align: 'left', field: 'str_km', sortable: true},
                {name: 'time_unix', label: 'Creation Date', align: 'left', field: 'time_unix', sortable: true},
                {name: 'z_links_railab_3p', label: 'Left Railab', align: 'left', field: 'z_links_railab_3p', sortable: true},
                {name: 'z_rechts_railab_3p', label: 'Rigth Railab', align: 'left', field: 'z_rechts_railab_3p', sortable: true},
            ],
            rows: []
        });

        const rowClick = (evt, rowData) => {
            console.log(typeof rowData)
            console.log(rowData)
            console.log(currentRow)
            Object.assign(currentRow, rowData);
            showDialog.value = true;
        }

        //-----------Graph-----------------

        const chartOptions = ref({
            chart: {
                type: 'bar',
                height: '350',
                id: 'vuechart'
            },
            plotOptions: {
                bar: {
                    colors: {
                        ranges: [{
                            from: -100,
                            to: -46,
                            color: '#F15B46'
                        }, {
                            from: -45,
                            to: 0,
                            color: '#FEB019'
                        }]
                    },
                    columnWidth: '100%',
                }
            },
            dataLabels: {
                enabled: false
            },
            yaxis: {
                forceNiceScale: true,
                labels: {
                    formatter: (value) => {return value}
                },
                title: {
                    text: 'Längenhöhe [mm]'
                }
            },
            xaxis: {
                categories: [],
                title: {
                    text: 'Aufnahmezeit'
                }
            }
        });

        const series = ref([
            {
                name: 'z_links_railab_3p',
                data: []
            },
            {
                name: 'z_rechts_railab_3p',
                data: []
            }
        ]);

        //--------Start-----------

        const checkScreenSize = () => {
            const screenSize = window.innerWidth;
            smallScreen.value = screenSize <= 500;
            largeScreen.value = screenSize > 500;
        };

        onMounted(async () => {
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);
            pointId.value = route.params.pointId
            const [response, responseInfo] = await Promise.all([
                getPointData(pointId.value),
                getPointInfo(pointId.value)
            ])
            console.log(responseInfo)
            console.log(response)
            lon.value = responseInfo.longitude
            lat.value = responseInfo.latitude
            trackId.value = responseInfo.strecken_id
            response.forEach(data => {
                const timestamp = data.time_unix
                const date = new Date(timestamp * 1000)
                const formattedDate = String(date.getDate()).padStart(2, '0') + '-' +
                    String(date.getMonth() + 1).padStart(2, '0') + '-' +
                    date.getFullYear() + ' ' +
                    String(date.getHours()).padStart(2, '0') + ':' +
                    String(date.getMinutes()).padStart(2, '0');
                series.value[0].data.push(data.z_links_railab_3p)
                series.value[1].data.push(data.z_rechts_railab_3p)
                chartOptions.value.xaxis.categories.push(formattedDate)
                table.rows.push({
                    setId: data.id,
                    str_km: data.str_km,
                    time_unix: formattedDate,
                    z_links_railab_3p: data.z_links_railab_3p,
                    z_rechts_railab_3p: data.z_rechts_railab_3p
                })
                console.log(table.rows)
            })
            loaded.value = true
        })

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });

        return {
            lon, lat, pointId, trackId, tab, chartOptions, series, loaded,
            table, smallScreen, largeScreen, rowClick
        }
    }
}
</script>

<template>
    <q-page>
        <div class="q-pa-md">
            <div class="q-gutter-y-md">
                <q-card>
                    <q-tabs
                        v-model="tab"
                        dense
                        infinite
                        class="text-grey"
                        active-color="primary"
                        indicator-color="primary"
                        align="justify"
                        narrow-indicator
                    >
                        <q-tab name="pointData" label="Daten" />
                        <q-tab name="gleislage" label="Gleislagedaten" />
                        <q-tab name="zeit" label="Zeitreihe" />
                        <q-tab name="fotos" label="Fotos" />
                        <q-tab name="auftraege" label="Aufträge" />
                    </q-tabs>

                    <q-separator />

                    <q-tab-panels v-model="tab" animated>
                        <q-tab-panel name="pointData">
                            <p>Point ID: {{pointId}}</p>
                            <p>Strecke: {{trackId}}</p>
                            <p>Longitude: {{lon}}</p>
                            <p>Latitude: {{lat}}</p>
                        </q-tab-panel>

                        <q-tab-panel name="gleislage">
                            <q-table
                                v-show="!smallScreen && largeScreen"
                                class="my-sticky-header-table"
                                flat bordered
                                :rows="table.rows"
                                :columns="table.columns"
                                row-key="username"
                                :filter="table.filter"
                                @row-click="rowClick"
                            >
                                <template v-slot:body="props">
                                    <q-tr :props="props" @click="rowClick($event, props.row)">
                                        <q-td key="setId" :props="props">
                                            {{ props.row.setId }}
                                        </q-td>
                                        <q-td key="str_km" :props="props">
                                            {{ props.row.str_km }}
                                        </q-td>
                                        <q-td key="time_unix" :props="props">
                                            {{ props.row.time_unix }}
                                        </q-td>
                                        <q-td key="z_links_railab_3p" :props="props">
                                            {{ props.row.z_links_railab_3p }}
                                        </q-td>
                                        <q-td key="z_rechts_railab_3p" :props="props">
                                            {{ props.row.z_rechts_railab_3p }}
                                        </q-td>
                                    </q-tr>
                                </template>
                            </q-table>

                            <q-table
                                v-show="!largeScreen && smallScreen"
                                class="my-sticky-header-table2"
                                flat bordered
                                grid
                                grid-header
                                hide-header
                                :rows="table.rows"
                                :columns="table.columns"
                                row-key="username"
                                :filter="table.filter"
                                @row-click="rowClick"
                            >
                                <template v-slot:header="props">
                                    <q-tr :props="props">
                                        <q-th :key="props.cols[0].name" :props="props">
                                            {{ props.cols[0].label }}
                                        </q-th>
                                        <q-th :key="props.cols[2].name" :props="props">
                                            {{ props.cols[2].label }}
                                        </q-th>
                                        <q-th :key="props.cols[4].name" :props="props">
                                            {{ props.cols[4].label }}
                                        </q-th>
                                    </q-tr>
                                </template>
                                <template v-slot:item="props">
                                    <div class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3">
                                        <q-card bordered flat @click="rowClick($event, props.row)">
                                            <q-list dense>
                                                <q-item v-for="col in props.cols" :key="col.name">
                                                    <q-item-section>
                                                        <q-item-label caption>{{ col.label }}</q-item-label>
                                                    </q-item-section>
                                                    <q-item-section>
                                                        <q-item-label>{{ props.row[col.field] }}</q-item-label>
                                                    </q-item-section>
                                                </q-item>
                                            </q-list>
                                        </q-card>
                                    </div>
                                </template>
                            </q-table>
                        </q-tab-panel>

                        <q-tab-panel name="zeit">
                            <div class="outline">
                                <q-linear-progress indeterminate color="black" v-if="!loaded" class="q-mt-sm">Loading</q-linear-progress>
                                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="series"></apexchart>
                            </div>
                        </q-tab-panel>

                        <q-tab-panel name="fotos">
                            <div class="text-h6">Alarms</div>
                            Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        </q-tab-panel>

                        <q-tab-panel name="auftraege">
                            <div class="text-h6">Movies</div>
                            Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        </q-tab-panel>
                    </q-tab-panels>
                </q-card>
            </div>
        </div>
        <!--<Dataviewer/>-->
        <!--<div class="outline">
            <p>Point ID: {{pointId}}</p>
            <p>Strecke: {{trackId}}</p>
            <p>Longitude: {{lon}}</p>
            <p>Latitude: {{lat}}</p>

        </div>-->
    </q-page>
</template>

<style scoped>

</style>
