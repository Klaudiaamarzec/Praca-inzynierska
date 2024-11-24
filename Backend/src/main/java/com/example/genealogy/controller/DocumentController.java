package com.example.genealogy.controller;

import com.example.genealogy.model.*;
import com.example.genealogy.service.*;
import com.example.genealogy.token.JwtUtil;
import com.example.genealogy.validator.OnUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/API/Documents")
public class DocumentController {

    private final DocumentService documentService;
    private final DateService dateService;
    private final UserService userService;
    private final AddressService addressService;
    private final NotificationService notificationService;
    private final PersonDocumentService personDocumentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DocumentController(DocumentService documentService, UserService userService, JwtUtil jwtUtil, DateService dateService, AddressService addressService, NotificationService notificationService, PersonDocumentService personDocumentService) {
        this.documentService = documentService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.dateService = dateService;
        this.addressService = addressService;
        this.notificationService = notificationService;
        this.personDocumentService = personDocumentService;
    }

    // 1. Dodawanie dokumentu

    @PostMapping("/Add")
    public ResponseEntity<?> addDocument(@Valid @RequestBody Document document, HttpServletRequest request) {

        try {
            // Pobierz token z nagłówka "Authorization"
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak tokenu JWT lub token jest nieprawidłowy");
            }

            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            // Pobierz zalogowanego użytkownika na podstawie wyodrębnionego username
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            // Jeśli to zwykły użytkownik, ustaw confirmed na false
            document.setConfirmed(currentUser.getIdRole().getId() == 1);

            document.setOwner(currentUser);

            // Obsługa daty: znajdź lub utwórz datę

            Date insertedDate = document.getDate();

            if (insertedDate != null) {

                // sprawdzenie czy taka data juz istnieje
                if(dateService.dateExists(insertedDate)) {
                    Date existingDate = dateService.findDate(insertedDate.getDay(), insertedDate.getMonth(), insertedDate.getYear());
                    document.setDate(existingDate);
                }
                else {
                    // stworzenie nowej daty
                    dateService.saveDate(insertedDate);
                    document.setDate(insertedDate);
                }
            }

            // Obsługa adresu: znajdź lub utwórz adres

            Address insertedPlace = document.getPlace();

            if(insertedPlace != null) {

                if(addressService.addressExists(insertedPlace)) {
                    Address existingAddress = addressService.getAddressByAllParams(
                            insertedPlace.getCountry(),
                            insertedPlace.getVoivodeship(),
                            insertedPlace.getCommunity(),
                            insertedPlace.getCity(),
                            insertedPlace.getLongitude(),
                            insertedPlace.getLatitude(),
                            insertedPlace.getAddress(),
                            insertedPlace.getPostalCode(),
                            insertedPlace.getParish(),
                            insertedPlace.getSecular()
                    );
                    document.setPlace(existingAddress);
                }
                else {
                    addressService.saveAddress(insertedPlace);
                    document.setPlace(insertedPlace);
                }
            }

            if (documentService.documentExists(document)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Dokument już istnieje");
            }

            // Zapisanie nowego dokumentu
            boolean isSaved = documentService.saveDocument(document);
            if (isSaved) {

                if(currentUser.getIdRole().getId() == 2) {

                    // Wyślij powiadomienie do genealogów
                    Notification notification = new Notification();
                    notification.setUser(currentUser);
                    notification.setDisplayed(false);
                    notification.setDate(LocalDate.now());
                    notification.setDocument(document);
                    notification.setTitle("Nowy dokument do weryfikacji");
                    notification.setContext("Użytkownik " + currentUser.getUserName() + " zaproponował dodanie dokumentu " + document.getTitle() + ".\nProsimy o weryfikację wprowadzonych danych. Możesz zaakceptować dokument i dodać go do serwisu.");

                    boolean notificationSaved = notificationService.saveNotification(notification);
                    if(notificationSaved) {

                        Map<String, Object> response = new HashMap<>();
                        response.put("message", "Propozycja dodania dokumentu została przesłana do zatwierdzenia przez genealoga.");
                        response.put("documentId", document.getId());

                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd podczas wysyłania powiadomienia odnośnie wprowadzonych zmian");
                    }
                }

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Dokument został prawidłowo zapisany");
                response.put("documentId", document.getId());

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas dodawania dokumentu");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // 2. Dodanie zdjęć do dokumentu
    @PostMapping("AddPhotos/{id}")
    public ResponseEntity<String> addPhotoToDocument(@PathVariable Long id, @RequestBody List<Long> photoIds) {

        Document document = documentService.getDocumentById(id);

        if (document == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie został znaleziony");
        }

        if (photoIds == null || photoIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Brak zdjęć do dodania");
        }

        for (Long photoID : photoIds) {

            Document photo = documentService.getDocumentById(photoID);

            // Sprawdź, czy zdjęcie ma prawidłowy typ (tylko dokumenty typu zdjęcie są akceptowane)
            if (photo.getType().getId() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dokument " + photo.getTitle() + " nie jest zdjęciem");
            }

            // Sprawdź, czy zdjęcie już odnosi się do dokumentu, do którego próbujemy je dodać
            if (photo.getPhotoRefers() != null && photo.getPhotoRefers().getId().equals(document.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zdjęcie " + photo.getTitle() + " jest już przypisane do tego dokumentu");
            }

            if(photo.getPhotoRefers() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zdjęcie " + photo.getTitle() + " odwołuje się już do dokumentu: " + photo.getPhotoRefers().getTitle());
            }

            // Dodaj zdjęcie do dokumentu
            boolean photoAdded = documentService.addPhotoToDocument(document, photo);
            if (!photoAdded) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nie udało się dodać zdjęcia: " + photo.getTitle());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Zdjęcia zostały pomyślnie dodane do dokumentu");
    }

    // 4. Pobranie wszystkich dokumentów

    @GetMapping("All")
    public ResponseEntity<List<Document>> getDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    // 5. Pobranie wszystkich zdjęć

    @GetMapping("GetPhotos")
    public ResponseEntity<List<Document>> getPhotos() {
        List<Document> photos = documentService.findDocumentsByTypeIds(null, null, List.of(1));
        return ResponseEntity.ok(photos);
    }

    // 6. Pobranie dokumentu po ID

    @GetMapping("Get/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    // 7. Pobranie "Moich" dokumentów
    @GetMapping("GetMyDocuments")
    public ResponseEntity<?> getMyDocuments(HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            List<Document> documents = documentService.findDocumentsByOwner(currentUser);
            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // 7. Aktualizacja dokumentu
    @PutMapping("Update/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable Long id, @Valid @RequestBody Document updatedDocument, HttpServletRequest request) {
        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            if(currentUser.getIdRole().getId() == 1) {
                return updateGenealogist(id, updatedDocument, request);
            } else if (currentUser.getIdRole().getId() == 2) {
                return updateUser(id, updatedDocument, request);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateGenealogist(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody Document updatedDocument, HttpServletRequest request) {
        try {

            // Sprawdź, czy dokument istnieje
            Document existingDocument = documentService.getDocumentById(id);
            if(existingDocument == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie został znaleziony");
            }

            // Ustaw aktualizowane pola dokumentu

            if(updatedDocument.getTitle() != null)
                existingDocument.setTitle(updatedDocument.getTitle());

            if(updatedDocument.getDescription() != null)
                existingDocument.setDescription(updatedDocument.getDescription());

            if(updatedDocument.getStartDate() != null)
                existingDocument.setStartDate(updatedDocument.getStartDate());

            if(updatedDocument.getEndDate() != null)
                existingDocument.setEndDate(updatedDocument.getEndDate());

            existingDocument.setConfirmed(true);
            existingDocument.setOwner(existingDocument.getOwner());

            // Obsługa daty: znajdź lub utwórz datę
            Date insertedDate = updatedDocument.getDate();
            if (insertedDate != null) {
                // sprawdzenie czy taka data już istnieje
                if(dateService.dateExists(insertedDate)) {
                    Date existingDate = dateService.findDate(insertedDate.getDay(), insertedDate.getMonth(), insertedDate.getYear());
                    existingDocument.setDate(existingDate);
                } else {
                    dateService.saveDate(insertedDate);
                    existingDocument.setDate(insertedDate);
                }
            }

            // Obsługa adresu: znajdź lub utwórz adres
            Address insertedPlace = updatedDocument.getPlace();
            if (insertedPlace != null) {
                if(addressService.addressExists(insertedPlace)) {
                    Address existingAddress = addressService.getAddressByAllParams(
                            insertedPlace.getCountry(),
                            insertedPlace.getVoivodeship(),
                            insertedPlace.getCommunity(),
                            insertedPlace.getCity(),
                            insertedPlace.getLongitude(),
                            insertedPlace.getLatitude(),
                            insertedPlace.getAddress(),
                            insertedPlace.getPostalCode(),
                            insertedPlace.getParish(),
                            insertedPlace.getSecular()
                    );
                    existingDocument.setPlace(existingAddress);
                } else {
                    addressService.saveAddress(insertedPlace);
                    existingDocument.setPlace(insertedPlace);
                }
            }

            // Zapisz zaktualizowany dokument
            boolean isUpdated = documentService.updateDocument(existingDocument);
            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.OK).body("Dokument został pomyślnie zaktualizowany");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas aktualizacji dokumentu");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateUser(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody Document updatedDocument, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            Document existingDocument = documentService.getDocumentById(id);
            if (existingDocument == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie został znaleziony");
            }

            // Stwórz nowy dokument z nowymi danymi
            Document newDocument = new Document();

            if(updatedDocument.getTitle() != null) {
                newDocument.setTitle(updatedDocument.getTitle());
            }
            else {
                newDocument.setTitle(existingDocument.getTitle());
            }

            if(updatedDocument.getDescription() != null) {
                newDocument.setDescription(updatedDocument.getDescription());
            }
            else {
                newDocument.setDescription(existingDocument.getDescription());
            }

            if(updatedDocument.getStartDate() != null) {
                newDocument.setStartDate(updatedDocument.getStartDate());
            }
            else {
                newDocument.setStartDate(existingDocument.getStartDate());
            }

            if(updatedDocument.getEndDate() != null) {
                newDocument.setEndDate(updatedDocument.getEndDate());
            }
            else {
                newDocument.setEndDate(existingDocument.getEndDate());
            }

            newDocument.setConfirmed(false);
            newDocument.setOwner(existingDocument.getOwner());
            newDocument.setType(existingDocument.getType());
            newDocument.setLocalization(existingDocument.getLocalization());
            newDocument.setPhotoRefers(existingDocument.getPhotoRefers());

            // Obsługa daty
            Date insertedDate = updatedDocument.getDate();
            if (insertedDate != null) {
                if (dateService.dateExists(insertedDate)) {
                    Date existingDate = dateService.findDate(insertedDate.getDay(), insertedDate.getMonth(), insertedDate.getYear());
                    newDocument.setDate(existingDate);
                } else {
                    dateService.saveDate(insertedDate);
                    newDocument.setDate(insertedDate);
                }
            }
            else {
                newDocument.setDate(existingDocument.getDate());
            }

            // Obsługa adresu
            Address insertedPlace = updatedDocument.getPlace();
            if (insertedPlace != null) {
                if (addressService.addressExists(insertedPlace)) {
                    Address existingAddress = addressService.getAddressByAllParams(
                            insertedPlace.getCountry(), insertedPlace.getVoivodeship(),
                            insertedPlace.getCommunity(), insertedPlace.getCity(),
                            insertedPlace.getLongitude(), insertedPlace.getLatitude(),
                            insertedPlace.getAddress(), insertedPlace.getPostalCode(),
                            insertedPlace.getParish(), insertedPlace.getSecular());
                    newDocument.setPlace(existingAddress);
                } else {
                    addressService.saveAddress(insertedPlace);
                    newDocument.setPlace(insertedPlace);
                }
            }
            else {
                newDocument.setPlace(existingDocument.getPlace());
            }

            boolean isSaved = documentService.saveDocument(newDocument);
            if (isSaved) {
                // Wyślij powiadomienie do genealogów
                Notification notification = new Notification();
                notification.setUser(currentUser);
                notification.setDisplayed(false);
                notification.setDate(LocalDate.now());
                notification.setDocument(existingDocument);
                notification.setNewDocument(newDocument);
                notification.setTitle("Propozycja aktualizacji dokumentu");
                notification.setContext("Użytkownik " + currentUser.getUserName() + " złożył propozycję zmian w dokumencie " + existingDocument.getTitle() + ".\nZweryfikuj wprowadzone zmiany. Możesz je zaakceptować, edytować bądź odrzucić.");

                boolean notificationSaved = notificationService.saveNotification(notification);
                if(notificationSaved) {
                    return ResponseEntity.status(HttpStatus.OK).body("Propozycja zmian została przesłana do zatwierdzenia przez genealoga");
                }
                else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd podczas wysyłania powiadomienia odnośnie wprowadzonych zmian");
                }


            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas dodawania propozycji zmian");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // 8. Wyszukiwanie dokumentu

    @GetMapping("Search")
    public ResponseEntity<List<Document>> searchDocuments(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<Integer> typeIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String voivodeship,
            @RequestParam(required = false) String community,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String startPostalCode,
            @RequestParam(required = false) String endPostalCode,
            @RequestParam(required = false) String parish,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double minLongitude,
            @RequestParam(required = false) Double maxLongitude,
            @RequestParam(required = false) Double minLatitude,
            @RequestParam(required = false) Double maxLatitude) {

        // Najpierw wyszukujemy adresy
        List<Address> addresses = addressService.searchAddress(
                country, voivodeship, community, city, address,
                postalCode, startPostalCode, endPostalCode, parish,
                longitude, latitude, minLongitude, maxLongitude, minLatitude, maxLatitude);

        List<Long> placeIds = addresses.stream()
                .map(Address::getId) // Załóżmy, że Address ma metodę getId()
                .collect(Collectors.toList());

        List<Document> documents = documentService.searchDocuments(name, surname, title, description, typeIds, fromDate, toDate, placeIds);
        return ResponseEntity.ok(documents);
    }

    // 10. Usuwanie dokumentu na podstawie ID

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            Document document = documentService.getDocumentById(id);

            boolean isDeleted = documentService.deleteDocument(document);
            if (isDeleted) {
                return ResponseEntity.ok("Dokument został usunięty");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie istnieje");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // Endpoint to add a path (photo) to a document
    @RequestMapping(value = "AddPath/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addPathToDocument(
            @PathVariable Long id,
            @RequestParam("photoFile") MultipartFile photoFile) {

        Document document = documentService.getDocumentById(id);

        try {
            // Call the service method to add the photo to the document
            boolean result = documentService.addPathToDocument(document, photoFile);

            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body("Zdjęcie zostało dodane do dokumentu.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas dodawania zdjęcia.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

}
