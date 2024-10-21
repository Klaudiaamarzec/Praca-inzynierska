<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  results: Array
});

// import { defineEmits } from 'vue';
//
// const emit = defineEmits(['backToSearch']); // Definiowanie emitowanego zdarzenia
//
// // Jeśli masz jakiś przycisk lub akcję, która powinna emitować `backToSearch`:
// const goBack = () => {
//   emit('backToSearch'); // Emitowanie zdarzenia, kiedy użytkownik chce wrócić do wyszukiwarki
// };
</script>

<template>
<!--  <section class="results">-->
<!--    <h2>Wyniki wyszukiwania:</h2>-->
<!--    <ul>-->
<!--      <li v-for="(result, index) in results" :key="index">-->
<!--        {{ result | json }} &lt;!&ndash; Możesz dopasować do struktury wyników &ndash;&gt;-->
<!--      </li>-->
<!--    </ul>-->
<!--    <button @click="$emit('backToSearch')">Wróć do wyszukiwarki</button>-->
<!--  </section>-->
</template>

<style scoped>

</style>
