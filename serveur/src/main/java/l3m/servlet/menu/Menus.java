package l3m.servlet.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Menus extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	File f = new File("src/main/java/l3m/bdd/xml/carte.xml");
		System.out.println();
		String s = loadFileString(f);
		if(s != null && !s.equals("")) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/xml");
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().print(s);
		}else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public static String loadFileString(File file) throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(file), "ascii");
        try {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            int nbRead = reader.read(buffer);
            while (nbRead > 0) {
                builder.append(buffer, 0, nbRead);
                nbRead = reader.read(buffer);
            }
            return builder.toString();
        } finally {
            reader.close();
        }
    }
}
