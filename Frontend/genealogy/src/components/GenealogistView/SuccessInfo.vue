<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Operacja zakończyła się sukcesem</h2>
      </div>
      <p>{{ successDetails }}</p>
      <button class="button-modal" @click="closeModal">OK</button>
    </div>
  </div>
</template>

<script setup>

import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps(['showModal', 'successDetails', 'redirectTo']);
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

  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (props.redirectTo === '/Notifications') {

    if (userRole === 'genealogist') {
      router.push({ name: 'Notifications' });
    } else {
      console.log("Brak uprawnień do odwiedzenia strony!");
    }

  } else if (props.redirectTo === 'stay') {
    return;
  }


};

</script>

<style scoped>

</style>
