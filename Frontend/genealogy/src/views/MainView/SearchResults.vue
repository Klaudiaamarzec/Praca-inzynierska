<script setup>
import { useRoute } from 'vue-router';
import Header from "@/components/MainView/Header.vue";
import Results from "@/components/MainView/Results.vue";

const route = useRoute();
const searchResults = JSON.parse(route.query.results || '[]');

</script>

<template>

  <Header/>

<!--  <section class="results-container">-->

<!--    <div class="filterMenu">-->
<!--      <label>-->
<!--        <input type="checkbox" > Filtr 1-->
<!--      </label>-->
<!--      <label>-->
<!--        <input type="checkbox" > Filtr 2-->
<!--      </label>-->
<!--    </div>-->

<!--    <div class="search-results">-->
<!--      <Results :results="searchResults" />-->
<!--    </div>-->

<!--  </section>-->

  <section class="results-section">
    <div class="search-results">
      <Results :results="searchResults" />
    </div>
  </section>

</template>

<style scoped>

.results-section {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  background-color: var(--grey);
  padding: 0;
}

</style>
