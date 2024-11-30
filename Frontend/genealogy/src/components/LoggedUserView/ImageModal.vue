<template>
  <div v-if="showModal" class="modal">
    <div class="image-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
      </div>
      <div class="container-image">
        <img
          :src="`/${path}`"
          alt="Zdjęcie dokumentu"
          @click="drawTag($event)"
          class="document-image-fullscreen"
          ref="imageRef"
        />
        <div
          v-if="modalTag"
          class="tag"
          :style="{ left: `${tag.x}%`, top: `${tag.y}%` }"
        > <img src="../../icons/pin.png" alt="Pinezka" class="pin-icon"/> </div>
      </div>
      <div class="right-button-section">
        <p v-if="errorMessageVisible" style="padding-top: 20px; color: red;">Nie zaznaczono osoby na zdjęciu!</p>
        <button class="add-button" @click="saveTags">Zapisz</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue';

defineProps(['showModal', 'path']);
const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

const tag = ref(null);
const modalTag = ref(null);
const imageRef = ref(null);
const errorMessageVisible = ref(false);

const drawTag = (event) => {

  const imageContainer = imageRef.value; // Find the parent container
  const rect = imageContainer.getBoundingClientRect(); // Get the position and size of the image container

  // Obliczenie współrzędnych x, y w procentach w odniesieniu do rozmiaru obrazu
  const x = ((event.offsetX) / rect.width) * 100; // Use offsetX to get the click position relative to the container
  const y = ((event.offsetY) / rect.height) * 100; // Use offsetY to get the click position relative to the container

  const modalX = event.offsetX;
  const modalY = event.offsetY;

  // Dodanie tagu do tablicy
  tag.value = { x: x, y: y };
  modalTag.value = {x: modalX, y: modalY};

};

const saveTags = () => {

  if(!tag.value)
    errorMessageVisible.value = true;
  else {
    emit('tag', tag.value);
    emit('close');
  }
};

</script>

<style scoped>

.document-image-fullscreen {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  margin: auto;
  border: 2px solid var(--dark-brown);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  cursor: crosshair;
}

.container-image {
  max-width: 100%;
  max-height: 88%;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tag {
  position: absolute;
  width: 24px;
  height: 24px;
  transform: translate(-50%, -100%);
}

.pin-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

</style>
