
package baseDatos;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class conexionMYSQL {
    private String driver="com.mysql.jdbc.Driver";
    private String cadenaConeccion="jdbc:mysql://localhost:3307/Corpo";
    private String usuario="root";
    private String contraseña="";
    public Connection con;

    public Connection Conectar(){
        
        try{
    Class.forName(driver);
    con=DriverManager.getConnection(cadenaConeccion,usuario,contraseña); 
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"no se ha podido establcer una conexion con la base"+e.getMessage());
        }
        return con;
    }}
    

    
