<script setup>
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({
  results: Array
});

const results = props.results;
const router = useRouter();

const viewDocumentDetails = (documentID) => {
  router.push({ name: 'MainDocumentDetails', params: { documentID } });
};

// const formatDate = (date) => {
//   const day = date.day ? String(date.day).padStart(2, '0') : '';
//   const month = date.month ? String(date.month).padStart(2, '0') : '';
//   const year = date.year ? date.year : '';
//
//   if (day && month) {
//     return `${day}.${month}.${year}`;
//   } else if (month) {
//     return `${month}.${year}`;
//   } else {
//     return `${year}`;
//   }
// };

const formatDate = (date) => {
  const day = date.day ? String(date.day).padStart(2, '0') : '';
  const month = date.month ? String(date.month).padStart(2, '0') : '';
  const year = date.year ? date.year : '';
  return day && month ? `${day}.${month}.${year}` : month ? `${month}.${year}` : `${year}`;
};

const formatPlace = (place) => {
  const parts = [];
  if (place.country) {
    parts.push(place.country);
  }
  if (place.voivodeship) {
    parts.push(place.voivodeship);
  }
  if (place.city) {
    parts.push(place.city);
  }
  return parts.join(', ');
};

const formatPeopleDocuments = (people) => {
  const maxDisplay = 3;
  const displayedPeople = people.slice(0, maxDisplay);
  const displayedNames = displayedPeople.map(person => `${person.firstName} ${person.lastName}`).join(', ');

  // Sprawdzenie, czy jest więcej osób do wyświetlenia
  if (people.length > maxDisplay) {
    return `${displayedNames} ...`;
  }

  return displayedNames;
};

</script>

<template>

  <section class="result-list">
    <div class="list-item"
         v-for="(result, index) in results"
         :key="index"
         @click="viewDocumentDetails(result.id)">

      <div class="title">{{ result.title }}</div>

      <div class="content-wrapper" :class="{'single-column': !result.path}">

        <div class="details-container">

          <div class="details">
            <div v-if="result.place.country || result.place.voivodeship || result.place.city" class="detail-item">
              <span class="label">Miejsce:</span>
              <span>{{ formatPlace(result.place) }}</span>
            </div>
            <div v-if="result.startDate || result.endDate" class="detail-item">
              <span class="label">Przedział dat:</span>
              <span>{{ result.startDate }} - {{ result.endDate }}</span>
            </div>
            <div v-if="result.date" class="detail-item">
              <span class="label">Data:</span>
              <span>{{ formatDate(result.date) }}</span>
            </div>
            <div class="detail-item">
              <span class="label">Rodzaj:</span>
              <span>{{ result.type.name }}</span>
            </div>
            <div v-if="result.peopleDocuments && result.peopleDocuments.length > 0" class="detail-item">
              <span class="label">Osoby występujące w dokumencie:</span>
              <span>{{ formatPeopleDocuments(result.peopleDocuments) }}</span>
            </div>
          </div>
        </div>

        <div v-if="result.path" class="image-container">
          <img :src="`/${result.path}`" alt="Zdjęcie dokumentu" class="result-image"/>
        </div>

      </div>
    </div>

  </section>

</template>

<style scoped>


p {
  margin: 2px 0;
  line-height: 1.5;
}

</style>
