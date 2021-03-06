/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asistelnet.controlador;

import gestordocumental.GestorDocumental;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Edgar J García L
 */
public class ControladorPrincipal {

    private static int timeOut = 0;
    private transient long tiempo;
    private final GestorDocumental gestorDocumental = new GestorDocumental();

    public ControladorPrincipal(int timeOut) {
        this.timeOut = timeOut;
    }

    public long getTiempo() {
        return tiempo;
    }

    public String imprimirAlServer(final String aImprimir, final String HOST, final int PUERTO) {
        try (Socket newClient = new Socket(HOST, PUERTO);
                final PrintWriter out = new PrintWriter(newClient.getOutputStream(), true);) {
            out.println(aImprimir);
            tiempo = System.currentTimeMillis();
            String respuesta = leerRespuestaServer(HOST, PUERTO, newClient);
            tiempo = System.currentTimeMillis() - tiempo;
            return respuesta;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Logger.getGlobal().log(Level.SEVERE, ex.toString());
            tiempo = System.currentTimeMillis() - tiempo;
            return null;
        }
    }

    public String leerRespuestaServer(final String HOST, final int PUERTO, Socket NewClient) {
        String trama = "Sin Respuesta Time Out excedido";
        try {

            NewClient.setSoTimeout(timeOut * 1000);
            int character;
            final BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(NewClient.getInputStream()));
            final StringBuilder string_builder = new StringBuilder();
            while (true) {
                character = buffer_reader.read();
                string_builder.append((char) character);
                if (character == 126 || character == 13) {
                    break;
                } else if (character == -1) {
                    break;
                }
            }
            trama = string_builder.toString();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            return trama;
        }
        return trama;
    }
    

}
