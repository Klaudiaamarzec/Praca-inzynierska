import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/MainView/HomeView.vue'
import HomeUser from '../views/UserView/HomeView.vue'
import HomeGenealogist from '../views/GenealogistView/HomeView.vue'
import LoginView from "@/views/MainView/LoginView.vue";
import RegisterView from "@/views/MainView/RegisterView.vue";
import SearchResults from "@/views/MainView/SearchResults.vue";
import GenealogistSearchResults from "@/views/GenealogistView/SearchResults.vue";
import UserSearchResults from "@/views/UserView/SearchResults.vue";
import NotificationView from "@/views/GenealogistView/NotificationView.vue";
import NotificationDetails from "@/views/GenealogistView/NotificationDetails.vue";
import AddDocumentUser from "@/views/UserView/AddDocument.vue"
import AddDocumentGenealogist from "@/views/GenealogistView/AddDocument.vue"
import MainDocumentDetails from "@/views/MainView/DocumentDetails.vue";
import UserDocumentDetails from "@/views/UserView/DocumentDetails.vue";
import GenealogistDocumentDetails from "@/views/GenealogistView/DocumentDetails.vue";
import GenealogistAddPersonDocument from "@/views/GenealogistView/AddPersonDocument.vue";
import UserAddPersonDocument from "@/views/UserView/AddPersonDocument.vue";
import Documents from "@/views/MainView/Documents.vue";
import UserDocuments from "@/views/UserView/Documents.vue";
import GenealogistDocuments from "@/views/GenealogistView/Documents.vue";
import GenealogistAddLocation from "@/views/GenealogistView/AddLocationToDocument.vue";
import UserAddLocation from "@/views/UserView/AddLocationToDocument.vue";
import GenealogistEditDocument from "@/views/GenealogistView/EditDocument.vue";
import UserEditDocument from "@/views/UserView/EditDocument.vue";
import People from "@/views/MainView/People.vue";
import UserPeople from "@/views/UserView/People.vue";
import GenealogistPeople from "@/views/GenealogistView/People.vue";
import PersonDetails from "@/views/MainView/PersonDetails.vue";
import UserPersonDetails from "@/views/UserView/PersonDetails.vue";
import GenealogistPersonDetails from "@/views/GenealogistView/PersonDetalis.vue";
import GenealogistAddPhotosToDocument from "@/views/GenealogistView/AddPhotosToDocument.vue";
import UserAddPhotosToDocument from "@/views/UserView/AddPhotosToDocument.vue";
import AddDocumentType from "@/views/GenealogistView/AddDocumentType.vue";
import UserMyDocuments from "@/views/UserView/MyDocuments.vue";
import GenealogistMyDocuments from "@/views/GenealogistView/MyDocuments.vue";
import UserAddPerson from "@/views/GenealogistView/AddPerson.vue";
import GenealogistAddPerson from "@/views/GenealogistView/AddPerson.vue";
import UserMyLocalizations from "@/views/UserView/MyLocalizations.vue";
import GenealogistMyLocalizations from "@/views/GenealogistView/MyLocalizations.vue";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },

  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },

  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },

  {
    path: '/homeUser',
    name: 'HomeUser',
    component: HomeUser
  },

  {
    path: '/homeGenealogist',
    name: 'HomeGenealogist',
    component: HomeGenealogist
  },

  {
    path: '/results',
    name: 'SearchResults',
    component: SearchResults
  },

  {
    path: '/genealogist/results',
    name: 'GenealogistSearchResults',
    component: GenealogistSearchResults
  },

  {
    path: '/user/results',
    name: 'UserSearchResults',
    component: UserSearchResults
  },

  {
    path: '/genealogist/notifications',
    name: 'Notifications',
    component: NotificationView
  },

  {
    path: '/genealogist/notificationDetails/:notificationID',
    name: 'NotificationDetails',
    component: NotificationDetails
  },

  {
    path: '/user/addDocument',
    name: 'AddDocumentUser',
    component: AddDocumentUser
  },

  {
    path: '/genealogist/addDocument',
    name: 'AddDocumentGenealogist',
    component: AddDocumentGenealogist
  },

  {
    path: '/documentDetails/:documentID',
    name: 'MainDocumentDetails',
    component: MainDocumentDetails,
    props: true
  },

  {
    path: '/genealogist/documentDetails/:documentID',
    name: 'GenealogistDocumentDetails',
    component: GenealogistDocumentDetails,
    props: true
  },

  {
    path: '/user/documentDetails/:documentID',
    name: 'UserDocumentDetails',
    component: UserDocumentDetails,
    props: true
  },

  {
    path: '/user/addPersonToDocument',
    name: 'UserAddPersonDocument',
    component: UserAddPersonDocument,
    props: true
  },

  {
    path: '/genealogist/addPersonToDocument',
    name: 'GenealogistAddPersonDocument',
    component: GenealogistAddPersonDocument,
    props: true
  },

  {
    path: '/documents',
    name: 'Documents',
    component: Documents,
    props: true
  },

  {
    path: '/user/documents',
    name: 'UserDocuments',
    component: UserDocuments,
    props: true
  },

  {
    path: '/genealogist/documents',
    name: 'GenealogistDocuments',
    component: GenealogistDocuments,
    props: true
  },

  {
    path: '/genealogist/addLocation',
    name: 'GenealogistAddLocation',
    component: GenealogistAddLocation,
    props: true
  },

  {
    path: '/user/addLocation',
    name: 'UserAddLocation',
    component: UserAddLocation,
    props: true
  },

  {
    path: '/user/editDocument/:documentID',
    name: 'UserEditDocument',
    component: UserEditDocument,
    props: true
  },

  {
    path: '/genealogist/editDocument/:documentID',
    name: 'GenealogistEditDocument',
    component: GenealogistEditDocument,
    props: true
  },

  {
    path: "/people",
    name: 'People',
    component: People,
    props: true
  },

  {
    path: "/user/people",
    name: 'UserPeople',
    component: UserPeople,
    props: true
  },

  {
    path: "/genealogist/people",
    name: 'GenealogistPeople',
    component: GenealogistPeople,
    props: true
  },

  {
    path: '/personDetails/:personID',
    name: 'PersonDetails',
    component: PersonDetails,
    props: true
  },

  {
    path: '/genealogist/personDetails/:personID',
    name: 'GenealogistPersonDetails',
    component: GenealogistPersonDetails,
    props: true
  },

  {
    path: '/user/personDetails/:personID',
    name: 'UserPersonDetails',
    component: UserPersonDetails,
    props: true
  },

  {
    path: '/genealogist/addPhotos',
    name: 'GenealogistAddPhotosToDocument',
    component: GenealogistAddPhotosToDocument,
    props: true
  },

  {
    path: '/user/addPhotos',
    name: 'UserAddPhotosToDocument',
    component: UserAddPhotosToDocument,
    props: true
  },

  {
    path: '/genealogist/addDocumentTyp',
    name: 'AddDocumentType',
    component: AddDocumentType,
    props: true
  },

  {
    path: '/genealogist/myDocuments',
    name: 'GenealogistMyDocuments',
    component: GenealogistMyDocuments,
    props: true
  },

  {
    path: '/user/myDocuments',
    name: 'UserMyDocuments',
    component: UserMyDocuments,
    props: true
  },

  {
    path: '/user/addPerson',
    name: 'UserAddPerson',
    component: UserAddPerson,
    props: true
  },

  {
    path: '/genealogist/addPerson',
    name: 'GenealogistAddPerson',
    component: GenealogistAddPerson,
    props: true
  },

  {
    path: '/user/locations',
    name: 'UserMyLocalizations',
    component: UserMyLocalizations,
    props: true
  },

  {
    path: '/genealogist/locations',
    name: 'GenealogistMyLocalizations',
    component: GenealogistMyLocalizations,
    props: true
  }

];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router
