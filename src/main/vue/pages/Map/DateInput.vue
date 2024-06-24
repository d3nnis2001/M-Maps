<script setup>
import {ref, watch} from 'vue';

const props = defineProps({
    modelValue: {
        type: String,
        required: true
    }
});

const emit = defineEmits(['update:modelValue']);

const date = ref(props.modelValue);

// Watcher to emit updates for date changes
watch(date, (newValue) => {
    emit('update:modelValue', newValue);
});

// Additional reference for time, if needed in future
const time = ref('00:00');

</script>

<template>
    <q-input filled v-model="date">
        <template v-slot:prepend>
            <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                    <q-date v-model="date" mask="YYYY-MM-DD">
                        <div class="row items-center justify-end">
                            <q-btn v-close-popup label="Close" color="primary" flat/>
                        </div>
                    </q-date>
                </q-popup-proxy>
            </q-icon>
        </template>
    </q-input>
</template>

<style scoped>
</style>
