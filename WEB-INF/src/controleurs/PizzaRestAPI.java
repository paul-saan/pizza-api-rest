package controleurs;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.PizzaDAODatabase;
import dao.VerifierToken;
import dto.Ingredient;
import dto.Pizza;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pizzas/*")
public class PizzaRestAPI extends DoPatch {
    PizzaDAODatabase dao = new PizzaDAODatabase();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException, java.io.IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            Collection<Pizza> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 4) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        Pizza e = dao.findById(id);
        if (e == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if(splits.length==3){
            if(splits[2].equals("prixfinal")){
                String jsonstring = objectMapper.writeValueAsString(dao.getPrixFinal(id));
                out.print(jsonstring);
                return;
            }if(splits[2].equals("name")){
                String jsonstring = objectMapper.writeValueAsString(e.getNom());
                out.print(jsonstring);
                return;
            }if(splits[2].equals("pate")){
                String jsonstring = objectMapper.writeValueAsString(e.getPate());
                out.print(jsonstring);
                return;
            }if(splits[2].equals("prixbase")){
                String jsonstring = objectMapper.writeValueAsString(e.getPrixBase());
                out.print(jsonstring);
                return;
            }
        }
        out.print(objectMapper.writeValueAsString(e));
        return;
        
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException, java.io.IOException {
        String autho = req.getHeader("token");
        if (autho==null || !autho.startsWith("Basic")) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        // on décode le token
        String token = autho.substring("Basic".length()).trim();
        byte[] base64 = Base64.getDecoder().decode(token);
        String[] lm = (new String(base64)).split(":");
        String login = lm[0];
        String pwd = lm[1];
        if(!VerifierToken.token(login,pwd)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            Pizza pizza = objectMapper.readValue(buffer.toString(), Pizza.class);
            if(dao.save(pizza.getId(), pizza.getNom(),pizza.getPate(), pizza.getPrixBase(), pizza.getIngredients())){
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            out.print(buffer.toString());
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        else{
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            int id_pizza = Integer.parseInt(splits[1]);
            Ingredient ingredient = objectMapper.readValue(buffer.toString(), Ingredient.class);
            if(dao.addIngredient(id_pizza, ingredient.getId())){
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            out.print(buffer.toString());
        }
        return;
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException, java.io.IOException {
            String autho = req.getHeader("token");
            if (autho==null || !autho.startsWith("Basic")) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            // on décode le token
            String token = autho.substring("Basic".length()).trim();
            byte[] base64 = Base64.getDecoder().decode(token);
            String[] lm = (new String(base64)).split(":");
            String login = lm[0];
            String pwd = lm[1];
            if(!VerifierToken.token(login,pwd)){
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
        res.setContentType("application/json;charset=UTF-8");
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2 && splits.length != 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id_pizza = Integer.parseInt(splits[1]);
        if (splits.length == 2){
            if (dao.delete(id_pizza)) {
                res.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        else if (splits.length ==3){
            int id_ingredient = Integer.parseInt(splits[2]);
            dao.deleteIngredient(id_pizza, id_ingredient);
        }
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, java.io.IOException {
        String autho = req.getHeader("token");
        if (autho==null || !autho.startsWith("Basic")) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        // on décode le token
        String token = autho.substring("Basic".length()).trim();
        byte[] base64 = Base64.getDecoder().decode(token);
        String[] lm = (new String(base64)).split(":");
        String login = lm[0];
        String pwd = lm[1];
        if(!VerifierToken.token(login,pwd)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        if (dao.findById(id).getId() == 0) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String payload = buffer.toString();

        @SuppressWarnings("unchecked")
        Map<String, String> jsonData = objectMapper.readValue(payload, Map.class);

        String prixString = jsonData.get("prix");
        int cpt =0;
        if(prixString!=null){
            int prix = Integer.parseInt(prixString);
            dao.patchPrix(id, prix);
            cpt++;
        }
        if(jsonData.get("name")!=null){
            dao.patchName(id, jsonData.get("name"));
            cpt++;
        }
        if(jsonData.get("pate")!=null){
            dao.patchPate(id, jsonData.get("pate"));
            cpt++;
        }
        if(cpt==0){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        out.print(objectMapper.writeValueAsString(dao.findById(id)));

        return;
    }    
}