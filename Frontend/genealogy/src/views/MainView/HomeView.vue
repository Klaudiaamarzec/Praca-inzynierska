<template>
  <div>
    <Header/>

    <section class="genealogy">
      <Genealogy/>
    </section>

    <section>
      <Browser/>
    </section>

  </div>
</template>

<script setup>
import Header from '../../components/MainView/Header.vue';
import Browser from "../../components/MainView/Browser.vue";
import Genealogy from "../../components/MainView/Genealogy.vue";
</script>

<style scoped>

section {
  margin: 0;
  background-color: var(--grey);
  height: 100%;
}

.genealogy {
  display: block;
  padding-left: 0;
  padding-right: 0;
}

</style>
