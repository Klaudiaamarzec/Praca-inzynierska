<script setup>

import AddDocumentType from "@/components/GenealogistView/AddDocumentType.vue";
import Header from "@/components/GenealogistView/Header.vue";
</script>

<template>

  <Header/>

  <section>
    <AddDocumentType/>
  </section>

</template>

<style scoped>

section {
  background-color: var(--grey);
  height: 100vh;
  display: block;
  padding: 30px 0;
  margin: 0;
}

</style>
