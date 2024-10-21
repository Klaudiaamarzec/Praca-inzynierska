<script setup>
import { useRoute } from 'vue-router';
import Header from "@/components/MainView/Header.vue";
import Results from "@/components/MainView/Results.vue";

const route = useRoute();
const searchResults = JSON.parse(route.query.results || '[]');
//const searchResults = route.state.results || [];

</script>

<template>

  <Header/>

  <section class="results-container">

    <div class="menu">
      <h2>Filtry</h2>
      <label>
        <input type="checkbox" > Filtr 1
      </label>
      <label>
        <input type="checkbox" > Filtr 2
      </label>
    </div>

    <div class="search-results">
      <Results :results="searchResults" />
    </div>

  </section>

</template>

<style scoped>

.results-container {
  display: grid;
  grid-template-columns: 2fr 8fr;
  padding: 0;
  height: 100vh;
}

.menu {
  border-right: 3px solid var(--brown);
  display: flex;
  flex-direction: column;
  height: 100%;
  padding-top: 20px;
  padding-left: 10px;
}

.search-results {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 100%;
}

</style>
