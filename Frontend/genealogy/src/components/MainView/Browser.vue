<script setup>
import { ref, onMounted, defineEmits } from 'vue';
const emit = defineEmits(['searchResults']); // Definiowanie emitowania zdarzenia

const documentTypes = ref([]);
const name = ref('');
const surname = ref('');
const selectedTypes = ref([]);

onMounted(async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/DocumentTypes/All');
    documentTypes.value = await response.json();
  } catch (error) {
    console.error('Error fetching document types:', error);
  }
});

const searchDocuments = async () => {

  try {
    const params = new URLSearchParams({
      name: name.value,
      surname: surname.value,
      typeIds: selectedTypes.value.join(',')
    });

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Search?${params}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error('Odpowiedź serwera nie była poprawna');
    }

    const results = await response.json();
    emit('searchResults', results); // Emituje wyniki do HomeView

    // const searchResults = await response.json();
    // console.log(searchResults);
  } catch (error) {
    console.error('Błąd podczas wyszukiwania dokumentów:', error);
  }
};

</script>

<template>

  <section class="browser">

    <div class="browser-header">
      <p>Wyszukiwarka</p>
    </div>

    <div class="separator"></div>

    <section class="content">

      <div class="left-site">

        <div class="login-section">
          <label for="name">Imię</label>
          <input id="name"  v-model="name"/>
        </div>

        <div class="login-section">
          <label for="surname">Nazwisko</label>
          <input id="surname"  v-model="surname"/>
        </div>
      </div>

      <div class="right-site">
        <ul>
          <div class="li-section">
            <li v-for="(type, index) in documentTypes" :key="index">
              <label >
                <input type="checkbox" :value="type.id" v-model="selectedTypes"> <!-- v-model do przechowywania zaznaczonych typów -->
                {{ type.typeName }}
              </label>
            </li>
          </div>
        </ul>
      </div>

    </section>

    <button @click="searchDocuments">Szukaj</button>

<!--    <div v-if="searchResults.length">-->
<!--      <h2>Wyniki wyszukiwania:</h2>-->
<!--      <ul>-->
<!--        <li v-for="(result, index) in searchResults" :key="index">-->
<!--          {{ result | json }} &lt;!&ndash; Zmień na odpowiednie właściwości &ndash;&gt;-->
<!--        </li>-->
<!--      </ul>-->
<!--    </div>-->

  </section>
</template>

<style scoped>

.browser {
  border: 2px solid #c9ad6e;
  width: 75%;
  display: flex;
  flex-direction: column;
  margin: auto;
  border-radius: 15px;
  padding-left: 0;
  padding-right: 0;
}

input {
  border: 2px solid #c9ad6e;
  border-radius: 10px;
  height: 35px;
  padding-left: 5px;
}

button {
  padding: 10px 15px;
  background-color: var(--brown);
  color: #000000;
  border: none;
  font-weight: bold;
  border-radius: 30px;
  cursor: pointer;
}

button:hover {
  background-color: var(--dark-brown);
  color: #eeebe9;
}

.custom-checkbox {
  display: none; /* Ukrywa standardowy checkbox */
}

.label-container {
  position: relative;
  padding-left: 30px; /* Odstęp dla niestandardowego checkboxa */
  cursor: pointer; /* Kursor zmienia się na wskaźnik */
}

.label-container::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 20px; /* Szerokość niestandardowego checkboxa */
  height: 20px; /* Wysokość niestandardowego checkboxa */
  border: 2px solid var(--brown); /* Brązowy kolor ramki */
  border-radius: 3px; /* Opcjonalne, zaokrąglone rogi */
  background-color: white; /* Tło checkboxa */
  transition: background-color 0.3s, border-color 0.3s; /* Płynna zmiana kolorów */
}

.custom-checkbox:checked + .label-container::before {
  background-color: var(--brown); /* Tło po zaznaczeniu (brązowe) */
}

.custom-checkbox:checked + .label-container::after {
  content: '✔'; /* Dodanie ptaszka */
  position: absolute;
  left: 4px; /* Ustawienie pozycji ptaszka */
  top: 50%;
  transform: translateY(-50%);
  color: white; /* Kolor ptaszka */
  font-size: 1px;
  display: block;
}

</style>
