<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Rejestracja przebiegła pomyślnie</h2>
      </div>
      <p>Teraz musisz się zalogować.</p>
      <button @click="goToLogin">OK</button>
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
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

button {
  padding: 10px 15px;
  background-color: var(--brown);
  color: #000000;
  border: none;
  font-weight: bold;
  border-radius: 30px;
  cursor: pointer;
}

button:hover {
  background-color: var(--dark-brown);
  color: #eeebe9;
}
</style>
