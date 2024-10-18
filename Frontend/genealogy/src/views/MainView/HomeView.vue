<template>
  <div>
    <Header @open-login-modal="showLoginModal" />
    <LoginModal v-if="isLoginModalVisible" @close="hideLoginModal" />
    <!-- Tutaj tylko nagłówek, bez dodatkowych elementów -->
  </div>
</template>

<script setup>
import Header from '../../components/MainView/Header.vue';
import LoginModal from '../../components/MainView/LoginModal.vue';

import { ref } from 'vue';

// Stan modal logowania
const isLoginModalVisible = ref(false);

// Funkcje otwierające i zamykające modal
const showLoginModal = () => {
  isLoginModalVisible.value = true;
};

const hideLoginModal = () => {
  isLoginModalVisible.value = false;
};
</script>

<style scoped>
/* Jeśli nie potrzebujesz dodatkowych stylów, można to zostawić puste */
</style>
