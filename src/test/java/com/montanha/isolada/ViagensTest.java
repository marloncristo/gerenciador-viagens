package com.montanha.isolada;

import com.montanha.factory.UsuarioDataFactory;
import com.montanha.factory.ViagemDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.Viagem;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static javax.swing.text.DefaultStyledDocument.ElementSpec.ContentType;
import static org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class ViagensTest
{
    private String token;
    @Before
    public void setUP() {
        // configuracao rest-assured
        baseURI = "http://localhost";
        port = 8089;
        basePath = "/api";

        Usuario usuarioAdministrador = UsuarioDataFactory.criarUsuarioAdministrador();

        this.token =
                given()
                    .contentType(io.restassured.http.ContentType.JSON)
                    .body(usuarioAdministrador)
                .when()
                    .post("/v1/auth")
                .then()
                    .extract()
                        .path("data.token");
    }
    @Test
    public void testCadastroViagemValidaRetornoSucesso() throws IOException {

        Viagem viagemValida = ViagemDataFactory.criarViagemValida();

        given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body(viagemValida)
            .header("Authorization", token)
        .when()
            .post("/v1/viagens")
        .then()
            .assertThat()
                .statusCode(201)
                .body("data.localDeDestino",equalTo("Itaperuçu"))
                .body("data.acompanhante", equalToIgnoringCase("marlon"));
    }

    @Test
    public void testViagensNaoPodemSerCadastradasSemLocalDeDestino() throws IOException {
        Viagem viagemSemLocalDeDestino = ViagemDataFactory.criarViagemSemLocalDeDestino();

        given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body(viagemSemLocalDeDestino)
            .header("Authorization", token)
        .when()
            .post("/v1/viagens")
        .then()
            .assertThat()
                .statusCode(400)
                .body("errors.defaultMessage",
                        hasItem("Local de Destino deve estar entre 3 e 40 caracteres"));
    }
}
