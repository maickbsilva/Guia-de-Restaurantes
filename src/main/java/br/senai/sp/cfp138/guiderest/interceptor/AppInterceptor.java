package br.senai.sp.cfp138.guiderest.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.Http;

import br.senai.sp.cfp138.guiderest.annotation.Privado;
import br.senai.sp.cfp138.guiderest.annotation.Publico;
import br.senai.sp.cfp138.guiderest.model.Usuario;
import br.senai.sp.cfp138.guiderest.repository.UsuarioRepository;
import br.senai.sp.cfp138.guiderest.rest.UsuarioRestController;

@Component
public class AppInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// variavel para obter a URI
		String uri = request.getRequestURI();

		// variavel para a sessao
		HttpSession session = request.getSession();

		if (uri.startsWith("/error")) {
			return true;
		}

		// verificar se handler é um HandlerMethod, o que indica que ele está chamando
		// um método em algum controller

		if (handler instanceof HandlerMethod) {
			// casting de Object para HandlerMethod
			HandlerMethod metodo = (HandlerMethod) handler;

			if (uri.startsWith("/api")) {
				// variavel para o token
				String token = null;
				// verifica se é um metodo privado
				if (metodo.getMethodAnnotation(Privado.class) != null) {
					try {
						// se o metodo for privado recupera o token
						token = request.getHeader("Authorization");
						Algorithm algoritmo = Algorithm.HMAC256(UsuarioRestController.SECRET);
						// objeto para verificar o token
						JWTVerifier verifier = JWT.require(algoritmo).withIssuer(UsuarioRestController.SECRET).build();
						// decodifica o Token
						DecodedJWT jwt = verifier.verify(token);
						// recupera os dados do payload
						Map<String, Claim> clains = jwt.getClaims();
						System.out.println(clains.get("name"));
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						} else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
					}
				}
				return false;
			} else {

				// verifica se este metodo é publico
				if (metodo.getMethodAnnotation(Publico.class) != null) {
					return true;
				}
				// verifica se existe um usuario logado
				if (session.getAttribute("usuarioLogado") != null) {
					return true;
				}
				// redireciona para a pagina inicial
				response.sendRedirect("/");
				return false;

			}
		}

		return true;
	}
}
