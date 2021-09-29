package com.montanha.isolada;

import io.restassured.http.ContentType;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static javax.swing.text.DefaultStyledDocument.ElementSpec.ContentType;
import static org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class ViagensTest
{
    @Test
    public void testCadastroViagemValidaRetornoSucesso()
    {
        // configuracao rest-assured
        baseURI = "http://localhost";
        port = 8089;
        basePath =  "/api";

        String token =
        given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body("{\n" +
                    "\t\"email\": \"admin@email.com\",\n" +
                    "\t\"senha\": \"654321\"\n" +
                    "}")
        .when()
            .post("/v1/auth")
        .then()
            .extract()
                .path("data.token");

        given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body("{\n" +
                    "\t\"acompanhante\": \"Jamile\",\n" +
                    "\t\"dataPartida\": \"2021-09-30\",\n" +
                    "\t\"dataRetorno\": \"2021-10-01\",\n" +
                    "\t\"localDeDestino\": \"Itaperuçu\",\n" +
                    "\t\"regiao\": \"Sul\"\n" +
                    "}")
            .header("Authorization", token)
        .when()
            .post("/v1/viagens")
        .then()
            .assertThat()
                .statusCode(201)
                .body("data.localDeDestino",equalTo("Itaperuçu"))
                .body("data.acompanhante", equalToIgnoringCase("jamile"));
    }
}
