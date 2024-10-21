<template>
  <div>
    <Header/>

    <section>
      <Browser/>
    </section>

  </div>
</template>

<script setup>
import Header from '../../components/MainView/Header.vue';
import Browser from "../../components/MainView/Browser.vue";
</script>

<style scoped>

section {
  margin: 20px 0;
}

</style>
