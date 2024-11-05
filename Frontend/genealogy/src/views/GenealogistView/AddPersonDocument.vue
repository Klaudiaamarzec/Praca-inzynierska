<script setup>
import { useRoute } from 'vue-router';
import Header from "@/components/GenealogistView/Header.vue";
import AddPersonToDocument from "@/components/LoggedUserView/AddPersonToDocument.vue";

const route = useRoute();
const documentID = route.query.documentID;

</script>

<template>

  <Header/>

  <AddPersonToDocument :documentID="documentID"/>

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
