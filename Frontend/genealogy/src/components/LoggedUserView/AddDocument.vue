<script setup>
import {onMounted, ref, watch} from "vue";
import ErrorModal from './ErrorModal.vue';
import SuccessModal from "@/components/LoggedUserView/SuccessModal.vue";

const showModal = ref(false);
const showSuccess = ref(false);
let errorText = ref('');
let successText = ref('');
const docID = ref(null);

const documentTypes = ref([]);
const selectedDocumentType = ref(null);
const templateFields = ref([]);

const showAdvancedAddress = ref(false);

// Zmienne do kontrolowania checkboxów
const notExactDate = ref(false);    // Czy dokładna data jest zaznaczona
const isDateRange = ref(false);    // Czy przedział dat jest zaznaczony

// Zmienne na daty
let exactDate = ref(null);       // Dokładna data
let startDate = ref(null);       // Data początkowa (przedział dat)
let endDate = ref(null);         // Data końcowa (przedział dat)

const title = ref('');
const description = ref('');
const year = ref('');
const month = ref('');
const day = ref('');
const placeID = ref('');
const country = ref('');
const voivodeship = ref('');
const community = ref('');
const city = ref('');
const address = ref('');
const postalCode = ref('');
const parish = ref('');
const secular = ref('');
const longitude = ref('');
const latitude = ref('');

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
    notExactDate.value = true;
    isDateRange.value = false;
    startDate.value = null;
    endDate.value = null;
  } else if (type === 'range') {
    notExactDate.value = false;
    isDateRange.value = true;
    exactDate.value = null;
  }
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
        id: placeID.value || null,
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

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(documentData)
    });

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
    showSuccess.value = true;

    //await router.push({name: 'GenealogistSearchResults', query: {results: JSON.stringify(results)},});

  } catch (error) {
    console.error('Błąd podczas dodawania dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
  }

}

</script>

<template>

  <section class="browser">
    <section class="advanced-section-adding">

      <div class="type-section">
        <label>Rodzaj dokumentu <span class="required-asterisk">*</span></label>
        <select class="main-select" v-model="selectedDocumentType" required>
          <option disabled value="">Wybierz rodzaj dokumentu</option>
          <option v-for="documentType in documentTypes" :key="documentType.id" :value="documentType.id">
            {{ documentType.typeName }}
          </option>
        </select>
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
          Niedokładna data
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
        <label for="exactDateInput">Niedokładna data</label>
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
          <input class="main-input" type="text" placeholder="parafia" id="parish" v-model="parish" />
        </div>

        <div class="parish-section">
          <label >Przynależność świecka</label>
          <input class="main-input" type="text" placeholder="Przynależność" id="secular" v-model="secular" />
        </div>

        <div class="coordinates-section-adding">
          <label >Współrzędne geograficzne</label>
          <input class="main-input" type="text" placeholder="Szerokość" id="latitude" v-model="latitude" />
          <input class="main-input" type="text" placeholder="Długość" id="parish" v-model="longitude" />
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
          <input class="main-input" v-if="field.type === 'number'" v-model="field.value" :id="field.name" type="number" />

        </div>

      </div>
    </div>

    <div class="end-section">
      <button class="add-button" type="submit" @click="addDocument">Dodaj</button>
    </div>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <SuccessModal v-if="showSuccess" :showModal="showSuccess" :successDetails="successText" :docID="docID" @close="showSuccess = false" />

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

</style>
