package rsa;

import funciones.Funcion;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import baseDatos.*;

import javax.crypto.Cipher;

public class RSAAsymetricCrypto {
   private static Cipher rsa;

   public static void main(String[] args) throws Exception {
      // Generar el par de claves de tipo RSA (el RSA funciona para indicar que tipo de algoritmo se va a crear)
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      PublicKey publicKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();

      // Se guarda el archivo de la clave publica
        Funcion.saveKey(publicKey, "publickey.dat");
      
      // Se recupera el fichero de la clave publica
      publicKey = Funcion.loadPublicKey("publickey.dat");

      // Se salva y recupera de fichero la clave privada
        Funcion.saveKey(privateKey, "privatekey.dat");
      privateKey = Funcion.loadPrivateKey("privatekey.dat");

      // Obtener la clase para encriptar/desencriptar
      rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      // Texto a encriptar
      String text = "7070";
      String text1 = "3130";

      // Se encripta
      rsa.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encriptado = rsa.doFinal(text.getBytes());      
      byte[] encriptado1 = rsa.doFinal(text1.getBytes());   
      
      
      //Base de datos_________________________________________________________________________________________________________________
        
	PreparedStatement ingreAbase=null;
      
      
      String insertTableSQL = "INSERT INTO Corpo"+"(TC,CVV) VALUES"+"(?,?)";

        try {
            conexionMYSQL mysql=new conexionMYSQL();
            Connection cn=mysql.Conectar();
            ingreAbase = cn.prepareStatement(insertTableSQL);

            ingreAbase.setBytes(1, encriptado);
            ingreAbase.setBytes(2, encriptado1);
            // execute insert SQL stetement
            ingreAbase.executeUpdate();

            System.out.println("Guardado...");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
      //Base de datos_________________________________________________________________________________________________________________
      
      
      
      /* Escribimos el encriptado para verlo, con caracteres visibles
      for (byte b : encriptado) {
         System.out.print(Integer.toHexString(0xFF & b));
      }*/
      /* Se desencripta
      rsa.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] bytesDesencriptados = rsa.doFinal(encriptado);
      String textoDesencripado = new String(bytesDesencriptados);*/
      
      
      ResultSet outBase=null;
      byte[] bytesDeBase = null;
      byte[] bytesDeBase2 = null;
      
      
      try{
          System.out.println("Consultando...\n");
          conexionMYSQL mysql=new conexionMYSQL();
          Connection cn=mysql.Conectar();
          Statement stat = cn.createStatement();
          outBase = stat.executeQuery("select * from Corpo");
          
          while (outBase.next())
          {
          bytesDeBase = outBase.getBytes(1);
          bytesDeBase2 = outBase.getBytes(2);
          }

        }
      
      catch (SQLException e) {
          System.out.println(e.getMessage());
      }
      
      rsa.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] bytesDesencriptados = rsa.doFinal(bytesDeBase);
      String textoDesencripado = new String(bytesDesencriptados);
      byte[] bytesDesencriptados1 = rsa.doFinal(bytesDeBase2);
      String textoDesencripado1 = new String(bytesDesencriptados1);
      System.out.println ("TC: "+textoDesencripado+"   Cvv: "+textoDesencripado1);

   }

}
