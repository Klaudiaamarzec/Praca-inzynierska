<template>

  <Header></Header>

  <section class="main-section">
    <section v-if="Object.keys(document).length" class="browser">

      <div class="browser-header">
        <p>{{ document.title }}</p>
      </div>

      <div class="separator"></div>

      <section class="advanced-section-adding">
        <div v-if="document.place.country || document.place.voivodeship || document.place.city" class="detail">
          <strong>Miejsce:</strong> {{ formatPlace(document.place) }}
        </div>

        <div v-if="document.startDate || document.endDate" class="detail">
          <strong>Przedział dat:</strong> {{ document.startDate }} - {{ document.endDate }}
        </div>
        <!--        &lt;!&ndash;      <div class="detail">&ndash;&gt;-->
        <!--        &lt;!&ndash;        <strong>Data:</strong> {{ formatDate(document.date) }}&ndash;&gt;-->
        <!--        &lt;!&ndash;      </div>&ndash;&gt;-->
        <div class="detail">
          <strong>Rodzaj:</strong> {{ document.type.name }}
        </div>
        <div v-if="document.peopleDocuments && document.peopleDocuments.length > 0" class="detail">
          <strong>Osoby występujące:</strong> {{ formatPeopleDocuments(document.peopleDocuments) }}
        </div>

        <button class="button-modal" @click="goBack">Powrót</button>
        <button class="button-modal" @click="goBack">Edytuj</button>
      </section>
    </section>
  </section>

</template>

<script setup>
import Header from "@/components/GenealogistView/Header.vue";
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

const formatPeopleDocuments = (people) => {
  return people.map(person => `${person.firstName} ${person.lastName}`).join(', ');
};
</script>

<style scoped>

.main-section {
  margin: 0;
  background-color: var(--grey);
  height: 100%;
}

.document-details {
  padding: 20px;
  font-size: 16px;
}

.detail {
  margin-bottom: 12px;
}
button {
  padding: 8px 16px;
}
</style>
