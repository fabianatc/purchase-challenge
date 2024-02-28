package com.purchase.integration.purchase;

import com.purchase.adapter.gateway.repository.purchase.PurchaseRepository;
import com.purchase.domain.entity.Purchase;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class PurchaseControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private PurchaseRepository repository;

    private Purchase purchase;

    @BeforeAll
    public void iniciar() {
        purchase = new Purchase();
        purchase.setDolarPrice(BigDecimal.valueOf(500));
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setId(UUID.randomUUID());
        purchase.setDescription("Item description");
    }

    @Test
    @DisplayName("Test execute success create new purchase")
    void testExecuteSuccessCreateNewPurchase() {
        CreatePurchaseInput input = new CreatePurchaseInput();
        input.setDolarPrice(BigDecimal.valueOf(500));
        input.setPurchaseDate(LocalDate.now());
        input.setDescription("Item description");

        HttpEntity<CreatePurchaseInput> httpEntity = new HttpEntity<>(input);
        ResponseEntity<Purchase> response = testRestTemplate
                .exchange("/purchase/", HttpMethod.POST, httpEntity, Purchase.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(purchase.getDescription(), Objects.requireNonNull(response.getBody()).getDescription());
    }

    @Test
    @DisplayName("Test execute bad request status when purchase data is null or blank")
    void testExecuteThrowsBadRequestExceptionWhenPurchaseDataIsNullOrBlank() {
        CreatePurchaseInput input = new CreatePurchaseInput();

        HttpEntity<CreatePurchaseInput> httpEntity = new HttpEntity<>(input);
        ResponseEntity<Purchase> response = testRestTemplate
                .exchange("/purchase/", HttpMethod.POST, httpEntity, Purchase.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test execute success get country currency")
    void testExecuteSuccessGetCountryCurrrency() {
        Purchase createdPurchase = repository.save(purchase);

        String url = "/purchase/country_currency?purchaseId=" + createdPurchase.getId() + "&country=Brazil";

        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test execute not found status when purchaseId not found")
    void testExecuteNotFoundStatusWhenPurchaseIdNotFound() {
        String url = "/purchase/country_currency?purchaseId=63574290-e139-44c5-999b-9a941e11f149&country=Brazil";

        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test execute bad request status when purchaseId data is invalid")
    void testExecuteBadRequestStatusWhenPurchaseIdIsInvalid() {
        String url = "/purchase/country_currency?purchaseId=9a941e11f149&country=Brazil";

        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test execute bad request status when purchaseId or country is blank or null")
    void testExecuteBadRequestStatusWhenPurchaseIdorCountryIsBlankOrNull() {
        String url = "/purchase/country_currency";

        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
