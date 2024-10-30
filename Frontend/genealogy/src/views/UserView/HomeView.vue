<template>
  <div>
    <Header />

    <section class="genealogy">
      <Genealogy/>
    </section>

    <section>
      <Browser/>
    </section>

  </div>
</template>

<script setup>
import Header from '../../components/UserView/Header.vue';
import Browser from "@/components/UserView/Browser.vue";
import Genealogy from "@/components/UserView/Genealogy.vue";
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
