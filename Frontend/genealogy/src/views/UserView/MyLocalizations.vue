<script setup>

import MyLocations from "@/components/LoggedUserView/MyLocations.vue";
import Header from "@/components/UserView/Header.vue";
</script>

<template>

  <Header/>

  <section class="results-section">
    <div class="search-results">
      <MyLocations/>
    </div>
  </section>

</template>

<style scoped>

</style>
