<script setup>

import Header from "@/components/GenealogistView/Header.vue";
import Notifications from "@/components/GenealogistView/Notifications.vue";
</script>

<template>
  <div>

    <Header />

    <section>
      <Notifications/>
    </section>

  </div>
</template>

<style scoped>

section {
  background-color: var(--grey);
  height: 100%;
  display: block;
  padding: 0;
  margin: 0;
}

</style>
