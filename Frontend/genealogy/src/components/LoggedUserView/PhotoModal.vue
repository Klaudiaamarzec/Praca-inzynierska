<script setup>
import { defineProps, defineEmits, ref } from 'vue';
defineProps(['showModal']);

const photo = ref(null);
const photoBase64 = ref(null);
const emit = defineEmits(['close', 'submit']);

const closeModal = () => {
  emit('close');
};

const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    photo.value = file;

    const reader = new FileReader();
    reader.onload = () => {
      photoBase64.value = reader.result;
    };
    reader.readAsDataURL(file);
  }
};

const submitPhoto = () => {
  if (photo.value) {
    //emit('submit', photo.value);  // Send the photo data back to parent component
    emit('submit', { photoFile: photo.value, photoBase64: photoBase64.value });
    closeModal();
  } else {
    alert('Proszę wybrać zdjęcie.');
  }
};
</script>

<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Dodaj zdjęcie dokumentu</h2>
      </div>
      <div>
        <label for="photoUpload" class="upload-label">Wybierz zdjęcie: </label>
        <input type="file" id="photoUpload" @change="handleFileUpload" accept="image/*" />
      </div>
      <div v-if="photo" class="photo-preview">
        <img :src="photoBase64" alt="Podgląd zdjęcia" />
      </div>
      <button class="button-modal" @click="submitPhoto">Zatwierdź</button>
      <button class="button-modal" @click="closeModal">Anuluj</button>
    </div>
  </div>
</template>

<style scoped>



</style>
