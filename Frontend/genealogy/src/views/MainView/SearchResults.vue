<script setup>
import { useRoute } from 'vue-router';
import Header from "@/components/MainView/Header.vue";
import Results from "@/components/MainView/Results.vue";

const route = useRoute();
const searchResults = JSON.parse(route.query.results || '[]');

</script>

<template>

  <Header/>

  <section class="results-section">
    <div class="search-results">
      <Results :results="searchResults" />
    </div>
  </section>

</template>

<style scoped>

</style>
