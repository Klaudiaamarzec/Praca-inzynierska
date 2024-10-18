<!-- LoginModal.vue -->
<template>
  <div class="modal-overlay" @click="close">
    <div class="modal-content" @click.stop>
      <h2>Logowanie</h2>
      <form @submit.prevent="handleLogin">
        <div>
          <label for="email">Email:</label>
          <input type="email" id="email" v-model="email" required />
        </div>
        <div>
          <label for="password">Hasło:</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <button type="submit">Zaloguj się</button>
        <button type="button" @click="close">Anuluj</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const email = ref('');
const password = ref('');

const handleLogin = () => {
  // Logika logowania (np. zapytanie do API)
};

const close = () => {
  // Logika do zamykania modalu
};
</script>

<style scoped>

h2 {
  text-align: center;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: rgba(248, 244, 241, 0.88);
  padding: 20px;
  border-radius: 5px;
  width: 800px;
  height: 500px;
}
</style>
