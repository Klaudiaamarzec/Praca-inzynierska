<script setup>

import {defineProps, defineEmits, ref} from 'vue';
import {useRouter} from "vue-router";
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";

const props = defineProps(['showModal', 'docID']);
const emit = defineEmits(['close']);
const router = useRouter();
const documentID = props.docID;

const showModalError = ref(false);
let errorText = ref('');

const closeModal = () => {
  emit('close');
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

const goBackToDocuments = () => {
  emit('close');

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistDocuments'});
  } else {
    console.log("Brak uprawnień!");
  }
}

const deleteDocument = async () => {
  try {

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Delete/${documentID}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }
    });

    // if (response.status === 401) {
    //   showLogoutModal.value = true;
    //   logoutText.value = await response.text();
    // }
    //

    if (!response.ok) {
      //closeModal();
      showModalError.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas usuwania dokumentu";
      return;
    }

    goBackToDocuments();


  } catch (error) {
    console.error('Błąd podczas usuwania dokumentu:', error);
    showModalError.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Czy na pewno chcesz usunąć dokument?</h2>
      </div>
      <button class="button-modal" @click="deleteDocument">TAK</button>
      <button class="button-modal" @click="closeModal">NIE</button>
    </div>
  </div>

  <ErrorModal v-if="showModalError" :showModal="showModalError" :errorDetails="errorText" @close="showModalError = false" />
</template>

<style scoped>

</style>
