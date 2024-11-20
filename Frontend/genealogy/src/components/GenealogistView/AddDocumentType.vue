<script setup>
import {ref} from "vue";

const typeName = ref('');

const fields = ref([
  {name: "", type: ""},
])

import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";
import SuccessInfo from "@/components/GenealogistView/SuccessInfo.vue";
const showSuccess = ref(false);
let successText = ref('');
const showModal = ref(false);
let errorText = ref('');

const addField = () => {
  fields.value.push({name: "", type: ""});
}

const removeField = (index) => {
  fields.value.splice(index, 1);
};

const addDocumentType = async () => {

  try {

    const template = JSON.stringify({ fields: fields.value });
    const documentTypeData = {

      typeName: typeName.value,
      template: template,
      parsedTemplate: {
        fields: fields.value
      }

    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/DocumentTypes/Add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(documentTypeData)
    });

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania nowego rodzaju dokumentu";
      return;
    }

    successText.value = await response.text();
    showSuccess.value = true;

  } catch (error) {
    console.error('Błąd podczas dodawania nowego rodzaju dokumentu:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>

  <section class="browser">
    <section class="advanced-section-adding">

      <div class="date-section">
        <label for="fromDate">Nazwa <span class="required-asterisk">*</span></label>
        <input class="input-title" type="text" id="typeName" v-model="typeName" />
      </div>

      <p>Dodaj dodatkowe pola</p>

      <div v-for="(field, index) in fields" :key="index" class="template-field">
        <input
          class="main-input"
          type="text"
          placeholder="Nazwa pola"
          v-model="field.name"
        />
        <select class="main-select" v-model="field.type">
          <option disabled value="">Wybierz typ danych</option>
          <option value="text">Tekst</option>
          <option value="bigint">Liczba</option>
          <option value="date">Data</option>
        </select>
        <button class="button-plus" @click="removeField(index)">-</button>
      </div>

<!--      <div class="template-field">-->
<!--        <input class="main-input" type="text" id="name" placeholder="Nazwa" v-model="name" />-->
<!--        <select class="main-select" v-model="type" required>-->
<!--          <option disabled value="">Wybierz typ danych</option>-->
<!--          <option>Tekst</option>-->
<!--          <option>Liczba</option>-->
<!--          <option>Data</option>-->
<!--        </select>-->
<!--      </div>-->

      <section class="header-section">
        <div class="left-section">
          <button class="button-plus" @click="addField">+</button>
        </div>
        <div class="right-section">
          <button class="button-modal" @click="addDocumentType">Dodaj</button>
        </div>
      </section>

    </section>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <SuccessInfo v-if="showSuccess" :showModal="showSuccess" :successDetails="successText" redirectTo="stay" @close="showSuccess = false" />

</template>

<style scoped>

.browser {
  display: flex;
  background: var(--light-white);
  width: 60%;
  margin: auto;
  height: auto;
  background: var(--light-white);
  padding-top: 15px;
  padding-bottom: 15px;
}

.input-title {
  width: 92%;
}

p {
  margin: 5px 0;
}

.button-modal {
  width: 100%;
}

.header-section {
  margin-top: 5px;
  padding: 0;
}

</style>
