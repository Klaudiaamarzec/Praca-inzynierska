<script setup>

import { useRouter, useRoute } from 'vue-router';
import { ref, computed, onMounted } from 'vue';

const router = useRouter();
const route = useRoute();

// Pobierz dokument z danych przekazanych przez route
const documentID = computed(() => route.params.documentID || {});

const document = ref({});

onMounted(async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Get/${documentID.value}`);
    if (response.ok) {
      document.value = await response.json();
    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
});

const goBack = () => {
  router.back();
};

// Funkcje formatowania
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

</script>

<template>

  <section class="main-section">
    <section v-if="Object.keys(document).length" class="browser">

      <div class="browser-header">
        <p>{{ document.title }}</p>
      </div>

      <div class="separator"></div>

      <section class="header-section">
        <div class="left-section">
          <a @click="goBack"><p>Powrót</p></a>
        </div>
        <div class="right-section">
          <button class="button-modal" @click="goBack">Edytuj</button>
        </div>
      </section>

      <section v-if="!document.path" class="advanced-section-adding">

        <div class="detail">
          <strong>Rodzaj:</strong> {{ document.type.name }}
        </div>

        <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
          <strong>Miejsce:</strong> {{ formatPlace(document.place) }}
        </div>

        <div v-if="document.startDate || document.endDate" class="detail">
          <strong>Przedział dat:</strong> {{ document.startDate }} - {{ document.endDate }}
        </div>
        <!--        &lt;!&ndash;      <div class="detail">&ndash;&gt;-->
        <!--        &lt;!&ndash;        <strong>Data:</strong> {{ formatDate(document.date) }}&ndash;&gt;-->
        <!--        &lt;!&ndash;      </div>&ndash;&gt;-->

        <div v-if="document.peopleDocuments && document.peopleDocuments.length > 0" class="detail">
          <strong>Osoby występujące w dokumencie:</strong>
          <ul class="people-list">
            <li v-for="(personDocument, index) in document.peopleDocuments" :key="index">
              - {{ formatPersonDocument(personDocument) }}
              <span v-if="personDocument.comment" class="comment"> ({{ personDocument.comment }})</span>
            </li>
          </ul>
        </div>

        <div v-if="document.localization" >

          <div v-if="document.localization.urls && document.localization.urls.length > 0" class="detail">
            <strong>Adresy URL:</strong>
            <ul class="url-list">
              <li v-for="(urlObject, index) in document.localization.urls" :key="index">
                - <a :href="urlObject.url" target="_blank" rel="noopener noreferrer" class="urls">{{ urlObject.url }}</a>
                <div v-if="urlObject.comment" class="comment">({{ urlObject.comment }})</div>
              </li>
            </ul>
          </div>

          <div v-if="document.localization.physicalLocations && document.localization.physicalLocations.length > 0" class="detail">
            <strong>Fizyczne lokalizacje:</strong>
            <ul class="physical-locations-list">
              <li v-for="(location, index) in document.localization.physicalLocations" :key="index" class="physical-location-item">
                <div v-if="location.type">
                  <strong>Rodzaj:</strong> {{ location.type }}<br />
                </div>
                <strong>Data dodania:</strong> {{ location.date }}<br />
                <strong>Oryginalny:</strong> {{ location.isOriginal ? 'Tak' : 'Nie' }}<br />
                <div v-if="location.condition">
                  <strong>Kondycja:</strong> {{ location.condition }}<br />
                </div>
                <div v-if="location.description">
                  <strong>Opis:</strong> {{ location.description }}<br />
                </div>
                <div v-if="location.localaddress">
                  <strong>Adres: </strong>
                  <span v-if="location.localaddress.country">{{ location.localaddress.country }}</span>
                  <span v-if="location.localaddress.voivodeship">{{ location.localaddress.voivodeship }}</span>
                  <span v-if="location.localaddress.community">{{ location.localaddress.community }}</span>
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

      <section v-if="document.path" class="content-details">

        <div class="left-site-details">

          <section class="advanced-section-adding">

            <div class="detail">
              <strong>Rodzaj:</strong> {{ document.type.name }}
            </div>

            <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
              <strong>Miejsce:</strong> {{ formatPlace(document.place) }}
            </div>

            <div v-if="document.startDate || document.endDate" class="detail">
              <strong>Przedział dat:</strong> {{ document.startDate }} - {{ document.endDate }}
            </div>
            <!--        &lt;!&ndash;      <div class="detail">&ndash;&gt;-->
            <!--        &lt;!&ndash;        <strong>Data:</strong> {{ formatDate(document.date) }}&ndash;&gt;-->
            <!--        &lt;!&ndash;      </div>&ndash;&gt;-->

            <div v-if="document.peopleDocuments && document.peopleDocuments.length > 0" class="detail">
              <strong>Osoby występujące w dokumencie:</strong>
              <ul class="people-list">
                <li v-for="(personDocument, index) in document.peopleDocuments" :key="index">
                  - {{ formatPersonDocument(personDocument) }}
                  <span v-if="personDocument.comment" class="comment"> ({{ personDocument.comment }})</span>
                </li>
              </ul>
            </div>

            <div v-if="document.localization" >

              <div v-if="document.localization.urls && document.localization.urls.length > 0" class="detail">
                <strong>Adresy URL:</strong>
                <ul class="url-list">
                  <li v-for="(urlObject, index) in document.localization.urls" :key="index">
                    - <a :href="urlObject.url" target="_blank" rel="noopener noreferrer" class="urls">{{ urlObject.url }}</a>
                    <div v-if="urlObject.comment" class="comment">({{ urlObject.comment }})</div>
                  </li>
                </ul>
              </div>

              <div v-if="document.localization.physicalLocations && document.localization.physicalLocations.length > 0" class="detail">
                <strong>Fizyczne lokalizacje:</strong>
                <ul class="physical-locations-list">
                  <li v-for="(location, index) in document.localization.physicalLocations" :key="index" class="physical-location-item">
                    <div v-if="location.type">
                      <strong>Rodzaj:</strong> {{ location.type }}<br />
                    </div>
                    <strong>Data dodania:</strong> {{ location.date }}<br />
                    <strong>Oryginalny:</strong> {{ location.isOriginal ? 'Tak' : 'Nie' }}<br />
                    <div v-if="location.condition">
                      <strong>Kondycja:</strong> {{ location.condition }}<br />
                    </div>
                    <div v-if="location.description">
                      <strong>Opis:</strong> {{ location.description }}<br />
                    </div>
                    <div v-if="location.localaddress">
                      <strong>Adres: </strong>
                      <span v-if="location.localaddress.country">{{ location.localaddress.country }}</span>
                      <span v-if="location.localaddress.voivodeship">{{ location.localaddress.voivodeship }}</span>
                      <span v-if="location.localaddress.community">{{ location.localaddress.community }}</span>
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

        </div>

        <div v-if="document.path" class="right-site-details">
          <img :src="`/${document.path}`" alt="Zdjęcie dokumentu" class="document-image"/>
        </div>

      </section>

    </section>
  </section>

</template>

<style scoped>

.main-section {
  height: 100%;
}

.header-section {
  width: 100%;
  padding: 0 20px 20px;
}

.button-modal {
  width: 100%;
}

a {
  color: var(--dark-brown);
}

.browser {
  margin-top: 10px;
  width: 75%;
}

button {
  padding: 8px 16px;
}

</style>
