<script setup>
import EditDocument from "@/components/LoggedUserView/EditDocument.vue";
import Header from "@/components/UserView/Header.vue";
</script>

<template>

  <Header/>

  <section>
    <EditDocument/>
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
