<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>{{ successDetails }}</h2>
      </div>
      <p>Możesz teraz dodać zdjęcie i osoby do dokumentu oraz lokalizację (miejsce przechowywania)</p> <!-- Wyświetl szczegóły błędu -->
      <button class="button-modal">Dodaj zdjęcie</button>
      <button class="button-modal">Dodaj osoby</button>
      <button class="button-modal">Dodaj lokalizacje</button>
      <button class="button-modal" @click="closeModal">POMIŃ</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

defineProps(['showModal', 'successDetails']);
const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};
</script>

<style scoped>

</style>
