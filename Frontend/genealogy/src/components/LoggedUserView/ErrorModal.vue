<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Wystąpił błąd podczas dodawania dokumentu</h2>
      </div>
      <p>{{ errorDetails }}</p> <!-- Wyświetl szczegóły błędu -->
      <button class="button-modal" @click="closeModal">OK</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

defineProps(['showModal', 'errorDetails']);
const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};
</script>

<style scoped>

</style>
