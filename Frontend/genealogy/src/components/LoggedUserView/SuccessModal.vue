<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>{{ successDetails }}</h2>
      </div>
      <p>Możesz teraz dodać zdjęcie i osoby do dokumentu oraz lokalizację (miejsce przechowywania)</p> <!-- Wyświetl szczegóły błędu -->
      <button class="button-modal">Dodaj zdjęcie</button>
      <button class="button-modal">Dodaj osoby</button>
      <button class="button-modal">Dodaj lokalizacje</button>
      <button class="button-modal" @click="closeModal">Pomiń</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps(['showModal', 'successDetails', 'docID']);
const emit = defineEmits(['close']);
const router = useRouter();
const documentID = props.docID;
console.log("Document ID: ", props.docID);

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

const closeModal = () => {
  emit('close');

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
};
</script>

<style scoped>

</style>
