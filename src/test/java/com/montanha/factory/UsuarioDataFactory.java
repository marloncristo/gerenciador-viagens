package com.montanha.factory;

import com.montanha.pojo.Usuario;

public class UsuarioDataFactory {
    public static Usuario criarUsuarioAdministrador(){
        Usuario usuarioAdministrador = new Usuario();

        usuarioAdministrador.setEmail("admin@email.com");
        usuarioAdministrador.setSenha("654321");

        return usuarioAdministrador;
    }
}
