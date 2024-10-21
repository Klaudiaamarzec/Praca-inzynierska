<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  results: Array
});

const results = props.results;

console.log('Przekazane wyniki:', results)

const formatDate = (date) => {
  const day = date.day ? String(date.day).padStart(2, '0') : '';
  const month = date.month ? String(date.month).padStart(2, '0') : '';
  const year = date.year ? date.year : '';

  if (day && month) {
    return `${day}.${month}.${year}`;
  } else if (month) {
    return `${month}.${year}`;
  } else {
    return `${year}`;
  }
};

// const formatRangeDate = (date) => {
//   if (!date) return null;
//
//   const day = String(date.getDate()).padStart(2, '0');
//   const month = String(date.getMonth() + 1).padStart(2, '0'); // Miesiące są indeksowane od 0
//   const year = date.getFullYear();
//
//   return { day, month, year };
// };

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
         :key="index">
      <div class="title">{{ result.title }}</div>
      <div class="details">
        <p v-if="result.place.country || result.place.voivodeship || result.place.city">Miejsce: {{ formatPlace(result.place) }}</p>
        <p v-if="result.startDate || result.endDate">Przedział dat: {{ result.startDate}} - {{ result.endDate }}</p>
        <p v-if="result.date">Data:
          <template v-if="result.date">
              <span>
                {{ formatDate(result.date) }}
              </span>
          </template>
        </p>
        <p>Rodzaj: {{ result.type.name }}</p>
        <p v-if="result.peopleDocuments && result.peopleDocuments.length > 0">
          Osoby występujące w dokumencie:
          <span >
            {{ formatPeopleDocuments(result.peopleDocuments) }}
          </span>
        </p>
      </div>
    </div>

  </section>

</template>

<style scoped>

.result-list {
  display: flex;
  flex-direction: column;
  margin: 25px auto;
  width: 95%;
  gap: 25px;
}

.list-item {
  width: 100%;
  border-radius: 15px;
  border: 2px solid var(--brown);
}

.title {
  background-color: var(--brown);
  font-size: 19px;
  border-top-left-radius: 14px;
  border-top-right-radius: 14px;
  margin: auto;
  font-family: "Quintessential", serif;
  text-align: center;
  padding: 5px;
}

.details {
  padding: 10px 20px;
  display: flex;
  flex-direction: column;
}

</style>
