<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Wystąpił błąd podczas autoryzacji</h2>
      </div>
      <p>{{ errorDetails }}</p>
      <button class="button-modal" @click="closeModal">OK</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';

defineProps(['showModal', 'errorDetails']);
const emit = defineEmits(['close']);
const router = useRouter();

const closeModal = () => {
  //localStorage.removeItem('token');
  emit('close');
  router.push({ name: 'Login' });
};
</script>

<style scoped>

</style>
