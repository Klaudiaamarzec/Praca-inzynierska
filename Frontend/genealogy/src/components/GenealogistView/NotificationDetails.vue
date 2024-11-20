<script setup>

import {useRoute, useRouter} from 'vue-router';
import {ref, computed, onMounted} from 'vue';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";
import SuccessInfo from "@/components/GenealogistView/SuccessInfo.vue";
import LogoutModal from "@/components/MainView/LogoutModal.vue";

const router = useRouter();
const route = useRoute();

const photoModal = ref(false);
const showMorePhotos = ref(false);
const showSuccess = ref(false);
let successText = ref('');
const showModal = ref(false);
let errorText = ref('');
const showLogoutModal = ref(false);
let logoutText = ref('');

const notificationID = computed(() => route.params.notificationID || {});

const notification = ref({});
const document = ref({});
const newDocument = ref({});

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

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (response.ok) {
      notification.value = await response.json();
    } else {
      console.error("Nie udało się pobrać szczegółów powiadomienia");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
};

const getDocumentDetails = async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Get/${notification.value.document}`);
    if (response.ok) {
      document.value = await response.json();
      console.log(document.value);
    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
}

const getNewDocumentDetails = async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Get/${notification.value.newDocument}`);
    if (response.ok) {
      newDocument.value = await response.json();
      console.log(newDocument.value);
    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
}

onMounted(async () => {
  await loadNotificationDetails();

  if(notification.value.document)
    await getDocumentDetails();

  if(notification.value.newDocument)
    await getNewDocumentDetails();

});

const toggleShowMorePhotos = () => {
  showMorePhotos.value = !showMorePhotos.value;
};

const formatDate = (date) => {
  const day = date.day ? String(date.day).padStart(2, '0') : '';
  const month = date.month ? String(date.month).padStart(2, '0') : '';
  const year = date.year ? date.year : '';
  return day && month ? `${day}.${month}.${year}` : month ? `${month}.${year}` : `${year}`;
};

const formatPlace = (place) => {
  const parts = [];
  if (place.country) parts.push(place.country);
  if (place.voivodeship) parts.push(place.voivodeship);
  if (place.city) parts.push(place.city);
  return parts.join(', ');
};

const formatPersonDocument = (personDocument) => {
  return `${personDocument.firstName} ${personDocument.lastName}`;
};

const showPhoto  = async () => {
  photoModal.value = true;
}

const confirmDocument = async () => {
  try {

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Notifications/ConfirmAdding/${notificationID.value}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania dokumentu";
      return;
    }

    successText.value = await response.text();
    showSuccess.value = true;

  } catch (error) {
    console.error('Błąd podczas dodawania dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>

  <section class="results-section">

    <div class="notification-detail">

      <section class="notification-detail-section">
        <div class="left-section">
          <button class="main-button" style="width: 100%" @click="goBack">Powrót</button>
        </div>
      </section>

      <section class="notification">

        <div class="notification-detail-header">
          <p style="font-size: 18px">{{ notification.title }}</p>
          <p>{{ notification.date }}</p>
        </div>

        <div class="notification-detail-context">
          <p style="font-size: 13px">{{ notification.context }}</p>
          <p>od: {{ notification.user && notification.user.username }}</p>
        </div>

        <div v-if="!notification.newDocument && notification.document" class="end-section">
          <button class="add-button" type="submit" @click="confirmDocument">Zatwierdź</button>
        </div>

        <div class="separator"></div>

        <section v-if="notification.document && !document.path" class="advanced-section-adding">

          <div class="detail">
            <strong>Tytuł: </strong> {{ document.title }}
          </div>

          <div v-if="document.description" class="detail">
            <strong>Tytuł: </strong> {{ document.description }}
          </div>

          <div class="detail">
            <strong>Rodzaj: </strong> {{ document.type.name }}
          </div>

          <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
            <strong>Miejsce: </strong> {{ formatPlace(document.place) }}
          </div>

          <div v-if="document.startDate || document.endDate" class="detail">
            <strong>Przedział dat: </strong> {{ document.startDate }} - {{ document.endDate }}
          </div>

          <div v-if="document.date" class="detail">
            <strong>Data: </strong> {{ formatDate(document.date) }}
          </div>

          <div v-if="document.peopleDocuments && document.peopleDocuments.length > 0" class="detail-section2">
            <strong style="padding-left: 15px">Osoby występujące w dokumencie:</strong>
            <ul class="people-list">
              <li v-for="(personDocument, index) in document.peopleDocuments" :key="index" class="person" @click="viewPersonDetails(personDocument.id)">
                <a href="#">
                  {{ formatPersonDocument(personDocument) }}
                  <span v-if="personDocument.comment" class="comment"> ({{ personDocument.comment }})</span>
                </a>
              </li>
            </ul>
          </div>

          <div v-if="document.localization" >

            <div v-if="document.localization.urls && document.localization.urls.length > 0" class="detail-section3">
              <strong class="address-url">Adresy URL:</strong>
              <ul class="url-list">
                <li v-for="(urlObject, index) in document.localization.urls" :key="index">
                  - <a :href="urlObject.url" target="_blank" rel="noopener noreferrer" class="urls">{{ urlObject.url }}</a>
                  <div v-if="urlObject.comment" class="comment">({{ urlObject.comment }})</div>
                </li>
              </ul>
            </div>

            <div v-if="document.localization.physicalLocations && document.localization.physicalLocations.length > 0" class="detail-section2">
              <strong style="padding-left: 15px">Fizyczne lokalizacje:</strong>
              <ul class="physical-locations-list">
                <li v-for="(location, index) in document.localization.physicalLocations" :key="index" class="physical-location-item">
                  <div v-if="location.type">
                    <strong>Rodzaj:</strong> {{ location.type }}<br />
                  </div>
                  <div>
                    <strong>Data dodania:</strong> {{ location.date }}<br />
                  </div>
                  <div>
                    <strong>Oryginalny:</strong> {{ location.isOriginal ? 'Tak' : 'Nie' }}<br />
                  </div>
                  <div v-if="location.condition">
                    <strong>Kondycja:</strong> {{ location.condition }}<br />
                  </div>
                  <div v-if="location.description">
                    <strong>Opis:</strong> {{ location.description }}<br />
                  </div>
                  <div v-if="location.localaddress">
                    <strong>Adres: </strong>
                    <span v-if="location.localaddress.country">{{ location.localaddress.country }}</span>
                    <span v-if="location.localaddress.voivodeship">, {{ location.localaddress.voivodeship }}</span>
                    <span v-if="location.localaddress.community">, {{ location.localaddress.community }}</span>
                    <span v-if="location.localaddress.city">, {{ location.localaddress.city }}</span>
                    <span v-if="location.localaddress.address">, {{ location.localaddress.address }}</span>
                    <span v-if="location.localaddress.postalCode">, {{ location.localaddress.postalCode }}</span>
                    <br />
                  </div>
                </li>
              </ul>
            </div>

          </div>

        </section>

      </section>

    </div>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <SuccessInfo v-if="showSuccess" :showModal="showSuccess" :successDetails="successText" redirectTo="/Notifications" @close="showSuccess = false" />
  <LogoutModal v-if="showLogoutModal" :showModal="showLogoutModal" :errorDetails="logoutText" @close="showLogoutModal = false" />

</template>

<style scoped>

.add-button {
  width: 6%;
}

</style>
