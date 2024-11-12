<script setup>

import {defineProps, onMounted, ref, computed} from 'vue';
import { useRouter } from 'vue-router';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";

const props = defineProps(['documentID']);
const documentID = props.documentID;

const document = ref({});
const photos = ref([]);
const selectedPhotos = ref([]);
const parameter = ref('');
const router = useRouter();

const photosWithPath = computed(() => photos.value.filter(photo => photo.path));

const showModal = ref(false);
let errorText = ref('');

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

const save = () => {

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistDocumentDetails', params: { documentID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserDocumentDetails', params: { documentID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }

}

onMounted(async () => {
  await fetchDocument();
  await fetchPhotos();
});

const fetchDocument = async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Get/${documentID}`);
    if (response.ok) {
      document.value = await response.json();
    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
};

const fetchPhotos = async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/GetPhotos`);
    if (response.ok) {
      photos.value = await response.json();
    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
};

const addPhotos = async () => {

  if (selectedPhotos.value.length === 0) {
    errorText.value = "Wybierz przynajmniej jedno zdjęcie!"
    showModal.value = true;
  }
  else {
    try {

      const response = await fetch(`http://127.0.0.1:8080/API/Documents/AddPhotos/${documentID}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedPhotos.value),
      });

      if (!response.ok) {
        showModal.value = true;
        errorText.value = await response.text();
        selectedPhotos.value = ([]);
        await fetchDocument();
        return;
      }

      console.log("Udało się dodać zdjęcie do dokumentu");
      selectedPhotos.value = ([]);
      await fetchDocument();

    } catch (error) {
      console.error('Błąd podczas wysyłania zdjęcia:', error);
    }
  }
};

const toggleSelection = (photo) => {
  const index = selectedPhotos.value.findIndex(selected => selected === photo.id);
  if (index === -1) {
    selectedPhotos.value.push(photo.id);
  } else {
    selectedPhotos.value.splice(index, 1);
  }
};

const searchPhotos = async () => {
  if (parameter.value === "") {
    await fetchPhotos();
  } else {
    try {
      const params = new URLSearchParams({
        typeIds: 1,
        title: parameter.value
      });

      const response = await fetch(`http://127.0.0.1:8080/API/Documents/Search?${params}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {

        showModal.value = true;
        const errorDetails = await response.text();
        const errorMessage = errorDetails.error || "Wystąpił błąd podczas dodawania zdjęć do dokumentu";
        console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
        errorText.value = errorMessage;
        return;
      }

      photos.value = await response.json();

    } catch (error) {
      console.error('Błąd podczas wyszukiwania dokumentów:', error);
    }
  }
}

const fetchPhotoPaths = async () => {

  try {
    // Mapowanie identyfikatorów zdjęć na obietnice żądań, aby pobrać ich szczegóły (w tym ścieżkę).
    const photoRequests = document.value.photos.map(photoId =>
      fetch(`http://127.0.0.1:8080/API/Documents/GetPhoto/${photoId}`)
        .then(response => response.ok ? response.json() : Promise.reject('Błąd przy pobieraniu zdjęcia'))
    );

    // Czekamy na zakończenie wszystkich żądań i przypisujemy wyniki do photos.value
    const photoDetails = await Promise.all(photoRequests);

    // Aktualizujemy photos.value
    photos.value = photoDetails.map(photo => ({
      id: photo.id,
      title: photo.title,
      path: photo.path || null // Ustawiamy path na null, jeśli jest pusty, aby łatwiej było filtrować.
    }));

  } catch (error) {
    console.error("Błąd przy pobieraniu ścieżek zdjęć:", error);
    errorText.value = "Nie udało się pobrać ścieżek zdjęć. Spróbuj ponownie później.";
    showModal.value = true;
  }

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
        </div>
        <div class="right-section">
          <button class="button-modal" @click="save">Zapisz</button>
        </div>
      </section>

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

        <div v-if="document.date" class="detail">
          <strong>Data: </strong> {{ formatDate(document.date) }}
        </div>

        <div v-if="document.photos.length > 0" class="photo-grid">
          <div v-for="(photo) in document.photos" :key="photo.id" class="photo-item">
            <img :src="`/${photo.path}`" alt="Zdjęcie" class="photo-image" />
            <p>{{ photo.title }}</p>
          </div>
        </div>

      </section>

      <div class="separator2"></div>

      <div class="browser-header">
        <p>Dodawanie zdjęć</p>
      </div>

      <section class="header-section">
        <div class="left-section">
          <input class="filter-input" type="text" placeholder="Znajdź" id="parameter" v-model="parameter" @keyup.enter="searchPhotos" required />
          <button class="button-modal" @click="searchPhotos">Szukaj</button>
        </div>
        <div class="right-section">
          <button class="button-modal" @click="addPhotos">Dodaj zdjęcia</button>
        </div>
      </section>

      <!-- Wyświetlanie zdjęć po 3-4 w jednym rzędzie -->
      <div v-if="photosWithPath.length > 0" class="photo-grid">
        <div v-for="(photo) in photosWithPath" :key="photo.id" :class="{'selected': selectedPhotos.includes(photo.id)}" @click="toggleSelection(photo)" class="photo-item">
          <img :src="`/${photo.path}`" alt="Zdjęcie" class="photo-image" />
          <p>{{ photo.title }}</p>
          <span v-if="selectedPhotos.includes(photo.id)" class="checkmark">✔</span>
        </div>
      </div>

    </section>
  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />

</template>

<style scoped>

.main-section {
  height: 100%;
}

.left-section {
  margin-left: 40px;
}

.left-section button {
  margin-left: 20px;
}

.browser {
  margin-top: 10px;
  width: 75%;
}

button {
  padding: 8px 16px;
}

.header-section {
  width: 100%;
  padding: 0 20px 10px;
}

.button-modal {
  width: 100%;
}

</style>
