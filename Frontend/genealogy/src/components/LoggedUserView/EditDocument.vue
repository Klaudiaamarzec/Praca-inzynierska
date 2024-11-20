<script setup>

import {useRoute, useRouter} from 'vue-router';
import { ref, computed, onMounted } from 'vue';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";

const route = useRoute();
const router = useRouter();

const showModal = ref(false);
const showAdvancedAddress = ref(false);
let errorText = ref('');

const notExactDate = ref(false);
const isDateRange = ref(false);

// Pobierz dokument z danych przekazanych przez route
const documentID = computed(() => route.params.documentID || {});
const document = ref({});

const startDate = ref(null);
const endDate = ref(null);

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
const longitude = ref('');
const latitude = ref('');
const additionalFields = ref([]);

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

const addPerson = () => {

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ path: '/genealogist/addPersonToDocument', query: { documentID: documentID.value } });
  } else if (userRole === 'user') {
    router.push({ path: '/user/addPersonToDocument', query: { documentID: documentID.value } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }
};

const addPhotos = () => {

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ path: '/genealogist/addPhotos', query: { documentID: documentID.value } });
  } else if (userRole === 'user') {
    router.push({ path: '/user/addPhotos', query: { documentID: documentID.value } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }

}

const addLocation = () => {
  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ path: '/genealogist/addLocation', query: { documentID: documentID.value } });
  } else if (userRole === 'user') {
    router.push({ path: '/user/addLocation', query: { documentID: documentID.value } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }
};

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

const cancel = () => {
  router.back();
};

const autoResize = (event) => {
  const textarea = event.target;
  textarea.style.height = 'auto';
  textarea.style.height = textarea.scrollHeight + 'px';
};

const formDate = (dateString) => {
  const [year, month, day] = dateString.split('-');
  return `${day}.${month}.${year}`;
};

onMounted(async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Get/${documentID.value}`);
    if (response.ok) {
      document.value = await response.json();

      title.value = document.value.title;
      description.value = document.value.description || '';
      year.value = document.value.date.year || '';
      month.value = document.value.date.month || '';
      day.value = document.value.date.day || '';
      startDate.value = document.value.startDate ? formDate(document.value.startDate) : null;
      endDate.value = document.value.endDate ? formDate(document.value.endDate) : null;
      country.value = document.value.place.country || '';
      voivodeship.value = document.value.place.voivodeship || '';
      community.value = document.value.place.community || '';
      city.value = document.value.place.city || '';
      address.value = document.value.place.address || '';
      postalCode.value = document.value.place.postalCode || '';
      parish.value = document.value.place.parish || '';
      secular.value = document.value.place.secular || '';
      longitude.value = document.value.place.longitude || '';
      latitude.value = document.value.place.latitude || '';
      additionalFields.value = document.value.additionalFields || [];

      if(document.value.date)
        notExactDate.value = true

      if(document.value.startDate || document.value.endDate)
        isDateRange.value = true

    } else {
      console.error("Nie udało się pobrać dokumentu");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
});

const editDocument = async() => {
  try {

    const token = localStorage.getItem('jwtToken');

    const isDateProvided = year.value || month.value || day.value;

    if(title.value === '' || (country.value === '' && voivodeship.value === '' && community.value === '' && city.value === '' && address.value === '' && postalCode.value === '' && parish.value === '' && secular.value === '' && longitude.value === '' && latitude.value === '')) {
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
      additionalFields

    }

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Update/${documentID.value}`, {
      method: 'PUT',
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

    cancel();

  } catch (error) {
    console.error('Błąd podczas dodawania dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>

  <section class="main-section">
    <section v-if="Object.keys(document).length" class="browser">

      <div class="edit-header">

        <div class="edit-title">
          <label for="fromDate">Tytuł:</label>
          <input class="edit-title-input" type="text" id="title" v-model="title" />
        </div>

      </div>

      <div class="separator"></div>

      <section class="header-section">
        <div class="left-section">
          <button class="button-modal" @click="cancel">Anuluj</button>
        </div>
        <div class="right-section">
          <button class="button-modal" @click="editDocument">Zapisz</button>
        </div>
      </section>

      <section class="header-section">
        <div class="left-section">
          <button class="edit-button" @click="addPhotos">Dodaj powiązane zdjęcia</button>
          <button class="edit-button" @click="addPerson">Dodaj osoby</button>
          <button class="edit-button" @click="addLocation">Dodaj lokalizacje</button>
        </div>
        <div class="right-section">
          <button v-if="!document.path" class="edit-button" type="button" @click="choosePhoto">
            Dodaj zdjęcie dokumentu
          </button>
          <button v-if="document.path" class="edit-button" type="button" @click="choosePhoto">
            Dodaj nowe zdjęcie dokumentu
          </button>
        </div>
      </section>

      <section v-if="!document.path" class="advanced-section-adding">

        <div v-if="document.type.id === 1">
          <p style="color: var(--dark-brown); padding-bottom: 8px">Dodaj zdjęcie dokumentu, aby móc dodawć je do innych dokumentów !</p>
        </div>

        <div>
          <strong>Opis:</strong>

          <textarea class="edit-textarea"
                    id="description"
                    v-model="description"
                    @input="autoResize"
                    :placeholder="description ? '' : 'Wpisz opis dokumentu'"
          ></textarea>
        </div>

        <div class="coordinates-section">
          <label><strong>Rodzaj daty</strong></label>
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

        <strong>Miejsce <span class="required-asterisk">*</span></strong>

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
            <input class="main-input" type="text" placeholder="Szerokość" id="latitude" v-model="latitude" />
            <input class="main-input" type="text" placeholder="Długość" id="parish" v-model="longitude" />
          </div>

        </section>

<!--        <div v-if="document.additionalFields">-->
<!--          <div v-for="(fieldValue, fieldName) in document.additionalFields" :key="fieldName" class="additional-detail">-->
<!--            <strong>{{ fieldName }}: </strong> {{ fieldValue }}-->
<!--          </div>-->
<!--        </div>-->

<!--        <div v-if="additionalFields.length > 0" :class="['template-fields-section', { 'is-visible': additionalFields.length > 0 }]">-->

<!--          <div class="separator"></div>-->

<!--          <div class="advanced-section-adding">-->

<!--            <div v-for="(field, index) in additionalFields" :key="index" class="advanced-data-section">-->
<!--              <label :for="field.name">{{ field.name }}</label>-->

<!--              <input class="main-input" v-if="field.type === 'text'" v-model="field.value" :id="field.name" type="text" />-->
<!--              <input class="main-input" v-if="field.type === 'date'" v-model="field.value" :id="field.name" type="date" />-->
<!--              <input class="main-input" v-if="field.type === 'number'" v-model="field.value" :id="field.name" type="number" />-->

<!--            </div>-->

<!--          </div>-->
<!--        </div>-->

      </section>

      <div v-if="document.path">

        <section class="content-details">

          <div class="left-site-details">

            <section class="advanced-section-adding">

              <div>
                <strong>Opis:</strong>

                <textarea class="edit-textarea"
                          id="description"
                          v-model="description"
                          @input="autoResize"
                          :placeholder="description ? '' : 'Wpisz opis dokumentu'"
                ></textarea>
              </div>

              <div class="coordinates-section">
                <label><strong>Rodzaj daty</strong></label>
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

            </section>

          </div>

          <div v-if="document.path" class="right-site-details">
            <img :src="`/${document.path}`" alt="Zdjęcie dokumentu" @click="showPhoto" class="document-image-edit"/>
          </div>

        </section>

        <section class="advanced-section-adding">

          <strong>Miejsce <span class="required-asterisk">*</span></strong>

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
              <input class="main-input" type="text" placeholder="Szerokość" id="latitude" v-model="latitude" />
              <input class="main-input" type="text" placeholder="Długość" id="parish" v-model="longitude" />
            </div>

          </section>

        </section>
      </div>

    </section>
  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />

</template>

<style scoped>

.right-site-details {
  display: flex;
  align-items: flex-start;
  margin: 0;
}

.main-section {
  height: 100%;
  padding-top: 0;
}

.main-input, .date-input, .address-input {
  border: 1px solid #c9ad6e;
}

.content-details {
  margin-bottom: 10px;
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
  margin-top: 0;
  padding-bottom: 50px;
  width: 75%;
}

button {
  padding: 8px 16px;
}

</style>
