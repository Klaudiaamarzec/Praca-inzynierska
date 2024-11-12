<script setup>
import { useRoute } from 'vue-router';
import AddPhotosToDocument from "@/components/LoggedUserView/AddPhotos.vue";
import Header from "@/components/GenealogistView/Header.vue";

const route = useRoute();
const documentID = route.query.documentID;
</script>

<template>

  <Header/>

  <AddPhotosToDocument :documentID="documentID"/>

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
