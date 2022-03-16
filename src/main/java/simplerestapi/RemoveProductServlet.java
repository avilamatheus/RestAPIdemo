package simplerestapi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class RemoveProductServlet extends HttpServlet {

    private ProductService productService;
    
    public RemoveProductServlet() {
        this.productService = new ProductService();  
    }
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("id") != ""){
			Integer id = Integer.parseInt(req.getParameter("id"));
			int rc = HttpServletResponse.SC_OK;
			boolean res = productService.removeProduct(id);
			if (!res) {
	            rc = HttpServletResponse.SC_BAD_REQUEST;
	        }
	        outputResponse(resp, null, rc);
		}
		else{
			int rc = HttpServletResponse.SC_BAD_REQUEST;
			outputResponse(resp, null, rc);
		}
    }
	//curl -i --data "id=" 'http://localhost:8080/removeproduct'
	
	private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type", "application/json");
        try {
            response.setStatus(status);
            if (payload != null) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
