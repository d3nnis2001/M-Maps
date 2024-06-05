<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

export default {
    setup () {
        const route = useRoute();
        const roles = ref([]);

        onMounted( async  () => {
            const username = route.params.username;
        })

        return {
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
    </div>
</template>


<style scoped>

</style>

