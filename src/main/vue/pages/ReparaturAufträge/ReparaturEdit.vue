<script>

import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { getDetailsByID } from "@/main/vue/api/reparatur";

export default {
    setup() {
        const route = useRoute();
        const repairDetails = ref({});
        const ticked = ref([])
        const options = ref([])

        onMounted(async () => {
            const name = route.params.name;
            repairDetails.value = await getDetailsByID(name);
            for (let i = 0;i<repairDetails.value.checklist.items.length;i++) {
                options.value.push({label: repairDetails.value.checklist.items[i], value: i})
            }
        });

        function saveData() {
            console.log(ticked.value)
            const response = updateValuesById()
        }
        return {
            repairDetails,
            ticked,
            options,
            saveData
        };
    }
}

</script>

<template>
    <div class="outline">
        <div class="outer-layer">
            <div>
                <p>ID: {{ repairDetails.id }}</p>
                <p>Zeitraum: {{ repairDetails.from }}</p>
                <p>Freigabeberechtigter: {{ repairDetails.freigabeberechtigter }}</p>
                <p>Status: {{ repairDetails.status }}</p>
            </div>
            <h5>Checkliste</h5>
            <div class="outline-nomar">
                <div class="q-pa-md">
                    <q-option-group
                        :options="options"
                        type="checkbox"
                        v-model="ticked"
                    />
                </div>
            </div>
            <q-btn label="Speichern" color="grey" @click="saveData" class=""></q-btn>
        </div>
    </div>
</template>

<style scoped>

.outer-layer {
    margin: 20px;
}
.outline {
    border: 1px solid black;
    padding: 10px;
    margin: 20px;
}
.outline-nomar {
    border: 1px solid black;
    padding: 10px;
    margin-bottom: 20px;
}
</style>
