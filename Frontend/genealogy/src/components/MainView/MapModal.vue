<template>
  <div v-if="showMap" class="modal">
    <div class="modal-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
        <h2>Miejsce wydarzenia</h2>
      </div>
      <div id="map" style="height: 400px; width: 100%;"></div>
      <button class="button-modal" @click="closeModal">Zamknij</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, onMounted } from 'vue';
import L from 'leaflet';

const props = defineProps(['showMap', 'latitude', 'longitude']);
const emit = defineEmits(['close']);

// Funkcja do zamknięcia modala
const closeModal = () => {
  emit('close');
};

// Inicjalizacja mapy Leaflet po zamontowaniu komponentu
onMounted(() => {
  if (props.latitude && props.longitude) {
    const map = L.map('map').setView([props.latitude, props.longitude], 13);

    // Dodanie warstwy mapy
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Dodanie markera w miejscu lokalizacji
    L.marker([props.latitude, props.longitude]).addTo(map)
      .bindPopup('Miejsce znajduje się tutaj!')
      .openPopup();
  }
});
</script>

<style scoped>

.modal-content {
  width: 80%;
  max-width: 800px;
}
</style>
