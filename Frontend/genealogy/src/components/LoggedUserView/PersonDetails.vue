<script setup>

import {useRoute, useRouter} from 'vue-router';
import {ref, computed, onMounted, watch} from 'vue';

const router = useRouter();
const route = useRoute();

// Pobierz dokument z danych przekazanych przez route
const personID = computed(() => route.params.personID || {});

const result = ref({});

const goBack = () => {
  router.back();
};

const loadPersonData = async () => {
  try {
    const response = await fetch(`http://127.0.0.1:8080/API/People/Person/${personID.value}`);
    if (response.ok) {
      result.value = await response.json();
    } else {
      console.error("Nie udało się pobrać danych osoby");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
};

onMounted(() => {
  loadPersonData();
});
watch(personID, () => {
  loadPersonData();
});


const formatPersonInFamily = (person) => {
  return `${person.name} ${person.surname}`;
};

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

const openDocument = (documentID) => {

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistDocumentDetails', params: { documentID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserDocumentDetails', params: { documentID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }

}

const showPerson = (personID) => {

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  console.log("ID PERSON: ", personID)

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistPersonDetails', params: { personID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserPersonDetails', params: { personID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }

}

</script>

<template>

  <section class="main-section">
    <section v-if="Object.keys(result).length" class="browser">

      <div class="browser-header">
        <p>{{ result.person.name }} {{ result.person.surname }}</p>
      </div>

      <div class="separator"></div>

      <section class="header-section">
        <div class="left-section">
          <button class="button-modal" @click="goBack">Powrót</button>
        </div>
      </section>

      <section class="advanced-section-adding">

        <div v-if="result.person.birthDate" class="detail">
          <strong>Data urodzenia:</strong> {{ result.person.birthDate }}
        </div>

        <div v-if="result.parents && (result.parents.mother || result.parents.father)" class="detail-section2">
          <strong>Rodzice:</strong>
          <ul class="people-list">
            <li v-if="result.parents.mother">
              - <a href="#" @click="showPerson(result.parents.mother.id)" class="urls">{{ formatPersonInFamily(result.parents.mother) }}</a>
            </li>
            <li v-if="result.parents.father">
              - <a href="#" @click="showPerson(result.parents.father.id)" class="urls">{{ formatPersonInFamily(result.parents.father) }}</a>
            </li>
          </ul>
        </div>

        <div v-if="result.children && result.children.length > 0" class="detail-section2">
          <strong>Dzieci:</strong>
          <ul class="people-list">
            <li v-for="(child, index) in result.children" :key="index">
              - <a href="#" @click="showPerson(child.id)" class="urls">{{ formatPersonInFamily(child) }}</a>
            </li>
          </ul>
        </div>

        <div v-if="result.person.personDocuments && result.person.personDocuments.length > 0" class="detail-section2">
          <strong>Powiązane dokumenty:</strong>
          <ul class="people-list">
            <li v-for="(document, index) in result.person.personDocuments" :key="index">
              - <a href="#" @click="openDocument(document.documentId)" class="urls">{{ document.documentTitle }}</a>
            </li>
          </ul>
        </div>

      </section>

    </section>
  </section>

</template>

<style scoped>

.main-section {
  height: 100%;
}

.header-section {
  width: 100%;
  padding: 0 20px 20px;
}

.browser {
  margin-top: 10px;
  padding-bottom: 50px;
  width: 75%;
}

.urls {
  color: black;
  font-weight: normal;
}

.button-modal {
  width: 100%;
}

.detail-section2 {
  padding-left: 15px;
}

</style>
