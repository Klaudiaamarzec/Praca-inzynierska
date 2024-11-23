<script setup>

import {ref, onMounted} from "vue";
import { useRouter } from 'vue-router';

const results = ref([]);
const parameter = ref("")
const router = useRouter();

const decodeJWT = (token) => {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
      .join('')
  );
  return JSON.parse(jsonPayload);
};

onMounted(async () => {
  await fetchAllPeople();
});

// Funkcja pobierająca wszystkich ludzi
const fetchAllPeople = async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/People/All');
    results.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania osób:', error);
  }
};

const searchPerson = async () => {
  if(parameter.value === "") {
    await fetchAllPeople();
  }
  else {
    try {
      const response = await fetch(`http://127.0.0.1:8080/API/People/Search/${parameter.value}`);
      results.value = await response.json();
    } catch (error) {
      console.error('Błąd podczas wyszukiwania osób:', error);
    }
  }
}

const viewPersonDetails = (personID) => {
  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistPersonDetails', params: { personID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserPersonDetails', params: { personID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }
}

const addPerson = (personID) => {
  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistAddPerson', params: { personID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserAddPerson', params: { personID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }
}

</script>

<template>

  <section class="results-section">

    <div class="results-container">

      <div class="filterMenu">
        <input class="filter-input" type="text" placeholder="Znajdź" id="parameter" v-model="parameter" @keyup.enter="searchPerson" required />
        <button class="search-button" @click="searchPerson">Szukaj</button>
        <button class="search-button" @click="addPerson">Dodaj osobę</button>
      </div>

      <div class="search-results">
        <section class="result-list">
          <div class="person-item"
               v-for="(result, index) in results"
               :key="index"
               @click="viewPersonDetails(result.id)">
            <img class="profile-image" src="../../icons/profile.png" alt="Podląd awatara"/>
            <p> {{ result.name }} {{ result.surname }}</p>
            <p v-if="result.birthDate"><strong>Data urodzenia:</strong> {{ result.birthDate }}</p>
          </div>

        </section>
      </div>

    </div>


  </section>

</template>

<style scoped>

.result-list {
  margin-top: 10px;
}

</style>
