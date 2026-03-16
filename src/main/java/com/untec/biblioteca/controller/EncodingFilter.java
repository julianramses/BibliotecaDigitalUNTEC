package com.untec.biblioteca.controller;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filtro de codificación UTF-8.
 * Se aplica a todas las peticiones para garantizar el correcto manejo
 * de caracteres especiales (tildes, ñ, etc.).
 *
 * NOTA: Este filtro se registra ÚNICAMENTE via web.xml (sin @WebFilter)
 * para evitar doble registro que rompe el enrutamiento de Servlets.
 */
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig config) {
        String enc = config.getInitParameter("encoding");
        if (enc != null && !enc.isEmpty()) {
            this.encoding = enc;
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding(encoding);
        resp.setCharacterEncoding(encoding);
        chain.doFilter(req, resp);
    }
}
