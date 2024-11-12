<template>
  <div v-if="showModal" class="modal">
    <div class="image-content">
      <div>
        <span class="close" @click="closeModal">&times;</span>
      </div>
      <img
        :src="`/${path}`"
        alt="Zdjęcie dokumentu"
        class="document-image-details"
      />
      <div
        v-for="(personDocument, index) in peopleDocuments"
        :key="index"
        :class="{ 'person-tag': true, 'drop-in': showTags }"
        :style="getTagStyle(personDocument.x, personDocument.y)">
        <span class="person-info">{{ personDocument.firstName }} {{ personDocument.lastName }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, onMounted, ref } from 'vue';

defineProps({
  showModal: Boolean,
  path: String,
  peopleDocuments: Array,
});
const emit = defineEmits(['close']);
const showTags = ref(false);

const getTagStyle = (x, y) => {
  return {
    top: `${y}%`,
    left: `${x}%`,
    transform: 'translate(-50%, -50%)'
  };
};

const closeModal = () => {
  emit('close');
};

onMounted(() => {
  setTimeout(() => {
    showTags.value = true;
  }, 200);
});

</script>

<style scoped>

</style>
