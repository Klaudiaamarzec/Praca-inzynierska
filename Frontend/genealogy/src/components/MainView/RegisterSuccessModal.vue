<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Rejestracja przebiegła pomyślnie</h2>
      </div>
      <p>Teraz musisz się zalogować.</p>
      <button class="main-button" @click="goToLogin">OK</button>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';
defineProps(['showModal']);
const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
  window.location.href = '/login';
};

const goToLogin = () => {
  closeModal();
  window.location.href = '/login';
};
</script>

<style scoped>

</style>
