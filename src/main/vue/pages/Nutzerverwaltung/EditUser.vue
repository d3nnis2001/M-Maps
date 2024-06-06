<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from "@/main/vue/router";

export default {
    setup () {
        const route = useRoute();
        const roles = ref([]);

        onMounted( async  () => {
            const username = route.params.username;
        })

        const abort = () => {
            router.push(`/admin`);
        };

        return {
            abort,
            group: ref([]),
            options: [
                { label: 'Admin', value: 'admin' },
                { label: 'Datenverwalter', value: 'datenverwalter', },
                { label: 'Bearbeiter', value: 'bearbeiter', },
                { label: 'Prüfer', value: 'prüfer', },
            ]
        }
    }
}
</script>

<template>
    <div class="q-pa-md">
        <q-option-group
            :options="options"
            type="checkbox"
            v-model="group"
        />
        <q-btn class="handleButton" style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Abbrechen" color="primary" @click=abort></q-btn>
    </div>
</template>


<style scoped>

</style>

