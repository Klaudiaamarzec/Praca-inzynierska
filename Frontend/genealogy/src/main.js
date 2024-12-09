import './assets/main.css'
import 'leaflet/dist/leaflet.css';

// Instncja aplikacji
import { createApp } from 'vue'
import { createPinia } from 'pinia'

// Tutaj importujemy App.vue i jest ono odpalane
import vuetify from './plugins/vuetify';
import App from './App.vue'
import router from './router'

// KOMPONENT GŁÓWNY
// Będzie zawierał w sobie więcej komponentów
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(vuetify);

// Za pomocą tej funkcji aplikacja renderuje komponenty
// Oczekuje argumentu 'container'
// Jest to nazwa id elementu z App.vue
app.mount('#app')
// To instancja głównego komponentu, nie instancja aplikacji
