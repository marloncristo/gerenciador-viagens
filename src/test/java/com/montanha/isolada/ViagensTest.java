package com.montanha.isolada;

import com.montanha.config.Configuracoes;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.factory.ViagemDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.Viagem;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class ViagensTest
{
    private String token;
    @Before
    public void setUP() {
        // configuracao rest-assured

        Configuracoes configuracoes = ConfigFactory.create(Configuracoes.class);
        baseURI = configuracoes.baseURI();
        port = configuracoes.port();
        basePath = configuracoes.basePath();

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
                .body("data.acompanhante", equalToIgnoringCase("Jamile"));
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

    @Test
    public void testCadastroViagemValidaContrato() throws IOException {

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
                .body(matchesJsonSchemaInClasspath("schemas/postV1ViagensViagemValida.json"));
    }
}
