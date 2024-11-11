<script setup>

import {useRoute, useRouter} from 'vue-router';
import {ref, computed, onMounted} from 'vue';

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

const formatPersonInFamily = (person) => {
  return `${person.name} ${person.surname}`;
};

const openDocument = (documentID) => {
  router.push({ name: 'MainDocumentDetails', params: { documentID } });
}

const showPerson = (personID) => {
  router.push({ name: 'PersonDetails', params: { personID } });
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
          <a @click="goBack"><p>Powrót</p></a>
        </div>
      </section>

      <section class="advanced-section-adding">

        <div v-if="result.person.birthDate" class="detail">
          <strong>Data urodzenia:</strong> {{ person.birthDate }}
        </div>

        <div v-if="result.parents && (result.parents.mother || result.parents.father)" class="detail">
          <strong>Rodzice:</strong>
          <ul class="people-list">
            <li v-if="result.parents.mother">
              <!--              - <strong>Matka:</strong> {{ formatPersonInFamily(result.parents.mother) }}-->
              - <a href="#" @click="showPerson(result.parents.mother.id)" class="urls">{{ formatPersonInFamily(result.parents.mother) }}</a>
            </li>
            <li v-if="result.parents.father">
              <!--              - <strong>Ojciec:</strong> {{ formatPersonInFamily(result.parents.father) }}-->
              - <a href="#" @click="showPerson(result.parents.father.id)" class="urls">{{ formatPersonInFamily(result.parents.father) }}</a>
            </li>
          </ul>
        </div>

        <div v-if="result.children && result.children.length > 0" class="detail">
          <strong>Dzieci:</strong>
          <ul class="people-list">
            <li v-for="(child, index) in result.children" :key="index">
              <!--              - {{ formatPersonInFamily(children) }}-->
              - <a href="#" @click="showPerson(child.id)" class="urls">{{ formatPersonInFamily(child) }}</a>
            </li>
          </ul>
        </div>

        <div v-if="result.person.personDocuments" class="detail">
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

</style>
