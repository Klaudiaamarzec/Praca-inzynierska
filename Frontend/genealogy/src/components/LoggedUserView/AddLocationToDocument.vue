<script setup>
import {defineProps, onMounted, ref} from 'vue';
import { useRouter } from 'vue-router';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";

const props = defineProps(['documentID']);
const documentID = props.documentID;

const document = ref({});
const router = useRouter();

let comment = ref('');
let url = ref('');

const isOriginal = ref(false);
const condition = ref('');
const type = ref('');
const description = ref('');

const country = ref('');
const voivodeship = ref('');
const community = ref('');
const city = ref('');
const address = ref('');
const postalCode = ref('');

const showModal = ref(false);
let errorText = ref('');

onMounted(async () => {
  await fetchDocument();
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

const formatPlace = (place) => {
  const parts = [];
  if (place.country) parts.push(place.country);
  if (place.voivodeship) parts.push(place.voivodeship);
  if (place.city) parts.push(place.city);
  return parts.join(', ');
};

const formatDate = (date) => {
  const day = date.day ? String(date.day).padStart(2, '0') : '';
  const month = date.month ? String(date.month).padStart(2, '0') : '';
  const year = date.year ? date.year : '';
  return day && month ? `${day}.${month}.${year}` : month ? `${month}.${year}` : `${year}`;
};

const formatPersonDocument = (personDocument) => {
  return `${personDocument.firstName} ${personDocument.lastName}`;
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

const addURL = async () => {

  if (!url.value) {
    console.error("Nie podano adresu URL");
    return;
  }

  try {

    const urlData = {

      url: url.value,
      comment: comment.value || null

    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/URLs/Add/${documentID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(urlData)
    });

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania adresu URL do dokumentu";
      return;
    }

    // Odświeżenie po dodaniu
    await fetchDocument();
    url.value = '';
    comment.value = '';

  } catch (error) {
    console.error('Błąd podczas dodawania adresu URL do dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
  }

}

const addPhysicalLocation = async () => {

  try {

    const physicalData = {

      isOriginal: isOriginal.value,
      condition: condition.value || null,
      type: type.value || null,
      description: description.value || null,
      localaddress: {
        country: country.value || null,
        voivodeship: voivodeship.value || null,
        community: community.value || null,
        city: city.value || null,
        address: address.value || null,
        postalCode: postalCode.value || null
      }

    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/PhysicalLocations/Add/${documentID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(physicalData)
    });

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania adresu do dokumentu";
      return;
    }

    // Odświeżenie po dodaniu
    await fetchDocument();
    isOriginal.value = false;
    condition.value = '';
    type.value = '';
    description.value = '';
    country.value = '';
    voivodeship.value = '';
    community.value = '';
    city.value = '';
    address.value = '';
    postalCode.value = '';

  } catch (error) {
    console.error('Błąd podczas dodawania adresu do dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
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
                  <strong>Stan:</strong> {{ location.condition }}<br />
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

      <div class="separator2"></div>

      <div class="browser-header">
        <p>Dodawanie adresu URL</p>
      </div>

      <section class="url-section">
        <div class="input-url">
          <label for="startDate">Adres URL</label>
          <input class="url-input" type="text" id="url" v-model="url" />
        </div>

        <div class="input-url">
          <label for="comment">Komentarz</label>
          <input class="url-input" type="text" id="comment" v-model="comment" />
          <button class="add-button2" type="button" @click="addURL">Dodaj</button>
        </div>

      </section>

      <div class="separator2"></div>

      <div class="browser-header">
        <p>Dodawanie fizycznej lokalizacji</p>
      </div>

      <section class="advanced-section-adding">

        <section class="physical-section">

          <div class="physical-input">
            <label for="startDate">Rodzaj</label>
            <input class="url-input" type="text" id="type" v-model="type" />
          </div>

          <div class="physical-input">
            <label for="startDate">Kondycja</label>
            <input class="url-input" type="text" id="condition" v-model="condition" />
          </div>

          <div class="physical-input">
            <label for="startDate">Opis</label>
            <input class="url-input" type="text" id="description" v-model="description" />
          </div>

          <div class="physical-input">
            <label  for="isOriginal" >Oryginał <span class="required-asterisk">*</span></label>
            <select class="main-select" id="isOriginal" v-model="isOriginal" required>
              <option :value="true">TAK</option>
              <option :value="false">NIE</option>
            </select>
          </div>

        </section>

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
          <button class="add-button2" type="button" @click="addPhysicalLocation">Dodaj</button>
        </div>

      </section>

    </section>
  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />

</template>

<style scoped>

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

.main-select {
  width: 25%;
  margin-left: 0;
}

</style>
