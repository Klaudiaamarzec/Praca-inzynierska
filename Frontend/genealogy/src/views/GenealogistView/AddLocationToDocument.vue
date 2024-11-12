<script setup>
import { useRoute } from 'vue-router';
import Header from "@/components/GenealogistView/Header.vue";
import AddLocationToDocument from "@/components/LoggedUserView/AddLocationToDocument.vue";

const route = useRoute();
const documentID = route.query.documentID;

</script>

<template>

  <Header/>

  <AddLocationToDocument :documentID="documentID"/>

</template>

<style scoped>

section {
  background-color: var(--grey);
  height: 100%;
  display: block;
  padding: 30px 0;
  margin: 0;
}

</style>
