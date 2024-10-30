<script setup>

import { ref, onMounted } from 'vue';

const notifications = ref([]); // Używamy ref do przechowywania powiadomień

onMounted(async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch('http://localhost:8080/API/Notifications/All', {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });
    if (response.ok) {
      notifications.value = await response.json(); // Przypisanie danych do zmiennej
    } else {
      console.error('Błąd przy pobieraniu powiadomień:', response.status);
    }
  } catch (error) {
    console.error('Wystąpił błąd:', error);
  }
});

</script>

<template>

  <section class="results-container">
    <section class="result-list">
      <div class="list-item" v-for="notification in notifications" :key="notification.id">
        <div class="title">{{ notification.title }}</div>
        <div class="details">
          <p>
            <span class="label">Data:</span>
            <span class="value">{{ notification.date }}</span>
          </p>
          <p>
            <span class="label">Kontekst:</span>
            <span class="value">{{ notification.context }}</span>
          </p>
          <p v-if="notification.displayed">
            <span class="label">Status:</span>
            <span class="value">Wyświetlono</span>
          </p>
        </div>
      </div>
    </section>
  </section>

</template>

<style scoped>

.result-list {
  margin: 0 auto;
  padding-top: 35px;
}

</style>
