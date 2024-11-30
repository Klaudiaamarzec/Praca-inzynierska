<script setup>

import Header from "@/components/GenealogistView/Header.vue";
import MyLocations from "@/components/LoggedUserView/MyLocations.vue";
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
