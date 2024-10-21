<template>
  <div>
    <Header/>

    <!-- Sekcja wyszukiwarki lub wyników -->
    <section v-if="!searchResults">
      <Browser @searchResults="handleSearchResults"/> <!-- Emituje wyniki do HomeView -->
    </section>

    <section v-if="searchResults">
      <Results :results="searchResults" @backToSearch="clearSearch"/> <!-- Przekazuje wyniki do Results -->
    </section>

<!--    <section>-->
<!--      <Browser/>-->
<!--    </section>-->

  </div>
</template>

<script setup>
import { ref } from 'vue';
import Header from '../../components/MainView/Header.vue';
import Browser from "@/components/MainView/Browser.vue";

import Results from '@/components/MainView/Results.vue';  // Import komponentu wyników

const searchResults = ref(null);

const handleSearchResults = (results) => {
  searchResults.value = results; // Odbiera wyniki z Browser i ustawia je
};

const clearSearch = () => {
  searchResults.value = null; // Powrót do wyszukiwarki
};
</script>

<style scoped>

section {
  margin: 20px 0;
}

</style>
