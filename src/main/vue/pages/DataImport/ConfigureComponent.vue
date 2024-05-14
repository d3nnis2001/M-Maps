<script setup>
import {onBeforeMount, onUnmounted, ref} from "vue";
import {getGleisLageRange, postGleisLageRange} from "@/main/vue/api/dataimport";

onBeforeMount(async () => {
    r.value = await getGleisLageRange();
})

onUnmounted(async () => {
    await postGleisLageRange(r.value)
})

const option = ref("")

const options = ["Gleislage-Daten", "ROS-Bag"]

const columns = [
    { name: '_smaller80_', label: '0 - 80', align: 'left', field: '_smaller80_' },
    { name: '_80to120_', label: '80 - 120', align: 'left', field: '_80to120_' },
    { name: '_120to160_', label: '120 - 160', align: 'left', field: '_120to160_' },
    { name: '_160to230_', label: '160 - 230', align: 'left', field: '_160to230_' },
    { name: '_greater230_', label: '> 230', align: 'left', field: '_greater230_' },
]

const r = ref(null)

/*
[[{_smaller80_: 12, _80to120_: 10, _120to160_: 8, _160to230_: 6, _greater230_: 5}],
    [{_smaller80_: 15, _80to120_: 13, _120to160_: 11, _160to230_: 9, _greater230_: 7}],
    [{_smaller80_: 21, _80to120_: 17, _120to160_: 14, _160to230_: 11, _greater230_: 9}]]
 */

const v = ["SR-A", "SR-100", "SR-lim"]

const errorRange = ref(false)
const errorMessage = ref("")

const RangeValidation = (index, idx) => {
    return function (newValue, oldValue) {
        if (index === 0) {
            if(newValue > r.value[1][0][idx]) {
                errorRange.value = true
                errorMessage.value = "Wert muss kleiner als " + r.value[1][0][idx] + " sein!"
                return false
            }
        }
        if(index === 1) {
            if(newValue < r.value[0][0][idx] || newValue > r.value[2][0][idx]) {
                errorRange.value = true
                errorMessage.value = "Wert muss größer als " + r.value[0][0][idx] + " und kleiner als " + r.value[2][0][idx] + " sein!"
                return false
            }
        }
        if(index === 2) {
            if(newValue < r.value[1][0][idx]) {
                errorRange.value = true
                errorMessage.value = "Wert muss größer als " + r.value[1][0][idx] + " sein!"
                return false
            }
        }
        errorRange.value = false
        errorMessage.value = ''
        return true
    }

}
const reset = () => {
    errorRange.value = false
    errorMessage.value = ""
}

</script>

<template>
    <div class="q-pa-md">
        <div class="q-py-md">
            <q-select outlined v-model="option" :options="options" label="Datentyp" />
        </div>

        <div v-if="option === options[0]">
            <div class="q-py-md" v-for="(a,index) in v">
                <q-table
                    :title="a"
                    class="my-sticky-header-table"
                    :rows="r.valueOf()[index]"
                    :columns="columns"
                    flat
                    hide-bottom
                    bordered
                    wrap-cells
                >
                    <template v-slot:body="props">
                        <q-tr :props="props">
                            <q-td v-for="(col, idx) in props.cols" :key="col.name">
                                {{  props.row[col.name] }}
                                <q-popup-edit

                                    v-model="props.row[col.name]"
                                    title="Update"
                                    label-set="Save"
                                    label-cancel="Close"
                                    buttons
                                    :validate="RangeValidation(index, col.name)"
                                    @hide="reset"
                                    v-slot="scope">
                                    <q-input
                                        type="number"
                                        v-model.number="scope.value"
                                        :error="errorRange"
                                        :error-message="errorMessage"
                                        dense
                                        @keyup.enter="scope.set"
                                        autofocus />
                                </q-popup-edit>
                            </q-td>
                        </q-tr>
                    </template>
                </q-table>
            </div>
        </div>


    </div>
</template>

<style lang="sass">
.my-sticky-header-table
    /* height or max-height is important */
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        /* bg color is important for th; just specify one */
        background-color: $blue-grey-5
    thead tr th
        position: sticky
        z-index: 0
    thead tr:first-child th
        top: 0
    /* this is when the loading indicator appears */
    &.q-table--loading thead tr:last-child th
        /* height of all previous header rows */
        top: 48px
    /* prevent scrolling behind sticky top row on focus */
    tbody
        /* height of all previous header rows */
        scroll-margin-top: 48px
</style>
