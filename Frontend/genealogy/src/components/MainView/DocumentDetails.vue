<script setup>

import { useRouter, useRoute } from 'vue-router';
import { ref, computed, onMounted } from 'vue';
import PhotoDetails from "@/components/LoggedUserView/PhotoDetails.vue";
import MapModal from "@/components/MainView/MapModal.vue";

const router = useRouter();
const route = useRoute();

const photoModal = ref(false);
const showMorePhotos = ref(false);

const showMap = ref(false);

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
  router.push({ name: 'Documents' });
};

const toggleShowMorePhotos = () => {
  showMorePhotos.value = !showMorePhotos.value;
};

const showMapModal = async () => {
  showMap.value = true;
}

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
  if (place.community) parts.push(place.community);
  if (place.city) parts.push(place.city);
  return parts.join(', ');
};

const formatPersonDocument = (personDocument) => {
  return `${personDocument.firstName} ${personDocument.lastName}`;
};

const filteredPeopleDocuments = computed(() => {
  return document.value.peopleDocuments.filter(person => person.x !== null && person.y !== null);
});

const showPhoto  = async () => {
  photoModal.value = true;
}

const convertLatitude = (latitude) => {
  const convertToDMS = (coordinate) => {
    const direction = coordinate >= 0 ? 'N' : 'S';
    const absCoord = Math.abs(coordinate);
    const degrees = Math.floor(absCoord);
    const minutes = Math.floor((absCoord - degrees) * 60);
    const seconds = ((absCoord - degrees - minutes / 60) * 3600).toFixed(2);

    return `${degrees}° ${minutes}' ${seconds}" ${direction}`;
  };

  return convertToDMS(latitude);
}

const convertLongitude = (longitude) => {
  const convertToDMS = (coordinate) => {
    const direction = coordinate >= 0 ? 'E' : 'W';
    const absCoord = Math.abs(coordinate);
    const degrees = Math.floor(absCoord);
    const minutes = Math.floor((absCoord - degrees) * 60);
    const seconds = ((absCoord - degrees - minutes / 60) * 3600).toFixed(2);

    return `${degrees}° ${minutes}' ${seconds}" ${direction}`;
  };

  return convertToDMS(longitude);
}

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
          <button class="button-modal" @click="goBack">Powrót</button>
        </div>
      </section>

      <section v-if="!document.path" class="advanced-section-adding">

        <div v-if="document.type.id === 1">
          <p style="color: var(--dark-brown); padding-bottom: 8px">Dodaj zdjęcie dokumentu, aby móc dodawć je do innych dokumentów !</p>
        </div>

        <div class="detail">
          <strong>Rodzaj: </strong> {{ document.type.name }}
        </div>

        <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
          <strong>Miejsce: </strong>
          <div>
            {{ formatPlace(document.place) }}
            <p v-if="document.place.address && document.place.postalCode"> {{document.place.address}}, {{document.place.postalCode}}</p>
            <p v-if="document.place.address && !document.place.postalCode"> {{document.place.address}}</p>
            <p v-if="document.place.postalCode && !document.place.address"> Kod pocztowy: {{document.place.postalCode}}</p>
            <p v-if="document.place.latitude"> Szerokość geograficzna: {{ convertLatitude(document.place.latitude) }}</p>
            <p v-if="document.place.longitude"> Długość geograficzna: {{ convertLongitude(document.place.longitude) }}</p>
            <p v-if="document.place.parish"> Przynależność parafialna: {{document.place.parish}}</p>
            <p v-if="document.place.secular"> Przynależność świecka: {{document.place.secular}}</p>

            <div v-if="document.place.latitude && document.place.longitude" class="end-section">
              <button  class="advanced-search" @click="showMapModal">Pokaż na mapie</button>
            </div>

          </div>
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

        <div v-if="document.additionalFields">
          <div v-for="(fieldValue, fieldName) in document.additionalFields" :key="fieldName" class="additional-detail">
            <strong>{{ fieldName }}: </strong> {{ fieldValue }}
          </div>
        </div>

      </section>

      <section v-if="document.path" class="content-details">

        <div class="left-site-details">

          <section class="advanced-section-adding">

            <div class="detail-small">
              <strong>Rodzaj: </strong> {{ document.type.name }}
            </div>

            <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail-small">
              <strong>Miejsce: </strong>
              <div>
                {{ formatPlace(document.place) }}
                <p v-if="document.place.address && document.place.postalCode"> {{document.place.address}}, {{document.place.postalCode}}</p>
                <p v-if="document.place.address && !document.place.postalCode"> {{document.place.address}}</p>
                <p v-if="document.place.postalCode && !document.place.address"> Kod pocztowy: {{document.place.postalCode}}</p>
                <p v-if="document.place.latitude"> Szerokość geograficzna: {{ convertLatitude(document.place.latitude) }}</p>
                <p v-if="document.place.longitude"> Długość geograficzna: {{ convertLongitude(document.place.longitude) }}</p>
                <p v-if="document.place.parish"> Przynależność parafialna: {{document.place.parish}}</p>
                <p v-if="document.place.secular"> Przynależność świecka: {{document.place.secular}}</p>

                <button v-if="document.place.latitude && document.place.longitude" class="button-modal-small" @click="showMapModal">
                  Pokaż na mapie
                </button>

              </div>
            </div>

            <div v-if="document.startDate || document.endDate" class="detail-small">
              <strong>Przedział dat: </strong> {{ document.startDate }} - {{ document.endDate }}
            </div>

            <div v-if="document.date" class="detail-small">
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

            <div v-if="document.additionalFields">
              <div v-for="(fieldValue, fieldName) in document.additionalFields" :key="fieldName" class="detail-small">
                <strong>{{ fieldName }}: </strong> {{ fieldValue }}
              </div>
            </div>

          </section>

        </div>

        <div v-if="document.path" class="right-site-details">
          <img :src="`/${document.path}`" alt="Zdjęcie dokumentu" @click="showPhoto" class="document-image"/>
        </div>

      </section>

      <div v-if="document.photos.length > 0" class="separator"></div>

      <div v-if="document.photos.length > 0" class="browser-header">
        <p>Powiązane zdjęcia</p>
      </div>

      <div v-if="document.photos.length > 0" ref="photoGrid" :class="['photo-grid', { 'is-visible': showMorePhotos }]">

        <div v-for="(photo) in document.photos.slice(0, showMorePhotos ? document.photos.length : 4)" :key="photo.id" class="photo-item">
          <img :src="`/${photo.path}`" alt="Zdjęcie" class="photo-image" />
          <p>{{ photo.title }}</p>
        </div>

      </div>

      <div class="photo-button-div" v-if="document.photos.length > 4">
        <button @click="toggleShowMorePhotos" class="advanced-search">
          {{ showMorePhotos ? 'Pokaż mniej' : 'Pokaż więcej' }}
        </button>
      </div>

    </section>
  </section>

  <PhotoDetails v-if="photoModal" :showModal="photoModal" :path="document.path" :peopleDocuments="filteredPeopleDocuments"  @close="photoModal = false"></PhotoDetails>
  <MapModal
    v-if="showMap"
    :showMap = "showMap"
    :latitude="document.place.latitude"
    :longitude="document.place.longitude"
    @close="showMap = false"
  />

</template>

<style scoped>

.right-site-details {
  display: flex;
  align-items: flex-start;
  margin-top: 20px;
}

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

.browser {
  margin-top: 10px;
  padding-bottom: 50px;
  width: 75%;
}

button {
  padding: 8px 16px;
}

</style>
