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
defineProps(['showModal', 'successDetails']);
const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

</script>

<style scoped>

</style>
