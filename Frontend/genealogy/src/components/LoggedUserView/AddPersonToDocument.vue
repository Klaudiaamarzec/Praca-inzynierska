<script setup>
import {defineProps, onMounted, ref} from 'vue';
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";

const props = defineProps(['documentID']);
const documentID = props.documentID;

const document = ref({});
const people = ref([]);
const selectedPerson = ref(null);

const showModal = ref(false);
let errorText = ref('');

onMounted(async () => {
  await fetchDocument();
  await fetchPeople();
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
    } else {
      console.error('Nie udało się pobrać listy osób');
    }
  } catch (error) {
    console.error('Błąd podczas pobierania listy osób:', error);
  }
};

const formatPlace = (place) => {
  const parts = [];
  if (place.country) parts.push(place.country);
  if (place.voivodeship) parts.push(place.voivodeship);
  if (place.city) parts.push(place.city);
  return parts.join(', ');
};

const formatPeopleDocuments = (people) => {
  return people.map(person => `${person.firstName} ${person.lastName}`).join(', ');
};

const addPerson = async () => {

  if (!selectedPerson.value) {
    console.error("Nie wybrano osoby.");
    return;
  }

  try {

    const personData = {

      person: {
        id: selectedPerson.value
      }

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
    selectedPerson.value = null; // Resetuj wybór po dodaniu

  } catch (error) {
    console.error('Błąd podczas dodawania osoby do dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem.";
  }

}

</script>

<template>

  <section v-if="Object.keys(document).length" class="browser">

    <div class="browser-header">
      <p>{{ document.title }}</p>
    </div>

    <div class="separator"></div>

    <section class="advanced-section-adding">

      <div class="detail">
        <strong>Rodzaj:</strong> {{ document.type.name }}
      </div>

      <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="info-section">
        <strong>Miejsce:</strong> {{ formatPlace(document.place) }}
      </div>

      <div v-if="document.startDate || document.endDate" class="info-section">
        <strong>Przedział dat:</strong> {{ document.startDate }} - {{ document.endDate }}
      </div>
      <!--        &lt;!&ndash;      <div class="detail">&ndash;&gt;-->
      <!--        &lt;!&ndash;        <strong>Data:</strong> {{ formatDate(document.date) }}&ndash;&gt;-->
      <!--        &lt;!&ndash;      </div>&ndash;&gt;-->

      <div v-if="document.peopleDocuments && document.peopleDocuments.length > 0" class="info-section">
        <strong>Osoby występujące:</strong> {{ formatPeopleDocuments(document.peopleDocuments) }}
      </div>

    </section>

    <div class="separator"></div>

    <section class="person-section">
      <select class="person-select" v-model="selectedPerson" required>
        <option disabled value="">Wybierz osobę</option>
        <option v-for="person in people" :key="person.id" :value="person.id">
          {{ person.name }} {{ person.surname }}
        </option>
      </select>
      <button class="add-button2" type="button" @click="addPerson">Dodaj</button>
    </section>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />

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
