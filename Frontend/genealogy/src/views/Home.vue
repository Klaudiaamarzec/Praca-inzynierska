<script setup>

import Header from '../components/MainView/Header.vue';

const firstName = ref('');
const lastName = ref('');
const location = ref('');
const documentType = ref('');

const handleSubmit = () => {
  console.log({
    firstName: firstName.value,
    lastName: lastName.value,
    location: location.value,
    documentType: documentType.value
  });
}

</script>

<template>
  <div>
    <Header />
    <section>
      <h2>Katalogowanie źródeł genealogicznych</h2>
      <p>Znajdź nowych przodków! Dodaj to, co już wiesz!</p>
      <form @submit.prevent="handleSubmit">
        <div>
          <label for="firstName">Imię</label>
          <input type="text" id="firstName" v-model="firstName" />
        </div>
        <div>
          <label for="lastName">Nazwisko</label>
          <input type="text" id="lastName" v-model="lastName" />
        </div>
        <div>
          <label for="location">Lokalizacja</label>
          <input type="text" id="location" v-model="location" />
        </div>
        <div>
          <h3>Dokumenty</h3>
          <label><input type="radio" value="akty-metrykalne" v-model="documentType" /> Akty metrykalne</label>
          <label><input type="radio" value="swiadectwa-szkolne" v-model="documentType" /> Świadectwa szkolne</label>
          <label><input type="radio" value="dokumenty-czelnadze" v-model="documentType" /> Dokumenty czeladnicze</label>
          <label><input type="radio" value="plakaty" v-model="documentType" /> Plakaty, ulotki, afisze</label>
          <label><input type="radio" value="listy" v-model="documentType" /> Listy i korespondencje</label>
          <label><input type="radio" value="zdjecia" v-model="documentType" /> Zdjęcia</label>
        </div>
        <button type="submit">Szukaj</button>
      </form>
    </section>
  </div>
</template>

<style scoped>
section {
  text-align: center;
  margin: 20px;
}

form {
  display: inline-block;
}

div {
  margin: 10px 0;
}
</style>
