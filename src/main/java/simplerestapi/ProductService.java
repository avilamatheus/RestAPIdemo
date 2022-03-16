package simplerestapi;
import entities.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

public class ProductService {
    //private AtomicInteger key;
    private String sqlSelectAllProducts = "SELECT * FROM db.products";
    private String connectionUrl = "jdbc:mysql://localhost:3306/?user=root";
    
    public String findAllProducts() {	
    	List<Product> prods = new ArrayList<Product>();
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "170799"); 
                PreparedStatement ps = conn.prepareStatement(sqlSelectAllProducts); 
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    Double price = rs.getDouble("price");
                    String category = rs.getString("category");
                    prods.add(new Product(id, name, price, category));
                }
                conn.close();
        } catch (SQLException e) {
        }
    	return toJson(prods);
    }

    private String toJson(Object list) {
        if (list == null) return null;
        Gson gson = new Gson();
        String json = null;
        try {
            json = gson.toJson(list);
        }
        catch (Exception e) {}
        return json;
    }
    
    public boolean createProduct(String jsonPayload) {
   
        if (jsonPayload == null) return false;
        
        else {
        	Gson gson = new Gson();
            try {
                Product product = (Product) gson.fromJson(jsonPayload, Product.class);
                if (product != null) {
                    return addProduct(product);
                }
            }
            catch (Exception e) {
            	return false;
            }
        }
        return true;
    }
    
    private boolean addProduct(Product product) {
    	String sqlInsertProduct = "INSERT INTO db.products (name, price, category) VALUES "
    			+ "('"+product.getName()+"', "+product.getPrice()+", '"+product.getCategory()+"');";
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	try {
    		Connection conn = DriverManager.getConnection(connectionUrl, "root", "170799"); 
            PreparedStatement ps = conn.prepareStatement(sqlInsertProduct); 
    		ps.executeUpdate();
    		conn.close();
    		return true;
    	}
        catch (SQLException e) {
        	return false;
        }
    }
    
    public boolean removeProduct(Integer id) {
    	String sqlRemoveProduct = "DELETE FROM db.products WHERE id = " + id + ";";
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	try {
    		Connection conn = DriverManager.getConnection(connectionUrl, "root", "170799"); 
            PreparedStatement ps = conn.prepareStatement(sqlRemoveProduct); 
    		ps.executeUpdate();
    		conn.close();
    		return true;
    	}
        catch (SQLException e) {
        	return false;
        }
    }
 
}
