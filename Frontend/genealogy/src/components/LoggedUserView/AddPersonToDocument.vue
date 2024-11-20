<script setup>
import {computed, defineProps, onMounted, ref, watch} from 'vue';
import { useRouter } from 'vue-router';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";
import ImageModal from "@/components/LoggedUserView/ImageModal.vue";
import PhotoDetails from "@/components/LoggedUserView/PhotoDetails.vue";

const props = defineProps(['documentID']);
const documentID = props.documentID;

const document = ref({});
const people = ref([]);
const filteredPeople = ref([]);
const comment = ref('');
const selectedPerson = ref(null);
const searchQuery = ref('');
const router = useRouter();

const photoModal = ref(false);
const showModal = ref(false);
const showImage = ref(false);
let errorText = ref('');

const x = ref(null);
const y = ref(null);

const showAdvancedPerson = ref(false);

onMounted(async () => {
  await fetchDocument();
  await fetchPeople();
});

watch(searchQuery, async (newQuery) => {
  if (newQuery) {
    await searchPeople(newQuery);
  } else {
    await fetchPeople();
  }
});

watch(selectedPerson, (newVal) => {
  showAdvancedPerson.value = !!newVal;
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

const fetchPeople = async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/People/All');
    if (response.ok) {
      people.value = await response.json();
      filteredPeople.value = people.value;
    } else {
      console.error('Nie udało się pobrać listy osób');
    }
  } catch (error) {
    console.error('Błąd podczas pobierania listy osób:', error);
  }
};

const searchPeople = async (query) => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/Search/${query}`);
    if (response.ok) {
      filteredPeople.value = await response.json();
    } else {
      console.error('Nie udało się pobrać wyników wyszukiwania');
      filteredPeople.value = [];
    }
  } catch (error) {
    console.error('Błąd podczas wyszukiwania osób:', error);
    filteredPeople.value = [];
  }
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
  if (place.community) parts.push(place.community);
  if (place.city) parts.push(place.city);
  return parts.join(', ');
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

const addPerson = async () => {

  if (!selectedPerson.value) {
    showModal.value = true;
    errorText.value = "Nie wybrano osoby";
    return;
  }

  if((!x.value || !y.value) && document.value.path) {
    showModal.value = true;
    errorText.value = "Nie zaznaczono osoby na zdjęciu";
    return;
  }

  try {

    const personData = {

      person: {
        id: selectedPerson.value
      },

      comment: comment.value || null,
      x: x.value || null,
      y: y.value || null

    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/AddPersonToDocument/${documentID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(personData)
    });

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania osoby do dokumentu";
      return;
    }

    // Odświeżenie listy osób po dodaniu
    await fetchDocument();
    await fetchPeople();
    selectedPerson.value = null;

  } catch (error) {
    console.error('Błąd podczas dodawania osoby do dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
  }

}

const tagPerson  = async () => {
  showImage.value = true;
}

const handleTagSaved = (tag) => {
  x.value = tag.x;
  y.value = tag.y;
};

const filteredPeopleDocuments = computed(() => {
  return document.value.peopleDocuments.filter(person => person.x !== null && person.y !== null);
});

const showPhoto  = async () => {
  photoModal.value = true;
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

      <section v-if="!document.path" class="advanced-section-adding">

        <div class="detail">
          <strong>Rodzaj:</strong> {{ document.type.name }}
        </div>

        <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
          <strong>Miejsce: </strong>
          <div>
            {{ formatPlace(document.place) }}
            <p v-if="document.place.address && document.place.postalCode"> {{document.place.address}}, {{document.place.postalCode}}</p>
            <p v-if="document.place.address && !document.place.postalCode"> {{document.place.address}}</p>
            <p v-if="document.place.postalCode && !document.place.address"> Kod pocztowy: {{document.place.postalCode}}</p>
            <p v-if="document.place.latitude"> Szerokość geograficzna: {{document.place.latitude}}</p>
            <p v-if="document.place.longitude"> Szerokość geograficzna: {{document.place.longitude}}</p>
            <p v-if="document.place.parish"> Przynależność parafialna: {{document.place.parish}}</p>
            <p v-if="document.place.secular"> Przynależność świecka: {{document.place.secular}}</p>

          </div>
        </div>

        <div v-if="document.startDate || document.endDate" class="detail">
          <strong>Przedział dat:</strong> {{ document.startDate }} - {{ document.endDate }}
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

      </section>

      <section v-if="document.path" class="content-details">

        <div class="left-site-details">

          <section class="advanced-section-adding">

            <div class="detail-small">
              <strong>Rodzaj: </strong> {{ document.type.name }}
            </div>

            <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail-small">
              <strong>Miejsce: </strong> {{ formatPlace(document.place) }}
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

          </section>

        </div>

        <div v-if="document.path" class="right-site-details">
          <img :src="`/${document.path}`" alt="Zdjęcie dokumentu" @click="showPhoto" class="document-image"/>
        </div>

      </section>

      <div class="separator2"></div>

      <div class="browser-header">
        <p>Dodawanie osób</p>
      </div>

      <section class="person-section">
        <div class="select-person">
          <select class="person-select" v-model="selectedPerson" required>
            <option disabled value="">Wybierz osobę</option>
            <option v-for="person in people" :key="person.id" :value="person.id">
              {{ person.name }} {{ person.surname }}
            </option>
          </select>
          <button class="advanced-search" type="button" @click="tagPerson">Oznacz</button>
          <button class="add-button2" type="button" @click="addPerson">Dodaj</button>
        </div>

        <section :class="['advanced-fields', { 'is-visible': showAdvancedPerson }]">

          <div class="comment-section">
            <label >Komentarz</label>
            <input class="main-input" type="text" id="comment" v-model="comment" />
          </div>

        </section>

      </section>

    </section>
  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <ImageModal
    v-if="showImage"
    :showModal="showImage"
    :path="document.path"
    @tag="handleTagSaved"
    @close="showImage = false" />

  <PhotoDetails v-if="photoModal" :showModal="photoModal" :path="document.path" :peopleDocuments="filteredPeopleDocuments"  @close="photoModal = false"></PhotoDetails>

</template>

<style scoped>

.main-input, .person-select {
  border: 1px solid #c9ad6e;
}

.content-details {
  margin-bottom: 20px;
}

.main-section {
  height: 100%;
}

.advanced-search {
  margin-left: 0;
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

.comment-section{
  margin-right: 240px;
}

.document-image {
  width: 50%;
}

</style>
