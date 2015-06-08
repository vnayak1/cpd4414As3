/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import databaseCrediential.Credentials;
import static databaseCrediential.Credentials.getConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

/**
 *
 * @author vinayak
 */
@WebServlet("/products")
public class Product extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain-text");
        try {
            PrintWriter output = response.getWriter();
            String query = "SELECT * FROM product;";
            if (!request.getParameterNames().hasMoreElements()) {
                output.println(resultMethod(query));
            } else {
                String id = request.getParameter("ProductID");
                output.println(resultMethod("SELECT * FROM product WHERE ProductID= ?",id));
            }

        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Set<String> keyValues = request.getParameterMap().keySet();

        try {
            PrintWriter output = response.getWriter();
            if (keyValues.contains("ProductID") && keyValues.contains("name") && keyValues.contains("description")
                    && keyValues.contains("quantity")) {
                String ProductID = request.getParameter("ProductID");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("INSERT INTO product (ProductID,name,description,quantity) VALUES (?, ?, ?, ?)", ProductID, name, description, quantity);

            } else {
                response.setStatus(500);
                output.println("No Input Find");
            }

        } catch (IOException ex) {
            System.err.println("Input error" + ex.getMessage());
        }

    }
    
    
    
        @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("ProductID") && keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                String ProductID = request.getParameter("ProductID");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("update product set ProductID = ?, name = ?, description = ?, quantity = ? where ProductID = ?", ProductID, name, description, quantity, ProductID);
            } else {
                out.println("Finding Error for data");
            }
        } catch (IOException ex) {
            response.setStatus(500);
            System.out.println(" output: " + ex.getMessage());
        }
    }

    private String resultMethod(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        String jsonString = "";

        try (java.sql.Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            List l1 = new LinkedList();
            while (rs.next()) {
                Map m1 = new LinkedHashMap();
                m1.put("ProductID", rs.getInt("ProductID"));
                m1.put("name", rs.getString("name"));
                m1.put("description", rs.getString("description"));
                m1.put("quantity", rs.getInt("quantity"));
                l1.add(m1);
            }

            jsonString = JSONValue.toJSONString(l1);
        } catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        }
        return jsonString.replace("},", "},\n");
    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (java.sql.Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("exception" + e.getMessage());
        }
        return numChanges;
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            Connection conn = getConnection();
            if (keySet.contains("ProductID")) {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM `product` WHERE `ProductID`=" + request.getParameter("ProductID"));
                try {
                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println("Update error " + ex.getMessage());
                    
                   
                }
            } else {
                out.println("table empty");
                
            }
        } catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        }
    }

}
