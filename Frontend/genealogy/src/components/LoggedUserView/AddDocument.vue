<script setup>
import {onMounted, ref, watch} from "vue";
import ErrorModal from './ErrorModal.vue';
import ErrorPhotoModal from "@/components/LoggedUserView/ErrorPhotoModal.vue";
import SuccessModal from "@/components/LoggedUserView/SuccessModal.vue";
import PhotoModal from "@/components/LoggedUserView/PhotoModal.vue";
import LogoutModal from "@/components/MainView/LogoutModal.vue";

const showModal = ref(false);
const showErrorModal = ref(false);
const showSuccess = ref(false);
const showPhotoModal = ref(false);
let errorText = ref('');
let successText = ref('');
let errorModalText = ref('');
const docID = ref(null);
const photoData = ref(null);
const photoFile = ref(null);
const showLogoutModal = ref(false);
let logoutText = ref('');

const documentTypes = ref([]);
const selectedDocumentType = ref(null);
const templateFields = ref([]);

const showAdvancedAddress = ref(false);

// Zmienne do kontrolowania checkboxów
const notExactDate = ref(false);    // Czy dokładna data jest zaznaczona
const isDateRange = ref(false);    // Czy przedział dat jest zaznaczony

// Zmienne na daty
let startDate = ref(null);       // Data początkowa (przedział dat)
let endDate = ref(null);         // Data końcowa (przedział dat)

const title = ref('');
const description = ref('');
const year = ref('');
const month = ref('');
const day = ref('');
const country = ref('');
const voivodeship = ref('');
const community = ref('');
const city = ref('');
const address = ref('');
const postalCode = ref('');
const parish = ref('');
const secular = ref('');
const longitude = ref(null);
const latitude = ref(null);

onMounted(async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/DocumentTypes/All');
    documentTypes.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania rodzajów dokumentów:', error);
  }
});

const autoResize = (event) => {
  const textarea = event.target;
  textarea.style.height = 'auto';  // Resetujemy wysokość, aby przeliczyć nową wysokość
  textarea.style.height = textarea.scrollHeight + 'px';  // Ustawiamy wysokość na podstawie zawartości
};

const fetchTemplate = async (id) => {

  try {
    const response = await fetch(`http://127.0.0.1:8080/API/DocumentTypes/GetTemplate/${id}`);
    const documentType = await response.json();
    if (documentType.parsedTemplate && documentType.parsedTemplate.fields) {
      templateFields.value = documentType.parsedTemplate.fields; // Ustaw dynamiczne pola
    } else {
      templateFields.value = []; // Wyczyść pola, jeśli brak danych
    }
  } catch (error) {
    console.error('Błąd podczas pobierania szablonu dokumentu:', error);
  }
};

watch(selectedDocumentType, (newTypeId) => {
  if (newTypeId) {
    fetchTemplate(newTypeId);
  } else {
    templateFields.value = [];  // Wyczyść pola, jeśli nie wybrano typu dokumentu
  }
});

const toggleDateSelection = (type) => {
  if (type === 'exact') {
    if (!notExactDate.value) {
      day.value = null;
      month.value = null;
      year.value = null;
    }
  } else if (type === 'range') {
    if (!isDateRange.value) {
      startDate.value = null;
      endDate.value = null;
    }
  }
};

const choosePhoto = () => {
  showPhotoModal.value = true;
};

const handlePhotoSubmit = (data) => {
  //photoData.value = photo;
  photoData.value = data.photoBase64;
  photoFile.value = data.photoFile;
  showPhotoModal.value = false;
};

const addDocument = async () => {

  try {

    const additionalFields = {};
    templateFields.value.forEach(field => {
      additionalFields[field.name] = field.value || null;
    });

    // Sprawdź, czy któreś z pól daty jest podane
    const isDateProvided = year.value || month.value || day.value;

    if(title.value === '' || selectedDocumentType.value === null || (country.value === '' && voivodeship.value === '' && community.value === '' && city.value === '' && address.value === '' && postalCode.value === '' && parish.value === '' && secular.value === '' && longitude.value === '' && latitude.value === '')) {
      showModal.value = true;
      errorText.value = "Uzupełnij wszystkie wymagane pola";
      return;
    }

    const documentData = {

      title: title.value,
      description: description.value,
      startDate: startDate.value || null,
      endDate: endDate.value || null,
      date: isDateProvided
        ? {
          year: parseInt(year.value, 10),
          month: month.value ? parseInt(month.value, 10) : null,
          day: day.value ? parseInt(day.value, 10) : null
        }
        : null,
      place: {
        country: country.value || null,
        voivodeship: voivodeship.value || null,
        community: community.value || null,
        city: city.value || null,
        address: address.value || null,
        postalCode: postalCode.value || null,
        parish: parish.value || null,
        secular: secular.value || null
      },
      type:{
        id: selectedDocumentType.value
      },
      additionalFields

    }

    if (longitude.value !== null) documentData.append('longitude', longitude.value);
    if (latitude.value !== null) documentData.append('latitude', latitude.value);

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(documentData)
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

    const results = await response.json();
    docID.value = results.documentId;
    successText.value = results.message;

    if (photoData.value) {
      await uploadPhoto(docID.value, photoFile.value);
    }
    else {
      showSuccess.value = true;
    }

  } catch (error) {
    console.error('Błąd podczas dodawania dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
  }
}

const uploadPhoto = async (docId, photoFile) => {
  try {
    const formData = new FormData();
    formData.append('photoFile', photoFile);  // Attach the photo

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/AddPath/${docId}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
      },
      body: formData,
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showErrorModal.value = true;
      const errorDetails = await response.text();
      const errorMessage = errorDetails.error || "Wystąpił błąd podczas dodawania zdjęcia do dokumentu";
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorModalText.value = errorMessage;
      return;
    }

    showSuccess.value = true;

  } catch (error) {
    console.error('Błąd podczas wysyłania zdjęcia:', error);
  }
};

</script>

<template>

  <section class="browser">
    <section class="advanced-section-adding">

      <div class="type-section">
        <div>
          <label>Rodzaj dokumentu <span class="required-asterisk">*</span></label>
          <select class="main-select" v-model="selectedDocumentType" required>
            <option disabled value="">Wybierz rodzaj dokumentu</option>
            <option v-for="documentType in documentTypes" :key="documentType.id" :value="documentType.id">
              {{ documentType.typeName }}
            </option>
          </select>
        </div>
        <div>
          <div v-if="photoData" class="photo-preview">
            <img :src="photoData" alt="Podgląd zdjęcia dokumentu" />
          </div>
          <button v-else class="photo-button" type="button" @click="choosePhoto">
            Dodaj zdjęcie dokumentu
          </button>
        </div>
      </div>

      <div v-if="selectedDocumentType === 1">
        <p style="color: var(--dark-brown)">Dodaj zdjęcie dokumentu, aby móc dodawć je do innych dokumentów !</p>
      </div>

      <div class="date-section">
        <label for="fromDate">Tytuł <span class="required-asterisk">*</span></label>
        <input class="input-title" type="text" id="title" v-model="title" />
      </div>

      <p>Opis</p>

      <textarea class="main-textarea"
        id="description"
        v-model="description"
        placeholder="Wpisz opis dokumentu"
        @input="autoResize"
      ></textarea>

      <div class="coordinates-section">
        <label>Wybierz rodzaj daty</label>
        <label>
          <input class="main-input"
            type="checkbox"
            v-model="notExactDate"
            @change="toggleDateSelection('exact')"
            :checked="notExactDate"
          />
          Data
        </label>

        <label>
          <input class="main-input"
            type="checkbox"
            v-model="isDateRange"
            @change="toggleDateSelection('range')"
            :checked="isDateRange"
          />
          Przedział dat
        </label>
      </div>

      <div v-if="notExactDate" class="date-inputs">
        <label for="exactDateInput">Data</label>
        <input class="date-input" type="number" placeholder="Dzień" id="day" v-model="day" min="1" max="31" />
        <input class="date-input" type="number" placeholder="Miesiąc" id="month" v-model="month" min="1" max="12" />
        <input class="date-input" type="number" placeholder="Rok" id="year" v-model="year" min="1000" max="3000" /><span class="required-asterisk">*</span>
      </div>

      <div v-if="isDateRange" class="data-section">
        <label for="startDate">Data początkowa</label>
        <input class="main-input" type="date" id="startDate" v-model="startDate" />

        <label class="end-date" for="endDate">Data końcowa</label>
        <input class="main-input" type="date" id="endDate" v-model="endDate" />
      </div>

      <p>Miejsce <span class="required-asterisk">*</span></p>

      <div class="address-section">
        <input class="main-input" type="text" placeholder="Kraj" id="country" v-model="country" />
        <input class="main-input" type="text" placeholder="Województwo" id="voivodeship" v-model="voivodeship" />
        <input class="main-input" type="text" placeholder="Gmina/Powiat" id="community" v-model="community" />
        <input class="main-input" type="text" placeholder="Miasto" id="city" v-model="city" />
      </div>

      <div class="address-section">
        <input class="address-input" type="text" placeholder="Adres" id="address" v-model="address" />
        <input class="main-input" type="text" placeholder="Kod pocztowy" id="postalCode" v-model="postalCode" />
      </div>

      <button class="advanced-address" @click="showAdvancedAddress = !showAdvancedAddress">{{ showAdvancedAddress ? 'Ukryj dodatkowe informacje' : 'Więcej informacji o adresie' }}</button>

      <section :class="['advanced-fields', { 'is-visible': showAdvancedAddress }]">

        <div class="parish-section">
          <label >Parafia</label>
          <input class="main-input" type="text" placeholder="Parafia" id="parish" v-model="parish" />
        </div>

        <div class="parish-section">
          <label >Przynależność świecka</label>
          <input class="main-input" type="text" placeholder="Przynależność" id="secular" v-model="secular" />
        </div>

        <div class="coordinates-section-adding">
          <label >Współrzędne geograficzne</label>
          <input class="main-input" type="number" placeholder="Szerokość" id="latitude" v-model="latitude" />
          <input class="main-input" type="number" placeholder="Długość" id="parish" v-model="longitude" />
        </div>

      </section>

    </section>

    <!-- Sprawdź czy mamy pola szablonu -->
    <div v-if="templateFields.length > 0" :class="['template-fields-section', { 'is-visible': templateFields.length > 0 }]">

      <div class="separator"></div>

      <div class="advanced-section-adding">

        <div v-for="(field, index) in templateFields" :key="index" class="advanced-data-section">
          <label :for="field.name">{{ field.name }}</label>

          <input class="main-input" v-if="field.type === 'text'" v-model="field.value" :id="field.name" type="text" />
          <input class="main-input" v-if="field.type === 'date'" v-model="field.value" :id="field.name" type="date" />
          <input class="main-input" v-if="field.type === 'bigint'" v-model="field.value" :id="field.name" type="number" />

        </div>

      </div>
    </div>

    <div class="end-section">
      <button class="add-button" type="submit" @click="addDocument">Dodaj</button>
    </div>

  </section>

  <PhotoModal v-if="showPhotoModal" :showModal="showPhotoModal" @submit="handlePhotoSubmit" @close="showPhotoModal = false" />
  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <ErrorPhotoModal v-if="showErrorModal" :showModal="showErrorModal" :errorDetails="errorModalText" :docID="docID" @close="showModal = false"></ErrorPhotoModal>
  <SuccessModal v-if="showSuccess" :showModal="showSuccess" :successDetails="successText" :docID="docID" @close="showSuccess = false" />
  <LogoutModal v-if="showLogoutModal" :showModal="showLogoutModal" :errorDetails="logoutText" @close="showLogoutModal = false" />

</template>

<style scoped>

.browser {
  display: flex;
  background: var(--light-white);
  width: 60%;
  margin: auto;
  background: var(--light-white);
  padding-top: 15px;
  padding-bottom: 15px;
}

p {
  margin: 5px 0;
}

.photo-preview img {
  max-width: 50%;
  height: auto;
}

.photo-preview {
  justify-content: flex-end;
}

</style>
