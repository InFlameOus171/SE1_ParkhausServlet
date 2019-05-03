import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {

	private int counter = 0;
	private double defaultRate = 1.0;
	private double dayRate = defaultRate, nightRate = defaultRate;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		if (request.getParameterMap().containsKey("tagespreisMultiplikator")
				|| request.getParameterMap().containsKey("nachtpreisMultiplikator")) {
			dayRate = request.getParameter("tagespreisMultiplikator").isEmpty() ? defaultRate
					: Double.parseDouble(request.getParameter("tagespreisMultiplikator"));
			nightRate = request.getParameter("nachtpreisMultiplikator").isEmpty() ? defaultRate
					: Double.parseDouble(request.getParameter("nachtpreisMultiplikator"));
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('Preise gesetzt');");
			out.println("window.history.back();");
			out.println("</script>");
			out.close();

		} else {
			Float sum = getPersistentSum();
			String body = getBody(request);
			System.out.println(body);
			String[] params = body.split(",");
			String event = params[0];
			String priceString = params[5];
			if (!"_".equals(priceString)) {
				// strip € in front, parse the number behind
				float price = Float.parseFloat(priceString.split(" ")[2]);
				sum += price;
				counter++;
				// store sum persistently in ServletContext
				getApplication().setAttribute("sum", sum);
				getApplication().setAttribute("avg", sum / counter);
			}

			PrintWriter out = response.getWriter();
			out.println(sum);
			out.close();
		}
	}

	private float getPersistentAvg() {
		Float avg;
		ServletContext application = getApplication();
		avg = (Float) application.getAttribute("avg");
		if (avg == null)
			avg = 0.0f;
		return avg;
	}

	private ServletContext getApplication() {
		return getServletConfig().getServletContext();
	}

	private Float getPersistentSum() {
		Float sum;
		ServletContext application = getApplication();
		sum = (Float) application.getAttribute("sum");
		if (sum == null)
			sum = 0.0f;
		return sum;
	}

	private static String getBody(HttpServletRequest request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return stringBuilder.toString();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] requestParamString = request.getQueryString().split("=");
		String command = requestParamString[0];
		String param = requestParamString[1];
		if ("fun".equals(command) && "sum".equals(param)) {
			Float sum = getPersistentSum();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(sum);
			System.out.println("sum = " + sum);
			out.close();
		} else {
			if ("fun".equals(command) && "avg".equals(param)) {
				String avg = String.format("%.2f", getPersistentAvg());
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(avg);
				System.out.println(avg);
				out.close();

			} else if ("fun".equals(command) && "prc".equals(param)) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("Dayrate: " + dayRate + " Euro/h - Nightrate: " + nightRate + " Euro/h");
				System.out.println(dayRate + " - " + nightRate);
				out.close();
			} else {
				System.out.println("Invalid Command: " + request.getQueryString());
			}
		}
	}

}