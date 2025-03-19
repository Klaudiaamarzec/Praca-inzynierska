<script setup>

import Header from "@/components/MainView/Header.vue";

import { ref, onMounted } from 'vue';

// Zmienna props token przekazywana z routera
const props = defineProps({
  token: String,
});

const newPassword = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');
const successMessage = ref('');

onMounted(() => {
  if (!props.token) {
    errorMessage.value = 'Brak tokenu w linku.';
  }
});

const handleResetPassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = 'Hasła nie są takie same.';
    return;
  }

  try {
    // Używamy fetch i wysyłamy parametr token w URL oraz hasło w ciele zapytania.
    const response = await fetch(`http://127.0.0.1:8080/Auth/ResetPassword?token=${props.token}&newPassword=${newPassword.value}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      // Ciało zapytania teraz jest puste, bo token i password są przekazywane przez URL
    });

    if (response.ok) {
      successMessage.value = 'Hasło zostało zaktualizowane pomyślnie.';
    } else {
      const message = await response.text();
      errorMessage.value = message || 'Wystąpił problem z resetowaniem hasła.';
    }
  } catch (error) {
    console.error('Błąd podczas resetowania hasła:', error);
    errorMessage.value = 'Wystąpił błąd. Spróbuj ponownie.';
  }
};

</script>

<template>
  <Header/>

  <section class="login-form-container">
    <div class="login-container">
      <h2>Resetowanie hasła</h2>
      <form @submit.prevent="handleResetPassword">
        <div class="login-section">
          <label for="newPassword">Nowe hasło:</label>
          <input class="main-input" type="password" id="newPassword" v-model="newPassword" required />
        </div>
        <div class="login-section">
          <label for="confirmPassword">Potwierdź hasło:</label>
          <input class="main-input" type="password" id="confirmPassword" v-model="confirmPassword" required />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit" class="send-button">Zresetuj hasło</button>
        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      </form>
    </div>
  </section>

</template>

<style scoped>

.error-message {
  color: red;
  font-size: 14px;
}

.success-message {
  color: green;
  font-size: 14px;
}

</style>
