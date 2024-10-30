<template>
  <div>

    <Header/>

    <section class="login-form-container">
      <div class="login-container">
        <h2>Logowanie</h2>
        <form @submit.prevent="handleLogin">
          <div class="login-section">
            <label for="emailOrUsername">Email lub login:</label>
            <input class="main-input" type="text" id="emailOrUsername" v-model="email" required />
          </div>
          <div class="login-section">
            <label for="password">Hasło:</label>
            <input class="main-input" type="password" id="password" v-model="password" required />
          </div>
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
          <p>Nie pamiętam hasła</p>
          <button class="send-button" type="submit">Zaloguj się</button>
        </form>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Header from '../../components/MainView/Header.vue';
const errorMessage = ref('');

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

const email = ref('');
const password = ref('');

const handleLogin = async () => {

  try {
    const response = await fetch('http://127.0.0.1:8080/Auth/Login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        usernameOrEmail: email.value,
        password: password.value,
      }),
    });

    if (response.ok) {

      const token = await response.text();
      localStorage.setItem('jwtToken', token);

      const decodedToken = decodeJWT(token);
      const userRole = decodedToken.role;

      console.log("User role: ", userRole)

      if (userRole === 'genealogist') {
        window.location.href = '/homeGenealogist';
      } else if (userRole === 'user') {
        window.location.href = '/homeUser';
      } else {
        errorMessage.value = `Nieznana rola użytkownika!`;
      }
    } else {
      // Obsługa błędów logowania
      const errorMsg = await response.text();
      errorMessage.value = `${errorMsg}`;
    }
  } catch (error) {
    console.error('Wystąpił błąd podczas logowania:', error);
  }

};
</script>

<style scoped>

</style>
