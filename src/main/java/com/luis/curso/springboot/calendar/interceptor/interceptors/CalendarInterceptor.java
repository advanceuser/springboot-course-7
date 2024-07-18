package com.luis.curso.springboot.calendar.interceptor.interceptors;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("calendarInterceptor")
public class CalendarInterceptor implements HandlerInterceptor {

	@Value("${config.calendar.open}")
	private Integer open;
	@Value("${config.calendar.close}")
	private Integer close;

	private static final Logger log = LoggerFactory.getLogger(CalendarInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		log.info("{}", hour);

		if (hour >= open && hour < close) {
			StringBuilder message = new StringBuilder("Bievenidos al horario de atencion a clientes");
			message.append(", antedemos desde las: ");
			message.append(open);
			message.append(" hrs. hasta las: ");
			message.append(close);
			message.append(" hrs.");
			message.append(" Gracias por su visita");
			request.setAttribute("message", message.toString());
			return true;
		}
		// Creando un json con ObjectMapper
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();

		StringBuilder message = new StringBuilder("Cerrado, fuera del horario de atencion ");
		message.append("por favor visitemos desde las ");
		message.append(open);
		message.append(" y las ");
		message.append(close);
		message.append(" hrs. Gracias!!!");

		data.put("message", message.toString());
		data.put("date", new Date().toString());

		response.setContentType("application/json");
		response.setStatus(401);
		response.getWriter().write(mapper.writeValueAsString(data));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

}
