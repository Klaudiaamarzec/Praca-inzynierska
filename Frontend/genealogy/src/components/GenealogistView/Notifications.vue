<script setup>

import { ref, onMounted } from 'vue';
import {useRouter} from "vue-router";

const notifications = ref([]);
const parameter = ref("");
const router = useRouter();


onMounted(async () => {
  await fetchAllNotification();
});

const decodeJWT = (token) => {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
      .join('')
  );
  return JSON.parse(jsonPayload);
};

const fetchAllNotification = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch('http://localhost:8080/API/Notifications/All', {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });

    notifications.value = await response.json();

  } catch (error) {
    console.error('Błąd podczas pobierania powiadomień:', error);
  }
};

const searchNotification = async () => {
  if(parameter.value === "") {
    await fetchAllNotification();
  }
  else {
    try {
      const token = localStorage.getItem('jwtToken');
      const response = await fetch(`http://localhost:8080/API/Notifications/Search/${parameter.value}`, {
        headers: {
          'accept': '*/*',
          'Authorization': `Bearer ${token}`,
        },
      });
      notifications.value = await response.json();
    } catch (error) {
      console.error('Błąd podczas wyszukiwania powiadomień:', error);
    }
  }
}

const notDisplayedNotification = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(`http://localhost:8080/API/Notifications/AllNotDisplayed`, {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });
    notifications.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania powiadomień:', error);
  }
}

const editingNotifications = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(`http://localhost:8080/API/Notifications/AboutEditing`, {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });
    notifications.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania powiadomień:', error);
  }
}

const addingNotifications = async () => {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(`http://localhost:8080/API/Notifications/AboutAdding`, {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${token}`,
      },
    });
    notifications.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania powiadomień:', error);
  }
}

const viewNotificationDetails = (notificationID) => {
  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'NotificationDetails', params: { notificationID } });
  } else if (userRole === 'user') {
    console.log("Brak uprawnień do wykonania tego zapytania!");
  } else {
    console.log("Nieznana rola użytkownika!");
  }
}

</script>

<template>

  <section class="results-section">
    <div class="results-container">

      <div class="filterMenu">
        <input class="filter-input" type="text" placeholder="Znajdź" id="parameter" v-model="parameter" @keyup.enter="searchNotification" required />
        <button class="search-button" @click="searchNotification">Szukaj</button>

        <div class="separator-menu"></div>

        <button class="search-button" @click="notDisplayedNotification">Niewyświetlone</button>
        <button class="search-button" @click="editingNotifications">Edytowanie dokumentów</button>
        <button class="search-button" @click="addingNotifications">Dodawanie dokumentów</button>
      </div>

      <div class="search-results">

        <section class="notification-header">
          <p>Temat</p>
          <p>Data</p>
          <p>Użytkownik</p>
        </section>

        <div class="result-list">
          <div class="notification-item" v-for="notification in notifications" :key="notification.id" @click="viewNotificationDetails(notification.id)">
            <p>{{ notification.title }}</p>
            <p class="value">{{ notification.date }}</p>
            <p class="value">{{ notification.user.username }}</p>
          </div>
        </div>
      </div>

    </div>
  </section>

</template>

<style scoped>

.result-list {
  margin: 0 auto;
  padding-top: 20px;
}

.search-results {
  width: 100%;
}

.search-button {
  width: 75%;
}

</style>
