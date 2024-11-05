<script setup>

import Header from "@/components/MainView/Header.vue";
import Results from "@/components/MainView/Results.vue";
import {onMounted, ref} from "vue";
let searchResults = ref([]);

const fetchDocuments = async () => {
  try {
    const response = await fetch("http://127.0.0.1:8080/API/Documents/All");

    if (!response.ok) {
      console.error(`Nie udało się pobrać dokumentów: ${response.status} - ${response.statusText}`);
      return;
    }

    const data = await response.json();
    if (Array.isArray(data)) {
      searchResults.value = data;
    } else {
      console.error("Nieprawidłowy format danych:", data);
    }
  } catch (error) {
    console.error("Błąd podczas pobierania dokumentów:", error);
  }
};

onMounted(() => {
  fetchDocuments();
});

</script>

<template>

  <Header/>

  <section class="results-section">
    <div class="search-results">
      <Results v-if="searchResults.length" :results="searchResults" />
    </div>
  </section>

</template>

<style scoped>

</style>
