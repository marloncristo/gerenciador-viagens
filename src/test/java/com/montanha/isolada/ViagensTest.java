package com.montanha.isolada;

import com.montanha.factory.UsuarioDataFactory;
import com.montanha.factory.ViagemDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.Viagem;
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

        Usuario usuarioAdministrador = UsuarioDataFactory.criarUsuarioAdministrador();

        String token =
        given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body(usuarioAdministrador)
        .when()
            .post("/v1/auth")
        .then()
            .extract()
                .path("data.token");

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
}
