package aroc.v1.modelo.entidades.dao.controladores;

import aroc.v1.modelo.entidades.Registro;
import aroc.v1.modelo.entidades.dao.RegistroJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

public class ControladorRegistro extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void recuperarPeticion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        switch(accion){
            case "Guardar":
                guardarPropietario(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    @Resource
    UserTransaction t;
    @PersistenceUnit
    EntityManagerFactory conexion;
    private void guardarPropietario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String puertoPHP = request.getParameter("puertophp");
        String puertoSQL = request.getParameter("puertosql");
        
        Registro r = new Registro(Integer.parseInt(id), Integer.parseInt(puertoPHP), Integer.parseInt(puertoSQL));
    
        //EntityManagerFactory conexion = Persistence.createEntityManagerFactory("clasePU");
        RegistroJpaController daoP = new RegistroJpaController(t, conexion);
        
        try {
            daoP.create(r);
            response.sendRedirect("web/registros/agregar.jsp?msg=ok al guardar " + r.getIdUsuario());
        } catch (Exception ex) {
            response.sendRedirect("web/registros/agregar.jsp?msg=error al guardar");
        }
    }

}
