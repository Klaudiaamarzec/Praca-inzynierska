<script setup>

import {useRoute, useRouter} from 'vue-router';
import {ref, computed, onMounted} from 'vue';

const router = useRouter();
const route = useRoute();

// Pobierz dokument z danych przekazanych przez route
const notificationID = computed(() => route.params.notificationID || {});

const notification = ref({});

const goBack = () => {
  router.back();
};

const loadNotificationDetails = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(`http://localhost:8080/API/Notifications/${notificationID.value}`, {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });
    if (response.ok) {
      notification.value = await response.json();
      console.log(notification.value);
    } else {
      console.error("Nie udało się pobrać szczegółów powiadomienia");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
};

onMounted(() => {
  loadNotificationDetails();
});

</script>

<template>

  <section class="results-section">
    <div class="results-container">

      <div class="filterMenu">
        <input class="filter-input" type="text" placeholder="Znajdź" id="parameter" v-model="parameter" @keyup.enter="searchNotification" required />
        <button class="search-button" @click="searchNotification">Szukaj</button>
        <button class="search-button" @click="notDisplayedNotification">Niewyświetlone</button>
        <button class="search-button" @click="editingNotifications">Edytowanie dokumentów</button>
        <button class="search-button" @click="addingNotifications">Dodawanie dokumentów</button>
      </div>

      <div class="search-results">

<!--        <section class="notification-header">-->
<!--          <p>Temat</p>-->
<!--          <p>Data</p>-->
<!--          <p>Użytkownik</p>-->
<!--        </section>-->

        <section class="notification-section">
          <div class="left-section">
            <button class="search-button" style="width: 100%" @click="goBack">Powrót</button>
          </div>
        </section>

        <div class="notification-item">
          <p>{{ notification.title }}</p>
          <p class="value">{{ notification.date }}</p>
          <p class="value">{{ notification.user.username }}</p>
        </div>

      </div>

    </div>
  </section>

</template>

<style scoped>

.search-results {
  width: 100%;
}

.search-button {
  width: 75%;
}

</style>
