package br.senai.sp.cfp138.guiderest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.senai.sp.cfp138.guiderest.annotation.Publico;

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
				return true;
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
