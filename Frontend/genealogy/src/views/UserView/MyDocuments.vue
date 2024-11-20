<script setup>

import Header from "@/components/UserView/Header.vue";
import Results from "@/components/LoggedUserView/Results.vue";
import LogoutModal from "@/components/MainView/LogoutModal.vue";
import {onMounted, ref} from "vue";
let searchResults = ref([]);

const showModal = ref(false);
let errorText = ref('');

const fetchDocuments = async () => {
  try {

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/GetMyDocuments`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }
    });

    if (response.status === 401) {
      showModal.value = true;
      errorText.value = await response.text();
    }

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

  <LogoutModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />

</template>

<style scoped>

</style>
