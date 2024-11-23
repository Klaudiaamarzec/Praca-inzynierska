<script setup>

import AddPerson from "@/components/LoggedUserView/AddPerson.vue";
import Header from "@/components/UserView/Header.vue";
</script>

<template>

  <Header/>

  <section>
    <AddPerson/>
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
