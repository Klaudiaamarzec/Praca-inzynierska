<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Wystąpił błąd</h2>
      </div>
      <p>{{ errorDetails }}</p>
      <button class="button-modal" @click="closeModal">OK</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import {useRouter} from "vue-router";

const props = defineProps(['showModal', 'errorDetails']);
const emit = defineEmits(['close']);
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

const closeModal = () => {
  emit('close');

  if (props.errorDetails === "Wystąpił nieoczekiwany błąd: Nie znaleziono dokumentu o id: 80") {
    const token = localStorage.getItem('jwtToken');
    const decodedToken = decodeJWT(token);
    const userRole = decodedToken.role;

    if (userRole === 'genealogist') {
      router.push({ name: 'GenealogistDocuments' });
    }
  }
};
</script>

<style scoped>

</style>
